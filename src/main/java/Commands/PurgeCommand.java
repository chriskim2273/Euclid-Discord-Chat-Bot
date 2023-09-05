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

public class PurgeCommand extends ListenerAdapter{
	
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
		String command = "purge";
		String raw = event.getMessage().getContentRaw();
		String[] Separated = raw.split("\\s+");
		String[] args = raw.replaceFirst(prefix + command + " ", "").split("\\s+");
		
		if(Separated[0].toLowerCase().equalsIgnoreCase(prefix + command)){
			if(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
				ArrayList<Message> MessageList = new ArrayList<>();
				for(Message m: channel.getHistoryBefore(channel.getLatestMessageId(), Integer.parseInt(args[0])).complete().getRetrievedHistory()) {
					MessageList.add(m);
				}
				channel.purgeMessages(MessageList);
				EmbedBuilder purge = new EmbedBuilder();
				purge.setTitle("Purged the last " + args[0] + " messages(s)...");
				purge.setFooter("Created by Teapot#2273", Bot.jda.getUserByTag("Teapot#2273").getAvatarUrl());
				channel.sendMessage(purge.build()).queue(message -> {message.delete().queueAfter(2, TimeUnit.SECONDS);});
				event.getMessage().delete().queue();
				
				String Log = event.getAuthor().getAsMention() + " ran command purge: Server:" + event.getGuild().getName() + "(" + event.getGuild().getId() + "), " + formattedDate();
				event.getJDA().openPrivateChannelById("125698369283686400").complete().sendMessage(Log).complete();
			}
		}
	}
}
