package org.apagar.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import org.apagar.shared.domain.model.CountryDebtProfile
import org.apagar.shared.domain.model.QuizActivity
import org.apagar.shared.domain.model.StealthQuestion

interface DebtDataRepository {
    fun getCountries(): List<CountryDebtProfile>
    fun getRandomStealthQuestion(): StealthQuestion
    fun getRecentQuizActivities(): List<QuizActivity>
    fun observeQuizActivities(): Flow<QuizActivity>
}
