package org.apagar.shared.data.repository

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.apagar.shared.data.dummy.DummyQuizActivityData
import org.apagar.shared.domain.model.CountryDebtProfile
import org.apagar.shared.domain.model.IdeologyAlignment
import org.apagar.shared.domain.model.QuizActivity
import org.apagar.shared.domain.model.StealthOption
import org.apagar.shared.domain.model.StealthQuestion
import org.apagar.shared.domain.repository.DebtDataRepository

class LocalDebtDataRepositoryImpl : DebtDataRepository {

    private val countries = listOf(
        CountryDebtProfile(
            code = "VE",
            nameEs = "Venezuela",
            flagEmoji = "🇻🇪",
            totalDebtUsd = 237_544_000_000.0, // $237.544 billion
            totalPopulation = 28_500_000L,    // 28.5 million -> $8,334.88 base/person
            activeTaxpayerRatio = 0.42,
            dataSourceNoteEs = "Deuda Externa Total Consolidada",
            lastUpdatedYear = 2026
        ),
        CountryDebtProfile(
            code = "AR",
            nameEs = "Argentina",
            flagEmoji = "🇦🇷",
            totalDebtUsd = 460_000_000_000.0,
            totalPopulation = 46_700_000L,
            activeTaxpayerRatio = 0.48,
            dataSourceNoteEs = "Deuda Pública Bruta Nacional",
            lastUpdatedYear = 2026
        ),
        CountryDebtProfile(
            code = "US",
            nameEs = "Estados Unidos",
            flagEmoji = "🇺🇸",
            totalDebtUsd = 35_400_000_000_000.0,
            totalPopulation = 340_000_000L,
            activeTaxpayerRatio = 0.50,
            dataSourceNoteEs = "Deuda Pública Federal Total",
            lastUpdatedYear = 2026
        ),
        CountryDebtProfile(
            code = "ES",
            nameEs = "España",
            flagEmoji = "🇪🇸",
            totalDebtUsd = 1_650_000_000_000.0,
            totalPopulation = 48_600_000L,
            activeTaxpayerRatio = 0.46,
            dataSourceNoteEs = "Deuda Pública según Protocolo PDE",
            lastUpdatedYear = 2026
        )
    )

    private val stealthQuestions = listOf(
        StealthQuestion(
            id = "q_island_book",
            questionEs = "Si quedaras atrapado en una isla desierta, ¿qué lectura preferirías tener?",
            options = listOf(
                StealthOption("q_island_1", "Las venas abiertas de América Latina o El Capital", IdeologyAlignment.LEFT_WING),
                StealthOption("q_island_2", "La Riqueza de las Naciones o Padre Rico, Padre Pobre", IdeologyAlignment.RIGHT_WING),
                StealthOption("q_island_3", "Un manual para hacer fuego y destilar agua", IdeologyAlignment.NEUTRAL_PRAGMATIC)
            )
        ),
        StealthQuestion(
            id = "q_found_money",
            questionEs = "Te encuentras un billete de $100 en la acera sin dueño a la vista. ¿Qué es lo más justo?",
            options = listOf(
                StealthOption("q_money_1", "Repartirlo en partes iguales entre todos los que iban pasando", IdeologyAlignment.LEFT_WING),
                StealthOption("q_money_2", "Quedártelo e invertirlo: el que lo encuentra asume el riesgo", IdeologyAlignment.RIGHT_WING),
                StealthOption("q_money_3", "Gastarlo hoy mismo antes de que la inflación se lo coma", IdeologyAlignment.NEUTRAL_PRAGMATIC)
            )
        ),
        StealthQuestion(
            id = "q_bread_price",
            questionEs = "Cuando sube el precio del pan en la panadería, ¿cuál es tu primera sospecha?",
            options = listOf(
                StealthOption("q_bread_1", "Especulación de los empresarios y acaparamiento", IdeologyAlignment.LEFT_WING),
                StealthOption("q_bread_2", "El Banco Central imprimió demasiados billetes sin respaldo", IdeologyAlignment.RIGHT_WING),
                StealthOption("q_bread_3", "Subió la harina importada o faltó levadura", IdeologyAlignment.NEUTRAL_PRAGMATIC)
            )
        )
    )

    override fun getCountries(): List<CountryDebtProfile> = countries

    override fun getRandomStealthQuestion(): StealthQuestion = stealthQuestions.random()

    // Método para obtener las actividades de cuestionario más recientes
    override fun getRecentQuizActivities(): List<QuizActivity> =
        DummyQuizActivityData.recentActivities(count = 500)

    override fun observeQuizActivities(): Flow<QuizActivity> = flow {
        emit(DummyQuizActivityData.randomActivity())
        while (currentCoroutineContext().isActive) {
            delay(6500)
            emit(DummyQuizActivityData.randomActivity())
        }
    }
}
