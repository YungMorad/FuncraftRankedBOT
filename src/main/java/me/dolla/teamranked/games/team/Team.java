package me.dolla.teamranked.games.team;

import lombok.Getter;
import lombok.Setter;
import me.dolla.teamranked.games.Game;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Team {

    public int team1 = 1;
    public int team2 = 2;

    public List<Member> team1members = new ArrayList<>();
    public List<Member> team2members = new ArrayList<>();

    public Member team1cap;
    public Member team2cap;

    public VoiceChannel team1voicechannel;
    public VoiceChannel team2voicechannel;

    public Guild guild;

    public Game game;

    public Team(List<Member> team1members, List<Member> team2members, Member team1cap, Member team2cap, Game game, Guild guild) {
        this.guild = guild;
        this.team1members = team1members;
        this.team2members = team2members;
        this.team1cap = team1cap;
        this.team2cap = team2cap;
        TeamManager.createVoiceChannel(guild, ("Team : " + team1cap.getUser().getName()), guild.getCategoryById("1089237836139606146"));
        this.team1voicechannel = TeamManager.getVoiceChannel(guild, "Team : " + team1cap.getUser().getName());
        TeamManager.createVoiceChannel(guild, ("Team : " + team2cap.getUser().getName()), guild.getCategoryById("1089237836139606146"));
        this.team2voicechannel = TeamManager.getVoiceChannel(guild, "Team : " + team2cap.getUser().getName());
        this.game = game;
    }
}
