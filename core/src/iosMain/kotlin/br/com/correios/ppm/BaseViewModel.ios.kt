package br.com.correios.ppm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual open class BaseViewModel : ViewModel() {
    actual val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
}
