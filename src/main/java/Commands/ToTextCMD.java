package Commands;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.json.simple.parser.ParseException;

import EmbedBuilders.ToText;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ToTextCMD extends ListenerAdapter{
	
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
		
		if(event.getMessage().getContentRaw().equals(MainComponent.Bot.prefix + "totext")){
			TextChannel channel = event.getChannel();
			if(event.getMessage().getAttachments().size()>0) {
				System.out.println(event.getMessage().getAttachments().get(0).getUrl());
				String url = event.getMessage().getAttachments().get(0).getUrl();
				try {
					Message ttm = channel.sendMessage(ToText.toText(url).setFooter("This bot was brought to you by Teapot#2273 :)").setThumbnail(url).setColor(new Color(0, 51, 102)).setFooter("Press 🔗 for a link").build()).complete();
					channel.sendMessage(event.getAuthor().getAsMention()).queue(message -> {message.delete().queueAfter(3, TimeUnit.SECONDS);});
					String Log = event.getAuthor().getAsMention() + " ran command totext: Server:" + event.getGuild().getName() + "(" + event.getGuild().getId() + "), " + formattedDate();
					event.getJDA().openPrivateChannelById("125698369283686400").complete().sendMessage(Log).complete();
					//ToText.toText(url).getfie
					ttm.addReaction("U+1F517").queue();
				} catch (IOException | ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
		if(event.getUser().equals(event.getJDA().getSelfUser())) {
			return;
		}
		if(event.getReactionEmote().getName().equals("🔗")) {
			Message message = event.retrieveMessage().complete();
			List<MessageEmbed> Embeds = message.getEmbeds();
			if(Embeds.size()<1 && !message.getAuthor().equals(event.getJDA().getSelfUser()))
				return;
			String URL = "https://www.google.com/search?q=";
			String EmbedDescription = Embeds.get(0).getDescription();
			String[] Description = EmbedDescription.split("\\s+");
			if(Description[0].equals("Unable"))
				return;
			for(String s: Description) {
				URL += s;
				URL += "+";
			}
			event.getChannel().sendMessage(URL).complete();
			message.clearReactions().complete();
		}
	}
}
