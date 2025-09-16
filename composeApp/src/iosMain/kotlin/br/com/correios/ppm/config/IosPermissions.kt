package br.com.correios.ppm.config

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
                AppPermission.Camera -> askCamera()

                // Sem equivalente direto no iOS:
                AppPermission.ReadPhoneState,
                AppPermission.ManageExternalStorage,
                AppPermission.RequestInstallPackages ->
                    PermissionStatus.Error("Não aplicável no iOS")

                // Mídia: tratar com PHPhotoLibrary/Files conforme o caso
                AppPermission.ReadMediaImages,
                AppPermission.ReadMediaVideo,
                AppPermission.ReadMediaAudio ->
                    PermissionStatus.Error("Use PHPhotoLibrary.requestAuthorization/UIDocumentPicker")

                // “normais” no Android — no iOS não há prompt
                AppPermission.Internet, AppPermission.NetworkState,
                AppPermission.Vibrate, AppPermission.WifiState,
                AppPermission.ChangeWifiState -> PermissionStatus.Granted
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

    private suspend fun askCamera(): PermissionStatus {
        val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        return when (status) {
            AVAuthorizationStatusAuthorized -> PermissionStatus.Granted
            AVAuthorizationStatusDenied -> PermissionStatus.PermanentlyDenied
            AVAuthorizationStatusNotDetermined -> {
                val def = CompletableDeferred<PermissionStatus>()
                requestAccessForMediaType(AVMediaTypeVideo, dispatch_get_main_queue()) { granted ->
                    def.complete(if (granted) PermissionStatus.Granted else PermissionStatus.Denied)
                }
                def.await()
            }
            else -> PermissionStatus.Denied
        }
    }
}