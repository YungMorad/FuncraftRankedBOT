package me.dolla.teamranked.games;

import me.dolla.teamranked.games.team.Team;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    public static boolean isGameExiste(String id) {
        if(Game.getGame(id) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static Game getGame(String id) {
        if(isGameExiste(id) == true) {
            return Game.getGame(id);
        } else {
            return null;
        }
    }

    public static void startPick(Team team, Game game, Guild guild, TextChannel textChannel) {
        Member cap1 = team.getTeam1cap();
        Member cap2 = team.getTeam2cap();

        textChannel.sendMessage("Choix des picks pour le capitaine 1 :" + cap1.getEffectiveName() +" et le capitaine 2 :" + cap2.getEffectiveName()).queue();

    }

    public static ArrayList<Member> getMembersGame(Game game) {
        return game.getMembersGame();
    }

}
