package MainComponent;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Trueeee extends ListenerAdapter{
	@Override
	public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
		if(!event.getVoiceState().getChannel().getId().equals("780871210133094413"))
			return;
		if(event.getMember().getUser().getId().equals("212759975003553792")) {
			Member ivan = event.getMember();
			VoiceChannel vc = event.getGuild().getVoiceChannelById("780875331635773480");
			event.getGuild().moveVoiceMember(ivan, vc).complete();
		}
	}
    @Override
	public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
		if(!event.getVoiceState().getChannel().getId().equals("780871210133094413"))
			return;
		if(event.getMember().getUser().getId().equals("212759975003553792")) {
			Member ivan = event.getMember();
			VoiceChannel vc = event.getGuild().getVoiceChannelById("780875331635773480");
			event.getGuild().moveVoiceMember(ivan, vc).complete();
		}
	} 
}
