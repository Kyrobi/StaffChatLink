package me.kyrobi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Bridge extends ListenerAdapter implements Listener {

    public static JDA jda;

    //Constructor to start the bot
    public Bridge(Main plugin){
        startBot();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        jda.addEventListener(this);
    }

    //Start bot method
    private void startBot(){
        try{
            jda = JDABuilder.createDefault(Main.discordToken).enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES).build();
            jda.getPresence().setActivity(Activity.watching("Stalking server chat again"));
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
        }
        catch (Exception e){
            System.out.println("Something went wrong enabling discord hook");
        }
    }

//    @EventHandler
//    public void onPlayerChat(AsyncPlayerChatEvent e){
//        System.out.println("BRUH");
//        String message = e.getMessage();
//
//        sendToDiscord(e.getMessage(),e.getPlayer());
//    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if(e.getAuthor().isBot() || e.isWebhookMessage()){
            return;
        }
        sendToMinecraft(e.getMessage().getContentRaw(), e.getAuthor());
    }

    public static void sendToDiscord(String message, Player player){
        String mcUsername = player.getName();
        TextChannel textChannel = jda.getTextChannelsByName("staff-chat", true).get(0);
        textChannel.sendMessage(mcUsername + ": " + message).queue();
    }

    public static void sendToMinecraft(String message, User user){
        //Loop through the online player list and send message to everyone
        for(final Player staffs: Bukkit.getOnlinePlayers()){
            if(staffs.hasPermission("staff.chat")){

                staffs.sendMessage("[StaffChat]-> " + user.getName() + ": " + message); //Sends staffchat message ingame
            }
        }
    }
}
