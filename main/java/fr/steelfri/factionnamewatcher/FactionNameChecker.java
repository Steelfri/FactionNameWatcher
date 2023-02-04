package fr.steelfri.factionnamewatcher;

import com.massivecraft.factions.event.FactionAttemptCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class FactionNameChecker extends JavaPlugin implements Listener {

    private List<String> allowedNames;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        allowedNames = getConfig().getStringList("allowed-names");
    }

    @EventHandler
    public void onFactionAttemptCreate(FactionAttemptCreateEvent event) {
        if (!allowedNames.contains(event.getFactionTag())) {
            event.setCancelled(true);
            event.getFPlayer().sendMessage(ChatColor.RED + "The name of your faction isn't allowed, choose another one.");
        }
    }
}