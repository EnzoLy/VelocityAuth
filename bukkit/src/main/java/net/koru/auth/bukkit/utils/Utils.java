package net.koru.auth.bukkit.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;

@UtilityClass
public class Utils {

    public boolean hasMoved(Location one, Location two) {
        return (one.getX() != two.getX()
            || one.getY() != two.getY()
            || one.getZ() != two.getZ());
    }

}