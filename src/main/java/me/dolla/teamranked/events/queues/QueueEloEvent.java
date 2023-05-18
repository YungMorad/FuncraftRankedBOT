package me.dolla.teamranked.events.queues;

import lombok.SneakyThrows;
import me.dolla.teamranked.games.Game;
import me.dolla.teamranked.games.GameManager;
import me.dolla.teamranked.queue.QueueState;
import me.dolla.teamranked.util.PropertiesReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import java.util.List;

public class QueueEloEvent extends ListenerAdapter {

    @SneakyThrows
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        JDA jda = event.getJDA();
        Category gamesCat = event.getGuild().getCategoryById("1089237836139606146");
        String userId = event.getEntity().getId();
        String guildId = event.getGuild().getId();
        String channelId = event.getChannelLeft() != null ? event.getChannelLeft().getId() : event.getChannelJoined().getId();
        VoiceChannel rankedWaitingChannel = event.getGuild().getVoiceChannelById(new PropertiesReader().getProperty("queueRankedId"));

        if(event.getEntity().getVoiceState() == null) {
            System.out.println("On return");
            return;
        }

        //Si qqun leave, on delete la queue
        if(!(event.getChannelLeft() == null) && event.getChannelLeft().getName().contains("Games")) {
            VoiceChannel channelLeft = (VoiceChannel) event.getChannelLeft();
            Member leaveMember = event.getMember();
            channelLeft.delete().queue();
            Game game = GameManager.getGame(leaveMember);
            //Close la game et supprimer les channels
            for(Member member : channelLeft.getMembers()) {
                event.getGuild().moveVoiceMember(member, rankedWaitingChannel).queue();
            }
            GameManager.closeGame(game);
            QueueState.setState(QueueState.WAITING);
        }

        if(rankedWaitingChannel.getMembers().size() < 2) {
            return;
        }


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(rankedWaitingChannel.getMembers().size() == 2) {
                    QueueState.setState(QueueState.STARTING);
                    final VoiceChannel gameChannel = null;
                    List<Member> members = rankedWaitingChannel.getMembers();
                    ArrayList<Member> gameMembers = new ArrayList<>(members);
                    //Creation de la game
                    Game game = GameManager.createGame(false, gameMembers);
                    String uniqueId = game.getUUID();

                    event.getGuild().createVoiceChannel("Games " + uniqueId, gamesCat).queue(channel -> {

                        for(Member member : rankedWaitingChannel.getMembers()) {
                            event.getGuild().moveVoiceMember(member, channel).queue();
                        }

                        int size = gameMembers.size();


                        List<Member> team1m = gameMembers.subList(0, size/2);
                        ArrayList<Member> team1Members = new ArrayList<>(team1m);
                        System.out.println(team1m);
                        List<Member> team2m = gameMembers.subList(size/2, size);
                        ArrayList<Member> team2Members = new ArrayList<>(team2m);
                        System.out.println(team2m);

                        game.setTeam1members(team1Members);
                        game.setTeam1members(team2Members);
                        GameManager.updateGameInformations(game, event.getGuild());

                        Random rand = new Random();
                        int rdm = rand.nextInt(2);

                        Member cap1 = team1m.get(rdm);
                        Member cap2 = team2m.get(rdm);
                        //Création des teams avec les capitaines, lancement des picks
                    });
                } else {
                    return;
                }
            }
        }, 5000); // 5000 ms = 5 secondes
    }
}
