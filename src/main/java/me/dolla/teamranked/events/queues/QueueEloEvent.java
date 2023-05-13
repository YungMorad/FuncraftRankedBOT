package me.dolla.teamranked.events.queues;

import lombok.SneakyThrows;
import me.dolla.teamranked.games.Game;
import me.dolla.teamranked.games.GameManager;
import me.dolla.teamranked.games.team.Team;
import me.dolla.teamranked.queue.QueueState;
import me.dolla.teamranked.util.PropertiesReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.xml.soap.Text;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
            System.out.println("On return;");
            return;
        }

        if(!(event.getChannelLeft() == null) && event.getChannelLeft().getName().contains("Games")) {
            VoiceChannel channelLeft = (VoiceChannel) event.getChannelLeft();
            Member leaveMember = event.getMember();
            String id = channelLeft.getName().substring(6, 11);
            List<TextChannel> textChannels = gamesCat.getTextChannels();
            channelLeft.delete().queue();
            for(TextChannel textChannel : textChannels) {
                if(textChannel.getName().contains(id)) {
                    textChannel.delete().queue();
                    break;
                }
            }
            Game game = GameManager.getGame(id);
            //Close la game et supprimer les channels
            for(Member member : channelLeft.getMembers()) {
                event.getGuild().moveVoiceMember(member, rankedWaitingChannel).queue();
                game.deleteOnGame(member, game);
            }
            game.deleteOnGame(leaveMember, game);
            game.deleteGameList(game);
            game = null;
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
                    Random random = new Random();
                    List<Member> members = rankedWaitingChannel.getMembers();
                    int randomNumber = random.nextInt(90000) + 10000; // génère un nombre aléatoire entre 10000 et 99999
                    String uniqueId = Integer.toString(randomNumber); // convertit le nombre aléatoire en chaîne de caractères
                    event.getGuild().createVoiceChannel("Games " + uniqueId, gamesCat).queue(channel -> {
                        Game game = new Game(uniqueId, channel);gamesCat.createTextChannel("games-" + uniqueId).queue(textChannel -> {
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setTitle("Games : " + uniqueId, null);
                            eb.setColor(Color.red);
                            eb.setDescription("**Le dodge ce solde par 24h de ban !**");
                            eb.addField("Joueurs", "1 - " + members.get(0).getEffectiveName() +"\n2 - " + members.get(1).getEffectiveName() , false);
                            textChannel.sendMessageEmbeds(eb.build()).queue();
                        });



                        List<Member> allMembersGames = new ArrayList<>();
                        for(Member member : rankedWaitingChannel.getMembers()) {
                            game.addOnGame(member, game);
                            event.getGuild().moveVoiceMember(member, channel).queue();
                            allMembersGames.add(member);
                        }

                        System.out.println(allMembersGames);
                        int size = allMembersGames.size();


                        List<Member> team1m = allMembersGames.subList(0, size/2);
                        System.out.println(team1m);
                        List<Member> team2m = allMembersGames.subList(size/2, size);
                        System.out.println(team2m);

                        Random rand = new Random();
                        int rdm = rand.nextInt(2);

                        Member cap1 = team1m.get(rdm);
                        Member cap2 = team2m.get(rdm);
                        Team team = new Team(team1m, team2m, cap1, cap2, game, event.getGuild());
                        for(TextChannel textChannel : gamesCat.getTextChannels()) {
                            if(textChannel.getName().contains(uniqueId)) {
                                GameManager.startPick(team, game, event.getGuild(), textChannel);
                                break;
                            }
                        }
                    });
                } else {
                    return;
                }
            }
        }, 5000); // 5000 ms = 5 secondes
    }
}
