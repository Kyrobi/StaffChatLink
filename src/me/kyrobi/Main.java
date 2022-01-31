package me.kyrobi;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main  extends JavaPlugin implements Listener {

    public ConsoleCommandSender console;
    public static String discordToken;

    @Override
    public void onEnable(){
        console = Bukkit.getServer().getConsoleSender();
        console.sendMessage("[StaffChatLink] Loaded!");

        //Fetches config stuff
        this.saveDefaultConfig();
        discordToken = this.getConfig().getString("botToken");

        new Bridge(this);
    }


    public void onDisable(){
        console.sendMessage("[StaffChatLink] Unloaded!");
    }
}
