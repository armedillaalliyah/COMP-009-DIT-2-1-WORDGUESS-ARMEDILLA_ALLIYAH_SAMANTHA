package Reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordGuess {
	private static final String DICTIONARY_FILE = "dictionary.csv";
	private static final int MAX_ATTEMPTS = 6;

	private String selectedWord;
	private StringBuilder maskedWord;
	private List<Character> guessedLetters;
	private int remainingAttempts;
	private List<String> dictionary;

	public WordGuess() {
		guessedLetters = new ArrayList<>();
		remainingAttempts = MAX_ATTEMPTS;
		loadDictionary();
		selectRandomWord();
		initMaskedWord();
	}

	private void loadDictionary() {
		dictionary = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_FILE))) {
			String line;
			while ((line = br.readLine()) != null) {
				dictionary.add(line.trim());
			}
		} catch (IOException e) {
			System.err.println("Error reading dictionary file: " + e.getMessage());
			System.exit(1);
		}

		if (dictionary.isEmpty()) {
			System.err.println("Dictionary file is empty.");
			System.exit(1);
		}
	}

	private void selectRandomWord() {
		Random random = new Random();
		int index = random.nextInt(dictionary.size());
		selectedWord = dictionary.get(index).toUpperCase();
	}

	private void initMaskedWord() {
		maskedWord = new StringBuilder();
		for (int i = 0; i < selectedWord.length(); i++) {
			maskedWord.append("_");
		}
	}

	public void play() {
		Scanner scanner = new Scanner(System.in);
		try {
			while (remainingAttempts > 0) {
				System.out.println("Word: " + maskedWord);
				System.out.print("Guess a letter: ");
				String input = scanner.nextLine().trim().toUpperCase();

				if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
					System.out.println("Invalid input. Please enter a single letter.");
					continue;
				}

				char letter = input.charAt(0);

				if (guessedLetters.contains(letter)) {
					System.out.println("You've already guessed that letter. Try again.");
					continue;
				}

				guessedLetters.add(letter);

				if (selectedWord.indexOf(letter) == -1) {
					remainingAttempts--;
					System.out.println("Wrong guess! Remaining attempts: " + remainingAttempts);
				} else {
					updateMaskedWord(letter);
				}

				if (maskedWord.toString().equals(selectedWord)) {
					System.out.println("Congratulations! You guessed the word: " + selectedWord);
					break;
				}
			}

			if (remainingAttempts == 0) {
				System.out.println("Game over! The word was: " + selectedWord);
			}
		} finally {
			scanner.close();
		}
	}

	private void updateMaskedWord(char letter) {
		StringBuilder newMaskedWord = new StringBuilder();
		for (int i = 0; i < selectedWord.length(); i++) {
			if (selectedWord.charAt(i) == letter) {
				newMaskedWord.append(letter);
			} else {
				newMaskedWord.append(maskedWord.charAt(i));
			}
		}
		maskedWord = newMaskedWord;
	}

	public static void main(String[] args) {
		WordGuess game = new WordGuess();
		game.play();
	}
}
