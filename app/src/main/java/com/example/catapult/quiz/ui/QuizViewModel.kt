package com.example.catapult.quiz.ui

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.auth.AuthStore
import com.example.catapult.breeds.db.BreedData
import com.example.catapult.breeds.list.BreedListContract
import com.example.catapult.quiz.model.AnswerOption
import com.example.catapult.quiz.model.Question
import com.example.catapult.photos.repository.PhotosRepository
import com.example.catapult.breeds.repository.BreedsRepository
import com.example.catapult.quiz.db.QuizResultEntity
import com.example.catapult.quiz.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.catapult.quiz.ui.QuizContract.QuizUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import java.util.Date


@HiltViewModel
class QuizViewModel @Inject constructor(
    private val photoRepository: PhotosRepository,
    private val breedsRepository: BreedsRepository,
    private val quizRepository: QuizRepository,  // Add QuizRepository here
    private val authStore: AuthStore  // Add AuthStore to get user details
) : ViewModel() {

    private val _state = MutableStateFlow(QuizUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: QuizUiState.() -> QuizUiState) = _state.update(reducer)

    private val events = MutableSharedFlow<QuizContract.QuizEvents>() // handle events that represent user interactions
    private var pausedTimeRemaining: Long = 0
    fun setEvent(event: QuizContract.QuizEvents) = viewModelScope.launch { events.emit(event) }

    private var timer: CountDownTimer? = null // timer

    init {
        observeEvents()
        fetchQuestions()
        fetchNickname()
        startTimer()
    }

    // Fetch questions from the repository
    private fun fetchQuestions() {
        viewModelScope.launch {
            setState { copy(updating = true) }
            try {
                withContext(Dispatchers.IO) {
                    val questions = generateQuestions()
                    setState { copy(questions = questions, updating = false) }
                }
            } catch (error: Exception) {
                Log.d("QuizViewModel", "Exception", error)
                setState { copy(updating = false, error = error) }
            }
        }
    }

    private fun fetchNickname() {
        viewModelScope.launch {
            val userProfile = authStore.authData.first()
            setState { copy(nickname = userProfile.nickname) }
        }
    }

    // collect - funkcija koja se koristi za prikupljanje vrednosti iz flow-a
    // flow je koncept koji se koristi za emitovanje (vise) dogadjaja asinhrono (kao stream u javi)
    private fun observeEvents() {
        viewModelScope.launch {       // izvrsava se na main threadu po defaultu
            events.collect {
                when (it) {
                    is QuizContract.QuizEvents.StopQuiz -> {
                        timer?.cancel()
                        setState { copy(showExitDialog = true) }
                    }
                    is QuizContract.QuizEvents.ContinueQuiz -> {
                        pausedTimeRemaining = _state.value.timeRemaining
                        startTimer(pausedTimeRemaining)
                        setState { copy(showExitDialog = false) }
                        //
                    }
                }
            }
        }
    }

    // Generate questions
    private suspend fun generateQuestions(): List<Question> {
        val questions = mutableListOf<Question>()
        for (i in 1..2) { // 10 pitanja
            val randomBreedOwnerId = photoRepository.getRandomBreedOwnerId()  // vadimo random breed
            Log.d("QuizViewModel1", "RandomBreedOwnerId: $randomBreedOwnerId")
            val randomAlbum = photoRepository.getRandomAlbumByBreedOwnerId(randomBreedOwnerId) // vadimo random sliku za odredjeni breed
            val breedData = breedsRepository.getBreedById(randomBreedOwnerId) // vadimo podatke o breedu
//            val questionType = (1..3).random() // random biramo tip pitanja
            val questionType = 1;
            val questionText = when (questionType) {
                1 -> "What breed is this cat?"
                2 -> "Find the odd one out for this cat."
                3 -> "Which temperament belongs to this cat?"
                else -> "What breed is this cat?"
            }
            Log.d("QuizViewModel", "BreedId for Question : ${breedData.id}")
            val options = generateOptionsForQuestion(questionType, breedData, randomAlbum.imageUrl)
            val question = Question(
                id = randomAlbum.albumId,
                type = questionType,
                questionText = questionText,
                imageUrl = randomAlbum.imageUrl,
                options = options
            )
            questions.add(question)
        }
        return questions
    }

    // Generate options for a question
    private suspend fun generateOptionsForQuestion(questionType: Int, breedData: BreedData, imageUrl: String?): List<AnswerOption> {
        return when (questionType) {
            1 -> {
                val correctOption = AnswerOption(breedData.name, imageUrl, true)
                Log.d("QuizViewModel", "Correct option: ${correctOption.text}")
                val incorrectOptions = breedsRepository.getRandomBreedsExcluding(breedData.id).map {
                    AnswerOption(it.name, imageUrl, false)
                }
                Log.d("QuizViewModel", "Incorrect options: ${incorrectOptions.map { it.text }}")
                (listOf(correctOption) + incorrectOptions).shuffled() // mesamo odgovore, da ne bi uvek tacan bio prvi ponudjeni
            }
            2 -> {
                val temperamentList = breedData.temperament.split(", ")
                val temperamentsExcluded = breedsRepository.getOneRandomTemperamentExcluding(temperamentList).toString().split(", ")
                val firstExcludedTemperament = getFirstExcludedTemperament(temperamentsExcluded, temperamentList)
                val correctOption = AnswerOption(firstExcludedTemperament.toString(), null, true)
                Log.d("QuizViewModel", "Correct option: ${correctOption.text}")
                val incorrectOptions = breedData.temperament.split(",").map {
                    AnswerOption(it.trim(), null, false)
                }.shuffled().take(3)

                Log.d("QuizViewModel", "Incorrect options: ${incorrectOptions.map { it.text }}")
                (listOf(correctOption) + incorrectOptions).shuffled()
            }
            3 -> {
                val correctOption = AnswerOption(breedData.temperament.split(",").random().trim(), null, true)
                Log.d("QuizViewModel", "Correct option: ${correctOption.text}")

                val temperamentList = breedData.temperament.split(", ")
                val temperamentsExcluded = breedsRepository.getRandomTemperamentsExcluding(breedData.temperament).toString().split(", ")
                var threeExcludedTemperaments =
                    getThreeExcludedTemperament(temperamentsExcluded, temperamentList).toMutableList()

                // problem je bio [[temperament1, temperament2, temperament3], pa sam morao da remove '['
                threeExcludedTemperaments[0] = threeExcludedTemperaments[0].replace("[", "")

                // nakon izmene tog problema, sada je potrebno da se prvo slovo svakog temperamenta veliko
                threeExcludedTemperaments = capitalizeFirstLetter(threeExcludedTemperaments)

                val incorrectOptions = threeExcludedTemperaments.map { AnswerOption(it, null, false) }
                Log.d("QuizViewModel", "Incorrect options: ${incorrectOptions.map { it.text }}")
                (listOf(correctOption) + incorrectOptions).shuffled()
            }
            else -> {
                emptyList()
            }
        }
    }


    fun submitAnswer(option: AnswerOption) {
        viewModelScope.launch {
            setState { copy(selectedOption = option, isOptionCorrect = option.isCorrect) }
            if (option.isCorrect) {
                setState { copy(score = score + 1) }
            }

            delay(1000)  // Delay to show the feedback before moving to the next question

            if (_state.value.currentQuestionIndex < _state.value.questions.size - 1) {
                setState {
                    copy(
                        currentQuestionIndex = currentQuestionIndex + 1,
                        selectedOption = null,
                        isOptionCorrect = null
                    )
                }

//                // ukoliko nije zavrsio sva pitanja a vreme je isteklo, prelazimo na Result screen
//                if (_state.value.timeRemaining <= 0) {
//                    Log.d("VremeJeIsteklo", "Vreme je isteklo")
//                    setState { copy(questions = emptyList()) }
//                    val ubp = calculateUBP();
//                    ubp.coerceAtMost(maximumValue = 100.00f)
//                    setState { copy(ubp = ubp) }
//                }

            } else {
                finishQuiz()
            }
        }
    }


    private fun finishQuiz() {
        viewModelScope.launch {
            Log.d("QuizViewModel", "Quiz completed with score: ${_state.value.score}")
            setState { copy(questions = emptyList()) }

            val userProfile = authStore.authData.first()
            val ubp = calculateUBP().coerceAtMost(100.00f)

            val quizResult = QuizResultEntity(
                nickname = userProfile.nickname,
                score = ubp,
                date = Date(), // trenutni datum
            )
            quizRepository.insertQuizResult(quizResult)

            setState { copy(ubp = ubp) }
        }
    }

    private fun calculateUBP(): Float {
        return (_state.value.score * 2.5 * (1 + ((_state.value.timeRemaining / 1000) + 120) / 300)).toFloat()
    }

    // ovo je pravljeno jer je bilo moguce da se neki od temperamenata od razlicitih macaka ponavljaju
    private fun getFirstExcludedTemperament(temperamentsExcluded: List<String>, temperamentList: List<String>): String? {
        for (temperament in temperamentsExcluded) {
            if (temperament !in temperamentList) {
                return temperament
            }
        }
        return null
    }

    // same function like getFirstExcludedTemperament, but for List<String>, max 3 items
    private fun getThreeExcludedTemperament(temperamentsExcluded: List<String>, temperamentList: List<String>): List<String> {
        val result = mutableListOf<String>()
        for (temperament in temperamentsExcluded) {
            if (temperament !in temperamentList) {
                result.add(temperament)
            }
            if (result.size == 3) {
                return result
            }
        }
        return result
    }

    // capitalize first letter of the string in mutable string list
    private fun capitalizeFirstLetter(temperaments: MutableList<String>): MutableList<String> {
        for (i in 0 until temperaments.size) {
            temperaments[i] = temperaments[i].replaceFirst(temperaments[i][0], temperaments[i][0].uppercaseChar())
        }
        return temperaments
    }

    fun stopTimer() {
        timer?.cancel()
    }

    private fun startTimer(time: Long = 10000) {
        timer = object : CountDownTimer(time, 1000) { // 1000ms = 1s, 300000ms = 5min
            override fun onTick(millisUntilFinished: Long) {
                setState { copy(timeRemaining = millisUntilFinished) }
            }

            override fun onFinish() {
//                // ukoliko nije zavrsio sva pitanja a vreme je isteklo, prelazimo na Result
//                setState { copy(questions = emptyList()) } // vracamo na Result screen
//
//                val ubp = calculateUBP();
//                ubp.coerceAtMost(maximumValue = 100.00f)
//                setState { copy(ubp = ubp) }
                finishQuiz()

            }
        }
        timer?.start() // timer? means that timer can be null
    }
}
