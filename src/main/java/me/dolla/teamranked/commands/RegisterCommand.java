package me.dolla.teamranked.commands;

import lombok.SneakyThrows;
import me.dolla.teamranked.apifuncraft.FuncraftApi;
import me.dolla.teamranked.player.Player;
import me.dolla.teamranked.player.PlayerManager;
import me.dolla.teamranked.util.PropertiesReader;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.io.IOException;

public class RegisterCommand extends ListenerAdapter {

    @SneakyThrows
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        //Register command
        if(event.getName().equals("register")) {
            OptionMapping nameOption = event.getOption("name");
            if(nameOption == null) {
                event.reply("Pleasy type a name /register <name>").queue();
                return;
            }
            if(PlayerManager.hasPlayerProfile(event.getMember())) {
                event.reply("Tu es déja register !").queue();
                return;
            }
            if(!FuncraftApi.hasFuncraftProfile(nameOption.getAsString())) {
                event.reply("Le profile " + nameOption.getAsString() +" n'existe pas sur FunCraft, veuillez rentrer un pseudo valide sur FunCraft").queue();
                return;
            }
            event.reply("Register avec succès !").queue();
            PlayerManager.register(event.getMember(), event.getOption("name").getAsString());
        }

        if(event.getName().equals("hasteam")) {
            if(PlayerManager.hasPlayerProfile(event.getMember())) {
                event.reply("Tu a un player profile").queue();
            } else {
                event.reply("Tu a aucune player profile").queue();
            }
        }
    }
}
