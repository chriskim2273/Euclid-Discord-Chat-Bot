package Commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import MainComponent.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SleepyTime extends ListenerAdapter{
	public static EmbedBuilder sleepyEmbed = new EmbedBuilder();
	
	private void buildEmbed() {
		sleepyEmbed.clear();
		sleepyEmbed.setTitle("SleepyTime");
		sleepyEmbed.setDescription("React with the ⌚ to switch between AM or PM. (SET IT BEFORE CHOOSING AN OPTION!) \n\nPlease Select Your Option: ");
		sleepyEmbed.addField("I have to WAKE UP at...","Please react with 🟦 if you have to wake up at a certain time.",true);
		sleepyEmbed.addField("I plan to FALL ASLEEP at...","Please react with 🟥 if you are planning to sleep at a certain time.",true);
		sleepyEmbed.setAuthor("PM or AM: AM");
	}
	
	private boolean checkMessage(Message message) {
		if(message.getEmbeds().size()==0)
			return false;
		MessageEmbed embed = message.getEmbeds().get(0);
		if(message.getAuthor().equals(Bot.jda.getSelfUser()) && embed.getTitle().equals("SleepyTime"))
			return true;
		return false;
	}
	
	public String formattedDate() {
		LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    return myDateObj.format(myFormatObj);
	}
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) throws IndexOutOfBoundsException{
		String command = event.getMessage().getContentRaw().toLowerCase();
		if(!command.equals(Bot.prefix + "sleepytime"))
			return;
		if(event.getAuthor().isBot() || event.isWebhookMessage())
			return;
		System.gc();
		buildEmbed();
		sleepyEmbed.setFooter(event.getAuthor().getId());
		Message sentEmbed = event.getChannel().sendMessage(sleepyEmbed.build()).complete();
		
		sentEmbed.clearReactions().queueAfter(60, TimeUnit.SECONDS);
		
		sentEmbed.addReaction("U+231A").queue();
		//sentEmbed.addReaction("U+23FA").queue();
		sentEmbed.addReaction("U+1F7E6").queue();
		sentEmbed.addReaction("U+1F7E5").queue();
		
		String Log = event.getAuthor().getAsMention() + " ran command sleepytime: Server:" + event.getGuild().getName() + "(" + event.getGuild().getId() + "), " + formattedDate();
		event.getJDA().openPrivateChannelById("125698369283686400").complete().sendMessage(Log).complete();
	}
	
	@Override
	@SuppressWarnings("unlikely-arg-type")
	public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
		if(event.getUser().isBot())
			return;
		Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
		if(checkMessage(message) == false)
			return;
		MessageEmbed embed = message.getEmbeds().get(0);
		if(!event.getJDA().getUserById(embed.getFooter().getText()).equals(event.getJDA().getUserById(event.getUserId())))
			return;
		System.out.println(event.getReactionEmote().getAsCodepoints());
		if(event.getReactionEmote().getAsCodepoints().equals("U+231a")) {
			String meridiem = embed.getAuthor().getName().toString().substring(10,12);
			System.out.println(meridiem);
			
			EmbedBuilder newEmbed = new EmbedBuilder();
			newEmbed.setTitle(embed.getTitle());
			newEmbed.setDescription(embed.getDescription());
			for(Field f: embed.getFields()) {
				newEmbed.addField(f);
			}
			newEmbed.setFooter(embed.getFooter().getText());
			
			if(meridiem.equals("PM")) {
				newEmbed.setAuthor("PM or AM: AM");
				message.editMessage(newEmbed.build()).queue();
			}
			else if(meridiem.equals("AM")) {
				newEmbed.setAuthor("PM or AM: PM");
				message.editMessage(newEmbed.build()).queue();
			}
		}
		else if(event.getReactionEmote().getAsCodepoints().equals("U+1f7e5")) {
			System.out.println("JOE");
			message.clearReactions().queue();
			//message.addReaction("U+231A").queue();
			EmbedBuilder newEmbed = new EmbedBuilder();
			newEmbed.setTitle(embed.getTitle());
			newEmbed.setDescription("Please respond within 15 seconds!");
			newEmbed.setFooter(embed.getFooter().getText());
			newEmbed.addField("You have selected: I plan to FALL ASLEEP at...","Please reply with the time in the format: \"HH:MM\"", false);
			message.editMessage(newEmbed.build()).queue();
			//addeventlistener
			boolean AM_PM = false;
			String meridiem = embed.getAuthor().getName().toString().substring(10,12);
			if(meridiem.equals("PM")) {
				AM_PM = false;
			}
			else if(meridiem.equals("AM")) {
				AM_PM = true;
			}
			SleepyTimeResponse STR = new SleepyTimeResponse(event.getMember(),event.getTextChannel(),event.getChannel().retrieveMessageById(event.getMessageId()).complete(),true,AM_PM);
			Bot.jda.addEventListener(STR);
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					if(message.getEmbeds().get(0).getTitle().equals("SleepyTime")) {
						//message.delete().queue();
						//event.getChannel().sendMessage(event.getMember().getAsMention() + ", you did not respond within 15 seconds. Cancelling command.").queue();
						Bot.jda.removeEventListener(STR);
					}
						
				}
				
			}, 40000);
		}
		else if(event.getReactionEmote().getAsCodepoints().equals("U+1f7e6")) {
			message.clearReactions().queue();
			//message.addReaction("U+231A").queue();
			EmbedBuilder newEmbed = new EmbedBuilder();
			newEmbed.setTitle(embed.getTitle());
			newEmbed.setDescription("Please respond within 15 seconds!");
			newEmbed.setFooter(embed.getFooter().getText());
			newEmbed.addField("You have selected: I have to WAKE UP at...","Please reply with the time in the format: \"HH:MM\"", false);
			message.editMessage(newEmbed.build()).queue();
			//addeventlistener
			
			boolean AM_PM = false;
			String meridiem = embed.getAuthor().getName().toString().substring(10,12);
			if(meridiem.equals("PM")) {
				AM_PM = false;
			}
			else if(meridiem.equals("AM")) {
				AM_PM = true;
			}
			SleepyTimeResponse STR = new SleepyTimeResponse(event.getMember(),event.getTextChannel(),event.getChannel().retrieveMessageById(event.getMessageId()).complete(),false,AM_PM);
			Bot.jda.addEventListener(STR);
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					if(message.getEmbeds().get(0).getTitle().equals("SleepyTime")) {
						//message.delete().queue();
						//event.getChannel().sendMessage(event.getMember().getAsMention() + ", you did not respond within 15 seconds. Cancelling command.").queue();
						Bot.jda.removeEventListener(STR);
					}
						
				}
				
			}, 40000);
		}
	}
	@Override
	@SuppressWarnings("unlikely-arg-type")
	public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
		if(event.getUser().isBot())
			return;
		Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
		if(checkMessage(message) == false)
			return;
		MessageEmbed embed = message.getEmbeds().get(0);
		if(!event.getJDA().getUserById(embed.getFooter().getText()).equals(event.getUser()))
			return;
		if(event.getReactionEmote().getName().equals("⌚")) {
			String meridiem = embed.getAuthor().getName().toString().substring(10,12);
			System.out.println(meridiem);
			
			EmbedBuilder newEmbed = new EmbedBuilder();
			newEmbed.setTitle(embed.getTitle());
			newEmbed.setDescription(embed.getDescription());
			for(Field f: embed.getFields())
				newEmbed.addField(f);
			newEmbed.setFooter(embed.getFooter().getText());
			
			if(meridiem.equals("PM")) {
				newEmbed.setAuthor("PM or AM: AM");
				message.editMessage(newEmbed.build()).queue();
			}
			else if(meridiem.equals("AM")) {
				newEmbed.setFooter("PM or AM: PM");
				message.editMessage(newEmbed.build()).queue();
			}
		}
	}
}
