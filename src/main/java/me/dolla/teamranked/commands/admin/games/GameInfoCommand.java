package me.dolla.teamranked.commands.admin.games;

import me.dolla.teamranked.games.Game;
import me.dolla.teamranked.games.GameManager;
import me.dolla.teamranked.util.PropertiesReader;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class GameInfoCommand extends ListenerAdapter {

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("gameinfo")) {
            OptionMapping gamesId = event.getOption("gamesid");
            if (!event.getMember().getRoles().contains(event.getGuild().getRoleById(new PropertiesReader().getProperty("adminRoleId")))) {
                event.reply("Commande pour les admins seulement !").queue();
                return;
            }
            if(gamesId == null) {
                event.reply("Veuillez indiquez l'ID de la game").queue();
                return;
            }
            if(!GameManager.isGameExiste(gamesId.getAsString())) {
                event.reply("Aucune game avec l'ID : " + gamesId.getAsString()).queue();
                        return;
            } else {
                Game game = GameManager.getGame(gamesId.getAsString());
                event.reply("Info de la game !").queue();
                System.out.println(GameManager.getMembersGame(game));

            }
        }
    }
}
