package Commands;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.json.simple.parser.ParseException;

import EmbedBuilders.ORC;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SolvingCMD extends ListenerAdapter{
	
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
		
		if(event.getMessage().getContentRaw().equals(MainComponent.Bot.prefix + "solve")){
			TextChannel channel = event.getChannel();
			if(event.getMessage().getAttachments().size()>0) {
				System.out.println(event.getMessage().getAttachments().get(0).getUrl());
				String url = event.getMessage().getAttachments().get(0).getUrl();
				try {
				    //, event.getJDA().getUserById("125698369283686400").getAvatarUrl()
					channel.sendMessage(ORC.OCR(url).setFooter("This bot was brought to you by Teapot#2273 :)").setThumbnail(url).setColor(new Color(0, 51, 102)).build()).queue();
					channel.sendMessage(event.getAuthor().getAsMention()).queue(message -> {message.delete().queueAfter(3, TimeUnit.SECONDS);});
					String Log = event.getAuthor().getAsMention() + " ran command solve: Server:" + event.getGuild().getName() + "(" + event.getGuild().getId() + "), " + formattedDate();
					event.getJDA().openPrivateChannelById("125698369283686400").complete().sendMessage(Log).complete();
				} catch (IOException | ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void onReady(ReadyEvent e){
		String server = "bot running\n";
		System.out.println(server);
	}   
}
