package me.dolla.teamranked.events;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import me.dolla.teamranked.RankedTeam;
import me.dolla.teamranked.database.MongoManager;
import me.dolla.teamranked.player.Player;
import me.dolla.teamranked.player.PlayerManager;
import me.dolla.teamranked.util.PropertiesReader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BotReady extends ListenerAdapter {

    @SneakyThrows
    public void onReady(ReadyEvent event) {
        Guild server = event.getJDA().getGuildById(new PropertiesReader().getProperty("guildId"));
        server.loadMembers();
        MongoCollection<Document> collection = MongoManager.database.getCollection("Players");
        Role registerRole = server.getRoleById(new PropertiesReader().getProperty("registerRoleId"));
        List<Member> allRegisteredMembers = server.getMembersWithRoles(registerRole);
        for(Member member : allRegisteredMembers) {
            Document doc = collection.find(Filters.eq("playerId", member.getId())).first();
            Player player = new Player(member, doc.getString("playerName"));

            player.setElo(doc.getInteger("playerElo"));
            player.setPlayerName(doc.getString("playerName"));
            player.setWins(doc.getInteger("playerWins"));
            player.setLooses(doc.getInteger("playerLooses"));
            player.setRank(doc.getString("playerRank"));

            PlayerManager.updatePlayerProfile(member);
        }
    }
    }

