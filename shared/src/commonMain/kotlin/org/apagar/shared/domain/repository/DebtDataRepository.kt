package org.apagar.shared.domain.repository

import org.apagar.shared.domain.model.CountryDebtProfile
import org.apagar.shared.domain.model.StealthQuestion

interface DebtDataRepository {
    fun getCountries(): List<CountryDebtProfile>
    fun getRandomStealthQuestion(): StealthQuestion
}
