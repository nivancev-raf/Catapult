package com.example.catapult.quiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    ubp: Float,
    onFinish: () -> Unit,
    onPublish: () -> Unit
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Quiz Result") }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Your Score: $ubp",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onPublish,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Publish")
                        }
                        Button(
                            onClick = onFinish,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Finish")
                        }
                    }
                }
            }
        }
    )
}