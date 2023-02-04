package fr.steelfri.factionnamewatcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.event.FactionAttemptCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FactionNameChecker extends JavaPlugin implements Listener {
    private Set<String> allowedNames;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        String allowedNamesFile = config.getString("allowed-names-file");
        File file = new File(allowedNamesFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
                allowedNames = new HashSet<>();
                FileWriter writer = new FileWriter(file);
                writer.write(new Gson().toJson(allowedNames));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileReader reader = new FileReader(file);
                allowedNames = new Gson().fromJson(reader, new TypeToken<Set<String>>() {}.getType());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onFactionAttemptCreate(FactionAttemptCreateEvent event) {
        if (!allowedNames.contains(event.getFactionTag())) {
            event.setCancelled(true);
            event.getFPlayer().sendMessage(ChatColor.RED + "The name of your faction isn't allowed, choose another one.");
        }
    }
}