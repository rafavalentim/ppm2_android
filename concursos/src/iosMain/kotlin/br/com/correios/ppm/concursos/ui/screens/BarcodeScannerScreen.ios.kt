package br.com.correios.ppm.concursos.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kashif.cameraK.compose.CameraKScreen
import com.kashif.cameraK.compose.rememberCameraKState
import com.kashif.cameraK.permissions.providePermissions
import com.kashif.qrscannerplugin.rememberQRScannerPlugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
actual fun BarcodeScannerScreen(onBarcodeScanned: (String) -> Unit) {
    val permissions = providePermissions()
    var hasCameraPermission by remember { mutableStateOf(permissions.hasCameraPermission()) }
    val coroutineScope = rememberCoroutineScope()

    if (!hasCameraPermission) {
        permissions.RequestCameraPermission(
            onGranted = {
                // AVCaptureDevice's completion handler runs on a background queue;
                // mutating Compose state off the main thread crashes the app on iOS.
                coroutineScope.launch(Dispatchers.Main) { hasCameraPermission = true }
            },
            onDenied = {}
        )
        return
    }

    val qrScannerPlugin = rememberQRScannerPlugin()
    val cameraState by rememberCameraKState(
        setupPlugins = { holder -> qrScannerPlugin.attachToStateHolder(holder) }
    )

    LaunchedEffect(qrScannerPlugin) {
        qrScannerPlugin.getQrCodeFlow().collect { code -> onBarcodeScanned(code) }
    }

    CameraKScreen(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState
    ) { }
}
