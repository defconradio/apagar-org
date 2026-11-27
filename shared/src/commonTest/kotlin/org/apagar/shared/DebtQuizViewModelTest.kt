package org.apagar.shared

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.apagar.shared.data.repository.LocalDebtDataRepositoryImpl
import org.apagar.shared.domain.usecase.CalculateDebtInvoiceUseCase
import org.apagar.shared.presentation.DebtQuizViewModel
import org.apagar.shared.presentation.QuizScreenStep
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class DebtQuizViewModelTest {

    @Test
    fun fullQuizFlowProducesInvoiceAndResetsCleanly() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = DebtQuizViewModel(
            repository = LocalDebtDataRepositoryImpl(),
            calculateDebtInvoiceUseCase = CalculateDebtInvoiceUseCase(random = Random(1)),
            dispatcher = dispatcher
        )

        assertIs<QuizScreenStep.Intro>(viewModel.uiState.value.step)

        viewModel.startQuiz()
        assertEquals(QuizScreenStep.QuestionStep(0), viewModel.uiState.value.step)

        // Advance through steps 0 -> 4
        repeat(4) { viewModel.nextStep() }
        assertEquals(QuizScreenStep.QuestionStep(4), viewModel.uiState.value.step)

        // Select stealth option and submit
        val option = viewModel.uiState.value.stealthQuestion.options.first()
        viewModel.selectStealthOption(option)
        viewModel.nextStep()

        // Complete simulated delays
        advanceUntilIdle()
        assertIs<QuizScreenStep.Result>(viewModel.uiState.value.step)

        // Reset back to Intro
        viewModel.resetQuiz()
        assertIs<QuizScreenStep.Intro>(viewModel.uiState.value.step)

        viewModel.clear()
    }
}

