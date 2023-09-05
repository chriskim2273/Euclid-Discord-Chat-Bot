package Commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import MainComponent.Bot;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class generateTeams extends ListenerAdapter{
	private static HashMap<Member,Message> UnevenTeamGenerationMessages = new HashMap<Member,Message>();
	private static HashMap<Member,Message> GeneratedTeamMessages = new HashMap<Member,Message>();
	private static HashMap<Message, Member> MessMem = new HashMap<Message, Member>();
	private static HashMap<Member,VoiceChannel> MemberVCMap = new HashMap<Member,VoiceChannel>();
	
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
		String command = "generateteams";
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
				MemberVCMap.remove(event.getMember());
				UnevenTeamGenerationMessages.remove(event.getMember());
				GeneratedTeamMessages.remove(event.getMember());
				ArrayList<Member> MembersInVC = new ArrayList<>(vc.getMembers());
				if(vc.getMembers().isEmpty()) {
					EmbedBuilder NoMemberEmbed = EmbedUtils.defaultEmbed()
							.setTitle("There are no users in the specified Voice Channel.");
					event.getChannel().sendMessage(NoMemberEmbed.build()).complete();
					return;
				}
				if(MembersInVC.size()%2!=0) {
					EmbedBuilder UTM = EmbedUtils.defaultEmbed()
							.setTitle("Uneven members are in the voice channel. Would you still like to generate teams?")
							.setFooter(event.getAuthor().getId());
					Message UnevenTeamsMessage = event.getChannel().sendMessage(UTM.build()).complete();
					UnevenTeamsMessage.addReaction("U+2705").queue();
					UnevenTeamsMessage.addReaction("U+274C").queue();
					UnevenTeamGenerationMessages.put(event.getMember(),UnevenTeamsMessage);
					MessMem.put(UnevenTeamsMessage,event.getMember());
					MemberVCMap.put(event.getMember(),vc);
				}else {
					//ArrayList<Member> adawd = new ArrayList<Member>(vc.getMembers());
					//System.out.println(adawd);
					Message GeneratedTeams = channel.sendMessage(generate(new ArrayList<Member>(vc.getMembers())).setFooter(event.getAuthor().getId()).build()).complete();
					GeneratedTeams.addReaction("U+1F501").queue();
					MessMem.put(GeneratedTeams,event.getMember());
					GeneratedTeamMessages.put(event.getMember(), GeneratedTeams);
					MemberVCMap.put(event.getMember(),vc);
				}
				String Log = event.getAuthor().getAsMention() + " ran command generateteams: Server:" + event.getGuild().getName() + "(" + event.getGuild().getId() + "), " + formattedDate();
				event.getJDA().openPrivateChannelById("125698369283686400").complete().sendMessage(Log).complete();
			}
		}
	}
	
	//GeneratedTeamMEssages
	
	@Override
	@SuppressWarnings("unlikely-arg-type")
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) throws NullPointerException{
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
		if(event.getJDA().getUserById(embed.getFooter().getText()).equals(event.getJDA().getUserById(event.getUserId()))) {
			//System.out.println("Check2");
			Message unevenmessage = UnevenTeamGenerationMessages.get(event.getMember());
			Message generatedmessage = GeneratedTeamMessages.get(event.getMember());
			if(!(unevenmessage==null)) {
				if(event.getReactionEmote().getName().equals("❌")) {
					unevenmessage.delete().complete();
					MemberVCMap.remove(UnevenTeamGenerationMessages.get(event.getMember()));
					UnevenTeamGenerationMessages.remove(event.getMember());
					GeneratedTeamMessages.remove(event.getMember());
					unevenmessage.getChannel().sendMessage("Team generation canceled.").queue(m -> {m.delete().queueAfter(5, TimeUnit.SECONDS);});
					event.retrieveMessage().complete().delete().queue();
					return;
				}else if(event.getReactionEmote().getName().equals("✅")) {
					message.clearReactions().queue();
					Message GeneratedTeams = message.editMessage(generate(new ArrayList<Member>(MemberVCMap.get(event.getMember()).getMembers())).setFooter(embed.getFooter().getText()).build()).complete();
					GeneratedTeams.addReaction("U+1F501").queue();
					message.clearReactions().queueAfter(30, TimeUnit.SECONDS);
					UnevenTeamGenerationMessages.remove(event.getMember());
					MessMem.put(GeneratedTeams,event.getMember());
					GeneratedTeamMessages.put(event.getMember(), GeneratedTeams);
					//event.retrieveMessage().complete().delete().queue();
				}
			}else if(!(generatedmessage==null)) {
				if(event.getReactionEmote().getName().equals("🔁")) {
					if(MemberVCMap.get(event.getMember()).getMembers().isEmpty()) {
						EmbedBuilder NoMemberEmbed = EmbedUtils.defaultEmbed()
								.setTitle("There are no users in the specified Voice Channel.");
						event.getChannel().sendMessage(NoMemberEmbed.build()).complete();
						return;
					}
					message.clearReactions().queue();
					Message GeneratedTeams = message.editMessage(generate(new ArrayList<Member>(MemberVCMap.get(event.getMember()).getMembers())).setFooter(embed.getFooter().getText()).build()).complete();
					GeneratedTeams.addReaction("U+1F501").queue();
					UnevenTeamGenerationMessages.remove(event.getMember());
					GeneratedTeamMessages.put(event.getMember(), GeneratedTeams);
					MessMem.put(GeneratedTeams,event.getMember());
					//event.retrieveMessage().complete().delete().queue();
				}
			}
		}
	}
	
	public EmbedBuilder generate(ArrayList<Member> MemberList){
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Generated Teams: ");
		builder.setDescription("If you are unsatisfied with these teams, please react with the 🔁");
		//builder.setFooter("Created by Teapot#2273", Bot.jda.getUserByTag("Teapot#2273").getAvatarUrl());
		builder.setColor(0x4287f5); //html color picker < into google
		//ArrayList<Member> Team1 = new ArrayList<Member>();
		//ArrayList<Member> Team2 = new ArrayList<Member>();
		//System.out.println(MemberList);
		Collections.shuffle(MemberList);
		builder.addField("Team 1: ","",true);
		builder.addBlankField(true);
		builder.addField("Team 2: ","",true);
		for(int i = 0; i<MemberList.size();i++) {
			Member m = MemberList.get(i);
			if(i%2==0) {
				builder.addField(m.getUser().getAsTag(),m.getAsMention(),true);
				builder.addBlankField(true);
			}
			else
				builder.addField(m.getUser().getAsTag(),m.getAsMention(),true);
			}
		return builder;
	}
}
