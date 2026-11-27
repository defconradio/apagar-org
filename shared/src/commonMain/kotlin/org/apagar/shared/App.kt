package org.apagar.shared

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import org.apagar.shared.resources.space_grotesk
import org.jetbrains.compose.resources.Font
import org.koin.mp.KoinPlatform

@Composable
fun App(
    viewModel: DebtQuizViewModel = remember { KoinPlatform.getKoin().get<DebtQuizViewModel>() }
) {
    DisposableEffect(viewModel) {
        onDispose { viewModel.clear() }
    }

    val uiState by viewModel.uiState.collectAsState()

    val tickerTransition = rememberInfiniteTransition(label = "footer_ticker")
    val tickerOffset by tickerTransition.animateFloat(
        initialValue = 900f,
        targetValue = -900f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "footer_ticker_offset"
    )

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .background(BabyBlue)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "apagar.org",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = FontFamily(Font(Res.font.space_grotesk)),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.8).sp,
                        color = HeroCream,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = viewModel::resetQuiz)
                    )

                    if (uiState.step !is QuizScreenStep.Intro) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clickable(onClick = viewModel::resetQuiz),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "←",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = HeroCream
                            )
                        }
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
                    val tickerText = "APAGAR.ORG  •  DEUDA EXTERNA  •  CUOTA PATRIÓTICA  •  "
                    Row(
                        modifier = Modifier.graphicsLayer {
                            translationX = tickerOffset
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(8) {
                            Text(
                                text = tickerText,
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
            }
        }
    }
}
