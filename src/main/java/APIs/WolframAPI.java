package APIs;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

import net.dv8tion.jda.api.EmbedBuilder;

public class WolframAPI {

    // PUT YOUR APPID HERE:
    private static String appid = "REDACTED";

    public static EmbedBuilder Solve(String Equation) {
        EmbedBuilder Embed = new EmbedBuilder();
        if (Equation == null) {
            Embed.setTitle("Unable to analyze the image. Sorry. Maybe try a clearer picture.");
            return Embed;
        }

        String input = Equation;

        // The WAEngine is a factory for creating WAQuery objects,
        // and it also used to perform those queries. You can set properties of
        // the WAEngine (such as the desired API output format types) that will
        // be inherited by all WAQuery objects created from it. Most applications
        // will only need to crete one WAEngine object, which is used throughout
        // the life of the application.

        WAEngine engine = new WAEngine();
        // These properties will be set in all the WAQuery objects created from this
        // WAEngine.
        engine.setAppID(appid);
        engine.addFormat("plaintext");

        // Create the query.
        WAQuery query = engine.createQuery();

        // Set properties of the query.
        query.setInput(input);

        // Embed
        Embed.setTitle("Solution (Please ensure the bot analyzed the image properly): ");

        try {
            // For educational purposes, print out the URL we are about to send:
            System.out.println("Query URL:");
            System.out.println(engine.toURL(query));
            System.out.println("");

            // This sends the URL to the Wolfram|Alpha server, gets the XML result
            // and parses it into an object hierarchy held by the WAQueryResult object.
            WAQueryResult queryResult = engine.performQuery(query);

            if (queryResult.isError()) {
                Embed.setTitle("Query Error.");
                Embed.addField("Error Code: ", Integer.toString(queryResult.getErrorCode()), true);
                Embed.addField("Error Message: ", queryResult.getErrorMessage(), true);
            } else if (!queryResult.isSuccess()) {
                Embed.setTitle("Query was not understood; no results available.");
            } else {
                // Got a result.
                System.out.println("Successful query. Pods follow:\n");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        String Title = "";
                        Embed.setFooter(pod.getTitle());
                        //
                        Title = pod.getTitle();
                        System.out.println(pod.getTitle());
                        System.out.println("------------");
                        pod.acquireImages();
                        for (WASubpod subpod : pod.getSubpods()) {

                            for (Object element : subpod.getContents()) {
                                // System.out.println(element.toString());
                                // System.out.println(element.getClass().toString());
                                if (element instanceof WAPlainText) {

                                    // Embed.appendDescription("\n");
                                    // Embed.appendDescription(((WAPlainText) element).getText());
                                    Embed.addField(Title, ((WAPlainText) element).getText(), false);
                                    // Embed.addField("----", "" , false);
                                    System.out.println(((WAPlainText) element).getText());
                                    System.out.println("");
                                }
                            }
                        }
                        Embed.appendDescription("\n");
                        System.out.println("");
                    }
                }
                // We ignored many other types of Wolfram|Alpha output, such as warnings,
                // assumptions, etc.
                // These can be obtained by methods of WAQueryResult or objects deeper in the
                // hierarchy.
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
        return Embed;
    }

}