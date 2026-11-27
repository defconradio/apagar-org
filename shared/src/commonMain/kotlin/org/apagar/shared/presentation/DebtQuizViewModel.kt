package org.apagar.shared.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apagar.shared.domain.model.ChildrenBracket
import org.apagar.shared.domain.model.CountryDebtProfile
import org.apagar.shared.domain.model.QuizSubmission
import org.apagar.shared.domain.model.StealthOption
import org.apagar.shared.domain.model.WorkSector
import org.apagar.shared.domain.repository.DebtDataRepository
import org.apagar.shared.domain.usecase.CalculateDebtInvoiceUseCase

class DebtQuizViewModel(
    private val repository: DebtDataRepository,
    private val calculateDebtInvoiceUseCase: CalculateDebtInvoiceUseCase
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val countries = repository.getCountries()
    private val initialCountry = countries.first()
    private val initialStealthQuestion = repository.getRandomStealthQuestion()

    private val _uiState = MutableStateFlow(
        DebtQuizUiState(
            step = QuizScreenStep.Intro,
            availableCountries = countries,
            selectedCountry = initialCountry,
            stealthQuestion = initialStealthQuestion
        )
    )
    val uiState: StateFlow<DebtQuizUiState> = _uiState.asStateFlow()

    fun startQuiz() {
        _uiState.update {
            it.copy(
                step = QuizScreenStep.QuestionStep(0),
                stealthQuestion = repository.getRandomStealthQuestion(),
                selectedStealthOption = null
            )
        }
    }

    fun selectCountry(country: CountryDebtProfile) {
        _uiState.update { it.copy(selectedCountry = country) }
    }

    fun updateBirthYear(year: Int) {
        _uiState.update { it.copy(birthYear = year.coerceIn(1930, 2025)) }
    }

    fun selectChildren(bracket: ChildrenBracket) {
        _uiState.update { it.copy(selectedChildren = bracket) }
    }

    fun selectSector(sector: WorkSector) {
        _uiState.update { it.copy(selectedSector = sector) }
    }

    fun selectStealthOption(option: StealthOption) {
        _uiState.update { it.copy(selectedStealthOption = option) }
    }

    fun nextStep() {
        val currentStep = _uiState.value.step
        if (currentStep is QuizScreenStep.QuestionStep) {
            if (currentStep.stepIndex < 4) {
                _uiState.update { it.copy(step = QuizScreenStep.QuestionStep(currentStep.stepIndex + 1)) }
            } else {
                submitQuiz()
            }
        }
    }

    fun previousStep() {
        val currentStep = _uiState.value.step
        if (currentStep is QuizScreenStep.QuestionStep) {
            if (currentStep.stepIndex > 0) {
                _uiState.update { it.copy(step = QuizScreenStep.QuestionStep(currentStep.stepIndex - 1)) }
            } else {
                _uiState.update { it.copy(step = QuizScreenStep.Intro) }
            }
        }
    }

    private fun submitQuiz() {
        val state = _uiState.value
        val stealthOption = state.selectedStealthOption ?: state.stealthQuestion.options.last()

        val submission = QuizSubmission(
            country = state.selectedCountry,
            birthYear = state.birthYear,
            childrenBracket = state.selectedChildren,
            workSector = state.selectedSector,
            selectedStealthOption = stealthOption
        )

        viewModelScope.launch {
            _uiState.update { it.copy(step = QuizScreenStep.Calculating("Consultando reservas del Banco Central...")) }
            delay(700)
            _uiState.update { it.copy(step = QuizScreenStep.Calculating("Calculando intereses moratorios acumulados...")) }
            delay(700)
            _uiState.update { it.copy(step = QuizScreenStep.Calculating("Emitiendo factura oficial inapelable...")) }
            delay(600)

            val invoice = calculateDebtInvoiceUseCase(submission)
            _uiState.update { it.copy(step = QuizScreenStep.Result(invoice)) }
        }
    }

    fun resetQuiz() {
        _uiState.update {
            it.copy(
                step = QuizScreenStep.Intro,
                stealthQuestion = repository.getRandomStealthQuestion(),
                selectedStealthOption = null
            )
        }
    }
}
