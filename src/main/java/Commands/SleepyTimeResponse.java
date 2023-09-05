package Commands;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import MainComponent.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SleepyTimeResponse extends ListenerAdapter{
	private Member member;
	private TextChannel channel;
	private Message message;
	private boolean mode;
	private boolean meri;
	
	@Override
	@SuppressWarnings("deprecation")
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		if(event.getMessage().getAuthor().isBot() || event.isWebhookMessage()) {
			Bot.jda.removeEventListener(this);
			System.gc();
			return;
		}
		if(!event.getChannel().equals(channel)) {
			System.out.println(event.getMember() + "not channel");
			return;
		}
		if(!event.getMember().equals(member)) {
			System.out.println(event.getMember() + "not member");
		}
		if(!event.getMessage().getContentRaw().contains(":"))
			return;
		else {
			System.out.println("JOE");
			String[] hoursMinutes = event.getMessage().getContentRaw().split(":");
			if(hoursMinutes.length != 2) {
				message.getChannel().sendMessage(member.getAsMention() + ", incorrect input. Canceling command.").queue(message -> message.delete().queueAfter(5,TimeUnit.SECONDS));
				message.delete().queue();
				Bot.jda.removeEventListener(this);
				System.gc();
				return;
			}
			int Hour = 0;
			int Minute = 0;
			try {
				Hour = Integer.parseInt(hoursMinutes[0]);
				Minute = Integer.parseInt(hoursMinutes[1]);
			}catch(NumberFormatException NFE){
				//message.clearReactions().queue();
				event.getChannel().sendMessage(new EmbedBuilder().setTitle("INVALID TIME FORMAT. PLEASE TYPE TIME IN (HH:MM)").build()).queue();
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR, Hour);
			cal.set(Calendar.MINUTE, Minute);
			if(Hour == 12) //Weird glitch with only 12 o'clock.
				if(meri == true)
					meri = false;
				else if(meri == false)
					meri = true;
			if(meri == true)
				cal.set(Calendar.AM_PM, Calendar.AM);
			else
				cal.set(Calendar.AM_PM, Calendar.PM);
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setDescription("A complete nights rest is 5-6 sleep cycles.");
			
			if(mode==true) {
				String meridiem = "";
				String sleepMinute = "";
				String sleepHour = "";
				
				if(cal.get(Calendar.AM_PM) == Calendar.PM)
					meridiem = "PM";
				else if(cal.get(Calendar.AM_PM) == Calendar.AM)
					meridiem = "AM";
				if(cal.get(Calendar.MINUTE) == 0)
					sleepMinute = "00";
				else 
					sleepMinute = Integer.toString(cal.get(Calendar.MINUTE));
				if(cal.get(Calendar.HOUR) == 0)
					sleepHour = "12";
				else 
					sleepHour = Integer.toString(cal.get(Calendar.HOUR));
				
				embed.setTitle("If you sleep at " + sleepHour + ":" + sleepMinute + " " + meridiem + ", you should wake up at...");
				for(int i = 1; i<8; i++) {
					cal.add(Calendar.HOUR, 1);
					cal.add(Calendar.MINUTE,30);
					
					if(cal.get(Calendar.AM_PM) == Calendar.PM)
						meridiem = "PM";
					else if(cal.get(Calendar.AM_PM) == Calendar.AM)
						meridiem = "AM";
					
					if(cal.get(Calendar.MINUTE) == 0) {
						sleepMinute = "00";
					}else {
						sleepMinute = Integer.toString(cal.get(Calendar.MINUTE));
					}
					if(cal.get(Calendar.HOUR) == 0)
						sleepHour = "12";
					else 
						sleepHour = Integer.toString(cal.get(Calendar.HOUR));
					
					embed.addField((i + " Sleep Cycle(s)"), sleepHour + ":" + sleepMinute + " " + meridiem , true);
				}
				event.getMessage().delete().queue();
				message.editMessage(embed.build()).queue();
				event.getChannel().sendMessage(member.getAsMention()).queue(message -> message.delete().queueAfter(1, TimeUnit.SECONDS));
				Bot.jda.removeEventListener(this);
				SleepyTimeResponse STR = this;
				STR = null;
				System.gc();
				return;
			}
			else if(mode == false) {
				String meridiem = "";
				String sleepMinute = "";
				String sleepHour = "";
				
				if(cal.get(Calendar.AM_PM) == Calendar.PM)
					meridiem = "PM";
				else if(cal.get(Calendar.AM_PM) == Calendar.AM)
					meridiem = "AM";
				if(cal.get(Calendar.MINUTE) == 0)
					sleepMinute = "00";
				else 
					sleepMinute = Integer.toString(cal.get(Calendar.MINUTE));
				if(cal.get(Calendar.HOUR) == 0)
					sleepHour = "12";
				else 
					sleepHour = Integer.toString(cal.get(Calendar.HOUR));
				
				embed.setTitle("If you want to wake up at " + sleepHour + ":" + sleepMinute + " " + meridiem + ", you should sleep at...");
				for(int i = 1; i<8; i++) {
					cal.add(Calendar.HOUR, -1);
					cal.add(Calendar.MINUTE, -30);
					
					if(cal.get(Calendar.AM_PM) == Calendar.PM)
						meridiem = "PM";
					else if(cal.get(Calendar.AM_PM) == Calendar.AM)
						meridiem = "AM";
					
					if(cal.get(Calendar.MINUTE) == 0) {
						sleepMinute = "00";
					}else {
						sleepMinute = Integer.toString(cal.get(Calendar.MINUTE));
					}
					if(cal.get(Calendar.HOUR) == 0)
						sleepHour = "12";
					else 
						sleepHour = Integer.toString(cal.get(Calendar.HOUR));
					
					embed.addField((i + " Sleep Cycle(s)"), sleepHour + ":" + sleepMinute + " " + meridiem , true);
				}
				event.getMessage().delete().queue();
				message.editMessage(embed.build()).queue();
				event.getChannel().sendMessage(member.getAsMention()).queue(message -> message.delete().queueAfter(1, TimeUnit.SECONDS));
				Bot.jda.removeEventListener(this);
				System.gc();
				return;
			}
		}
	}
	
	public SleepyTimeResponse(Member member, TextChannel channel, Message message, boolean mode, boolean meri) {
		this.member = member;
		this.channel = channel;
		this.message = message;
		this.mode = mode;
		this.meri = meri;
	}
}
