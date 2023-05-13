package me.dolla.teamranked.games.team;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class TeamManager {


    public static void createVoiceChannel(Guild guild, String channelName, Category categoryById) {
        guild.createVoiceChannel(channelName).queue(voiceChannel -> {
            VoiceChannel myVoiceChannel = voiceChannel;
        });
    }

    public static VoiceChannel getVoiceChannel(Guild guild, String name) {
        for(VoiceChannel voiceChannel : guild.getVoiceChannels()) {
            if(voiceChannel.getName().contains(name)) {
                return voiceChannel;
            }
        }
        return null;
    }


}
