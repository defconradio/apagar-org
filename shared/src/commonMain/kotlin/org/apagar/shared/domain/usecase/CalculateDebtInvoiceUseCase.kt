package org.apagar.shared.domain.usecase

import org.apagar.shared.domain.model.DebtInvoice
import org.apagar.shared.domain.model.InvoiceLineItem
import org.apagar.shared.domain.model.QuizSubmission
import kotlin.math.roundToInt
import kotlin.random.Random

class CalculateDebtInvoiceUseCase(
    private val random: Random = Random.Default
) {

    companion object {
        private const val CURRENT_YEAR = 2026
        private const val PAYMENT_MONTHS_40_YEARS = 480
    }

    operator fun invoke(submission: QuizSubmission): DebtInvoice {
        val baseDebt = submission.country.basePerCapitaDebtUsd
        val lineItems = mutableListOf<InvoiceLineItem>()

        // 1. Age Modifier
        val age = (CURRENT_YEAR - submission.birthYear).coerceAtLeast(0)
        val (ageModifier, ageReason) = when {
            age < 25 -> 0.15 to "Recargo por juventud (le quedan décadas para pagar)"
            age <= 55 -> 0.00 to "Tasa estándar de fuerza laboral activa"
            else -> -0.10 to "Descuento por desgaste histórico acumulado"
        }
        if (ageModifier != 0.0) {
            lineItems.add(
                InvoiceLineItem(
                    descriptionEs = ageReason,
                    percentageChange = (ageModifier * 100).roundToInt(),
                    amountUsd = baseDebt * ageModifier
                )
            )
        }

        // 2. Children Modifier
        val children = submission.childrenBracket
        lineItems.add(
            InvoiceLineItem(
                descriptionEs = children.invoiceReasonEs,
                percentageChange = (children.modifier * 100).roundToInt(),
                amountUsd = baseDebt * children.modifier
            )
        )

        // 3. Work Sector Modifier
        val sector = submission.workSector
        lineItems.add(
            InvoiceLineItem(
                descriptionEs = sector.invoiceReasonEs,
                percentageChange = (sector.modifier * 100).roundToInt(),
                amountUsd = baseDebt * sector.modifier
            )
        )

        // 4. Stealth Ideology Modifier
        val ideology = submission.selectedStealthOption.alignment
        lineItems.add(
            InvoiceLineItem(
                descriptionEs = ideology.invoiceReasonEs,
                percentageChange = (ideology.modifier * 100).roundToInt(),
                amountUsd = baseDebt * ideology.modifier
            )
        )

        // 5. Final Calculation (never drops below 10% of base debt)
        val adjustmentsSum = lineItems.sumOf { it.amountUsd }
        val totalPayable = (baseDebt + adjustmentsSum).coerceAtLeast(baseDebt * 0.10)
        val monthly40Years = totalPayable / PAYMENT_MONTHS_40_YEARS

        val randomSerial = random.nextInt(1000, 9999)
        val invoiceNumber = "${submission.country.code}-$CURRENT_YEAR-$randomSerial"

        return DebtInvoice(
            invoiceNumber = invoiceNumber,
            country = submission.country,
            basePerCapitaUsd = baseDebt,
            lineItems = lineItems,
            totalPayableUsd = totalPayable,
            monthlyInstallment40YearsUsd = monthly40Years
        )
    }
}
