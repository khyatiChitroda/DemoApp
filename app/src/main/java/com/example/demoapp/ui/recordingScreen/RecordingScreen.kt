package com.example.demoapp.ui.recordingScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Locale
import androidx.compose.material3.Text
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordingScreen(
    viewModel: RecordingViewModel = hiltViewModel<RecordingViewModel>(),
    onNavigateBack: () -> Unit
) {

    val context = LocalContext.current
    var recognizedText = viewModel.recognizedText.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by viewModel.snackbarMessage.collectAsState(initial = null)

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                viewModel.onInputTextChanged(matches[0])// Get the first, most accurate result
            }
        } else {
            // Handle cases where the user cancels or an error occurs
            Toast.makeText(context, "Speech recognition failed.", Toast.LENGTH_SHORT).show()
        }
    }

    // Composable function to trigger the speech recognition intent
    fun startSpeechRecognition() {
        if (!isSpeechRecognitionAvailable(context)) {
            Toast.makeText(
                context,
                "Speech recognition not available on this device.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
        }

        try {
            speechRecognizerLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    println("in add new user")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record New User Details") },
                navigationIcon = {
                    IconButton(onClick = {onNavigateBack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = recognizedText.value,
                onValueChange = { newText: String ->
                    viewModel.onInputTextChanged(newText)
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )


            Row {
                Button(onClick = {
                    viewModel.saveUser(recognizedText.value)
                },modifier = Modifier.semantics { contentDescription = "Save New User" }) {
                    Text(text = "Save User",
                        style = MaterialTheme.typography.bodyLarge)

                }

                Spacer(modifier = Modifier.padding(5.dp))
                Button(onClick = {
                    startSpeechRecognition()
                },modifier = Modifier.semantics { contentDescription = "Record new user" }) {
                    Text(text = "Voice Input",
                        style = MaterialTheme.typography.bodyLarge)

                }
            }
        }
    }

}


// Helper function to check if speech recognition is available
fun isSpeechRecognitionAvailable(context: Context): Boolean {
    val pm = context.packageManager
    val activities = pm.queryIntentActivities(
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0
    )
    return activities.isNotEmpty()
}