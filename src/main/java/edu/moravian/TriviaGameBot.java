package edu.moravian;

import edu.moravian.exceptions.SecretsException;
import edu.moravian.exceptions.StorageException;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * This class is responsible for starting the bot and connecting it to Discord as well as initiating the game.
 */

public class TriviaGameBot {
    public static void main(String[] args) {
        TriviaStorage storage = createStorage();
        BotResponder responder = createResponder(storage);
        String token = loadToken();
        startBot(responder, token);

    }

    private static RedisStorage createStorage() {
        RedisStorage storage = null;
        try {
            storage = new RedisStorage("localhost", 6379);
            storage.testConnection();
        } catch (StorageException e) {
            System.err.println("Failed to connect to Redis\n\nIs it running?");
            System.exit(1);
        }

        return storage;
    }

    private static BotResponder createResponder(TriviaStorage storage) {
        TriviaGame game = new TriviaGame(storage);
        return new BotResponder(game);
    }

    private static String loadToken() {
        try {
            String secretName = "220_Discord_Token";
            String secretKey = "DISCORD_TOKEN";

            Secrets secrets = new Secrets();

            String secret = secrets.getSecret(secretName, secretKey);
            return secret;
        } catch (SecretsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

//        try
//        {
//            Dotenv dotenv = Dotenv.load();
//            return dotenv.get("DISCORD_TOKEN");
//        }
//        catch(DotenvException e)
//        {
//            System.err.println("Failed to load .env file\n\nIs it present?");
//            System.exit(1);
//            return null;
//        }
//}
    private static void startBot(BotResponder responder , String token) {
        JDA api = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        api.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(MessageReceivedEvent event) {
                if (event.getAuthor().isBot())
                    return;
                if (!event.getChannel().getName().equals("deborah-channel"))
                    return;
                String username = event.getAuthor().getName();
                String message = event.getMessage().getContentRaw();
                String response = responder.respond(username, message);
                if (response != null && !response.isEmpty()) {
                    if (response.contains("|")) {
                        response = formatQuestion(response);
                }
                    event.getChannel().sendMessage(response).queue();
            }
        }
    });
    }
    private static String formatQuestion(String question) {
        String[] parts = question.split("\\|");
        if (parts.length != 2) {
            return question;
        }
        String questionText = parts[0];
        String[] options = parts[1].split(",");
        StringBuilder formattedQuestion = new StringBuilder("**Question:** " + questionText + "\n\n");
        char optionLabel = 'A';
        for (String option : options) {
            formattedQuestion.append("**").append(optionLabel).append(")** ").append(option).append("\n");
            optionLabel++;
        }

        return formattedQuestion.toString();
    }

}
