package me.kyrobi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffCommand implements CommandExecutor {

    private Main plugin;

    public StaffCommand(final Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final Player player = (Player)commandSender;

        //Check if the player has permission
        if(!player.hasPermission("staff.chat")){
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }

        //Formats the message. Joins together message after the root command
        final String message = String.join(" ", (CharSequence[])strings);

        if(message.equals("")){
            player.sendMessage(ChatColor.RED + "You need send a message! /sc (message)");
            return true;
        }

        //Loop through the online player list and send message to everyone
        for(final Player staffs: Bukkit.getOnlinePlayers()){
            if(staffs.hasPermission("staff.chat")){

                String prefix = this.plugin.getConfig().getString("ingameFormatPrefix").replace("{player}", player.getName());
                staffs.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ": " + message); //Sends staffchat message ingame

            }
        }
        Bridge.sendToDiscord(message, player); //Sends staffchat message to Discord
        return false;
    }
}
