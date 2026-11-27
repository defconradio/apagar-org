package org.apagar.shared.presentation.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apagar.shared.domain.model.CountryDebtProfile
import org.apagar.shared.presentation.ui.theme.BabyBlue
import org.apagar.shared.presentation.ui.theme.HeroCream
import org.apagar.shared.presentation.ui.theme.HeroInk
import org.apagar.shared.presentation.ui.theme.InkMuted
import org.apagar.shared.presentation.ui.theme.StampRed
import org.apagar.shared.resources.Res
import org.apagar.shared.resources.hero
import org.apagar.shared.resources.space_grotesk
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

@Composable
fun IntroScreen(
    selectedCountry: CountryDebtProfile,
    onStartQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shimmerTransition = rememberInfiniteTransition(label = "hero_cta_shimmer")
    val shimmerPosition by shimmerTransition.animateFloat(
        initialValue = -500f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "hero_cta_shimmer_position"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(BabyBlue)
            .verticalScroll(rememberScrollState())
    ) {
        val bannerWidth = minOf(maxWidth, 1680.dp)
        val bannerHeight = bannerWidth * 9f / 16f

        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(bannerWidth)
                    .height(bannerHeight)
            ) {
                Image(
                    painter = painterResource(Res.drawable.hero),
                    contentDescription = "Ilustración principal de APAGAR.ORG",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Button(
                    onClick = onStartQuiz,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, HeroCream.copy(alpha = 0.35f)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HeroInk.copy(alpha = 0.45f),
                        contentColor = HeroCream
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .widthIn(min = 180.dp, max = 220.dp)
                        .height(48.dp)
                ) {
                    val shimmerBrush = Brush.linearGradient(
                        colors = listOf(
                            HeroCream.copy(alpha = 0.62f),
                            HeroCream,
                            HeroCream.copy(alpha = 0.62f)
                        ),
                        start = Offset(shimmerPosition - 180f, 0f),
                        end = Offset(shimmerPosition + 180f, 0f)
                    )

                    Text(
                        text = "Calcula mi deuda",
                        style = TextStyle(
                            brush = shimmerBrush,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                    Text(
                        text = " →",
                        style = MaterialTheme.typography.labelLarge,
                        color = HeroCream.copy(alpha = 0.78f),
                        fontWeight = FontWeight.Bold
                    )
                } /*else {
                    Card(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .widthIn(max = 560.dp)
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(2.dp, HeroInk)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "APAGAR.ORG • NOTIFICACIÓN FISCAL",
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = StampRed,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "¡Felicidades! Usted debe ${formatUsd(selectedCountry.basePerCapitaDebtUsd)} USD.",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "La deuda externa no se paga sola. Calcule su cuota patriótica en menos de 60 segundos y descubra cuánto le toca aportar para sacar al país adelante.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = InkMuted,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${selectedCountry.flagEmoji} Referencia base (${selectedCountry.nameEs}): ${selectedCountry.dataSourceNoteEs} (${selectedCountry.lastUpdatedYear})",
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                color = InkMuted,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = onStartQuiz,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = HeroInk,
                                    contentColor = HeroCream
                                )
                            ) {
                                Text(
                                    text = "Calcular mi factura",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }*/
            }
        }
    }
}

