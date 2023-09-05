package Games;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Player extends ListenerAdapter{
	public static ArrayList<Member> AllPlayers = new ArrayList<Member>();
	
	private Member Player;
	private int Lives;
	private int Health;
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		if(event.getMember().equals(Player)) {
			event.getChannel().sendMessage("Joe.." + Player.getAsMention()).submit();
		}
	}
	public Player(Member member) {
		this.Player = member;
		AllPlayers.add(Player);
	}
}
