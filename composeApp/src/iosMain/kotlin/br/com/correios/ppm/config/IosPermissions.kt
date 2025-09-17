package br.com.correios.ppm.config

import br.com.correios.ppm.config.PermissionStatus.*
import kotlinx.coroutines.CompletableDeferred
import platform.AVFoundation.*
import platform.CoreLocation.*
import platform.darwin.dispatch_get_main_queue

class IosPermissions : Permissions {

    override suspend fun ensure(vararg permissions: AppPermission): Map<AppPermission, PermissionStatus> {
        val out = mutableMapOf<AppPermission, PermissionStatus>()
        for (p in permissions) {
            out[p] = when (p) {
                AppPermission.FineLocation, AppPermission.CoarseLocation -> askLocation()

                // Sem equivalente direto no iOS:
                AppPermission.ReadPhoneState,
                AppPermission.ManageExternalStorage,
                AppPermission.RequestInstallPackages ->
                    Error("Não aplicável no iOS")

                // Mídia: tratar com PHPhotoLibrary/Files conforme o caso
                AppPermission.ReadMediaImages,
                AppPermission.ReadMediaVideo,
                AppPermission.ReadMediaAudio ->
                    Error("Use PHPhotoLibrary.requestAuthorization/UIDocumentPicker")

                // “normais” no Android — no iOS não há prompt
                AppPermission.Internet, AppPermission.NetworkState,
                AppPermission.Vibrate, AppPermission.WifiState,
                AppPermission.ChangeWifiState -> PermissionStatus.Granted

                AppPermission.Camera -> TODO()
            }
        }
        return out
    }

    private fun askLocation(): PermissionStatus {
        val status = CLLocationManager.authorizationStatus()
        return when (status) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> PermissionStatus.Granted
            kCLAuthorizationStatusDenied -> PermissionStatus.PermanentlyDenied
            kCLAuthorizationStatusNotDetermined -> PermissionStatus.Denied // implemente delegate p/ fluxo assíncrono real
            else -> PermissionStatus.Denied
        }
    }
}