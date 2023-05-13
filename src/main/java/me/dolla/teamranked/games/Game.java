package me.dolla.teamranked.games;

import lombok.Getter;
import lombok.Setter;
import me.dolla.teamranked.player.Player;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Game {

    public String id;

    public static ArrayList<Game> allGames = new ArrayList<Game>();
    public  ArrayList<Member> membersGame = new ArrayList<Member>();


    public VoiceChannel gamesChannels;

    public static HashMap<String, Game> idGameHashMap = new HashMap<String, Game>();
    public static HashMap<Member, Game> memberGameHashMap = new HashMap<Member, Game>();


    public Game(String id, VoiceChannel gamesChannels) {
        this.id = id;
        this.gamesChannels = gamesChannels;
        idGameHashMap.put(id, this);
        allGames.add(this);
    }

    public static Game getGame(String id) {
        if(idGameHashMap.containsKey(id)) {
            return idGameHashMap.get(id);
        } else {
            return null;
        }
    }

    public void addOnGame(Member member, Game game) {
        memberGameHashMap.put(member, game);
        this.membersGame.add(member);
    }

    public void deleteOnGame(Member member, Game game) {
        memberGameHashMap.remove(member, game);
        this.membersGame.remove(member);
    }

    public void deleteGameList(Game game) {
        allGames.remove(game);
    }


}
