package org.apagar.shared.domain.model

data class InvoiceLineItem(
    val descriptionEs: String,
    val percentageChange: Int, // e.g., +35 or -10
    val amountUsd: Double
)

data class DebtInvoice(
    val invoiceNumber: String, // e.g., "VE-2026-8334"
    val country: CountryDebtProfile,
    val basePerCapitaUsd: Double,
    val lineItems: List<InvoiceLineItem>,
    val totalPayableUsd: Double,
    val monthlyInstallment40YearsUsd: Double
)
