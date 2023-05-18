package me.dolla.teamranked.games;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.UUID;

@Getter @Setter
public class Game {

    public UUID uuid;
    public boolean ranked;
    public ArrayList<Member> members = new ArrayList<>();
    public Member cap1;
    public Member cap2;

    public ArrayList<Member> team1members;
    public ArrayList<Member> team2members;


    public Game(boolean ranked, ArrayList<Member> members) {
        this.uuid = UUID.randomUUID();
        this.ranked = ranked;
        this.members = members;
        this.cap1 = null;
        this.cap2 = null;
        team1members = new ArrayList<>();
        team2members = new ArrayList<>();
    }

    public String getUUID() {
        return uuid.toString();
    }


}
