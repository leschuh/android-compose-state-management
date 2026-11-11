package de.hhn.mplab.trainingstagebuch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import de.hhn.mplab.trainingstagebuch.submission.SubmissionFrame
import de.hhn.mplab.trainingstagebuch.ui.theme.TrainingstagebuchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainingstagebuchTheme {
                SubmissionFrame {
                    TrainingApp()
                }
            }
        }
    }
}
