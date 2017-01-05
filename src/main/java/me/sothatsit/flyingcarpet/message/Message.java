package me.sothatsit.flyingcarpet.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message
{
    private String key;
    private List<String> messages;
    private List<Argument> arguments;

    public Message(String key)
    {
        this.key = key;
        this.messages = new ArrayList();
        this.arguments = new ArrayList();

        this.messages.add("Unable to find the message \"" + key + "\"");
    }

    public Message(String key, List<String> messages) {
        this.key = key;
        this.messages = new ArrayList(messages);
        this.arguments = new ArrayList();
    }

    public Message(String key, String message) {
        this.key = key;
        this.messages = new ArrayList(Arrays.asList(new String[] { message }));
        this.arguments = new ArrayList();
    }

    public String getKey() {
        return this.key;
    }

    public List<String> getMessages() {
        List<String> coloured = new ArrayList();

        for (String message : this.messages) {
            for (Argument a : this.arguments) {
                message = a.replace(message);
            }

            coloured.add(ChatColor.translateAlternateColorCodes('&', message));
        }

        return coloured;
    }

    public String getMessage() {
        if (this.messages.size() > 0) {
            String message = (String)this.messages.get(0);

            for (Argument a : this.arguments) {
                message = a.replace(message);
            }

            return ChatColor.translateAlternateColorCodes('&', message);
        }

        return "";
    }

    public List<Argument> getArguments() {
        return this.arguments;
    }

    public Message argument(String key, String value) {
        this.arguments.add(new Argument(key, value));

        return this;
    }

    public Message arguments(Argument... arguments) {
        return arguments(Arrays.asList(arguments));
    }

    public Message arguments(List<Argument> arguments) {
        this.arguments.addAll(arguments);

        return this;
    }

    public Message message(String message) {
        this.messages.add(message);

        return this;
    }

    public void send(CommandSender sender) {
        for (String message : this.messages)
            if ((message != null) && ((this.messages.size() != 1) || (!message.isEmpty())))
            {

                for (Argument a : this.arguments) {
                    message = a.replace(message);
                }

                message = new Argument("%reciever%", sender.getName()).replace(message);

                message = ChatColor.translateAlternateColorCodes('&', message);

                sender.sendMessage(message);
            }
    }

    public void broadcast() {
        broadcastPlayers(Bukkit.getOnlinePlayers());
        send(Bukkit.getConsoleSender());
    }

    public void broadcastPlayers(Player... players) {
        broadcastPlayers(Arrays.asList(players));
    }

    public void broadcastPlayers(java.util.Collection<? extends Player> players) {
        List<CommandSender> recievers = new ArrayList();

        recievers.addAll(players);

        broadcastSenders(recievers);
    }

    public void broadcastSenders(List<CommandSender> recievers) {
        for (Iterator localIterator1 = this.messages.iterator(); localIterator1.hasNext();) {
            String message = (String)localIterator1.next();
            if ((message != null) && ((this.messages.size() != 1) || (!message.isEmpty())))
            {


                for (Argument a : this.arguments) {
                    message = a.replace(message);
                }

                message = ChatColor.translateAlternateColorCodes('&', message);

                for (CommandSender sender : recievers) {
                    message = new Argument("%reciever%", sender.getName()).replace(message);

                    sender.sendMessage(message);
                }
            }
        }
        String message;
    }

    public void broadcastPermission(String permission) { for (String message : this.messages) {
        if ((message != null) && ((this.messages.size() != 1) || (!message.isEmpty())))
        {


            for (Argument a : this.arguments) {
                message = a.replace(message);
            }

            message = ChatColor.translateAlternateColorCodes('&', message);

            Bukkit.broadcast(message, permission);
        }
    }
    }
}


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/message/Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */