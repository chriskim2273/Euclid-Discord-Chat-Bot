package Commands;

import javax.annotation.Nonnull;

import Games.Player;
import Games.WordBomb;
import MainComponent.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class startWordBomb extends ListenerAdapter{
	
	public WordBomb getGame(TextChannel eventChannel) {
    	for(WordBomb games: WordBomb.onGoingGames) {
    		if(games.getGameChannel().equals(eventChannel))
    			return games;
    	}
    	return null;
	}
	
	public boolean ServerHasGame(TextChannel eventChannel) {
    	for(WordBomb games: WordBomb.onGoingGames) {
    		if(games.getGameChannel().getGuild().equals(eventChannel.getGuild()))
    			return true;
    	}
    	return false;
	}
	
	public boolean checkMessage(String id) {
		if(WordBomb.messageIDtoGame.containsKey(id))
			return true;
		return false;
	}
	
	public EmbedBuilder returnPurgatoryEmbed(User author) {
		EmbedBuilder Purgatory = new EmbedBuilder();
		Purgatory.setAuthor(author.getName() + "'s Game.",author.getAvatarUrl());
		Purgatory.setTitle("WordBomb!");
		//Purgatory.setColor(color);
		Purgatory.setDescription("Players:");
		Purgatory.setFooter("Game begins once Host reacts with ");
		return Purgatory;
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().isBot() || event.isWebhookMessage())
			return;
		
		//Start Game
		if(event.getMessage().getContentRaw().equals(Bot.prefix + "WordBomb")) {
			
			if(WordBomb.checkIfGameExists(event.getChannel()))
				return;
			if(ServerHasGame(event.getChannel()))
				return;
			
			EmbedBuilder Purgatory = returnPurgatoryEmbed(event.getAuthor());
			
			Message joinEmbed = event.getChannel().sendMessage(Purgatory.build()).complete();
			joinEmbed.addReaction("U+2705").queue();
			joinEmbed.addReaction("U+1F7E2").queue();
			
			WordBomb newGame = new WordBomb(event.getChannel(),event.getAuthor());
			WordBomb.messageIDtoGame.put(joinEmbed.getId(), newGame);
			/*
			try {
				event.getChannel().sendMessage(WordBomb.APITest("Josdasdade")).complete(); // Returns exception because Method returns null, and you can not send a null message.
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		else if(event.getMessage().getContentRaw().equals(Bot.prefix + "JOE")) {
			System.out.println("Debug1");
			event.getJDA().addEventListener(new Player(event.getMember()));
			//event.getJDA().removeEventListener(listeners);
		}
	}
	
    @Override
	public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
    	if(event.getUser().equals(event.getJDA().getSelfUser()))
    		return;
    	
    	// ADD MESSAGES AS TO WHY GAME IS NOT STARTING
    	
    	
    	if(checkMessage(event.getMessageId())==false)
    		return;
    	WordBomb Game = getGame(event.getChannel());
    	if(Game == null)
    		return;
    	System.out.println("1");
    	if(event.getReactionEmote().getName().equals("✅")) {
	    	User user = event.getUser();
	    	Message gameMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
	    	MessageEmbed gameEmbed = gameMessage.getEmbeds().get(0);
	    	EmbedBuilder Purgatory = returnPurgatoryEmbed(Game.getHost());
	    	for(Field f: gameEmbed.getFields()) {
	    		System.out.println("f");
	    		Purgatory.addField(f);
	    	}
	    	Purgatory.addField(user.getAsTag(),user.getAsMention(),true);
	    	gameMessage.editMessage(Purgatory.build()).queue();
	    	Game.addPlayer(event.getMember());
	    	
	    	//Maybe send Player added message?
    	}
    	else if(event.getReactionEmote().getName().equals("🟢")) {
    		if(event.getUser().equals(Game.getHost())) {
    			//start game
    			Message purgatoryMessage = event.retrieveMessage().complete();
    			purgatoryMessage.delete().queue();
    			
    			Game.StartGame();
    			
    			/*
    			Timer timer = new Timer();
    			timer.scheduleAtFixedRate(new TimerTask() {
    				int interval = 20;
					@Override
					public void run() {
						event.getChannel().editMessageById(event.getMessageId(), Integer.toString(setInterval())).queue();
					}
					public int setInterval() {
						if(interval == 1) {
							timer.cancel();
						}
						return --interval;
					}
					
    			}, 100, 3000);*/

    		}
    	}

    }
    @Override
	public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
    	if(!event.getReactionEmote().getName().equals("✅"))
    		return;
    	if(event.getUser().equals(event.getJDA().getSelfUser()))
    		return;
    	if(checkMessage(event.getMessageId())==false)
    		return;
    	
    	WordBomb Game = getGame(event.getChannel());
    	if(Game == null)
    		return;
    	
    	User user = event.getUser();
    	Message gameMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
    	MessageEmbed gameEmbed = gameMessage.getEmbeds().get(0);
    	EmbedBuilder Purgatory = returnPurgatoryEmbed(Game.getHost());
    	for(Field f: gameEmbed.getFields()) {
    		if(!f.getName().equals(user.getAsTag()))
    			Purgatory.addField(f);
    	}
    	gameMessage.editMessage(Purgatory.build()).queue();
    	Game.removePlayer(event.getMember());
    	
    	//Maybe send Player removed message?
    }
	
}
