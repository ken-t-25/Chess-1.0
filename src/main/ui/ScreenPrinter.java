package ui;

import java.awt.*;

import javax.swing.*;

import model.Event;
import model.EventLog;


public class ScreenPrinter extends JInternalFrame {

    private JTextArea textArea;


    // EFFECTS: constructs a screen printer to print events in event log
    public ScreenPrinter() {
        super("Event log", false, false, false, false);
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);
        setVisible(true);
    }

    // REQUIRES: textArea must be previously initialized
    // MODIFIES: this
    // EFFECTS: print all events in event log onto textArea
    public void printLog(EventLog el) {
        for (Event next : el) {
            textArea.setText(textArea.getText() + next.toString() + "\n\n");
        }
        repaint();
    }
}
