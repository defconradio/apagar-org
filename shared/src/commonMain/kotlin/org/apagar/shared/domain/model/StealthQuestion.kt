package org.apagar.shared.domain.model

data class StealthOption(
    val id: String,
    val textEs: String,
    val alignment: IdeologyAlignment
)

data class StealthQuestion(
    val id: String,
    val questionEs: String,
    val options: List<StealthOption>
)
