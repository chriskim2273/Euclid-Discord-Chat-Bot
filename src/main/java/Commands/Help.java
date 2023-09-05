package Commands;

import MainComponent.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Help extends ListenerAdapter{
	public static EmbedBuilder HelpEmbed = new EmbedBuilder();
	//private final CommandManager manager = new CommandManager();
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User user = event.getAuthor();
		if(user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		String prefix = Bot.prefix;
		String raw = event.getMessage().getContentRaw();
		
		if(raw.equalsIgnoreCase(prefix + "help")){
			HelpEmbed.clear();
			HelpEmbed.setTitle("Commands List: ")
			.setDescription("The Prefix for Euclid is \"" + Bot.prefix + "\"");
			//.addBlankField(false);
			
			HelpEmbed.addField("help","Bruh.",false);
			HelpEmbed.addField("solve","Type =solve and paste image in same message for solution to equation.",false);
			HelpEmbed.addField("totext","Type =totext and paste image in same message for text conversion.",false);
			HelpEmbed.addField("invite","Sends user bot invite link. Remember, the bot might be removed if unapproved by creator. (Teapot#2273)",false);
			HelpEmbed.addField("purge","Type =purge (x) to purge x amount of messages in the channel.",false);
			HelpEmbed.addField("sleepytime","Type =sleepytime to know when to sleep or when to wake up in order to not feel tired.",false);
			HelpEmbed.addField("generateteams","Type =generateteams {voice channel name (CASE SENSITIVE)} to generate randomized teams.",false);
			
			event.getChannel().sendMessage(HelpEmbed.build()).complete();
		}
		else if(raw.equalsIgnoreCase(prefix + "invite")){
			String inviteLink = "https://discord.com/oauth2/authorize?client_id=771871894806200341&scope=bot&permissions=8";
			String message = "Please remember that the bot is subject to removal if the creator disaproves.";
			event.getChannel().sendMessage(inviteLink + "\n" + message).queue();
		}
		/*
		if(raw.startsWith(prefix)) {
			manager.handle(event);
		}*/
	}
}
