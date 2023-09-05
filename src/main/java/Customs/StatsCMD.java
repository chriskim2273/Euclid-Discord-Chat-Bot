package Customs;

import MainComponent.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StatsCMD extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User user = event.getAuthor();
		if(user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		TextChannel channel = event.getChannel();
		Message message = event.getMessage();
		
		String prefix = Bot.prefix;
		String command = "cstats";
		String raw = event.getMessage().getContentRaw();
		String[] Separated = raw.split("\\s+");
		String arg = raw.replaceFirst(prefix + command + " ", "");
		
		if(Separated[0].toLowerCase().equalsIgnoreCase(prefix + command)){
			Member member = null;
			String avatarURL = "";
			if(message.getMentionedMembers().size()>0) {
				//Get Data for another Person
				member = message.getMentionedMembers().get(0);
				avatarURL = member.getUser().getAvatarUrl();
			}
			else {
				//Get Data for Self
				member = event.getMember();
				avatarURL = event.getAuthor().getAvatarUrl();
			}
			CustomsPlayer player = CustomsPlayer.returnPlayer(member);
			if(player == null) {
				player = new CustomsPlayer(event.getAuthor().getId());
			}
			
			EmbedBuilder Stats = new EmbedBuilder();
			Stats.setTitle("Customs Stats: " + member.getUser().getAsTag());
			Stats.setFooter(event.getAuthor().getId());
			Stats.setImage(avatarURL);
			
			Stats.addField("MMR",Integer.toString(player.getMMR()),true);
			Stats.addField("Wins",Integer.toString(player.getWins()),true);
			Stats.addField("Losses",Integer.toString(player.getLosses()),true);
			
			float totalGames = (float)(player.getLosses()+player.getWins());
			Stats.addField("Total Games",Integer.toString(Math.round(totalGames)),true);
			
			if(totalGames == 0)
				Stats.addField("Win Percentage","N/A",true);
			else
				Stats.addField("Win Percentage",Float.toString((player.getWins()/totalGames)*100) + "%",true);
			
			channel.sendMessage(Stats.build()).queue();
		}
	}
}
