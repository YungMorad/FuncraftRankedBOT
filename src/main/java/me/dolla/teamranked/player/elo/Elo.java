package me.dolla.teamranked.player.elo;

import me.dolla.teamranked.player.Player;
import me.dolla.teamranked.player.PlayerManager;
import net.dv8tion.jda.api.entities.Member;

public class Elo {

    public static void setElo(int elo, Member member) {
        PlayerManager.getPlayerProfileMember(member).setElo(elo);
    }

    public static void addElo(int amount, Player player) {
        player.setElo(player.getElo() + amount);
    }

    public static void delElo(int amount, Player player) {
        player.setElo(player.getElo() - amount);
    }
}
