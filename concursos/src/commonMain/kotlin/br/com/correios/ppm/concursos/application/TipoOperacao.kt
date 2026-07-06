package br.com.correios.ppm.concursos.application

enum class TipoOperacao(val codigo: String, val label: String) {
    PRIMEIRO_DIA_ENTREGA("1", "1º dia Entrega"),
    PRIMEIRO_DIA_COLETA("2", "1º dia Coleta"),
    SEGUNDO_DIA_ENTREGA("3", "2º dia Entrega"),
    SEGUNDO_DIA_COLETA("4", "2º dia Coleta"),
    AUSENTE("5", "Ausente");

    companion object {
        val options: List<String> = entries.map { it.label }

        fun fromLabel(label: String): TipoOperacao =
            entries.firstOrNull { it.label == label } ?: AUSENTE
    }
}
