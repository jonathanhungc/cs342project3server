/**
 * FIlE: Server.java
 *
 * This file contains class Server, and two inner classes TheServer and ClientThread. This file implements the game
 * logic of the guessing game using other classes.
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Server: Used to start the server for the word guessing game.
 */
public class Server {
	int port;
	int count = 1;
	TheServer server;
	private Consumer<Serializable> callback;

	// When creating server, specify port number and callback
	Server(int port, Consumer<Serializable> call) {

		this.port = port;
		callback = call;
		server = new TheServer();
		server.start();
	}

	/**
	 * TheServer: acts as the primary server for the game, listening for client
	 * connections.
	 */
	public class TheServer extends Thread {

		public void run() {

			try (ServerSocket mysocket = new ServerSocket(port);) {

				callback.accept("Server started!");
				System.out.println("Server is waiting for a client!");

				while (true) {

					ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept("Client #" + count + ": connected to server.");
					c.start();

					System.out.println("Client #" + count + ": joined server.");
					count++;

				}
			} // end of try
			catch (Exception e) {
				callback.accept("Server socket did not launch");
			}
		}// end of while
	}

	/**
	 * ClientThread: handles interaction with the user client by handling game
	 * logic, receiving input from user
	 * and sending corresponding response based on the user's request.
	 */
	class ClientThread extends Thread {

		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;
		GuessingGame game;
		GuessingRound round;

		// socket for connection, and count for number of client connected
		ClientThread(Socket s, int count) {
			this.connection = s;
			this.count = count;
			game = new GuessingGame();
		}

		public void run() {

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			} catch (Exception e) {
				System.out.println("Streams not open");
			}

			while (true) {
				try {
					// read response from client
					GameInfo clientResponse = (GameInfo) in.readObject();

					System.out.println("Flag: " + clientResponse.flag);

					// server receives request to send categories to the client
					if (clientResponse.flag.equals("sendCategories")) {
						GameInfo serverResponse = new GameInfo("selectCategory");

						serverResponse.setCategories(game.getCategoriesNames());
						serverResponse.setWordsInCategories(game.getWordsInCategories());

						out.writeObject(serverResponse);

						callback.accept("Client #" + count + ": started game. Categories sent.");
					}

					// server receives request to send word to guess to the client
					else if (clientResponse.flag.equals("selectedCategory")) {

						round = new GuessingRound(game.getCategory(clientResponse.message));

						GameInfo serverResponse = new GameInfo("guess");
						serverResponse.setElements(round.getUserGuess(), round.getNumLettersGuessed(),
								round.getNumMisses(), game.getCategoriesPassed());
						serverResponse.setCategories(game.getCategoriesNames());

						out.writeObject(serverResponse);

						callback.accept("Client #" + count + ": selected category " + round.getCategoryName() +
								". Guessing word: " + round.getCurrentWord() + ".");

						System.out.println("Client #" + count + ": selected category " + round.getCategoryName() +
								". Guessing word: " + round.getCurrentWord() + ".");
					}

					// server receives letter from the client
					else if (clientResponse.flag.equals("letter")) {

						round.checkLetterInWord(clientResponse.message.charAt(0)); // check letter is in the word

						// if client won round
						if (round.wonRound()) {
							game.setCategoriesPassed(game.getCategoriesPassed() + 1);
							game.setConsecutiveMisses(0);

							// after winning round, check if client won game
							if (game.wonGame()) {
								GameInfo serverResponse = new GameInfo("wonGame");
								callback.accept("Client #" + count + ": won game.");
								out.writeObject(serverResponse);
								continue;
							}

							GameInfo serverResponse = new GameInfo("wonRound");
							callback.accept("Client #" + count + ": won round of " + round.getCategoryName() +
									" with word " + round.getCurrentWord() + ".");

							serverResponse.setMessage(round.getCurrentWord());
							//serverResponse.setCategories(game.getCategoriesNames());
							out.writeObject(serverResponse);
							game.removeCategory(round.getCategoryName());
							continue;
						}

						// if client lost round
						if (round.lostRound()) {
							game.setConsecutiveMisses(game.getConsecutiveMisses() + 1);
							game.getCategory(round.getCategoryName()).nextWord();

							// if client lost round, check if client lost game
							if (game.lostGame()) {
								GameInfo serverResponse = new GameInfo("lostGame");
								callback.accept("Client #" + count + ": lost game.");
								out.writeObject(serverResponse);
								continue;
							}

							GameInfo serverResponse = new GameInfo("lostRound");
							callback.accept("Client #" + count + ": lost round of " + round.getCategoryName() +
									" with word " + round.getCurrentWord() + ".");
							serverResponse.setMessage(round.getCurrentWord());
							out.writeObject(serverResponse);
							continue;
						}

						// user didn't win or lose, so send updated guess
						GameInfo serverResponse = new GameInfo("guess");
						serverResponse.setElements(round.getUserGuess(), round.getNumLettersGuessed(),
								round.getNumMisses(), game.getCategoriesPassed());
						serverResponse.setCategories(game.getCategoriesNames());

						callback.accept("Client #" + count + ": sent the letter " + clientResponse.message.charAt(0)
								+ " for word " + round.getCurrentWord() + ".");
						out.writeObject(serverResponse);
					}

					// client restarts game
					else if (clientResponse.flag.equals("restart")) {
						callback.accept("Client #" + count + ": restarted game.");
						game = new GuessingGame();

						GameInfo serverResponse = new GameInfo("selectCategory");

						serverResponse.setCategories(game.getCategoriesNames());
						serverResponse.setWordsInCategories(game.getWordsInCategories());

						out.writeObject(serverResponse);

						callback.accept("Client #" + count + ": started game. Categories sent.");
					}

					// client exits game
					else if (clientResponse.flag.equals("exit")) {
						callback.accept("Client #" + count + ": ended game.");
						break;
					}

				} catch (Exception e) {
					callback.accept("Client #" + count + ": disconnected unexpectedly.");
					break;
				}
			}
		}// end of run
	}// end of client thread
}
