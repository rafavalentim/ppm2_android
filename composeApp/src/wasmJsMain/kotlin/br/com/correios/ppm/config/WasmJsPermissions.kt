package br.com.correios.ppm.config

class WasmJsPermissions : Permissions {
    override suspend fun ensure(vararg permissions: AppPermission): Map<AppPermission, PermissionStatus> {
        val out = mutableMapOf<AppPermission, PermissionStatus>()
        permissions.forEach { p ->
            out[p] = when (p) {
                AppPermission.Internet, AppPermission.NetworkState,
                AppPermission.Vibrate, AppPermission.WifiState,
                AppPermission.ChangeWifiState -> PermissionStatus.Granted

                AppPermission.Camera,
                AppPermission.FineLocation, AppPermission.CoarseLocation,
                AppPermission.ReadPhoneState,
                AppPermission.ReadMediaImages, AppPermission.ReadMediaVideo, AppPermission.ReadMediaAudio,
                AppPermission.ManageExternalStorage, AppPermission.RequestInstallPackages ->
                    PermissionStatus.Error("Permissão não aplicável no navegador")
            }
        }
        return out
    }
}