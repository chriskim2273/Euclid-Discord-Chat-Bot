package Commands;

import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class AdminstrativeCMDs extends ListenerAdapter{
	
	public static AudioManager audioManagervc;
	public static VoiceChannel voiceChannelvc;
	
	public int[] returnCurlyBracketIndices(String message) {
		int Indices[] = new int[2];
		for(int i = 0; i<message.length();i++) {
			if(message.charAt(i) == '{') {
				Indices[0] = i;
			}
			if(message.charAt(i) == '}') {
				Indices[1] = i;
			}
		}
		return Indices;
	}
	
	@Override
	public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
		User user = event.getAuthor();
		if(user.isBot())
			return;
		if(user.equals(event.getJDA().getSelfUser()))
			return;
		if(event.getMessage().getContentRaw().startsWith("=")) {
			String messageWithoutEquals = event.getMessage().getContentRaw().replaceFirst("=", "");
			int Indices[] = returnCurlyBracketIndices(messageWithoutEquals);
			String[] args = messageWithoutEquals.split("\\s+");
			messageWithoutEquals = messageWithoutEquals.substring((Indices[0]+1), Indices[1]);
			
			TextChannel textChannel = event.getJDA().getGuildById(args[0]).getTextChannelById(args[1]);
			if(textChannel == null)
				return;
			textChannel.sendMessage(messageWithoutEquals).queue();
		}
		else if(event.getMessage().getContentRaw().startsWith("+")) {
			String messageWithoutEquals = event.getMessage().getContentRaw().replaceFirst("=", "");
			int Indices[] = returnCurlyBracketIndices(messageWithoutEquals);
			String[] args = messageWithoutEquals.split("\\s+");
			
			audioManagervc = event.getJDA().getGuildById(args[0]).getAudioManager();
			VoiceChannel voiceChannel = event.getJDA().getGuildById(args[0]).getVoiceChannelById(args[1]);
			if(voiceChannel == null)
				return;
			audioManagervc.openAudioConnection(voiceChannel);
		}else if(event.getMessage().getContentRaw().startsWith("-")) {
			audioManagervc.closeAudioConnection();
		}
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		User user = event.getAuthor();
		if(user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		if(event.getMessage().getContentRaw().equals(MainComponent.Bot.prefix + "leaveserver")) {
			String[] args = event.getMessage().getContentRaw().split("\\s+");
			if(user.getId().equals("125698369283686400")) {
				if(args.length==2)
					event.getJDA().getGuildById(args[1]).leave().complete();
				else if(args.length==1)
					event.getGuild().leave().complete();
			}
		}
		
		
		else if(event.getMessage().getContentRaw().equals(MainComponent.Bot.prefix + "listservers")) {
			if(user.getId().equals("125698369283686400")) {
				List<Guild> cGuild = event.getJDA().getGuilds();
				String listOfGuilds = "";
				for(Guild g: cGuild) {
					listOfGuilds += g.getName() + ": " + g.getId() + "\n\n";
				}
				event.getChannel().sendMessage(listOfGuilds).complete();
			}
		}
	}
}
