package br.com.correios.ppm.ui.components

// Evento de UI genérico (pode crescer no futuro)
sealed interface UiEvent {
    data class ShowMessage(
        val message: String,
        val actionLabel: String? = null
    ) : UiEvent
}