package org.apagar.shared.presentation

import org.apagar.shared.domain.model.ChildrenBracket
import org.apagar.shared.domain.model.CountryDebtProfile
import org.apagar.shared.domain.model.DebtInvoice
import org.apagar.shared.domain.model.StealthOption
import org.apagar.shared.domain.model.StealthQuestion
import org.apagar.shared.domain.model.WorkSector

sealed interface QuizScreenStep {
    data object Intro : QuizScreenStep
    data class QuestionStep(val stepIndex: Int) : QuizScreenStep // 0..4
    data class Calculating(val statusMessageEs: String) : QuizScreenStep
    data class Result(val invoice: DebtInvoice) : QuizScreenStep
}

data class DebtQuizUiState(
    val step: QuizScreenStep = QuizScreenStep.Intro,
    val availableCountries: List<CountryDebtProfile> = emptyList(),
    val selectedCountry: CountryDebtProfile,
    val birthYear: Int = 1995,
    val selectedChildren: ChildrenBracket = ChildrenBracket.NONE,
    val selectedSector: WorkSector = WorkSector.PRIVATE_TECH,
    val stealthQuestion: StealthQuestion,
    val selectedStealthOption: StealthOption? = null
)
