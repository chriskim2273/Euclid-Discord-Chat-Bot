package Customs;

import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class Teams {
	public static ArrayList<Teams> allTeams = new ArrayList<Teams>();
	
	private Guild guild;
	private ArrayList<CustomsPlayer> teamOne = new ArrayList<CustomsPlayer>();
	private ArrayList<CustomsPlayer> teamTwo = new ArrayList<CustomsPlayer>();
	
	public Teams(Guild guild, ArrayList<CustomsPlayer> teamOne, ArrayList<CustomsPlayer> teamTwo) {
		this.setTeamOne(teamOne);
		this.setTeamTwo(teamTwo);
		this.guild = guild;
		allTeams.add(this);
	}

	public Guild returnGuild() {
		return guild;
	}
	
	public static Teams checkGuild(Guild guild) {
		for(Teams t: allTeams) {
			if(t.returnGuild().equals(guild))
				return t;
		}
		return null;
	}

	public ArrayList<CustomsPlayer> getTeamOne() {
		return teamOne;
	}

	public void setTeamOne(ArrayList<CustomsPlayer> teamOne) {
		this.teamOne = teamOne;
	}

	public ArrayList<CustomsPlayer> getTeamTwo() {
		return teamTwo;
	}

	public void setTeamTwo(ArrayList<CustomsPlayer> teamTwo) {
		this.teamTwo = teamTwo;
	}
	
	
	public void addTeamOne(CustomsPlayer member) {
		if(this.teamOne.contains(member))
			return;
		this.teamOne.add(member);
	}
	
	public void addTeamTwo(CustomsPlayer member) {
		if(this.teamTwo.contains(member))
			return;
		this.teamTwo.add(member);
	}
	
	public void removeTeam() {
		allTeams.remove(this);
		System.gc();
	}
}
