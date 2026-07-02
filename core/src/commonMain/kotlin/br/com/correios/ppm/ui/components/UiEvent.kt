package br.com.correios.ppm.ui.components

sealed interface UiEvent {
    data class ShowMessage(
        val message: String,
        val actionLabel: String? = null
    ) : UiEvent

    data object LoginSuccess : UiEvent
}
