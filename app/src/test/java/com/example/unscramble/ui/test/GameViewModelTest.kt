package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotEquals

class GameViewModelTest {

    private val viewModel =GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset(){

        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value

        assertFalse(currentGameUiState.isGuessedWordWrong)

        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER,currentGameUiState.score)


    }

    @Test
    fun gameViewModel_InCorrectWordErrorFlagSet(){
        var currentGameUiState = viewModel.uiState.value
        val incorrectWord = "and"

        viewModel.updateUserGuess(incorrectWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value

        assertTrue(currentGameUiState.isGuessedWordWrong)
        assertEquals(0,currentGameUiState.score)

    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded(){

        val gameUiState = viewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(gameUiState.currentScrambledWord)

        // Assert that current word is scrambled.
        assertNotEquals(unScrambledWord, gameUiState.currentScrambledWord)
        assertEquals(1,gameUiState.currentWordCount)
        assertEquals(0,gameUiState.score)
        assertFalse(gameUiState.isGameOver)
        assertFalse(gameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly(){
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value

        var correctPlayerdWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        repeat(MAX_NO_OF_WORDS){
            expectedScore += SCORE_INCREASE

            viewModel.updateUserGuess(correctPlayerdWord)
            viewModel.checkUserGuess()

            currentGameUiState =viewModel.uiState.value
            correctPlayerdWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

            assertEquals(expectedScore,currentGameUiState.score)
        }

        assertEquals(MAX_NO_OF_WORDS,currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        val lastWordCount = currentGameUiState.currentWordCount
        viewModel.skipWord()
        currentGameUiState = viewModel.uiState.value
        // Assert that score remains unchanged after word is skipped.
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
        // Assert that word count is increased by 1 after word is skipped.
        assertEquals(lastWordCount + 1, currentGameUiState.currentWordCount)
    }
    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }

}