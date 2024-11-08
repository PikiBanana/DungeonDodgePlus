package io.github.pikibanana.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class TaskScheduler {

    public static void scheduleDelayedTask(int delayTicks, Runnable task) {
        DelayedTaskHandler delayedTaskHandler = new DelayedTaskHandler(delayTicks, task);
        ClientTickEvents.END_CLIENT_TICK.register(client -> delayedTaskHandler.onTick());
    }

    private static class DelayedTaskHandler {
        private final Runnable task;
        private int remainingTicks;
        private boolean taskExecuted = false;

        public DelayedTaskHandler(int delayTicks, Runnable task) {
            this.remainingTicks = delayTicks;
            this.task = task;
        }

        public void onTick() {
            if (remainingTicks > 0) {
                remainingTicks--;
            } else if (!taskExecuted) {
                task.run();
                taskExecuted = true;
            }
        }
    }
}
