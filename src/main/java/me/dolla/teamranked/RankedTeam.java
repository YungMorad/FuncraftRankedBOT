package me.dolla.teamranked;

import lombok.Getter;
import me.dolla.teamranked.commands.RegisterCommand;
import me.dolla.teamranked.commands.UpdateCommand;
import me.dolla.teamranked.commands.admin.EloCommand;
import me.dolla.teamranked.commands.admin.UnRegisterCommand;
import me.dolla.teamranked.database.MongoManager;
import me.dolla.teamranked.events.BotReady;
import me.dolla.teamranked.events.queues.QueueEloEvent;
import me.dolla.teamranked.games.GameManager;
import me.dolla.teamranked.player.PlayerManager;
import me.dolla.teamranked.util.PropertiesReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.IOException;

public class RankedTeam {


    private MongoManager mongoManager;

    public static void main(String[] args) throws InterruptedException, IOException {



        MongoManager.init();
        MongoManager.loadCollections();
        System.out.println("==================");
        System.out.println("[RankedTeam]");
        System.out.println("Ranked Team is ready !");
        System.out.println("MondoDB Connected");
        System.out.println("==================");


        JDA jda = JDABuilder.createDefault(new PropertiesReader().getProperty("token"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setActivity(Activity.listening("Divisions Rush"))
                .addEventListeners(new RegisterCommand(), new UnRegisterCommand(), new EloCommand(), new UpdateCommand(), new BotReady(), new QueueEloEvent())
                .build().awaitReady();

        Guild server = jda.getGuildById(new PropertiesReader().getProperty("guildId"));
        server.loadMembers();

        if(server != null){

            //Commandes

            server.upsertCommand("register", "Permet de se register dans la ligue")
                    .addOption(OptionType.STRING, "name", "Pseudo avec lequel tu veux te register !")
                    .queue();
            server.upsertCommand("unregister", "Permet de se unregister dans la ligue")
                    .addOption(OptionType.MENTIONABLE, "name", "Mentionne qui tu veux unregister !")
                    .queue();
            server.upsertCommand("setelo", "Modifier l'elo d'un joueur")
                    .addOption(OptionType.MENTIONABLE, "name", "Mentionne qui tu veux modifier l'elo")
                    .addOption(OptionType.INTEGER, "elo", "Nombre d'elo")
                    .queue();
            server.upsertCommand("addelo", "Ajouter de l'élo à un joueur")
                    .addOption(OptionType.MENTIONABLE, "name", "Mentionne à qui tu veux modifier l'elo")
                    .addOption(OptionType.INTEGER, "amount", "Nombre d'elo")
                    .queue();
            server.upsertCommand("delelo", "Enlever l'élo d'un joueur")
                    .addOption(OptionType.MENTIONABLE, "name", "Mentionne à qui tu veux modifier l'elo")
                    .addOption(OptionType.INTEGER, "amount", "Nombre d'elo")
                    .queue();
            server.upsertCommand("update", "Update ton profile")
                    .queue();
            server.upsertCommand("gameinfo", "Permet de donner les informations d'une game")
                    .addOption(OptionType.STRING, "gamesid", "L'ID de la game")
                    .queue();
            //Events


        }

    }

}
