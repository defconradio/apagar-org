package org.apagar.shared.di

import org.apagar.shared.data.repository.LocalDebtDataRepositoryImpl
import org.apagar.shared.domain.repository.DebtDataRepository
import org.apagar.shared.domain.usecase.CalculateDebtInvoiceUseCase
import org.apagar.shared.presentation.DebtQuizViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val sharedModule = module {
    // Data Layer
    single<DebtDataRepository> { LocalDebtDataRepositoryImpl() }

    // Domain Layer
    factory { CalculateDebtInvoiceUseCase() }

    // Presentation Layer
    single { DebtQuizViewModel(repository = get(), calculateDebtInvoiceUseCase = get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(sharedModule)
}
