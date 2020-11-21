package me.greaperc4.playerglow;

import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerGlow extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("playerglow").setExecutor(new GlowCommand());
        this.getCommand("playerglow").setTabCompleter(new GlowCompleter());
    }

    @Override
    public void onDisable() {
    }
}
