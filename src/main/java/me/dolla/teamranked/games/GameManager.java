package me.dolla.teamranked.games;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import me.dolla.teamranked.RankedTeam;
import me.dolla.teamranked.database.MongoManager;
import me.dolla.teamranked.player.Player;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameManager {

    public static HashMap<Member, Game> memberGameHashMap = new HashMap<Member, Game>();
    public static HashMap<UUID, Game> uuidGameHashMap = new HashMap<UUID, Game>();


    public static Game createGame(boolean ranked, ArrayList<Member> members) {
        Game game = new Game(ranked, members);
        uuidGameHashMap.put(game.uuid, game);
        ArrayList<Member> gameMembers = game.members;
        for(Member member : gameMembers) {
            if(memberGameHashMap.containsKey(member)) {
                break;
            }
            memberGameHashMap.put(member, game);
        }

        ArrayList<Member> gameMembersTeam1 = game.team1members;
        ArrayList<Member> gameMembersTeam2 = game.team2members;

        //Creation des données MongoDB
        Document document = new Document("gameUUID", game.getUUID());

        Document membersDoc = new Document();
        int index = 0;
        for(Member member : gameMembers) {
            index ++;
            membersDoc.put(String.valueOf(index), member.getEffectiveName());
        }
        document.put("members", membersDoc);

        document.put("ranked", game.ranked);
        document.put("cap1", game.cap1);
        document.put("cap2", game.cap2);

        Document team1MembersDoc = new Document();
        int index1 = 0;
        for(Member member : gameMembersTeam1) {
            index1 ++;
            team1MembersDoc.put(String.valueOf(index1), member.getEffectiveName());
        }
        document.put("team1members", team1MembersDoc);

        Document team2MembersDoc = new Document();
        int index2 = 0;
        for(Member member : gameMembersTeam2) {
            index2 ++;
            team2MembersDoc.put(String.valueOf(index2), member.getEffectiveName());
        }
        document.put("team2members", team2MembersDoc);


        MongoManager.database.getCollection("Games").insertOne(document);
        //Profile.getCollection().replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
        return game;
    }

    public static void updateGameInformations(Game game, Guild guild) {
        MongoCollection<Document> collection = MongoManager.database.getCollection("Games");
        if(collection.find(Filters.eq("gameUUID", game.getUUID())).first() != null) {
            Document doc = collection.find(Filters.eq("gameUUID", game.getUUID())).first();

            doc.put("cap1", game.getCap1().getEffectiveName());
            doc.put("cap2", game.getCap2().getEffectiveName());


           // ArrayList<Member> team1Members = new ArrayList<>();
            //for(String str : doc.getList("team1members", String.class)) {
             //   for(Member member : guild.getMembersByEffectiveName(str, true)) {
             //       team1Members.add(member);
             //   }
           // }
           // game.setTeam1members(team1Members);


           // ArrayList<Member> team2Members = new ArrayList<>();
            //for(String str : doc.getList("team2members", String.class)) {
             //   for(Member member : guild.getMembersByEffectiveName(str, true)) {
               //     team2Members.add(member);
              //  }
           // }
           // game.setTeam2members(team2Members);
            collection.replaceOne(Filters.eq("gameUUID", game.getUUID()), doc, new ReplaceOptions().upsert(true));
        }
    }

    public static Game getGame(UUID uuid) {
        return uuidGameHashMap.get(uuid);
    }

    public static Game getGame(Member member) {
        if(memberGameHashMap.containsKey(member)) {
            return memberGameHashMap.get(member);
        } else {
            return null;
        }
    }

    public static void closeGame(Game game) {
        if(game == null) {
            return;
        }
        MongoCollection<Document> collection = MongoManager.database.getCollection("Games");
        if (game != null && game.getMembers() != null) {
            for (Member member : game.getMembers()) {
                if (memberGameHashMap != null && member != null && memberGameHashMap.containsKey(member)) {
                    memberGameHashMap.remove(member);
                }
            }
        }
        if(game != null) {
            if (uuidGameHashMap != null && game.getUUIDObj() != null) {
                uuidGameHashMap.remove(game.getUUIDObj());
            }
        }
        collection.deleteOne(Filters.eq("gameUUID", game.getUUID()));
    }

    public static boolean isInGame(Member member) {
        return memberGameHashMap.containsKey(member);
    }


}
