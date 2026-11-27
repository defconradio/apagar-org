package org.apagar.shared.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.apagar.shared.domain.model.DebtInvoice
import org.apagar.shared.presentation.ui.theme.DiscountGreen
import org.apagar.shared.presentation.ui.theme.InkMuted
import org.apagar.shared.presentation.ui.theme.StampRed

@Composable
fun InvoiceReceiptScreen(
    invoice: DebtInvoice,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    @Suppress("DEPRECATION")
    val clipboardManager = LocalClipboardManager.current
    var copied by remember(invoice.invoiceNumber) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "FACTURA OFICIAL DE DEUDA SOBERANA",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "N.º ${invoice.invoiceNumber} • ${invoice.country.flagEmoji} ${invoice.country.nameEs}",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = InkMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                // Base quota row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Cuota base per cápita",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = formatUsd(invoice.basePerCapitaUsd),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                // Itemized modifiers
                invoice.lineItems.forEach { item ->
                    val isSurcharge = item.amountUsd >= 0
                    val accentColor = if (isSurcharge) StampRed else DiscountGreen
                    val pctLabel = if (item.percentageChange >= 0) {
                        "+${item.percentageChange}%"
                    } else {
                        "${item.percentageChange}%"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp)
                        ) {
                            Text(
                                text = item.descriptionEs,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = pctLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                        }
                        Text(
                            text = formatUsd(item.amountUsd, showSign = true),
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL A PAGAR",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "${formatUsd(invoice.totalPayableUsd)} USD",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        color = StampRed
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Plan de financiamiento a 40 años (480 cuotas): ${formatUsd(invoice.monthlyInstallment40YearsUsd)} USD / mes",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = InkMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                clipboardManager.setText(AnnotatedString(buildShareSummary(invoice)))
                copied = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = if (copied) "¡Resumen copiado al portapapeles!" else "Copiar resumen para compartir",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onReset,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Calcular otra factura")
        }
    }
}

private fun buildShareSummary(invoice: DebtInvoice): String = buildString {
    appendLine("FACTURA OFICIAL DE DEUDA SOBERANA #${invoice.invoiceNumber}")
    appendLine("${invoice.country.flagEmoji} ${invoice.country.nameEs}")
    appendLine("Total a pagar: ${formatUsd(invoice.totalPayableUsd)} USD")
    appendLine("480 cuotas mensuales de ${formatUsd(invoice.monthlyInstallment40YearsUsd)} USD")
    append("Calcula la tuya en apagar.org")
}

