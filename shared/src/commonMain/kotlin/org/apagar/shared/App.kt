package org.apagar.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.rememberScrollState
import kotlinx.coroutines.delay
import org.apagar.shared.domain.model.QuizActivity
import org.apagar.shared.presentation.DebtQuizViewModel
import org.apagar.shared.presentation.QuizScreenStep
import org.apagar.shared.presentation.ui.CalculatingScreen
import org.apagar.shared.presentation.ui.IntroScreen
import org.apagar.shared.presentation.ui.InvoiceReceiptScreen
import org.apagar.shared.presentation.ui.QuizStepperScreen
import org.apagar.shared.presentation.ui.theme.AppTheme
import org.apagar.shared.presentation.ui.theme.BabyBlue
import org.apagar.shared.presentation.ui.theme.HeroCream
import org.apagar.shared.presentation.ui.theme.HeroInk
import org.apagar.shared.resources.Res
import org.apagar.shared.resources.apagar_logo
import org.apagar.shared.resources.space_grotesk
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.koin.mp.KoinPlatform

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            containerColor = BabyBlue
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(BabyBlue)
            ) {
                BoxWithConstraints {
                    val logoWidth = if (maxWidth < 600.dp) 120.dp else 160.dp
                    val logoHeight = if (maxWidth < 600.dp) 48.dp else 64.dp
                    val headerTopPadding = if (maxWidth < 600.dp) 4.dp else 8.dp

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .background(BabyBlue)
                            .padding(start = 16.dp, top = headerTopPadding, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.apagar_logo),
                            contentDescription = "apagar.org",
                            modifier = Modifier
                                .width(logoWidth)
                                .height(logoHeight)
                                .weight(1f, fill = false)
                                .clickable(
                                    interactionSource = null,
                                    indication = null,
                                    onClick = viewModel::resetQuiz
                                ),
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.CenterStart
                        )
                    }
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val contentWidth = if (uiState.step is QuizScreenStep.Intro) {
                        minOf(maxWidth, 1680.dp)
                    } else {
                        minOf(maxWidth, 1680.dp)
                    }
                    val horizontalPadding = if (maxWidth < 600.dp) 16.dp else 24.dp

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .width(contentWidth)
                            .fillMaxSize()
                            .padding(horizontal = horizontalPadding)
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(HeroInk.copy(alpha = 0.88f))
                        .clipToBounds(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val tickerScrollState = rememberScrollState()
                    var tickerCopyWidth by remember { mutableStateOf(0f) }
                    var tickerSnapshotText by remember { mutableStateOf("") }

                    LaunchedEffect(uiState.tickerActivities) {
                        if (tickerSnapshotText.isEmpty() && uiState.tickerActivities.isNotEmpty()) {
                            tickerSnapshotText = uiState.tickerActivities.joinToString("   •   ") { activity ->
                                "${activity.fullName}(${activity.countryCode}) · $${formatTickerAmount(activity)}"
                            }
                        }
                    }

                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(90_000)
                            tickerScrollState.scrollTo(0)
                            tickerCopyWidth = 0f
                            tickerSnapshotText = uiState.tickerActivities.joinToString("   •   ") { activity ->
                                "${activity.fullName}(${activity.countryCode}) · $${formatTickerAmount(activity)}"
                            }
                        }
                    }

                    if (uiState.tickerActivities.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .horizontalScroll(tickerScrollState)
                                .onGloballyPositioned { coordinates ->
                                    tickerCopyWidth = coordinates.size.width / 2f
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(2) {
                                Text(
                                    text = tickerSnapshotText,
                                    modifier = Modifier.padding(end = 48.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = HeroCream,
                                    letterSpacing = 1.sp,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    LaunchedEffect(tickerCopyWidth) {
                        while (tickerCopyWidth > 0f) {
                            if (tickerScrollState.value >= tickerCopyWidth.toInt()) {
                                tickerScrollState.scrollTo(0)
                            } else {
                                tickerScrollState.scrollBy(0.6f)
                            }
                            delay(16)
                        }
                    }
                }
            }
        }
    }
}

private fun formatTickerAmount(activity: QuizActivity): String {
    val cents = (activity.debtAmountUsd * 100).toLong()
    val whole = cents / 100
    val fraction = (cents % 100).toString().padStart(2, '0')
    val grouped = whole.toString()
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()

    return "$grouped,$fraction"
}
