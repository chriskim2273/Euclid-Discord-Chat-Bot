package Commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import MainComponent.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class osuYuri extends ListenerAdapter{
	
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
		int type = 0;
		String raw = event.getMessage().getContentRaw();
		
		if(raw.equalsIgnoreCase("get on osu")){
			event.getChannel().sendMessage("https://images-ext-1.discordapp.net/external/oIQfsW0Sz8whu30C1j7mSkjsmTCbBD_QExjk89XOhTc/%3Fwidth%3D710%26height%3D676/https/media.discordapp.net/attachments/647290867022823427/843843773859233792/20210516_234930.png").queue();
			type = 3;
		}
		else if(raw.equalsIgnoreCase("full combo") || raw.equalsIgnoreCase("fc")) {
			type = 1;
			event.getChannel().sendMessage("https://media.discordapp.net/attachments/647290867022823427/843842356461699102/20210514_063732.jpg?width=552&height=676").queue();
		} 
		else if(raw.equalsIgnoreCase("get on roblox")) {
			type = 2;
			event.getChannel().sendMessage("https://media.discordapp.net/attachments/647290867022823427/843846476768739368/4b6698c1b5135079e738af2f684f892f.jpg").queue();
		}
		if(type != 0) {
			String Log = event.getAuthor().getAsMention() + " ran command osuYuri ( " + type + " ): Server:" + event.getGuild().getName() + "(" + event.getGuild().getId() + "), " + formattedDate();
			event.getJDA().openPrivateChannelById("125698369283686400").complete().sendMessage(Log).complete();
		}
		/*if(user.getId().equals("615038138397491210")) // mute deston
			event.getMessage().delete().queue();*/
	}
}
