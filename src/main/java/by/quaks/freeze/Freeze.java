package by.quaks.freeze;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Freeze implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player victim = Bukkit.getPlayerExact(args[0]);
        if (victim != null && victim.isOnline()) {
            TextComponent button = new TextComponent();
            button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/freeze "+victim.getDisplayName()));
            TextComponent victimName = new TextComponent(victim.getDisplayName());
            victimName.setColor(getColor(Config.get().getString("PlayerNameColor")));
            if(!victim.getScoreboardTags().contains("Frozen")){
                //ЗАМОРАЖИВАЕМ
                button.setText(Config.get().getString("Button.Unfreeze.Label"));
                button.setColor(getColor(Config.get().getString("Button.Unfreeze.Color")));
                button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Config.get().getString("Button.Freeze.HoverText").replaceAll("%player-name%", victim.getDisplayName())).color(ChatColor.GRAY).create()));
                Map<String, TextComponent> placeholders = new HashMap<>();
                placeholders.put("player-name",victimName);
                placeholders.put("button",button);
                sender.spigot().sendMessage(replacePlaceholders(Config.get().getString("Freeze-form"),placeholders));
                victim.addScoreboardTag("Frozen");
            }else{
                //ЗАМОРАЖИВАЕМ
                button.setText(Config.get().getString("Button.Freeze.Label"));
                button.setColor(getColor(Config.get().getString("Button.Freeze.Color")));
                button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Config.get().getString("Button.Unfreeze.HoverText").replaceAll("%player-name%", victim.getDisplayName())).color(ChatColor.GRAY).create()));
                Map<String, TextComponent> placeholders = new HashMap<>();
                placeholders.put("player-name",victimName);
                placeholders.put("button",button);
                sender.spigot().sendMessage(replacePlaceholders(Config.get().getString("Unfrozen-form"),placeholders));
                victim.removeScoreboardTag("Frozen");
            }
        }else{
            TextComponent victimName = new TextComponent(args[0]);
            victimName.setColor(getColor(Config.get().getString("PlayerNameColor")));
            Map<String, TextComponent> placeholders = new HashMap<>();
            placeholders.put("player-name",victimName);
            sender.spigot().sendMessage(replacePlaceholders(Config.get().getString("Offline"),placeholders));
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length){
            case 1:
                return null;
            default:
                return Collections.emptyList();
        }
    }
    private static ChatColor getColor(String text) {
        if (text.startsWith("#")) {
            return ChatColor.of(text);
        } else {
            switch (text.toUpperCase()) {
                case "BLACK":
                    return ChatColor.BLACK;
                case "DARK_BLUE":
                    return ChatColor.DARK_BLUE;
                case "DARK_GREEN":
                    return ChatColor.DARK_GREEN;
                case "DARK_AQUA":
                    return ChatColor.DARK_AQUA;
                case "DARK_RED":
                    return ChatColor.DARK_RED;
                case "DARK_PURPLE":
                    return ChatColor.DARK_PURPLE;
                case "GOLD":
                    return ChatColor.GOLD;
                case "GRAY":
                    return ChatColor.GRAY;
                case "DARK_GRAY":
                    return ChatColor.DARK_GRAY;
                case "BLUE":
                    return ChatColor.BLUE;
                case "GREEN":
                    return ChatColor.GREEN;
                case "AQUA":
                    return ChatColor.AQUA;
                case "RED":
                    return ChatColor.RED;
                case "LIGHT_PURPLE":
                    return ChatColor.LIGHT_PURPLE;
                case "YELLOW":
                    return ChatColor.YELLOW;
                case "WHITE":
                    return ChatColor.WHITE;
                default:
                    return ChatColor.WHITE;
            }
        }
    }
    private TextComponent replacePlaceholders(String message, Map<String, TextComponent> placeholders) {
        TextComponent textComponent = new TextComponent();

        // Use a regular expression to match placeholders in the message
        Pattern pattern = Pattern.compile("%(.*?)%");
        Matcher matcher = pattern.matcher(message);
        int lastIndex = 0;

        // Iterate through the matches and replace them with TextComponents
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            TextComponent replacement = placeholders.getOrDefault(placeholder, new TextComponent());
            if (matcher.start() > lastIndex) {
                // Add any non-matching text to the final TextComponent
                textComponent.addExtra(message.substring(lastIndex, matcher.start()));
            }
            // Add the replacement TextComponent to the final TextComponent
            textComponent.addExtra(replacement);
            lastIndex = matcher.end();
        }

        // Add any remaining non-matching text to the final TextComponent
        if (lastIndex < message.length()) {
            textComponent.addExtra(message.substring(lastIndex));
        }

        return textComponent;
    }
}
