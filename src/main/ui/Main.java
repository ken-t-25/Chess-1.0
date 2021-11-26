package ui;

import model.Event;
import model.EventLog;

public class Main {
    public static void main(String[] args) {
        PlayGame game = new PlayGame();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Event next : EventLog.getInstance()) {
                System.out.println(next.toString() + "\n");
            }
        }));
    }
}
