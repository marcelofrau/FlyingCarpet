package me.sothatsit.flyingcarpet;

import java.util.Set;

import me.sothatsit.flyingcarpet.message.Message;
import me.sothatsit.flyingcarpet.message.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyingCarpetCommand
        implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((args.length != 0) && (args[0].equalsIgnoreCase("worldguard"))) {
            if (!sender.hasPermission("flyingcarpet.worldguard")) {
                Messages.get("error.no-permissions.worldguard").send(sender);
                return true;
            }

            if (!FlyingCarpet.getInstance().isWorldGuardHooked()) {
                Messages.get("error.worldguard-not-hooked").send(sender);
                return true;
            }

            if (args.length == 1) {
                Messages.get("error.invalid-arguments").argument("%valid%", "/mc worldguard <add:remove:list>").send(sender);
                return true;
            }

            if (args[1].equalsIgnoreCase("add")) {
                if (args.length != 3) {
                    Messages.get("error.invalid-arguments").argument("%valid%", "/mc worldguard add <region>").send(sender);
                    return true;
                }

                FlyingCarpet.getInstance().getWorldGuardHook().addBlacklistedRegion(args[2]);
                Messages.get("message.wg.add").argument("%region%", args[2]).send(sender);
                return true;
            }

            if (args[1].equalsIgnoreCase("remove")) {
                if (args.length != 3) {
                    Messages.get("error.invalid-arguments").argument("%valid%", "/mc worldguard remove <region>").send(sender);
                    return true;
                }

                FlyingCarpet.getInstance().getWorldGuardHook().removeBlacklistedRegion(args[2]);
                Messages.get("message.wg.remove").argument("%region%", args[2]).send(sender);
                return true;
            }

            if (args[1].equalsIgnoreCase("list")) {
                if (args.length != 2) {
                    Messages.get("error.invalid-arguments").argument("%valid%", "/mc worldguard list").send(sender);
                    return true;
                }

                Set<String> regions = FlyingCarpet.getInstance().getWorldGuardHook().getBlacklistedRegions();

                if (regions.size() == 0) {
                    Messages.get("message.wg.list-none").send(sender);
                    return true;
                }

                Messages.get("message.wg.list-header").send(sender);

                for (String region : regions) {
                    Messages.get("message.wg.list-line").argument("%region%", region).send(sender);
                }

                return true;
            }

            Messages.get("error.invalid-arguments").argument("%valid%", "/mc worldguard <add:remove:list>").send(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            Messages.get("error.must-be-player").send(sender);
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("flyingcarpet.use")) {
            Messages.get("error.no-permissions.carpet").send(sender);
            return true;
        }

        UPlayer up = FlyingCarpet.getInstance().getUPlayer(p);

        if (args.length == 0) {
            if ((!up.isEnabled()) && (!FlyingCarpet.getInstance().isCarpetAllowed(p.getLocation()))) {
                Messages.get("error.region-blocked").send(sender);
                return true;
            }

            up.setEnabled(!up.isEnabled());

            if (up.isEnabled()) {
                Messages.get("message.carpet-on").send(sender);
            } else {
                Messages.get("message.carpet-off").send(sender);
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("on")) {
            if (args.length != 1) {
                Messages.get("error.invalid-arguments").argument("%valid%", "/mc on").send(sender);
                return true;
            }

            if (!FlyingCarpet.getInstance().isCarpetAllowed(p.getLocation())) {
                Messages.get("error.region-blocked").send(sender);
                return true;
            }

            up.setEnabled(true);

            Messages.get("message.carpet-on").send(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("off")) {
            if (args.length != 1) {
                Messages.get("error.invalid-arguments").argument("%valid%", "/mc off").send(sender);
                return true;
            }

            up.setEnabled(false);
            up.removeCarpet();

            Messages.get("message.carpet-off").send(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.hasPermission("flyingcarpet.reload")) {
                Messages.get("error.no-permissions.reload").send(sender);
                return false;
            }

            if (args.length != 1) {
                Messages.get("error.invalid-arguments").argument("%valid%", "/mc off").send(sender);
                return true;
            }

            FlyingCarpet.getInstance().reloadConfiguration();

            Messages.get("message.reload").send(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("tools")) {
            if (!p.hasPermission("flyingcarpet.tools")) {
                Messages.get("error.no-permissions.tools").send(sender);
                return false;
            }

            if ((args.length != 1) && (args.length != 2)) {
                Messages.get("error.invalid-arguments").argument("%valid%", "/mc tools [on:off]").send(sender);
                return true;
            }

            if (args.length == 1) {
                up.setTools(!up.isTools());

                if (up.isTools()) {
                    Messages.get("message.tools-on").send(sender);
                } else {
                    Messages.get("message.tools-off").send(sender);
                }
                return true;
            }

            if (args[1].equalsIgnoreCase("on")) {
                up.setTools(true);

                Messages.get("message.tools-on").send(sender);
                return true;
            }

            if (args[1].equalsIgnoreCase("off")) {
                up.setTools(false);

                Messages.get("message.tools-off").send(sender);
                return true;
            }

            Messages.get("error.invalid-arguments").argument("%valid%", "/mc tools [on:off]").send(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("light")) {
            if (!p.hasPermission("flyingcarpet.light")) {
                Messages.get("error.no-permissions.light").send(sender);
                return true;
            }

            if ((args.length != 1) && (args.length != 2)) {
                Messages.get("error.invalid-arguments").argument("%valid%", "/mc light [on:off]").send(sender);
                return true;
            }

            if (args.length == 1) {
                up.setLight(!up.isLight());

                if (up.isLight()) {
                    Messages.get("message.light-on").send(sender);
                } else {
                    Messages.get("message.light-off").send(sender);
                }
                return true;
            }

            if (args[1].equalsIgnoreCase("on")) {
                up.setLight(true);

                Messages.get("message.light-on").send(sender);
                return true;
            }

            if (args[1].equalsIgnoreCase("off")) {
                up.setLight(false);

                Messages.get("message.light-off").send(sender);
                return true;
            }

            Messages.get("error.invalid-arguments").argument("%valid%", "/mc light [on:off]").send(sender);
            return true;
        }

        Messages.get("error.invalid-arguments").argument("%valid%", "/mc [on:off:tools:light" + ((FlyingCarpet.getInstance().isWorldGuardHooked()) && (p.hasPermission("flyingcarpet.worldguard")) ? ":worldguard" : "") + "]").send(sender);
        return true;
    }
}


/* Location:              /home/marcelo-frau/Dropbox/mundominecraft_/sources/FlyingCarpet/FlyingCarpet.jar!/me/sothatsit/flyingcarpet/FlyingCarpetCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */