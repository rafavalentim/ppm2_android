package br.com.correios.ppm

import kotlinx.coroutines.CoroutineScope

expect open class BaseViewModel() {

    val scope: CoroutineScope

}