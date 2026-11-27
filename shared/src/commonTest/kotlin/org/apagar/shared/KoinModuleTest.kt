package org.apagar.shared

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.apagar.shared.di.initKoin
import org.apagar.shared.domain.repository.DebtDataRepository
import org.apagar.shared.domain.usecase.CalculateDebtInvoiceUseCase
import org.apagar.shared.presentation.DebtQuizViewModel
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KoinModuleTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun koinResolvesCoreAndAppDependencies() {
        val koinApp = initKoin {}
        val json: Json = koinApp.koin.get()
        val httpClient: HttpClient = koinApp.koin.get()
        val repository: DebtDataRepository = koinApp.koin.get()
        val useCase: CalculateDebtInvoiceUseCase = koinApp.koin.get()
        val viewModel: DebtQuizViewModel = koinApp.koin.get()

        assertNotNull(json)
        assertNotNull(httpClient)
        assertTrue(repository.getCountries().isNotEmpty())
        assertNotNull(useCase)
        assertNotNull(viewModel)

        viewModel.clear()
        httpClient.close()
    }
}


