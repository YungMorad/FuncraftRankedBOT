package me.dolla.teamranked.commands.admin;

import lombok.SneakyThrows;
import me.dolla.teamranked.player.PlayerManager;
import me.dolla.teamranked.util.PropertiesReader;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class UnRegisterCommand extends ListenerAdapter {
    @SneakyThrows
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        //Unregister command
        if(event.getName().equals("unregister")) {
            OptionMapping nameOption = event.getOption("name");
            if(!event.getMember().getRoles().contains(event.getGuild().getRoleById(new PropertiesReader().getProperty("adminRoleId")))) {
                event.reply("Tu n'a pas la permission").queue();
                return;
            }
            if(nameOption == null) {
                event.reply("Mentionne l'utilisateur que tu veux unregister");
                return;
            }
            if(PlayerManager.hasPlayerProfile(event.getMember())) {
                event.reply("L'utilisateur n'est pas register !").queue();
                return;
            }
            PlayerManager.unregister(event.getOption("name").getAsMember());
            event.reply("Tu as unregister " + event.getOption("name").getAsMember().getAsMention() +  " avec succès !").queue();
        }
    }
}



