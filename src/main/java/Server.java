import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server{

	int port;
	int count = 1;	
	TheServer server;
	private Consumer<Serializable> callback;
	
	
	Server(int port, Consumer<Serializable> call){

		this.port = port;
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(port);){

				callback.accept("Server started!");
		    	System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				c.start();
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
	}
	

	class ClientThread extends Thread{

		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;
		GuessingGame game;
		GuessingRound round;

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
							info.setElements(round.userGuess, round.numLettersGuessed, round.numMisses, game.categoriesPassed);
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
								game.categoriesPassed += 1;
								game.consecutiveMisses = 0;

								// after winning round, check if client won game
								if (game.wonGame()) {
									GameInfo info = new GameInfo("wonGame");
									out.writeObject(info);
									continue;
								}

								GameInfo info = new GameInfo("wonRound");
								out.writeObject(info);
								game.removeCategory(round.currentCategoryName);
								continue;
							}

							// if client lost round
							if (round.lostRound()) {
								game.consecutiveMisses += 1;

								// if client lost round, check if client lost game
								if (game.lostGame()) {
									GameInfo info = new GameInfo("lostGame");
									out.writeObject(info);
									continue;
								}

								GameInfo info = new GameInfo("lostRound");
								out.writeObject(info);
								game.getCategory(round.currentCategoryName).nextWord();
								continue;
							}

							GameInfo info = new GameInfo("guess");
							info.setElements(round.userGuess, round.numLettersGuessed, round.numMisses, game.categoriesPassed);
							info.setCategories(game.getCategoriesNames());

							out.writeObject(info);
						}

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


	
	

	
