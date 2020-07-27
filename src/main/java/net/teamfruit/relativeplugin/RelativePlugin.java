package net.teamfruit.relativeplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public final class RelativePlugin extends JavaPlugin {
    public static RelativePlugin INSTANCE;
    public boolean enabled;
    public Map<String, Vector> offsets = new HashMap<>();
    public String king;

    @Override
    public void onEnable() {
        INSTANCE = this;

        // イベント登録
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getCommand("relative").setExecutor(new CommandListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
