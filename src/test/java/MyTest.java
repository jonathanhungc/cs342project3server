/**
 * FILE: MyTest.java
 *
 * Test file for the logic of a guessing round (where the user guesses one word), and the logic of a
 * guessing game (where the user tries to guess a word from 3 different categories).
 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyTest {

	GuessingGame game;
	GuessingRound round;

	@BeforeEach
	void init() {
		game = new GuessingGame();
		round = new GuessingRound(game.getCategory("animals")); // current category for the round
	} // end of init()

	// method to test valid letter guesses of a word
	@Test
	void testValidLettersInWord() {
		char[] userGuess, expected;

		// set known string for testing
		round.setCurrentWord("panther");
		round.setUserGuess();

		round.checkLetterInWord('p'); // letter in word

		userGuess = round.getUserGuess();
		expected = new char[]{'p', 0, 0, 0, 0, 0, 0};
		assertArrayEquals(userGuess, expected); // check that both char arrays are equal

		round.checkLetterInWord('n'); // letter in word
		round.checkLetterInWord('h'); // letter in word

		userGuess = round.getUserGuess();
		expected = new char[]{'p', 0, 'n', 0, 'h', 0, 0};
		assertArrayEquals(userGuess, expected); // check that both char arrays are equal

		assertEquals(3, round.getNumLettersGuessed()); // check number of letters guessed
		assertEquals(0, round.getNumMisses()); // check number of misses

	} // end of testLetters()

	// method to test invalid letter guesses of a word
	@Test
	void testMissesInWord() {
		char[] userGuess, expected;

		// set known string for testing
		round.setCurrentWord("panther");
		round.setUserGuess();

		round.checkLetterInWord('u'); // letter not in word
		round.checkLetterInWord('k'); // letter not in word

		userGuess = round.getUserGuess();
		expected = new char[]{0, 0, 0, 0, 0, 0, 0};
		assertArrayEquals(userGuess, expected); // check that both char arrays are equal, userGuess should be empty

		assertEquals(0, round.getNumLettersGuessed()); // check number of letters guessed
		assertEquals(2, round.getNumMisses()); // check number of misses

		round.checkLetterInWord('e'); // letter in word

		userGuess = round.getUserGuess();
		expected = new char[]{0, 0, 0, 0, 0, 'e', 0};
		assertArrayEquals(userGuess, expected); // check that both char arrays are equal

		assertEquals(1, round.getNumLettersGuessed()); // check number of letters guessed
		assertEquals(2, round.getNumMisses()); // check number of misses
	} // end of testMisses()

	// testing the name of the category
	@Test
	void testName() {
		assertEquals("animals", round.getCategoryName());
	}

	// testing overall logic of the guessing game, if user won/lost
	@Test
	void testGuessingGame() {
        assertArrayEquals(new String[]{"animals", "foods", "us states"}, game.getCategoriesNames());
		game.removeCategory("foods");
        assertArrayEquals(new String[]{"animals", "us states"}, game.getCategoriesNames());

		game.setCategoriesPassed(3); // name of categories to win
		assertTrue(game.wonGame());
		assertFalse(game.lostGame());

		game.setCategoriesPassed(0); // reset number of categories passed

		game.setConsecutiveMisses(3); // number of misses to lose
		assertFalse(game.wonGame());
		assertTrue(game.lostGame());

		game.setConsecutiveMisses(3); // reset number of misses

		Category category = game.getCategory("animals");
		category.nextWord();
		category.nextWord();
		category.nextWord();

		assertTrue(game.lostGame()); // there are no words left in the category, which means there were
									// 3 failed attempts in the category, so lost game
	}

}
