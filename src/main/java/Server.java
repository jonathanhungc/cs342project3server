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
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Server: Used to start the server for the word guessing game.
 */
public class Server{

	int port;
	int count = 1;	
	TheServer server;
	private Consumer<Serializable> callback;

	// When creating server, specify port number and callback
	Server(int port, Consumer<Serializable> call){

		this.port = port;
		callback = call;
		server = new TheServer();
		server.start();
	}


	/**
	 * TheServer: acts as the primary server for the game, listening for client connections.
	 */
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(port);){

				callback.accept("Server started!");
		    	System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("Client #" +  count + ": connected to server.");
				c.start();
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
	}

	/**
	 * ClientThread: handles interaction with the user client by handling game logic, receiving input from user
	 * and sending corresponding response based on the user's request.
	 */
	class ClientThread extends Thread{

		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;
		GuessingGame game;
		GuessingRound round;

		// socket for connection, and count for number of client connected
		ClientThread(Socket s, int count){
			this.connection = s;
			this.count = count;
			game = new GuessingGame();
		}

		public void run(){

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}

			 while(true) {
					try {
						// read info from client
						GameInfo data = (GameInfo) in.readObject();

						// server receives request to send categories to the client
						if (data.flag.equals("sendCategories")) {
							GameInfo info = new GameInfo("selectCategory");

							info.setCategories(game.getCategoriesNames());
							info.setWordsInCategories(game.getWordsInCategories());

							out.writeObject(info);

							callback.accept("Client #" + count + ": started game. Categories sent.");
						}

						// server receives request to send word to guess to the client
						else if (data.flag.equals("selectedCategory")) {

							round = new GuessingRound(game.getCategory(data.message));

							GameInfo info = new GameInfo("guess");
							info.setElements(round.getUserGuess(), round.getNumLettersGuessed(), round.getNumMisses(), game.getCategoriesPassed());
							info.setCategories(game.getCategoriesNames());

							out.writeObject(info);

							callback.accept("Client #" + count + ": selected category '" + round.getCurrentCategoryName() +
									"'. Guessing word: " + round.getCurrentWord());
						}

						// server receives letter from the client
						else if (data.flag.equals("letter")) {

							round.checkLetterInWord(data.message.charAt(0)); // check letter is in the word

							// if client won round
							if (round.wonRound()) {
								game.setCategoriesPassed(game.getCategoriesPassed() + 1);
								game.setConsecutiveMisses(0);

								// after winning round, check if client won game
								if (game.wonGame()) {
									GameInfo info = new GameInfo("wonGame");
									callback.accept("Client #" + count + ": won game.");
									out.writeObject(info);
									continue;
								}

								GameInfo info = new GameInfo("wonRound");
								callback.accept("Client #" + count + ": won round of '" + round.getCurrentCategoryName() +
										"' with word '" + round.getCurrentWord() + "'.");
								out.writeObject(info);
								game.removeCategory(round.getCurrentCategoryName());
								continue;
							}

							// if client lost round
							if (round.lostRound()) {
								game.setConsecutiveMisses(game.getConsecutiveMisses() + 1);

								// if client lost round, check if client lost game
								if (game.lostGame()) {
									GameInfo info = new GameInfo("lostGame");
									callback.accept("Client #" + count + ": lost game.");
									out.writeObject(info);
									continue;
								}

								GameInfo info = new GameInfo("lostRound");
								callback.accept("Client #" + count + ": lost round of '" + round.getCurrentCategoryName() +
										"' with word '" + round.getCurrentWord() + "'.");
								out.writeObject(info);
								game.getCategory(round.getCurrentCategoryName()).nextWord();
								continue;
							}

							// user didn't win or lose, so send updated guess
							GameInfo info = new GameInfo("guess");
							info.setElements(round.getUserGuess(), round.getNumLettersGuessed(), round.getNumMisses(), game.getCategoriesPassed());
							info.setCategories(game.getCategoriesNames());

							out.writeObject(info);
						}

						// client restarts game
						else if (data.flag.equals("restart")) {
							callback.accept("Client #" + count + ": restarted game.");
							game = new GuessingGame();
						}

						// client exits game
						else if (data.flag.equals("exit")) {
							callback.accept("Client #" + count + ": ended game.");
							break;
						}

						}
					catch(Exception e) {
						callback.accept("Client # " + count + ": error with socket connection.");
						break;
					}
				}
			}//end of run


	}//end of client thread
}


	
	

	
