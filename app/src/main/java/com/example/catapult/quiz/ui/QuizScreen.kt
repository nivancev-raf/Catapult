package com.example.catapult.quiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberImagePainter
import com.example.catapult.quiz.model.AnswerOption
import com.example.catapult.quiz.model.Question
import com.example.catapult.quiz.ui.QuizContract.QuizUiState

fun NavGraphBuilder.quiz(
    route: String,
    arguments: List<NamedNavArgument>,
    onQuizCompleted: () -> Unit,
    onClose: () -> Unit,
    onPublishScore: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->

    val quizViewModel: QuizViewModel = hiltViewModel(navBackStackEntry)

    val state = quizViewModel.state.collectAsState()

    if (state.value.questions.isEmpty()) {
        ResultScreen(
            score = state.value.score,
            onFinish = onClose,
            onPublish = onPublishScore
        )
    } else {
        QuizScreen(
            state = state.value,
            onOptionSelected = { option -> quizViewModel.submitAnswer(option) },
            onQuizCompleted = onQuizCompleted,
            onClose = onClose
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    state: QuizUiState,
    onOptionSelected: (AnswerOption) -> Unit,
    onQuizCompleted: () -> Unit,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Quiz") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },actions = {
                    Text(
                        text = "Time remaining: ${state.timeRemaining / 1000} sec",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.updating) {
                    CircularProgressIndicator()
                } else if (state.questions.isNotEmpty()) {
                    val question = state.questions[state.currentQuestionIndex]
                    QuestionScreen(
                        question = question,
                        onOptionSelected = onOptionSelected
                    )
                } else {
                    onQuizCompleted()
//                    Text("No questions available")
                }
            }
        }
    )
}

@Composable
fun QuestionScreen(question: Question, onOptionSelected: (AnswerOption) -> Unit) {
    val quizViewModel: QuizViewModel = hiltViewModel()
    val state = quizViewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (question.imageUrl != null) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = rememberImagePainter(question.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            question.options.forEach { option ->
                val buttonColor = when {
                    state.selectedOption == null -> MaterialTheme.colorScheme.primary
                    option == state.selectedOption && state.isOptionCorrect == true -> Color.Green
                    option == state.selectedOption && state.isOptionCorrect == false -> Color.Red
                    else -> MaterialTheme.colorScheme.primary
                }
                Button(
                    onClick = { if (state.selectedOption == null) onOptionSelected(option) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(option.text)
                }
            }
        }
    }
}

