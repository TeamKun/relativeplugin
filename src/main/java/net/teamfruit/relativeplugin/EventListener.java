package net.teamfruit.relativeplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Objects;

public class EventListener implements Listener {
    public static final double EPSILON = 0.1D;
    public static final long TIME_EPSILON_MS = 50;
    private long lastTime;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (RelativePlugin.INSTANCE.king == null)
            return;

        if (!RelativePlugin.INSTANCE.enabled)
            return;

        Player player = event.getPlayer();
        String name = player.getName();
        if (Objects.equals(name, RelativePlugin.INSTANCE.king)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (Objects.equals(p, player))
                    continue;
                long time = System.currentTimeMillis();
                if (time - lastTime > TIME_EPSILON_MS) {
                    lastTime = time;
                    Location to = move(p, player, p.getLocation(), p.getLocation(), EPSILON);
                    if (to != null)
                        p.teleport(to);
                }
            }
        } else {
            Player king = Bukkit.getPlayer(RelativePlugin.INSTANCE.king);
            if (king != null) {
                Location to = move(player, king, event.getFrom(), event.getTo(),
                        event.getFrom().getDirection().equals(event.getTo().getDirection())
                                || player.getVelocity().length() > 0 ? 1 : EPSILON);
                if (to != null)
                    event.setTo(to);
            }
        }
    }

    private Location move(Player player, Player king, Location fromLoc, Location loc, double epsilon) {
        Vector offset = RelativePlugin.INSTANCE.offsets.computeIfAbsent(player.getName(), e -> king.getLocation().subtract(loc).toVector());
        Location kingLoc = king.getLocation().subtract(offset);
        if (Math.abs(loc.getX() - kingLoc.getX()) > epsilon
                || Math.abs(loc.getZ() - kingLoc.getZ()) > epsilon) {
            loc.setX(kingLoc.getX());
            loc.setZ(kingLoc.getZ());
            return loc;
        }
        return null;
    }

    /*
    private void motion(Player player, Player king, Location fromLoc, Location toLoc) {
        Location loc = fromLoc.clone();
        Vector offset = RelativePlugin.INSTANCE.offsets.computeIfAbsent(player.getName(), e -> king.getLocation().subtract(loc).toVector());
        Location kingLoc = king.getLocation().subtract(offset);
        loc.setX(kingLoc.getX());
        loc.setZ(kingLoc.getZ());
        Vector sub = kingLoc.subtract(loc).toVector();
        sub.setY(0);
        player.setVelocity(player.getVelocity().add(sub));
    }
     */
}
