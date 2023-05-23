package de.hthoene.playerstatslib;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class ManagedStatsDatabase extends AsyncStatsDatabase {

    private final JavaPlugin javaPlugin;

    public ManagedStatsDatabase(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void connect(String configurationName) throws IOException, SQLException {
        Files.createDirectories(javaPlugin.getDataFolder().toPath());
        File file = new File(javaPlugin.getDataFolder(), configurationName);
        file.createNewFile();
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.options().copyDefaults(true);
        yamlConfiguration.addDefault("database.host", "your_host");
        yamlConfiguration.addDefault("database.port", 3306);
        yamlConfiguration.addDefault("database.database", "your_database");
        yamlConfiguration.addDefault("database.username", "your_username");
        yamlConfiguration.addDefault("database.password", "your_password");
        yamlConfiguration.save(file);
        connect(yamlConfiguration);
    }

}
