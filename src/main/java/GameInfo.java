import java.io.Serializable;

public class GameInfo implements Serializable {
    char[] userGuess;
    int numLettersGuessed;
    int numMisses;
    String message;
    String flag;

    GameInfo(String userFlag, String userMessage, char[] guess, int lettersGuessed, int misses) {
        message = userMessage;
        flag = userFlag;
        userGuess = guess;
        numLettersGuessed = lettersGuessed;
        numMisses = misses;
    }
}
