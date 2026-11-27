package org.apagar.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.apagar.shared.presentation.DebtQuizViewModel
import org.apagar.shared.presentation.QuizScreenStep
import org.apagar.shared.presentation.ui.CalculatingScreen
import org.apagar.shared.presentation.ui.IntroScreen
import org.apagar.shared.presentation.ui.InvoiceReceiptScreen
import org.apagar.shared.presentation.ui.QuizStepperScreen
import org.apagar.shared.presentation.ui.theme.AppTheme
import org.koin.mp.KoinPlatform

@Composable
fun App(
    viewModel: DebtQuizViewModel = remember { KoinPlatform.getKoin().get<DebtQuizViewModel>() }
) {
    DisposableEffect(viewModel) {
        onDispose { viewModel.clear() }
    }

    val uiState by viewModel.uiState.collectAsState()

    AppTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.TopCenter
            ) {
                when (val step = uiState.step) {
                    is QuizScreenStep.Intro -> IntroScreen(
                        selectedCountry = uiState.selectedCountry,
                        onStartQuiz = viewModel::startQuiz
                    )
                    is QuizScreenStep.QuestionStep -> QuizStepperScreen(
                        uiState = uiState,
                        stepIndex = step.stepIndex,
                        onSelectCountry = viewModel::selectCountry,
                        onUpdateBirthYear = viewModel::updateBirthYear,
                        onSelectChildren = viewModel::selectChildren,
                        onSelectSector = viewModel::selectSector,
                        onSelectStealthOption = viewModel::selectStealthOption,
                        onNext = viewModel::nextStep,
                        onBack = viewModel::previousStep
                    )
                    is QuizScreenStep.Calculating -> CalculatingScreen(
                        statusMessageEs = step.statusMessageEs
                    )
                    is QuizScreenStep.Result -> InvoiceReceiptScreen(
                        invoice = step.invoice,
                        onReset = viewModel::resetQuiz
                    )
                }
            }
        }
    }
}
