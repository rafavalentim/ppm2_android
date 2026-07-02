package br.com.correios.ppm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

actual open class BaseViewModel : ViewModel() {

    actual val scope: CoroutineScope
        get() = CoroutineScope(Dispatchers.IO)
}
