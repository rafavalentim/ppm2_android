package br.com.correios.ppm.config

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private class PermissionHelperFragment : Fragment() {
    private val pending = mutableMapOf<String, CompletableDeferred<PermissionStatus>>()
    private var currentSingleKey: String? = null

    private val singleLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val key = currentSingleKey ?: return@registerForActivityResult
        val status = if (granted) PermissionStatus.Granted
        else if (!shouldShowRequestPermissionRationale(key)) PermissionStatus.PermanentlyDenied
        else PermissionStatus.Denied
        pending.remove(key)?.complete(status)
        currentSingleKey = null
    }

    private val multipleLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        results.forEach { (perm, granted) ->
            val status = if (granted) PermissionStatus.Granted
            else if (!shouldShowRequestPermissionRationale(perm)) PermissionStatus.PermanentlyDenied
            else PermissionStatus.Denied
            pending.remove(perm)?.complete(status)
        }
    }

    suspend fun request(perms: List<String>): Map<String, PermissionStatus> {
        val ctx = requireContext()
        val results = mutableMapOf<String, PermissionStatus>()

        val needRequest = perms.filter { perm ->
            val granted = ContextCompat.checkSelfPermission(
                ctx, perm
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (granted) results[perm] = PermissionStatus.Granted
            !granted
        }

        if (needRequest.isEmpty()) return results

        return withContext(Dispatchers.Main) {
            if (needRequest.size == 1) {
                val p = needRequest.first()
                val def = CompletableDeferred<PermissionStatus>()
                pending[p] = def
                currentSingleKey = p
                singleLauncher.launch(p)
                results[p] = def.await()
            } else {
                val defs = needRequest.associateWith { CompletableDeferred<PermissionStatus>() }
                pending.putAll(defs)
                multipleLauncher.launch(needRequest.toTypedArray())
                defs.forEach { (perm, def) -> results[perm] = def.await() }
            }
            results
        }
    }
}

private fun FragmentActivity.permissionFragment(): PermissionHelperFragment {
    val tag = "PermissionHelperFragment"
    val fm = supportFragmentManager
    (fm.findFragmentByTag(tag) as? PermissionHelperFragment)?.let { return it }
    return PermissionHelperFragment().also { fm.beginTransaction().add(it, tag).commitNow() }
}

class AndroidPermissions(private val activity: FragmentActivity) : Permissions {

    override suspend fun ensure(vararg permissions: AppPermission): Map<AppPermission, PermissionStatus> {
        val map = mutableMapOf<AppPermission, PermissionStatus>()

        // Especiais (Settings)
        permissions.forEach { p ->
            when (p) {
                AppPermission.ManageExternalStorage -> {
                    map[p] = if (Build.VERSION.SDK_INT >= 30) {
                        if (Environment.isExternalStorageManager()) PermissionStatus.Granted
                        else {
                            val i = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                                .setData(Uri.parse("package:${activity.packageName}"))
                            activity.startActivity(i)
                            PermissionStatus.Denied
                        }
                    } else PermissionStatus.Granted
                }
                AppPermission.RequestInstallPackages -> {
                    map[p] = if (Build.VERSION.SDK_INT >= 26) {
                        if (activity.packageManager.canRequestPackageInstalls())
                            PermissionStatus.Granted
                        else {
                            val i = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                                .setData(Uri.parse("package:${activity.packageName}"))
                            activity.startActivity(i)
                            PermissionStatus.Denied
                        }
                    } else PermissionStatus.Granted
                }
                else -> Unit
            }
        }

        // Normais (não pedem prompt) — marcamos como Granted
        permissions.filter {
            it in listOf(
                AppPermission.Internet, AppPermission.NetworkState, AppPermission.Vibrate,
                AppPermission.WifiState, AppPermission.ChangeWifiState
            )
        }.forEach { map[it] = PermissionStatus.Granted }

        // Dangerous — pedem prompt
        val dangerousToAndroid = permissions.mapNotNull { p ->
            if (p in map.keys) null else when (p) {
                AppPermission.Camera -> Manifest.permission.CAMERA
                AppPermission.FineLocation -> Manifest.permission.ACCESS_FINE_LOCATION
                AppPermission.CoarseLocation -> Manifest.permission.ACCESS_COARSE_LOCATION
                AppPermission.ReadPhoneState -> Manifest.permission.READ_PHONE_STATE
                AppPermission.ReadMediaImages ->
                    if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_IMAGES else null
                AppPermission.ReadMediaVideo ->
                    if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_VIDEO else null
                AppPermission.ReadMediaAudio ->
                    if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_AUDIO else null
                else -> null
            }?.let { p to it }
        }

        if (dangerousToAndroid.isNotEmpty()) {
            val helper = activity.permissionFragment()
            val requested = helper.request(dangerousToAndroid.map { it.second })
            dangerousToAndroid.forEach { (app, androidPerm) ->
                map[app] = requested[androidPerm] ?: PermissionStatus.Error("Unknown")
            }
        }

        return map
    }
}