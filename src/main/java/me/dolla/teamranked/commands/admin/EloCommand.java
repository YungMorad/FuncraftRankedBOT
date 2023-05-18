package me.dolla.teamranked.commands.admin;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.SneakyThrows;
import me.dolla.teamranked.RankedTeam;
import me.dolla.teamranked.apifuncraft.FuncraftApi;
import me.dolla.teamranked.database.MongoManager;
import me.dolla.teamranked.player.Player;
import me.dolla.teamranked.player.PlayerManager;
import me.dolla.teamranked.player.elo.Elo;
import me.dolla.teamranked.util.PropertiesReader;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bson.Document;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class EloCommand extends ListenerAdapter {

    @SneakyThrows
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("setelo")) {
            OptionMapping nameOption = event.getOption("name");
            OptionMapping eloOption = event.getOption("elo");
            if (!event.getMember().getRoles().contains(event.getGuild().getRoleById(new PropertiesReader().getProperty("adminRoleId")))) {
                event.reply("Tu n'a pas la permission").queue();
                return;
            } else {
                if (eloOption == null || nameOption == null) {
                    event.reply("/setelo <name> <elo>").queue();
                } else {
                    if (!PlayerManager.hasPlayerProfile(nameOption.getAsMember())) {
                        event.reply("Le joueur " + Objects.requireNonNull(nameOption.getAsMember()).getAsMention() + "n'est pas register !");
                        return;
                    } else {
                        Player player = PlayerManager.getPlayerProfileMember(Objects.requireNonNull(event.getOption("name")).getAsMember());
                        Elo.setElo(Objects.requireNonNull(event.getOption("elo")).getAsInt(), Objects.requireNonNull(event.getOption("name")).getAsMember());
                        event.reply("Le joueur " + Objects.requireNonNull(nameOption.getAsMember()).getAsMention() + " possède désormais: " + player.getElo() + " ELO").queue();
                        event.getMember().getGuild().modifyNickname(Objects.requireNonNull(Objects.requireNonNull(event.getOption("name")).getAsMember()), "[" + player.getElo() + "] " + player.getPlayerName()).queue();
                        player.setElo(eloOption.getAsInt());
                        MongoCollection<Document> collection = MongoManager.database.getCollection("Players");
                        //Document doc = collection.find(Filters.eq("playerId", nameOption.getAsMember().getId())).first();
                        //doc.put("playerElo", player.getElo());
                        collection.updateOne(Filters.eq("playerId", nameOption.getAsMember().getId()), Updates.set("playerElo", player.getElo()));
                    }
                }
            }
        }
        if (event.getName().equals("addelo")) {
            //Commande addelo
        }
        if (event.getName().equals("delelo")) {
            //Commande delelo
        }
        if (event.getName().equals("hasfc")) {
            OptionMapping nameOption = event.getOption("name");
            if (nameOption == null) {
                event.reply("Please type a name").queue();
                return;
            } else {
                try {
                    if (FuncraftApi.hasFuncraftProfile(nameOption.getAsString())) {
                        event.reply(nameOption.getAsString() + " a un profile sur Funcraft").queue();
                    } else {
                        event.reply(nameOption.getAsString() + " n'a pas de profile sur Funcraft").queue();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (event.getName().equals("hasban")) {
            OptionMapping nameOption = event.getOption("name");
            if (nameOption == null) {
                event.reply("Please type a name").queue();
                return;

            } else {
                if(!FuncraftApi.hasFuncraftProfile(nameOption.getAsString())) {
                    event.reply("Le joueur " + nameOption.getAsString()+ " est pas enregistré sur FunCraft").queue();
                } else {
                    if(FuncraftApi.hasBanFuncraft(nameOption.getAsString())) {
                        event.reply("Le joueur " + nameOption.getAsString() + " est banni de FunCraft").queue();
                    } else {
                        event.reply("Le joueur " + nameOption.getAsString() + " n'est pas banni de funcraft").queue();
                    }
                }
            }
        }
        if(event.getName().equals("listregister")) {
            event.getGuild().loadMembers();
            List<Member> allMembers = event.getGuild().getMembers();
            for(Member member : allMembers) {
                event.reply(member.getEffectiveName()).queue();
                if(member.getRoles().contains(event.getGuild().getRoleById(new PropertiesReader().getProperty("registerRoleId")))) {
                    event.reply(member.getEffectiveName()).queue();
                }
            }
        }
    }
}
