package com.practice.blueTeam.UI;

import com.practice.blueTeam.DataBase.DataBase;
import com.practice.blueTeam.GameState.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MainWindow extends  JFrame {
    public static LevelSelectWindow getSelectWindow() {
        return selectWindow;
    }
    private static LevelSelectWindow selectWindow;
    private static MainWindow ui = null;
    private JButton newGame = new JButton("New Game");
    private JButton close = new JButton("Close");

    public static MainWindow getUI() {
        if (ui == null)
            ui = new MainWindow();
        return ui;
    }

    public void showUI() {
        ui.setVisible(true);
    }
    public void closeUI() { ui.setVisible(false); }
    private MainWindow() {
        super("Пятнашки");
        selectWindow = LevelSelectWindow.getSelectWindow();
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = 800;
        int sizeHeight = 600;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        this.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        newGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ui.setVisible(false);
                selectWindow.setVisible(true);
            }
        });
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });
        JPanel mainPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        this.setContentPane(mainPanel);
        mainPanel.setLayout(null);
        mainPanel.setSize(this.getSize());
        buttonsPanel.setLayout(null);
        buttonsPanel.setLocation(mainPanel.getX() + 10,mainPanel.getY() + 20);
        buttonsPanel.setSize(mainPanel.getWidth() - 10, mainPanel.getHeight() - 50);
        buttonsPanel.add(newGame);
        buttonsPanel.add(close);
        newGame.setSize(buttonsPanel.getWidth() - 50, buttonsPanel.getHeight()/2 - 40);
        close.setSize(buttonsPanel.getWidth() - 50, buttonsPanel.getHeight()/2 - 40);
        newGame.setLocation(buttonsPanel.getX(),0);
        close.setLocation(buttonsPanel.getX(),buttonsPanel.getHeight()/2);
        mainPanel.add(buttonsPanel);
    }
}
