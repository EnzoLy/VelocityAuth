package net.koru.auth.utils;

import net.koru.auth.Auth;

import java.util.concurrent.TimeUnit;

public class TaskUtil {

    public static void runAsync(Runnable runnable) {
        Auth.get().getServer().getScheduler().buildTask(Auth.get(), runnable).schedule();
    }

    public static void runLater(Runnable runnable, int time, TimeUnit unit) {
        Auth.get().getServer().getScheduler().buildTask(Auth.get(), runnable).delay(time, unit).schedule();
    }

}
