package org.apagar.shared.domain.model

data class QuizSubmission(
    val country: CountryDebtProfile,
    val birthYear: Int,
    val childrenBracket: ChildrenBracket,
    val workSector: WorkSector,
    val selectedStealthOption: StealthOption
)
