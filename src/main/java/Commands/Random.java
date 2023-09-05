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

public class Random extends ListenerAdapter{
	
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
		
		String prefix = Bot.prefix;
		String command = "ran";
		String raw = event.getMessage().getContentRaw();
		String[] Separated = raw.split("\\s+");
		String[] args = raw.replaceFirst(prefix + command + " ", "").split("\\s+");
		
		if(Separated[0].toLowerCase().equalsIgnoreCase(prefix + command)){
			TextChannel channel = event.getChannel();
			if(args.length < 2) {
				channel.sendMessage(event.getAuthor().getAsMention() + " Please provide two numbers. =random {min} {max}").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
				return;
			}
			int min = Integer.parseInt(args[0]);
			int max = Integer.parseInt(args[1]);
			int random = (int) (Math.random() * (max - min + 1) + min);
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Your Generated Random Number: " + Integer.toString(random));
			channel.sendMessage(embed.build()).queue();
		}
	}
}
