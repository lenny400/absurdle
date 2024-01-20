// This class represents a game of Absurdle, a variation of the popular game Wordle. Like Wordle,
// the player tries to guess a word and is given feedback in the form of colored tiles indicating
// correct or incorrect letter placement. The difference is that Absurdle tries to prolong the
// game by having multiple words that can be the solution word instead of picking one in the
// beginning.

import java.util.*;

public class AbsurdleManager {
    private Set<String> selectedWords;
    private int selectedLength;
    
   // Constructs a new AbsurdleManager.
   // Parameters:
   //    Collection<String> dictionary -- contains all of the words that can be used for the game.
   //    int length -- the length of the words selected for the game.
   // Exceptions:
   //    IllegalArgumentException -- if length parameter is less than 1.
   // Returns:
   //    none
    public AbsurdleManager(Collection<String> dictionary, int length) {
        if (length < 1) {
            throw new IllegalArgumentException();
        }
        selectedLength = length;
        selectedWords = new TreeSet<>();
        for (String word: dictionary) {
            if (word.length() == length) {
                selectedWords.add(word);
            }
        }
    }

   // Returns the words that have been selected for the game of Absurdle.
   // Parameters:
   //    none
   // Exceptions:
   //    none
   // Returns:
   //    Set<String> -- the words that have been selected for the current game of Absurdle.
    public Set<String> words() {
        return selectedWords;
    }

    // The comment for this method is provided. Do not modify this comment:
    // Parameters:
    //  String word -- the secret word trying to be guessed. Assumes word is made up of only
    //                 lower case letters and is the same length as guess.
    //  String guess -- the guess for the word. Assumes guess is made up of only
    //                  lower case letters and is the same length as word.
    // Exceptions:
    //   none
    // Returns:
    //   returns a string, made up of gray, yellow, or green squares, representing a
    //   standard wordle clue for the provided guess made against the provided secret word.
    public static String patternFor(String word, String guess) {
        Map<Character, Integer> counts = new TreeMap<>();
        String pattern[] = new String[word.length()];
        String tiles = "";
        for (int i = 0; i < word.length(); i++) {
            if (counts.get(word.charAt(i)) == null) {
                counts.put(word.charAt(i), 0);
            }
            counts.put(word.charAt(i), counts.get(word.charAt(i)) + 1);
        }
        for (int i = 0; i < word.length(); i++) {
            if (guess.charAt(i) == word.charAt(i)) {
                pattern[i] = "🟩";
                counts.put(guess.charAt(i), counts.get(guess.charAt(i)) - 1);
            }
        }
        for (int i = 0; i < word.length(); i++) {
            String guessLetter = guess.charAt(i) + "";
            if (word.contains(guessLetter) && guess.charAt(i) != word.charAt(i)
                    && counts.get(guess.charAt(i)) != 0) {
                pattern[i] = "🟨";
                counts.put(guess.charAt(i), counts.get(guess.charAt(i)) - 1);        
            } else if (pattern[i] == null) {
               pattern[i] = "⬜";
            }
            tiles += pattern[i];
        }
        return tiles;
    }

   // Records the guess that the player inputs and displays a pattern based on their guess.
   // Updates the current set of words being considered with each guess.
   // Parameters:
   //    String guess -- the word that the player guesses.
   // Exceptions:
   //    IllegalStateException -- if there are no words that have been selected for the game.
   //    IllegalArgumentException -- if the length of the guess is longer than the length of
   //                                the selected words.                    
   // Returns:
   //    String -- returns a String of the pattern associated with the most words.
    public String record(String guess) {
        if (selectedWords.isEmpty()) {
            throw new IllegalStateException();
        } else if (guess.length() != selectedLength) {
            throw new IllegalArgumentException();
        }
        return findMostWords(createMap(guess));
    }

   // Creates a pattern for each word in the selected words compared to the player's guess.
   // Adds these patterns as keys to a map and adds the words associated with these patterns
   // as values.
   // Parameters:
   //    String guess -- the word that the player guesses.
   // Exceptions:
   //    none
   // Returns:
   //    Map<String, Set<String>> -- returns a map containing the patterns as keys and the words
   //                                associated with them as values.
    private Map<String, Set<String>> createMap(String guess) {
        Map<String, Set<String>> patternMap = new TreeMap<>();
        for (String word: selectedWords) {
            String picture = patternFor(word, guess);
            if (patternMap.containsKey(picture)) {
                patternMap.get(picture).add(word);
            } else {
                Set<String> buckets = new TreeSet<>();
                buckets.add(word);
                patternMap.put(picture, buckets);
            }
        }
        return patternMap;
    }

   // Finds the set in the map passed in the parameter with the most words and returns the
   // pattern associated with it.
   // Parameters:
   //    Map<String, Set<String>> patternMap -- map containing pattern keys and sets of words
   //                                           as values.
   // Exceptions:
   //    none
   // Returns:
   //    String -- the pattern that has the most words in the set associated with it.
    private String findMostWords(Map<String, Set<String>> patternMap) {
        int maxSize = 0;
        String pattern = "";
        for (String key: patternMap.keySet()) {
            if (patternMap.get(key).size() > maxSize) {
                maxSize = patternMap.get(key).size();
                selectedWords = patternMap.get(key);
                pattern = key;
            }
        }
        return pattern;
    }
}