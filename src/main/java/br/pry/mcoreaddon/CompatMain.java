package br.pry.mcoreaddon;

import net.Indyuce.mmocore.api.MMOCoreAPI;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.skilltree.SkillTreeStatus;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class CompatMain extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        if (!getServer().getPluginManager().isPluginEnabled("MMOCore")) {
            getLogger().severe("MMOCore not found!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Objects.requireNonNull(getCommand("rpgst")).setExecutor(this);
        getLogger().info("This plugin was made for Astra minecraft server.");
        getLogger().info("All services online.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Astra addon offline.");
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      // Example usage: /rpgst <Player> <skillTreeNodeId> <Level>
        if (!sender.hasPermission("mmocore.admin")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You don't have the permission to do that."));
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Use: /rpgst <Player> <skillTreeNodeId> <Level>"));
            return true;
        }

        Player t = Bukkit.getPlayerExact(args[0]);
        if (t == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Targeted player is offline or invalid!"));
            return true;
        }

        PlayerData pd = PlayerData.get(t.getUniqueId());
        try {
            int level = Integer.parseInt(args[2]);
            // Get all nodes on the player data, note that get nodes from all skill-trees.

            pd.getNodeStates().keySet().forEach(nodet -> {
                if (nodet.getId().equalsIgnoreCase(args[1])) {
                    pd.setNodeState(nodet, SkillTreeStatus.UNLOCKED);
                    pd.setNodeLevel(nodet, level);
                }
            });
        } catch (NumberFormatException e){
           sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>The input level was not a number!"));
        }
        return true;
    }

}
