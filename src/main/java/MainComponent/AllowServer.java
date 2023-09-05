package MainComponent;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AllowServer extends ListenerAdapter{
	
	@Override
	public void onGuildJoin(@Nonnull GuildJoinEvent event) {
		String GuildID = event.getGuild().getId();
		String GuildName = event.getGuild().getName();
		//String GuildOwner = event.getGuild().
		String myID = "125698369283686400";
		String Message = "Euclid has joined server " + "(" + GuildName + ":" + GuildID + "). Do you accept?";
		
		PrivateChannel VerificationChannel = event.getJDA().openPrivateChannelById(myID).complete();
		
		VerificationChannel.sendMessage(Message).complete(); // Verification Detail Message
		Message IDMessage = VerificationChannel.sendMessage(GuildID).complete(); // Message that has the Server ID
		IDMessage.addReaction("U+2705").queue();
		IDMessage.addReaction("U+274C").queue();
		
	}
	
	@Override
	public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
		User user = event.getUser();
		if(user.isBot())
			return;
		if(user.equals(event.getJDA().getSelfUser()))
			return;
		if(event.getReactionEmote().getName().equals("✅")) {
			event.getChannel().deleteMessageById(event.getMessageId()).queue();
		}else if(event.getReactionEmote().getName().equals("❌")) {
			String ServerID = event.getChannel().retrieveMessageById(event.getMessageId()).complete().getContentRaw();
			Guild leavingGuild = event.getJDA().getGuildById(ServerID);
			TextChannel mainTextChannel = leavingGuild.getSystemChannel();
			if(mainTextChannel == null)
				mainTextChannel = leavingGuild.getDefaultChannel();
			event.getChannel().deleteMessageById(event.getMessageId()).complete();
			try {
				mainTextChannel.sendMessage("The owner of this bot (Teapot#2273) has rejected the bot's presence in this server, and has therefore left. If this was an issue please contact Teapot#2273").queue();
			}catch(Exception InsufficientPermissionException) { System.out.println("Can't chat in channel...bruh!");}
			leavingGuild.leave().queue();
		}
	}
}
