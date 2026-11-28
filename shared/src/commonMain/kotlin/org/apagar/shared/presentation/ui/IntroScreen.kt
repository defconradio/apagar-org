package org.apagar.shared.presentation.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
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
        val availableWidth = maxWidth - if (maxWidth >= 600.dp) 48.dp else 32.dp
        val viewportBannerWidth = if (maxWidth >= 600.dp) {
            (maxHeight - 32.dp) * 1792f / 592f
        } else {
            availableWidth
        }
        val bannerWidth = if (maxWidth >= 600.dp) {
            minOf(availableWidth, 1680.dp, viewportBannerWidth)
        } else {
            availableWidth
        }
        val bannerHeight = bannerWidth * 592f / 1792f

        val isMobileBanner = maxWidth < 600.dp
        val ctaWidth = if (isMobileBanner) 150.dp else bannerWidth.coerceIn(180.dp, 220.dp)
        val ctaHeight = if (isMobileBanner) 36.dp else 48.dp
        val ctaPadding = if (isMobileBanner) 8.dp else 12.dp

        val topBannerPadding = if (maxWidth >= 600.dp) 24.dp else 16.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topBannerPadding)
        ) {
            val bannerInteraction = remember { MutableInteractionSource() }
            val isBannerHovered by bannerInteraction.collectIsHoveredAsState()
            val isBannerPressed by bannerInteraction.collectIsPressedAsState()

            val bannerScale by animateFloatAsState(
                targetValue = if (isBannerPressed) 0.995f else if (isBannerHovered) 1.018f else 1f,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                label = "banner_scale"
            )
            val bannerElevation by animateDpAsState(
                targetValue = if (isBannerHovered) 16.dp else 4.dp,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                label = "banner_elevation"
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(bannerWidth)
                    .height(bannerHeight)
                    .shadow(
                        elevation = bannerElevation,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .graphicsLayer {
                        scaleX = bannerScale
                        scaleY = bannerScale
                    }
                    .clickable(
                        interactionSource = bannerInteraction,
                        indication = null,
                        onClick = onStartQuiz
                    )
            ) {
                Image(
                    painter = painterResource(Res.drawable.hero),
                    contentDescription = "Ilustración principal de APAGAR.ORG",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(ctaPadding)
                        .width(ctaWidth)
                        .height(ctaHeight)
                        .clip(RoundedCornerShape(if (isMobileBanner) 4.dp else 6.dp))
                        .background(HeroInk.copy(alpha = 0.28f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onStartQuiz
                        )
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
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
                        text = "calcula tu deuda",
                        style = TextStyle(
                            brush = shimmerBrush,
                            fontSize = if (isMobileBanner) {
                                MaterialTheme.typography.labelMedium.fontSize
                            } else {
                                MaterialTheme.typography.labelLarge.fontSize
                            },
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = if (isMobileBanner) 0.4.sp else 0.8.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}

