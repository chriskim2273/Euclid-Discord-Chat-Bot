package Commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NoNickChange extends ListenerAdapter{
	
	@Override
	public void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {
		Guild guild = event.getGuild();
		Member member = event.getMember();
		if(!guild.getId().equals("647290866146344979"))
			return;
		if(member.getId().equals("125698369283686400")) {
			if(!member.getNickname().equals("Netorare Apollinaire"))
				guild.modifyNickname(member, "Netorare Apollinaire").queue();
		}
		/*
		else if(member.getId().equals("674728526217478195")) {
			if(!member.getNickname().equals("Poyato's Sex Slave"))
				guild.modifyNickname(member, "Poyato's Sex Slave").queue();
		}
		else if(member.getId().equals("451467799974641684")) {
			if(!member.getNickname().equals("nerd"))
				guild.modifyNickname(member, "nerd").queue();
		}
		else if(member.getId().equals("735528991234195617")) {
			if(!member.getNickname().equals("JOAEISBAE69"))
				guild.modifyNickname(member, "JOAEISBAE69").queue();
		}
		*/
		else
			return;
	}
	
}
