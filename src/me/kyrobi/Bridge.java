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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Bridge extends ListenerAdapter implements Listener {

    public static JDA jda;
    private Main plugin;

    //Constructor to start the bot
    public Bridge(Main plugin){
        startBot();
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        jda.addEventListener(this);
    }

    //Start bot method
    private void startBot(){
        try{
            jda = JDABuilder.createDefault(Main.discordToken).enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES).build();
            jda.getPresence().setActivity(Activity.watching("Stalking staff chat"));
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
        }
        catch (Exception e){
            System.out.println("Something went wrong enabling discord hook");
        }
    }

    /*
    Event listener for Discord messages. This called the sendToMinecraft method which
    send sit into the actual game
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if(e.getAuthor().isBot() || e.isWebhookMessage()){
            return;
        }

        if(e.getChannel().getIdLong() == Long.parseLong(Main.staffChannel)){
            sendToMinecraft(e.getMessage().getContentRaw(), e.getAuthor());
        }
    }

    /*
    Sends a message to the relevant discord channel when method is called
     */
    public static void sendToDiscord(String message, Player player){
        String mcUsername = player.getName();

        //Since we don't have event, we need to manually get the text channel from the jda
        //TextChannel textChannel = jda.getTextChannelsByName(Main.staffChannel, true).get(0);
        TextChannel textChannel = jda.getTextChannelById(Main.staffChannel);
        textChannel.sendMessage(mcUsername + ": " + message).queue();
    }

    /*
    Sends a message to ingame. This method is called by the onGuildMessageReceived is tripped
     */
    public void sendToMinecraft(String message, User user){

        //Loop through the online player list and send message to everyone
        for(final Player staffs: Bukkit.getOnlinePlayers()){
            if(staffs.hasPermission("staff.chat")){

                String prefix = this.plugin.getConfig().getString("ingameFromDiscordFormat").replace("{player}", user.getName());
                staffs.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ": " + message); //Sends staffchat message ingame
            }
        }
    }
}
