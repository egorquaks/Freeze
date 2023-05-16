package by.quaks.freeze;

import org.bukkit.plugin.java.JavaPlugin;

public final class FreezeMain extends JavaPlugin {

    @Override
    public void onEnable() {
        if(!this.getDataFolder().exists()) { // Создание папки для хранения конфигурационных файлов
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Config.setup();
        getCommand("freeze").setExecutor(new Freeze());
        getServer().getPluginManager().registerEvents(new FreezeListener(),this);
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
