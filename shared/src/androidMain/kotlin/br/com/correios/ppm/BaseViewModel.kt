package br.com.correios.ppm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

actual open class BaseViewModel : ViewModel() {

    actual val scope = viewModelScope
    
}