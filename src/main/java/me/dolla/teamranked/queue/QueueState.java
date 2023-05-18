package me.dolla.teamranked.queue;

import java.util.Arrays;

public enum QueueState {
    WAITING, STARTING, PLAYING;

    private static QueueState state;

    public static void setState(QueueState state)
    {
        QueueState.state = state;
    }

    public static boolean isState(QueueState state)
    {
        return QueueState.state == state;
    }

    public static QueueState getState()
    {
        return state;
    }
}
