package Games;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class WordBomb{
	public static ArrayList<WordBomb> onGoingGames = new ArrayList<WordBomb>();
	public static Map<String,WordBomb> messageIDtoGame = new HashMap<String,WordBomb>();
	
	public static boolean checkWord(String word) throws UnsupportedEncodingException, IOException, ParseException {
		URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word);
		URLConnection conn = url.openConnection();
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(
		      new InputStreamReader(conn.getInputStream(), "UTF-8"));
		System.out.println(jsonObject.toJSONString());
		if(jsonObject.get("title").toString().equals("No Definitions Found"))
			return false;
		return true;
	}
	
	public static String APITest(String word) throws UnsupportedEncodingException, IOException, ParseException {
		URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word);
		URLConnection conn = url.openConnection();
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) jsonParser.parse(
		      new InputStreamReader(conn.getInputStream(), "UTF-8"));
		}catch(FileNotFoundException F) {
			//F.printStackTrace();
			return null;
		}
		return jsonArray.toString();
	}
	
	public static boolean checkIfGameExists(TextChannel Channel) {
		for(WordBomb game: onGoingGames) {
			if(game.gameChannel.equals(Channel))
				return true;
		}
		return false;
	}
	
	private TextChannel gameChannel;
	private User Host;
	private ArrayList<Member> Players = new ArrayList<Member>();
	private boolean gameInProgress, Started = false;
	private Timer timer;
	private int bombTickingTime = 20;
	private static int Interval = 20;

	public WordBomb(TextChannel gameChannel, User Host){
		this.gameChannel = gameChannel;
		this.Host = Host;
		gameInProgress = false;
		timer = new Timer();
		WordBomb thisGameClass = this;
		/*
		timer.schedule(new TimerTask() {
			public void run() {
				if(gameInProgress == true) {
					onGoingGames.add(thisGameClass);
					timer.cancel();
				}
			}
		}, 5000);
		*/
		onGoingGames.add(thisGameClass);
		
	}
	/*
	private static final int setInterval() {
		if(Interval == 1)
			timer.cancel();
		return --Interval;
	}*/
	
	public TextChannel getGameChannel() {
		return gameChannel;
	}
	
	public User getHost() {
		return Host;
	}
	
	public void addPlayer(Member Player) {
		Players.add(Player);
	}
	public void removePlayer(Member Player) {
		Players.remove(Player);
	}
	
	public void StartGame() {
		boolean gameOngoing = true;
		
		int playerCount = Players.size();
		Collections.shuffle(Players);
		Queue<Member> currentPlayers = new LinkedList<Member>(Players);
		//
		//Embed
		EmbedBuilder gameEmbed = new EmbedBuilder();
		
		//Game Bomb Timer
		timer.scheduleAtFixedRate(new TimerTask() {
			int countDown = bombTickingTime;
			@Override
			public void run() {
				if(countDown==0)
					countDown = 20;
				else
					Interval = --countDown;
			}
		}, 1000, 1000);
		
		while(gameOngoing == true) {
			//
			if(Interval==0) {
				//Player Loses a life
				// If player is at 0 hearts, they die
				
			}
		}
		
		Member currentPlayer = currentPlayers.poll();
		currentPlayers.add(currentPlayer);
	}
	
	private void playerRight() {
		
	}
	
	private void playerWrong() {
		
	}
	
	
	private String lettersGenerator() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String generatedString = "";
		int Length = 3;
		Random rand = new Random();
		for(int i = 0; i<Length; i++) {
			int randomIndex = rand.nextInt(alphabet.length());
			generatedString += alphabet.substring(randomIndex,(randomIndex+1));
		}
		return generatedString;
	}
	
	public void endGame() {
		// Remove All Embeds
		// Send Embed Saying Game Ended
		// If Canceled, Say Canceled
		// If Won, Name Winner.
		
		//Remove Class
		onGoingGames.remove(this);
		//messageIDtoGame.
		//Garbage Cleaner
		System.gc();
	}
}
