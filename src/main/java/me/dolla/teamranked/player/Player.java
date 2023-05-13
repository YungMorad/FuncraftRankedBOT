package me.dolla.teamranked.player;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Getter @Setter
public class Player {


    public String playerName;
    public Member playerMember;
    public String playerId;
    public int elo;
    public int wins;
    public int looses;
    public String rank;

    public static List<Player> allMembersProfile = new ArrayList<Player>();

    public static HashMap<Member, Player> memberPlayerHashMap = new HashMap<Member, Player>();

    public Player(Member playerMember, String playerName) {
        this.playerMember = playerMember;
        this.playerName = playerName;
        this.playerId = playerMember.getId();
        memberPlayerHashMap.put(playerMember, this);
        allMembersProfile.add(this);
        this.elo = 0;
        this.wins = 0;
        this.looses = 0;
        this.rank = "Iron";
    }

    public static boolean hasPlayerProfile(Member user) {
        return memberPlayerHashMap.containsKey(user);
    }

    public void addOnProfile(Member user) {
        memberPlayerHashMap.put(user, this);
    }

    public static void removeOfProfile(Member user) {
        if(!hasPlayerProfile(user)) {
            return;
        }
        memberPlayerHashMap.remove(user);
    }

    public static Player getProfile(Member user) {
        if(memberPlayerHashMap.containsKey(user)) {
            return memberPlayerHashMap.get(user);
        } else {
            return null;
        }
    }
}
