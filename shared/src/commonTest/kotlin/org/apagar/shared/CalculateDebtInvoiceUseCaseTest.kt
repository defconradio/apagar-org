package org.apagar.shared

import org.apagar.shared.domain.model.ChildrenBracket
import org.apagar.shared.domain.model.CountryDebtProfile
import org.apagar.shared.domain.model.IdeologyAlignment
import org.apagar.shared.domain.model.QuizSubmission
import org.apagar.shared.domain.model.StealthOption
import org.apagar.shared.domain.model.WorkSector
import org.apagar.shared.domain.usecase.CalculateDebtInvoiceUseCase
import org.apagar.shared.presentation.ui.formatUsd
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CalculateDebtInvoiceUseCaseTest {

    private val venezuela = CountryDebtProfile(
        code = "VE",
        nameEs = "Venezuela",
        flagEmoji = "🇻🇪",
        totalDebtUsd = 100_000_000.0,
        totalPopulation = 10_000L, // $10,000.00 base per capita for clean math
        activeTaxpayerRatio = 0.50,
        dataSourceNoteEs = "Test",
        lastUpdatedYear = 2026
    )

    @Test
    fun youngLeftCryptoNoKidsAppliesAllSurcharges() {
        val useCase = CalculateDebtInvoiceUseCase(random = Random(42))
        val submission = QuizSubmission(
            country = venezuela,
            birthYear = 2004, // age 22 (<25 -> +15%)
            childrenBracket = ChildrenBracket.NONE, // +20%
            workSector = WorkSector.CRYPTO_INFLUENCER, // +30%
            selectedStealthOption = StealthOption("id", "opt", IdeologyAlignment.LEFT_WING) // +35%
        )

        val invoice = useCase(submission)

        // 10,000 * (1 + 0.15 + 0.20 + 0.30 + 0.35) = 20,000.0
        assertTrue(abs(invoice.totalPayableUsd - 20_000.0) < 0.01)
        assertEquals(4, invoice.lineItems.size)
        assertTrue(invoice.invoiceNumber.startsWith("VE-2026-"))
        assertTrue(abs(invoice.monthlyInstallment40YearsUsd - (20_000.0 / 480.0)) < 0.01)
    }

    @Test
    fun olderRightPrivateParentAppliesAllDiscounts() {
        val useCase = CalculateDebtInvoiceUseCase(random = Random(42))
        val submission = QuizSubmission(
            country = venezuela,
            birthYear = 1960, // age 66 (>55 -> -10%)
            childrenBracket = ChildrenBracket.THREE_OR_MORE, // -15%
            workSector = WorkSector.PRIVATE_TECH, // -10%
            selectedStealthOption = StealthOption("id", "opt", IdeologyAlignment.RIGHT_WING) // -10%
        )

        val invoice = useCase(submission)

        // 10,000 * (1 - 0.10 - 0.15 - 0.10 - 0.10) = 5,500.0
        assertTrue(abs(invoice.totalPayableUsd - 5_500.0) < 0.01)
        assertEquals(4, invoice.lineItems.size)
    }

    @Test
    fun midAgeOmitsAgeLineItem() {
        val useCase = CalculateDebtInvoiceUseCase(random = Random(42))
        val submission = QuizSubmission(
            country = venezuela,
            birthYear = 1995, // age 31 (25..55 -> 0%)
            childrenBracket = ChildrenBracket.ONE_TO_TWO,
            workSector = WorkSector.PRIVATE_TECH,
            selectedStealthOption = StealthOption("id", "opt", IdeologyAlignment.NEUTRAL_PRAGMATIC)
        )

        val invoice = useCase(submission)
        assertEquals(3, invoice.lineItems.size)
    }

    @Test
    fun formatUsdFormatsThousandsAndSigns() {
        assertEquals("$8,334.88", formatUsd(8334.88))
        assertEquals("+$1,250.50", formatUsd(1250.5, showSign = true))
        assertEquals("-$500.00", formatUsd(-500.0, showSign = true))
        assertEquals("$0.00", formatUsd(0.0))
    }
}

