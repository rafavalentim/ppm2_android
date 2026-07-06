package br.com.correios.ppm.concursos.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kashif.cameraK.compose.CameraKScreen
import com.kashif.cameraK.compose.rememberCameraKState
import com.kashif.cameraK.permissions.providePermissions
import com.kashif.qrscannerplugin.rememberQRScannerPlugin

@Composable
actual fun BarcodeScannerScreen(onBarcodeScanned: (String) -> Unit) {
    val permissions = providePermissions()
    var hasCameraPermission by remember { mutableStateOf(permissions.hasCameraPermission()) }

    if (!hasCameraPermission) {
        permissions.RequestCameraPermission(
            onGranted = { hasCameraPermission = true },
            onDenied = {}
        )
        return
    }

    val qrScannerPlugin = rememberQRScannerPlugin()
    val cameraState by rememberCameraKState(
        setupPlugins = { stateHolder -> qrScannerPlugin.attachToStateHolder(stateHolder) }
    )

    LaunchedEffect(qrScannerPlugin) {
        qrScannerPlugin.getQrCodeFlow().collect { code -> onBarcodeScanned(code) }
    }

    CameraKScreen(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState
    ) { }
}
