package me.dolla.teamranked.commands;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import me.dolla.teamranked.RankedTeam;
import me.dolla.teamranked.database.MongoManager;
import me.dolla.teamranked.player.Player;
import me.dolla.teamranked.player.PlayerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

public class UpdateCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("update")) {
            Member member = event.getMember();
            MongoCollection<Document> collection = MongoManager.database.getCollection("Players");
            if(collection.find(Filters.eq("playerId", member.getId())).first() == null) {
                event.reply("Tu es pas register, utilise la commande /register pour te register").queue();
                return;
            } else {
                PlayerManager.updatePlayerProfile(member);
                event.reply("Update avec succès !").queue();
            }
        }
    }
}
