package org.apagar.shared.domain.model

data class CountryDebtProfile(
    val code: String,
    val nameEs: String,
    val flagEmoji: String,
    val totalDebtUsd: Double,       // e.g., 237_544_000_000.0
    val totalPopulation: Long,      // e.g., 28_500_000L
    val activeTaxpayerRatio: Double,// e.g., 0.45 (45% economically active)
    val dataSourceNoteEs: String,   // e.g., "Estimación Deuda Externa Consolidada"
    val lastUpdatedYear: Int
) {
    /** Base debt per citizen before personal quiz modifiers */
    val basePerCapitaDebtUsd: Double
        get() = if (totalPopulation > 0) totalDebtUsd / totalPopulation else 0.0

    /** Satirical reference: debt if split only among active taxpayers */
    val activeTaxpayerQuotaUsd: Double
        get() = if (activeTaxpayerRatio > 0.0) basePerCapitaDebtUsd / activeTaxpayerRatio else basePerCapitaDebtUsd
}
