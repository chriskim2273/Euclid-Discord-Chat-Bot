package Customs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import Commands.SleepyTimeResponse;
import MainComponent.Bot;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Customs extends ListenerAdapter{
	/*
	private static HashMap<Member,Message> UnevenTeamGenerationMessages = new HashMap<Member,Message>();
	private static HashMap<Member,Message> GeneratedTeamMessages = new HashMap<Member,Message>();
	private static HashMap<Message, Member> MessMem = new HashMap<Message, Member>();
	private static HashMap<Member,VoiceChannel> MemberVCMap = new HashMap<Member,VoiceChannel>();
	*/
	private static HashMap<User,VoiceChannel> idToVC = new HashMap<User,VoiceChannel>();
	
	public String formattedDate() {
		LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    return myDateObj.format(myFormatObj);
	}
	
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User user = event.getAuthor();
		if(user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		TextChannel channel = event.getChannel();
		
		String prefix = Bot.prefix;
		String command = "customs";
		String raw = event.getMessage().getContentRaw();
		String[] Separated = raw.split("\\s+");
		String arg = raw.replaceFirst(prefix + command + " ", "");
		
		if(Separated[0].toLowerCase().equalsIgnoreCase(prefix + command)){
			if(Separated.length<2) {
				channel.sendMessage("Please provide the name of the voice channel.").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
				return;
			}
			VoiceChannel vc = event.getGuild().getVoiceChannelsByName(arg, false).get(0);
			if(vc==null) {
				channel.sendMessage("Error. Voice Channel does not exist!").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
				return;
			}else {
				Teams Team = Teams.checkGuild(event.getGuild());
				if(!(Team == null)) {
					return;
					//Game is already ongoing - please send message and add =exit command.
				}
				ArrayList<Member> MembersInVC = new ArrayList<>(vc.getMembers());
				if(vc.getMembers().isEmpty()) {
					EmbedBuilder NoMemberEmbed = EmbedUtils.defaultEmbed()
							.setTitle("There are no users in the specified Voice Channel.");
					event.getChannel().sendMessage(NoMemberEmbed.build()).complete();
					return;
				}
//				else if(MembersInVC.size()!=10) {
//					EmbedBuilder UTM = EmbedUtils.defaultEmbed()
//							.setTitle("The voice channel does not have 10 people!")
//							.setFooter(event.getAuthor().getId());
//					Message UnevenTeamsMessage = event.getChannel().sendMessage(UTM.build()).complete();
//				}
				else {
					Message GeneratedTeams = channel.sendMessage(generate(new ArrayList<Member>(vc.getMembers()),event.getGuild()).setFooter(event.getAuthor().getId()).build()).complete();
					GeneratedTeams.addReaction("U+1F501").queue();
					GeneratedTeams.addReaction("U+2705").queue();
					GeneratedTeams.addReaction("U+274C").queue();
					idToVC.put(event.getAuthor(), vc);
				}
				String Log = event.getAuthor().getAsMention() + " ran command customs: Server:" + event.getGuild().getName() + "(" + event.getGuild().getId() + "), " + formattedDate();
				event.getJDA().openPrivateChannelById("125698369283686400").complete().sendMessage(Log).complete();
			}
		}
	}
	
	//GeneratedTeamMEssages
	
	@Override
	@SuppressWarnings("unlikely-arg-type")
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) throws IndexOutOfBoundsException{
		if(event.getUser().equals(event.getJDA().getSelfUser())) {
			return;
		}
		//event.getChannel().sendMessage(event.retrieveMessage().complete().getMember().toString()).complete();
		//event.getChannel().sendMessage(event.getMember().toString()).complete();
		Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
		if(!message.getAuthor().equals(event.getJDA().getSelfUser()))
			return;
		if(message.getEmbeds().size()==0)
			return;
		MessageEmbed embed = message.getEmbeds().get(0);
		User user = null;
		try {
			user = event.getJDA().getUserById(embed.getFooter().getText());
		}catch(NumberFormatException nfe) {}
		if(user.equals(event.getJDA().getUserById(event.getUserId()))) {
			String title = message.getEmbeds().get(0).getTitle();
			if((title.equals("Generated Custom Teams:"))) {
				if(event.getReactionEmote().getName().equals("🔁")) {
					if(idToVC.get(user).getMembers().isEmpty()) {
						EmbedBuilder NoMemberEmbed = EmbedUtils.defaultEmbed()
								.setTitle("There are no users in the specified Voice Channel. Redo the command.");
						message.editMessage(NoMemberEmbed.build()).complete();
						message.clearReactions();
						return;
					}
//					else if(idToVC.get(user).getMembers().size()!=10) {
//						EmbedBuilder NoMemberEmbed = EmbedUtils.defaultEmbed()
//								.setTitle("There are not enough users in the specified Voice Channel. Redo the command.");
//						message.editMessage(NoMemberEmbed.build()).complete();
//						message.clearReactions();
//						return;
//					}
					message.clearReactions().queue();
					Message GeneratedTeams = message.editMessage(generate(new ArrayList<Member>(idToVC.get(event.getUser()).getMembers()), event.getGuild()).setFooter(embed.getFooter().getText()).build()).complete();
					GeneratedTeams.addReaction("U+1F501").queue();
					GeneratedTeams.addReaction("U+2705").queue();
					GeneratedTeams.addReaction("U+274C").queue();
				}
				else if(event.getReactionEmote().getAsCodepoints().equals("U+274c")) {
					Teams customTeams = Teams.checkGuild(event.getGuild());
					customTeams.removeTeam();
					customTeams = null; // Does this even do anything
					idToVC.remove(event.getUser());
					message.delete().queue();
					event.getChannel().sendMessage("Game Canceled...").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
				}
				else if(event.getReactionEmote().getName().equals("✅")) {
					// Clear Hashmap
					//idToVC.remove(event.getUser());
					// Give MMR to teams after win cmd
					// Move teams
					// Make Teams object null
					
					//Edit message to moving teams...
					//Move Players
					//Edit message to Who Won? 
					// Add reactions
					// Check for reactions
						//Check if reaction is from person who initiated game.
					message.clearReactions().queue();
					message.editMessage("Moving Players...").queue(); //.queue(m -> {m.delete().queueAfter(5, TimeUnit.SECONDS);});
					
					
					Guild guild = event.getGuild();
					VoiceChannel teamOne = null;
					VoiceChannel teamTwo = null;
					
					
					if(checkVoiceChannels(event.getGuild())==false) {
						Category customCategory = guild.createCategory("Custom").complete();
						teamOne = customCategory.createVoiceChannel("Team 1").complete();
						teamTwo = customCategory.createVoiceChannel("Team 2").complete();
					}else {
						teamOne = guild.getVoiceChannelsByName("Team 1", true).get(0);
						teamTwo = guild.getVoiceChannelsByName("Team 2", true).get(0);
					}
					
					Teams customTeams = Teams.checkGuild(event.getGuild());
					
					//Move Team 1
					for(CustomsPlayer m: customTeams.getTeamOne()) {
						guild.moveVoiceMember(event.getGuild().getMemberById(m.getUserID()), teamOne).queue();
					}
					//Move Team 2
					for(CustomsPlayer m: customTeams.getTeamTwo()) {
						guild.moveVoiceMember(event.getGuild().getMemberById(m.getUserID()), teamTwo).queue();
					}
					
					message.addReaction("U+1F7E6").queue();
					message.addReaction("U+1F7E5").queue();
					//message.addReaction("U+274C").queue();
					EmbedBuilder winnerConfirmation = new EmbedBuilder();
					winnerConfirmation.setTitle("Who Won?");
					winnerConfirmation.setDescription("The blue square is Team One and the red square is Team Two");
					winnerConfirmation.setFooter(event.getUserId());
					
					message.editMessage(winnerConfirmation.build()).queue();
				}
			}
			/////
			else if((title.equals("Who Won?"))) {
				if(event.getReactionEmote().getAsCodepoints().equals("U+1f7e6")) {
					
					//Change MMR
					Teams customTeams = Teams.checkGuild(event.getGuild());
					for(CustomsPlayer m: customTeams.getTeamOne()) {
						m.addWins(1);
						m.addMMR(10);
					}
					for(CustomsPlayer m: customTeams.getTeamTwo()) {
						m.addLosses(1);
						m.addMMR(-10);
					}
					
					customTeams.removeTeam();
					customTeams = null; // Does this even do anything
					
					
					EmbedBuilder gameEnded = new EmbedBuilder();
					gameEnded.setTitle("Game has concluded.");
					gameEnded.setDescription("Team One has Won!");
					gameEnded.setFooter(event.getUserId());
					message.editMessage("Finished.").queue();
					message.editMessage(gameEnded.build()).queue();
					message.clearReactions().queue();
					
					//Move everyone back
					VoiceChannel vc1 = event.getGuild().getVoiceChannelsByName("Team 1", true).get(0);
					VoiceChannel vc2 = event.getGuild().getVoiceChannelsByName("Team 2", true).get(0);
					VoiceChannel ovc = idToVC.get(user);
					System.out.println(vc1.toString());
					System.out.println(vc2.toString());
					System.out.println(ovc.toString());
					for(Member m: vc1.getMembers()) {
						event.getGuild().moveVoiceMember(m, ovc).queue();
					}
					for(Member m: vc2.getMembers()) {
						event.getGuild().moveVoiceMember(m, ovc).queue();
					}
					idToVC.remove(user);
					
				}
				else if(event.getReactionEmote().getAsCodepoints().equals("U+1f7e5")) {
					
					//Change MMR
					Teams customTeams = Teams.checkGuild(event.getGuild());
					for(CustomsPlayer m: customTeams.getTeamOne()) {
						m.addLosses(1);
						m.addMMR(-10);
					}
					for(CustomsPlayer m: customTeams.getTeamTwo()) {
						m.addWins(1);
						m.addMMR(10);
					}
					
					customTeams.removeTeam();
					customTeams = null; // Does this even do anything
					
					EmbedBuilder gameEnded = new EmbedBuilder();
					gameEnded.setTitle("Game has concluded.");
					gameEnded.setDescription("Team Two has Won!");
					gameEnded.setFooter(event.getUserId());
					message.editMessage("Finished.").queue();
					message.editMessage(gameEnded.build()).queue();
					message.clearReactions().queue();
					
					//Move everyone back
					VoiceChannel vc1 = event.getGuild().getVoiceChannelsByName("Team 1", true).get(0);
					VoiceChannel vc2 = event.getGuild().getVoiceChannelsByName("Team 2", true).get(0);
					VoiceChannel ovc = idToVC.get(user);
					System.out.println(vc1.toString());
					System.out.println(vc2.toString());
					System.out.println(ovc.toString());
					for(Member m: vc1.getMembers()) {
						event.getGuild().moveVoiceMember(m, ovc).queue();
					}
					for(Member m: vc2.getMembers()) {
						event.getGuild().moveVoiceMember(m, ovc).queue();
					}
					idToVC.remove(user);
				}
			}
		}
	}
	
	private boolean checkVoiceChannels(Guild guild) {
		if(guild.getVoiceChannelsByName("Team 1", true).size() == 0)
			return false;
		if(guild.getVoiceChannelsByName("Team 1", true).size() == 0)
			return false;
		return true;
	}
	
	public EmbedBuilder generate(ArrayList<Member> MemberList, Guild guild){
		Teams Team = Teams.checkGuild(guild);
		if(!(Team == null)) {
			Team.removeTeam();
		}
		Team = new Teams(guild, new ArrayList<CustomsPlayer>(), new ArrayList<CustomsPlayer>());
			//Create two arraylists
			//create new teams object after all for loops
		EmbedBuilder builder = new EmbedBuilder();
		EmbedBuilder finalembed = new EmbedBuilder();
		builder.setTitle("Generated Custom Teams: ");
		builder.setDescription("If you are unsatisfied with these teams, please react with the 🔁");
		//builder.setFooter("Created by Teapot#2273", Bot.jda.getUserByTag("Teapot#2273").getAvatarUrl());
		builder.setColor(0x4287f5); //html color picker < into google
		//ArrayList<Member> Team1 = new ArrayList<Member>();
		//ArrayList<Member> Team2 = new ArrayList<Member>();
		//System.out.println(MemberList);
		int teamOneMMR = 0;
		int teamTwoMMR = 0;
		int count = 0;
		int lowest = 50;
		
//		int lowesttwommr = 0;
//		int lowestonemmr = 0;
//		ArrayList<Member> finalList = new ArrayList<Member>();
		
//		while(count++ < 5) {
			do {
				builder.clearFields();
				Team.setTeamOne(new ArrayList<CustomsPlayer>());
				Team.setTeamTwo(new ArrayList<CustomsPlayer>());
				Collections.shuffle(MemberList);
				builder.addField("Team 1: ","",true);
				builder.addBlankField(true);
				builder.addField("Team 2: ","",true);
				
				teamOneMMR = 0;
				teamTwoMMR = 0;
				
				for(int i = 0; i<MemberList.size();i++) {
					Member m = MemberList.get(i);
					if(i%2==0) {
						if(CustomsPlayer.returnPlayer(m)==null)
							Team.addTeamOne(new CustomsPlayer(m.getUser().getId()));
						else
							Team.addTeamOne(CustomsPlayer.returnPlayer(m));
						builder.addField("MMR: " + CustomsPlayer.returnPlayer(m).getMMR(),m.getAsMention(),true);
						builder.addBlankField(true);
						teamOneMMR += CustomsPlayer.returnPlayer(m).getMMR();
						//System.out.println("Team 1 Player" + CustomsPlayer.returnPlayer(m).getMMR());
					}
					else {
						if(CustomsPlayer.returnPlayer(m)==null)
							Team.addTeamTwo(new CustomsPlayer(m.getUser().getId()));
						else
							Team.addTeamTwo(CustomsPlayer.returnPlayer(m));
						builder.addField("MMR: " + CustomsPlayer.returnPlayer(m).getMMR(),m.getAsMention(),true);
						teamTwoMMR += CustomsPlayer.returnPlayer(m).getMMR();
						//System.out.println("Team 2 Player" + CustomsPlayer.returnPlayer(m).getMMR());
					}
				}
			}while(checkBalance(Team.getTeamOne(),Team.getTeamTwo())==false);
//			if(Math.abs(teamOneMMR-teamTwoMMR)<lowest) {
//				//System.out.println(teamOneMMR);
//				//System.out.println(teamTwoMMR);
//				lowest = Math.abs(teamOneMMR-teamTwoMMR);
//				//System.out.println(lowest);
//				//finalembed = builder;
//				finalList = MemberList;
//				lowesttwommr = teamTwoMMR;
//				lowestonemmr = teamOneMMR;
//			}
//		}
//		Team.setTeamOne(new ArrayList<CustomsPlayer>());
//		Team.setTeamTwo(new ArrayList<CustomsPlayer>());
//		builder.clearFields();
//		teamOneMMR = 0;
//		teamTwoMMR = 0;
//		for(int i = 0; i<finalList.size();i++) {
//			Member m = finalList.get(i);
//			if(i%2==0) {
//				if(CustomsPlayer.returnPlayer(m)==null)
//					Team.addTeamOne(new CustomsPlayer(m.getUser().getId()));
//				else
//					Team.addTeamOne(CustomsPlayer.returnPlayer(m));
//				builder.addField("MMR: " + CustomsPlayer.returnPlayer(m).getMMR(),m.getAsMention(),true);
//				builder.addBlankField(true);
//				teamOneMMR += CustomsPlayer.returnPlayer(m).getMMR();
//			}
//			else {
//				if(CustomsPlayer.returnPlayer(m)==null)
//					Team.addTeamTwo(new CustomsPlayer(m.getUser().getId()));
//				else
//					Team.addTeamTwo(CustomsPlayer.returnPlayer(m));
//				builder.addField("MMR: " + CustomsPlayer.returnPlayer(m).getMMR(),m.getAsMention(),true);
//				teamTwoMMR += CustomsPlayer.returnPlayer(m).getMMR();
//			}
//		}
		builder.addField("Total MMR:", Integer.toString(teamOneMMR),true);
		builder.addBlankField(true);
		builder.addField("Total MMR:", Integer.toString(teamTwoMMR),true);
		return builder;
	}
	
	public boolean checkBalance(ArrayList<CustomsPlayer> teamOne,ArrayList<CustomsPlayer> teamTwo) {
		int teamOneMMR = 0;
		int teamTwoMMR = 0;
		for(CustomsPlayer cP: teamOne) {
			teamOneMMR += cP.getMMR();
		}
		for(CustomsPlayer cP: teamTwo) {
			teamTwoMMR += cP.getMMR();
		}
		if(Math.abs(teamOneMMR-teamTwoMMR)>30)
			return false;
		return true;
	}
}
