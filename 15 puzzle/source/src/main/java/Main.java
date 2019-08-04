import com.practice.blueTeam.UI.MainWindow;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        MainWindow ui = MainWindow.getUI();
        ui.showUI();
    }
}
