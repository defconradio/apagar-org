package org.apagar.shared.di

import org.apagar.shared.data.repository.LocalDebtDataRepositoryImpl
import org.apagar.shared.domain.repository.DebtDataRepository
import org.apagar.shared.domain.usecase.CalculateDebtInvoiceUseCase
import org.apagar.shared.presentation.DebtQuizViewModel
import org.koin.dsl.module

val appModule = module {
    // Data Layer
    single<DebtDataRepository> { LocalDebtDataRepositoryImpl() }

    // Domain Layer
    factory { CalculateDebtInvoiceUseCase() }

    // Presentation Layer
    factory {
        DebtQuizViewModel(
            repository = get(),
            calculateDebtInvoiceUseCase = get()
        )
    }
}
