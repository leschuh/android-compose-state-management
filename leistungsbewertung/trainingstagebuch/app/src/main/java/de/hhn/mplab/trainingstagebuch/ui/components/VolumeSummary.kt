package de.hhn.mplab.trainingstagebuch.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.hhn.mplab.trainingstagebuch.ui.formatKg
import de.hhn.mplab.trainingstagebuch.ui.theme.Dimens
import de.hhn.mplab.trainingstagebuch.ui.theme.TrainingstagebuchTheme

/** Kopfbereich der Liste: Gesamtvolumen und Fortschritt des heutigen Trainings. */
@Composable
fun VolumeSummary(
    totalVolumeKg: Double,
    doneCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.CardPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.ItemSpacing),
        ) {
            Text(
                text = "Gesamtvolumen heute",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = formatKg(totalVolumeKg),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            LinearProgressIndicator(
                progress = { if (totalCount == 0) 0f else doneCount.toFloat() / totalCount },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "$doneCount von $totalCount Übungen erledigt",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VolumeSummaryPreview() {
    TrainingstagebuchTheme {
        VolumeSummary(totalVolumeKg = 4640.0, doneCount = 2, totalCount = 7)
    }
}
