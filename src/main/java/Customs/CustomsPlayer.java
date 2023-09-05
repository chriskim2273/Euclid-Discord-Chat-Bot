package Customs;

import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Member;

public class CustomsPlayer{
	public static ArrayList<CustomsPlayer> allCustomsPlayers = new ArrayList<CustomsPlayer>();
	
	private String userID;
	private int Wins;
	private int Losses;
	private int MMR;
	
	private boolean checkIfAlreadyExists(String userID) {
		for(CustomsPlayer cP: allCustomsPlayers) {
			if(userID.equals(this.userID))
				return true;
		}
		return false;
	}
	
	public String getUserID() {
		return userID;
	}

	/*
	public void setUserID(String userID) {
		this.userID = userID;
		MongoDB.savePlayer(this);
	}
	*/

	public int getWins() {
		return Wins;
	}

	public void setWins(int wins) {
		Wins = wins;
		CustomMongoDB.savePlayer(this);
	}

	public void addWins(int wins) {
		Wins += wins;
		CustomMongoDB.savePlayer(this);
	}
	
	public int getLosses() {
		return Losses;
	}

	public void setLosses(int losses) {
		Losses = losses;
		CustomMongoDB.savePlayer(this);
	}

	public void addLosses(int losses) {
		Losses += losses;
		CustomMongoDB.savePlayer(this);
	}
	
	public int getMMR() {
		return MMR;
	}

	public void setMMR(int mmr) {
		MMR = mmr;
		CustomMongoDB.savePlayer(this);
	}

	public void addMMR(int mmr) {
		MMR += mmr;
		CustomMongoDB.savePlayer(this);
	}
	
	public CustomsPlayer(String userID) {
		if(checkIfAlreadyExists(userID) == true)
			return;
		else {
			this.userID = userID;
			this.setLosses(0);
			this.setWins(0);
			this.setMMR(100);
			allCustomsPlayers.add(this);
			CustomMongoDB.savePlayer(this);
		}
	}
	
	public CustomsPlayer(String userID, String Wins, String Losses, String MMR) {
		if(checkIfAlreadyExists(userID) == true)
			return;
		else {
			this.userID = userID;
			this.Wins = Integer.parseInt(Wins);
			this.Losses = Integer.parseInt(Losses);
			this.MMR = Integer.parseInt(MMR);
			allCustomsPlayers.add(this);
			CustomMongoDB.savePlayer(this);
		}
	}
	
	public static CustomsPlayer returnPlayer(Member m) {
		for(CustomsPlayer cP: allCustomsPlayers) {
			if(cP.getUserID().equals(m.getUser().getId()))
				return cP;
		}
		return null;
	}
	
	public static void printPlayers() {
		for(CustomsPlayer cP: allCustomsPlayers) {
			System.out.println(cP.toString());
		}
	}
	
}
