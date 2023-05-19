package me.dolla.teamranked.events.queues;

import lombok.SneakyThrows;
import me.dolla.teamranked.games.Game;
import me.dolla.teamranked.games.GameManager;
import me.dolla.teamranked.queue.QueueState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class PickEvent extends ListenerAdapter {

    @SneakyThrows
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (event.getName().equals("pick")) {
            OptionMapping nameOption = event.getOption("name");
            if (nameOption == null) {
                event.reply("/pick <name>");
                return;
            }
            if(GameManager.getGame(member) == null) {
                event.reply("Tu ne peux pas utiliser cette commande maintenant !").queue();
                return;
            }
            if(!QueueState.isState(QueueState.PICKING)) {
                event.reply("Tu ne peux pas utiliser cette commande maintenant !").queue();
                return;
            }
            if(!GameManager.isInGame(member)) {
                event.reply("Tu ne peux pas utiliser cette commande car tu n'est pas en game").queue();
                return;
            }
            Game game = GameManager.getGame(member);
            if(game.getCap1() != member || game.getCap2() != member) {
                event.reply("Tu neux pas pick car tu n'est pas le capitaine").queue();
                return;
            }
            Member cap1 = null;
            Member cap2 = null;
            if(game.getCap1() == member) {
                cap1 = member;
                cap2 = game.getCap2();
            } else {
                cap2 = member;
                cap1 = game.getCap1();
            }
            System.out.println(cap1.getEffectiveName());
            System.out.println(cap2.getEffectiveName());


        }
    }

}
