package net.koru.auth.utils;

import net.koru.auth.Auth;

public class TaskUtil {

    public static void runAsync(Runnable runnable) {
        Auth.get().getServer().getScheduler().buildTask(Auth.get(), runnable).schedule();
    }

}
