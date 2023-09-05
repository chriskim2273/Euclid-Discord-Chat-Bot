package Commands;

import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class VibrateCommand extends ListenerAdapter{
	@SuppressWarnings("unlikely-arg-type")
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().equals(MainComponent.Bot.jda.getSelfUser())) {
			return;
		}
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if(args[0].toLowerCase().equalsIgnoreCase(MainComponent.Bot.prefix + "vibrate")) {
			//Give role
			if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				EmbedBuilder VibrateRoleEmbed = new EmbedBuilder()
						.setFooter("Created by Teapot#2273", MainComponent.Bot.jda.getUserByTag("Teapot#2273").getAvatarUrl());
				//Make role
				if(event.getGuild().getRolesByName("Vibrator", true).isEmpty()) {
					Role role = event.getGuild().createRole().setName("Vibrator").complete();
					VibrateRoleEmbed.setTitle("Created Vibrator role. If you are an administrator, you will automatically receive the role. Only those with this role can use the command.");
					event.getChannel().sendMessage(VibrateRoleEmbed.build()).complete();
				}
				if(!(event.getMember().getRoles().contains(event.getGuild().getRolesByName("Vibrator", true).get(0)))){
					event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("Vibrator", true).get(0)).complete();
					VibrateRoleEmbed.setTitle("You have received the Vibrator role. Please re-use the command.");
					event.getChannel().sendMessage(VibrateRoleEmbed.build()).complete();
				}
			}
		}
		
		
		if(args[0].toLowerCase().equalsIgnoreCase(MainComponent.Bot.prefix + "vibrate")&& event.getMember().getRoles().contains(event.getGuild().getRolesByName("Vibrator", true).get(0))) {
			String Username = "";
			if(args.length<2) {
				event.getChannel().sendMessage("Enter a username.");
			}
			else {
				Username = event.getMessage().getContentRaw().replace(MainComponent.Bot.prefix + "vibrate ", "");
				System.out.println(Username);
				VoiceChannel Current = event.getGuild().getMemberByTag(Username).getVoiceState().getChannel();
				ArrayList<VoiceChannel> vcs = new ArrayList<VoiceChannel>(event.getGuild().getVoiceChannels());
				Member victim = event.getGuild().getMemberByTag(Username);
				//System.out.println("Bot " + event.getJDA().getSelfUser().getAsTag() + " is now vibrating " + event.getGuild().getMemberByTag(Username).getNickname().toString());
				for(int i = 0; i<9;i++) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					VoiceChannel vc = vcs.get((int)(Math.random()*vcs.size()));
					event.getGuild().moveVoiceMember(victim, vc).complete();
					//System.out.println(count);
				}
				event.getGuild().moveVoiceMember(victim, Current).complete();
			}
		}
	}
}
