package com.example.catapult.quiz.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberImagePainter
import com.example.catapult.animations.AnimatedQuestionContent
import com.example.catapult.breeds.list.BreedListContract
import com.example.catapult.quiz.model.AnswerOption
import com.example.catapult.quiz.model.Question
import com.example.catapult.quiz.ui.QuizContract.QuizUiState
import kotlinx.coroutines.launch

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


    if (state.value.updating){
        CircularProgressIndicator()
    }else if (state.value.questions.isEmpty()) {
        ResultScreen(
            ubp = state.value.ubp,
            onFinish = onClose,
            nickname = state.value.nickname,
            onPublish = onPublishScore
        )
    } else {
        QuizScreen(
            state = state.value,
            eventPublisher = {
                quizViewModel.setEvent(it)
            },
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
    eventPublisher: (uiEvent: QuizContract.QuizEvents) -> Unit,
    onOptionSelected: (AnswerOption) -> Unit,
    onQuizCompleted: () -> Unit,
    onClose: () -> Unit,
) {
    // if showdialog is true, show alert dialog
    if (state.showExitDialog) {
        AlertDialog(
            onDismissRequest = { eventPublisher(QuizContract.QuizEvents.StopQuiz) }, // onDismissRequest is called when user clicks outside the dialog or presses the back button
            title = { Text("Exit Quiz") },
            text = { Text("Are you sure you want to exit the quiz?") },
            confirmButton = {
                Button(
                    onClick = { onClose() }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { eventPublisher(QuizContract.QuizEvents.ContinueQuiz) }
                ) {
                    Text("No")
                }
            }
        )
    }



    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Quiz") },
                navigationIcon = {
                    IconButton(onClick = { eventPublisher(QuizContract.QuizEvents.StopQuiz) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },actions = {
                    Column{
                        Text(
                            text = "Time remaining: ${state.timeRemaining / 1000} sec",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        // add text current question index
                        Text(
                            text = "Question ${state.currentQuestionIndex + 1}/${state.questions.size}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
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
                        onOptionSelected = onOptionSelected,
                    )
                } else {
                    onQuizCompleted()
//                    Text("No questions available")
                }
            }
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuestionScreen(question: Question, onOptionSelected: (AnswerOption) -> Unit) {
    val quizViewModel: QuizViewModel = hiltViewModel()
    val state = quizViewModel.state.collectAsState().value

    AnimatedQuestionContent(targetState = state.currentQuestionIndex){
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

}

