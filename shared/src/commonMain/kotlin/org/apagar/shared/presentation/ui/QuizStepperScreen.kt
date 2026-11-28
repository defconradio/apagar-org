package org.apagar.shared.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.apagar.shared.domain.model.ChildrenBracket
import org.apagar.shared.domain.model.CountryDebtProfile
import org.apagar.shared.domain.model.StealthOption
import org.apagar.shared.domain.model.WorkSector
import org.apagar.shared.presentation.DebtQuizUiState
import org.apagar.shared.presentation.DebtQuizViewModel
import org.apagar.shared.presentation.ui.theme.BabyBlue
import org.apagar.shared.presentation.ui.theme.HeroCream
import org.apagar.shared.presentation.ui.theme.HeroInk
import org.apagar.shared.presentation.ui.theme.InkMuted
import kotlin.math.roundToInt

@Composable
fun QuizStepperScreen(
    uiState: DebtQuizUiState,
    stepIndex: Int,
    onSelectCountry: (CountryDebtProfile) -> Unit,
    onUpdateBirthYear: (Int) -> Unit,
    onSelectChildren: (ChildrenBracket) -> Unit,
    onSelectSector: (WorkSector) -> Unit,
    onSelectStealthOption: (StealthOption) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSteps = DebtQuizViewModel.TOTAL_STEPS
    val progress = (stepIndex + 1).toFloat() / totalSteps.toFloat()
    val isLastStep = stepIndex == totalSteps - 1
    val canProceed = !isLastStep || uiState.selectedStealthOption != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "PASO ${stepIndex + 1} DE $totalSteps",
                style = MaterialTheme.typography.labelLarge,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = HeroCream
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = HeroCream,
                trackColor = HeroCream.copy(alpha = 0.25f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            when (stepIndex) {
                0 -> CountryStep(
                    countries = uiState.availableCountries,
                    selected = uiState.selectedCountry,
                    onSelect = onSelectCountry
                )
                1 -> BirthYearStep(
                    birthYear = uiState.birthYear,
                    onUpdateBirthYear = onUpdateBirthYear
                )
                2 -> ChildrenStep(
                    selected = uiState.selectedChildren,
                    onSelect = onSelectChildren
                )
                3 -> WorkSectorStep(
                    selected = uiState.selectedSector,
                    onSelect = onSelectSector
                )
                else -> StealthQuestionStep(
                    questionEs = uiState.stealthQuestion.questionEs,
                    options = uiState.stealthQuestion.options,
                    selected = uiState.selectedStealthOption,
                    onSelect = onSelectStealthOption
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = HeroCream
                ),
                border = BorderStroke(1.dp, HeroCream.copy(alpha = 0.65f)),
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
            ) {
                Text("Atrás")
            }
            Button(
                onClick = onNext,
                enabled = canProceed,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HeroInk,
                    contentColor = HeroCream,
                    disabledContainerColor = HeroInk.copy(alpha = 0.38f),
                    disabledContentColor = HeroCream.copy(alpha = 0.55f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
            ) {
                Text(if (isLastStep) "Emitir factura" else "Siguiente")
            }
        }
    }
}

@Composable
private fun CountryStep(
    countries: List<CountryDebtProfile>,
    selected: CountryDebtProfile,
    onSelect: (CountryDebtProfile) -> Unit
) {
    Text(
        text = "País de residencia u origen",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = HeroCream
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Seleccione el tesoro nacional al que desea contribuir hoy.",
        style = MaterialTheme.typography.titleMedium,
        color = HeroCream.copy(alpha = 0.82f)
    )
    Spacer(modifier = Modifier.height(16.dp))

    countries.forEach { country ->
        SelectableOptionCard(
            title = country.nameEs,
            subtitle = "Cuota base: ${formatUsd(country.basePerCapitaDebtUsd)} USD",
            selected = country.code == selected.code,
            onClick = { onSelect(country) },
            leadingContent = { CountryBadge(country.code) }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun BirthYearStep(
    birthYear: Int,
    onUpdateBirthYear: (Int) -> Unit
) {
    val estimatedAge = (2026 - birthYear).coerceAtLeast(0)

    Text(
        text = "¿En qué año nació?",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = HeroCream
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Esto determina cuántos años productivos le quedan para cancelar la deuda.",
        style = MaterialTheme.typography.titleMedium,
        color = HeroCream.copy(alpha = 0.82f)
    )
    Spacer(modifier = Modifier.height(24.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = HeroCream),
        border = BorderStroke(1.dp, HeroInk)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = birthYear.toString(),
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                color = HeroInk
            )
            Text(
                text = "Edad estimada en 2026: $estimatedAge años",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
            Spacer(modifier = Modifier.height(16.dp))
            Slider(
                value = birthYear.toFloat(),
                onValueChange = { onUpdateBirthYear(it.roundToInt()) },
                valueRange = 1930f..2025f,
                colors = SliderDefaults.colors(
                    thumbColor = HeroInk,
                    activeTrackColor = HeroInk,
                    inactiveTrackColor = BabyBlue.copy(alpha = 0.45f)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onUpdateBirthYear(birthYear - 1) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HeroInk),
                    border = BorderStroke(1.dp, HeroInk),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("-1 año")
                }
                OutlinedButton(
                    onClick = { onUpdateBirthYear(birthYear + 1) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HeroInk),
                    border = BorderStroke(1.dp, HeroInk),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+1 año")
                }
            }
        }
    }
}

@Composable
private fun ChildrenStep(
    selected: ChildrenBracket,
    onSelect: (ChildrenBracket) -> Unit
) {
    Text(
        text = "¿Cuántos hijos tiene?",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = HeroCream
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Más hijos significan más futuros contribuyentes para compartir la cuenta.",
        style = MaterialTheme.typography.titleMedium,
        color = HeroCream.copy(alpha = 0.82f)
    )
    Spacer(modifier = Modifier.height(16.dp))

    ChildrenBracket.entries.forEach { bracket ->
        SelectableOptionCard(
            title = bracket.labelEs,
            subtitle = null,
            selected = bracket == selected,
            onClick = { onSelect(bracket) }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun WorkSectorStep(
    selected: WorkSector,
    onSelect: (WorkSector) -> Unit
) {
    Text(
        text = "¿En qué sector se desempeña?",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = HeroCream
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Necesitamos evaluar su nivel de contribución real al PIB.",
        style = MaterialTheme.typography.titleMedium,
        color = HeroCream.copy(alpha = 0.82f)
    )
    Spacer(modifier = Modifier.height(16.dp))

    WorkSector.entries.forEach { sector ->
        SelectableOptionCard(
            title = sector.labelEs,
            subtitle = null,
            selected = sector == selected,
            onClick = { onSelect(sector) }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun StealthQuestionStep(
    questionEs: String,
    options: List<StealthOption>,
    selected: StealthOption?,
    onSelect: (StealthOption) -> Unit
) {
    Text(
        text = "Pregunta final de perfil ciudadano",
        style = MaterialTheme.typography.labelMedium,
        fontFamily = FontFamily.Monospace,
        color = InkMuted
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = questionEs,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = HeroCream
    )
    Spacer(modifier = Modifier.height(16.dp))

    options.forEach { option ->
        SelectableOptionCard(
            title = option.textEs,
            subtitle = null,
            selected = selected?.id == option.id,
            onClick = { onSelect(option) }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun SelectableOptionCard(
    title: String,
    subtitle: String?,
    selected: Boolean,
    onClick: () -> Unit,
    leadingContent: (@Composable () -> Unit)? = null
) {
    val borderColor = if (selected) HeroCream else HeroCream.copy(alpha = 0.72f)
    val containerColor = if (selected) HeroInk else HeroCream

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent?.invoke()
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = HeroCream,
                    unselectedColor = HeroInk.copy(alpha = 0.55f)
                )
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) HeroCream else HeroInk
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        color = if (selected) HeroCream.copy(alpha = 0.82f) else InkMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryBadge(countryCode: String) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(HeroInk),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = countryCode,
            style = MaterialTheme.typography.labelLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraBold,
            color = HeroCream
        )
    }
}

