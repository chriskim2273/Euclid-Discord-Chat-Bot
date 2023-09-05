package MainComponent;

import java.net.UnknownHostException;

import javax.security.auth.login.LoginException;

import Commands.AdminstrativeCMDs;
import Commands.Help;
import Commands.PurgeCommand;
import Commands.Random;
import Commands.SleepyTime;
import Commands.SolvingCMD;
import Commands.ToTextCMD;
import Commands.VibrateCommand;
import Commands.generateTeams;
import Commands.osuYuri;
import Customs.CustomMongoDB;
import Customs.Customs;
import Customs.StatsCMD;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {
	public static String prefix = "=";
	public static JDA jda = null;
	public static String app_id = "REDACTED", app_key = "REDACTED"; // For MATHPIX
	public static boolean loadedPlayers = false;

	@SuppressWarnings({ "deprecation", "static-access" })
	private Bot() throws LoginException {
		String Token = "Insert Token (REMOVED TO MAKE REPOSITORY PUBLIC)";
		new JDABuilder();
		jda = JDABuilder
				.createDefault(Token)
				// Disable parts of the cache
				// .enableCache(CacheFlag.
				.setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
				.setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
				.enableIntents(GatewayIntent.GUILD_MEMBERS)
				// EnumSet.allOf(GatewayIntent.class)
				// Enable the bulk delete event
				// .setBulkDeleteSplittingEnabled(false)
				// Disable compression (not recommended)
				// .setCompression(Compression.NONE)
				.build();

		jda.getPresence().setStatus(OnlineStatus.ONLINE);
		jda.getPresence().setActivity(Activity.playing(" =help "));
		//

		jda.addEventListener(new SolvingCMD());
		jda.addEventListener(new ToTextCMD());
		jda.addEventListener(new AdminstrativeCMDs());
		jda.addEventListener(new AllowServer());
		jda.addEventListener(new Help());
		// jda.addEventListener(new startWordBomb());
		jda.addEventListener(new SleepyTime());
		jda.addEventListener(new PurgeCommand());
		jda.addEventListener(new generateTeams());
		jda.addEventListener(new Customs());
		jda.addEventListener(new StatsCMD());
		jda.addEventListener(new Random());
		jda.addEventListener(new VibrateCommand());
		// jda.addEventListener(new NoNickChange());
		jda.addEventListener(new osuYuri());
	}

	public static void main(String[] args) throws LoginException, UnknownHostException {
		new Bot();
		new CustomMongoDB();
		CustomMongoDB.retrievePlayers();
		// if(loadedPlayers==false) {
		// MongoDB.retrievePlayers();
		// System.out.println(CustomsPlayer.allCustomsPlayers.toString());
		// loadedPlayers = true;
		// }
		// CustomsPlayer.printPlayers();
	}
}
