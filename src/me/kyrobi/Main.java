package me.kyrobi;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static me.kyrobi.Bridge.jda;

public class Main  extends JavaPlugin implements Listener {

    public ConsoleCommandSender console;
    public static String discordToken;
    public static String staffChannel;
    public static String status;

    @Override
    public void onEnable(){
        console = Bukkit.getServer().getConsoleSender();
        console.sendMessage("[StaffChatLink] Loaded!");

        //Fetches config stuff
        this.saveDefaultConfig();
        discordToken = this.getConfig().getString("botToken");
        staffChannel = this.getConfig().getString("staffChannel");
        status = this.getConfig().getString("watchingStatus");


        this.getCommand("sc").setExecutor((CommandExecutor)new StaffCommand(this));
        new Bridge(this);
    }


    public void onDisable(){
        console.sendMessage("[StaffChatLink] Unloaded!");
        jda.shutdownNow(); // Shuts down the JDA instance incase we need to reload plugin
    }
}
