package br.com.correios.ppm.config

enum class AppPermission {
    // “Dangerous” no Android / prompt nos outros quando aplicável
    Camera,
    FineLocation,
    CoarseLocation,
    ReadPhoneState,
    ReadMediaImages, ReadMediaVideo, ReadMediaAudio,

    // Especiais Android (via Settings)
    ManageExternalStorage,
    RequestInstallPackages,

    // “Normais” no Android; não têm prompt
    Internet, NetworkState, Vibrate, WifiState, ChangeWifiState
}

sealed class PermissionStatus {
    data object Granted : PermissionStatus()
    data object Denied : PermissionStatus()
    data object PermanentlyDenied : PermissionStatus() // “never ask again” / bloqueado no SO
    data class Error(val reason: String) : PermissionStatus()
}

interface Permissions {
    suspend fun ensureOne(permission: AppPermission): PermissionStatus =
        ensure(permission)[permission] ?: PermissionStatus.Error("Unknown") // aqui 'ensure(vararg ...)' é selecionado


    suspend fun ensure(vararg permissions: AppPermission): Map<AppPermission, PermissionStatus>
}
