package me.dolla.teamranked.player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import me.dolla.teamranked.RankedTeam;
import me.dolla.teamranked.database.MongoManager;
import me.dolla.teamranked.util.PropertiesReader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bson.Document;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlayerManager {

    public static boolean hasPlayerProfile(Member member) {
        MongoCollection<Document> collection = MongoManager.database.getCollection("Players");
        if(collection.find(Filters.eq("playerId", member.getId())).first() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void unregister (Member member) throws InterruptedException {
            Player player = Player.getProfile(member);
            player.removeOfProfile(member);
            MongoCollection<Document> collection = MongoManager.database.getCollection("Players");
            TimeUnit.SECONDS.sleep(2);
            member.getGuild().modifyNickname(member, member.getUser().getName()).queue();
            member.getGuild().removeRoleFromMember(member, Objects.requireNonNull(member.getGuild().getRoleById(new PropertiesReader().getProperty("registerRoleId")))).queue();

        collection.deleteOne(Filters.eq("playerId", member.getId()));
        }

    @SneakyThrows
    public static void updatePlayerProfile(Member member) {

        MongoCollection<Document> collection = MongoManager.database.getCollection("Players");
        //Document doc = collection.find(Filters.eq("playerId", member.getId())).first();
        //System.out.println(doc.toJson());
       //Update en trouvant doc (si redemm bot)
        //if(player == null) {
           // if(doc != null) {
            //    System.out.println("Doc trouvé");
           // } else {
           //     System.out.println("Aucun Doc trouvé");
          //  }
       // }
        if(collection.find(Filters.eq("playerId", member.getId())).first() != null) {
            Document doc = collection.find(Filters.eq("playerId", member.getId())).first();
            Player player = new Player(member, doc.getString("playerName"));

            player.setElo(doc.getInteger("playerElo"));
            player.setPlayerName(doc.getString("playerName"));
            player.setWins(doc.getInteger("playerWins"));
            player.setLooses(doc.getInteger("playerLooses"));
            player.setRank(doc.getString("playerRank"));
            member.getGuild().modifyNickname(member, "[" + player.getElo() + "] " + player.getPlayerName()).queue();
            if(!member.getRoles().contains(member.getGuild().getRoleById("1074696435716603945"))) {
                member.getGuild().addRoleToMember(member, Objects.requireNonNull(member.getGuild().getRoleById("1074696435716603945"))).queue();
            }
        }
    }

    public static Player getPlayerProfileMember(Member member) {
        return Player.getProfile(member);
    }

    public static boolean isPlayerProfileIs(Player player, Member member) {
        if(hasPlayerProfile(member)) {
            if(getPlayerProfileMember(member) == player) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void updateAllPlayers(Guild guild) {


    }

    public static void register(Member member, String name) {
        if(hasPlayerProfile(member)) {
            return;
        }
        Player player = new Player(member, name);

        member.getGuild().modifyNickname(member, "[" + player.getElo() + "] " + name).queue();
        member.getGuild().addRoleToMember(member, member.getGuild().getRoleById(new PropertiesReader().getProperty("registerRoleId"))).queue();

        Document document = new Document("playerName", name);
        document.append("memberName", member.getEffectiveName());
        document.append("playerId", member.getId());
        document.append("playerElo", player.getElo());
        document.append("playerWins", player.getWins());
        document.append("playerLooses", player.getLooses());
        document.append("playerRank", player.getRank());
        MongoManager.database.getCollection("Players").insertOne(document);
    }

}
