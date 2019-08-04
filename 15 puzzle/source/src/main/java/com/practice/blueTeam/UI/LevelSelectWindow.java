package com.practice.blueTeam.UI;


import com.practice.blueTeam.DataBase.DataBase;
import com.practice.blueTeam.GameState.GameState;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelSelectWindow extends JFrame {
    private static LevelSelectWindow selectWindow = new LevelSelectWindow();
    // массив кнопок выбора уровня

    private static levelButton[] levelButtons;
    // класс кнопки выбора уровня
    private class levelButton extends JButton{
        public int getLevelNumber() {
            return levelNumber;
        }

        public void setLevelNumber(int levelNumber) {
            this.levelNumber = levelNumber;
        }

        private int levelNumber;
        public levelButton(int levelNumber, String name) {
            super(name);
            this.levelNumber = levelNumber;
            this.addMouseListener(mouseAdapter);
        }
        public  levelButton(){
            super();
            this.levelNumber = 0;
            this.addMouseListener(mouseAdapter);
        }
        public  levelButton(int levelNumber){
            super();
            this.levelNumber = levelNumber;

        }
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectWindow.setVisible(false);
                DataBase.getLevelWindows(levelNumber).setVisible(true);
                DataBase.randomizePuzzle(levelNumber);
                focusReleased(e.getComponent());
            }
        };
    }
    // метод отвечающий за создание массива кнопок
    private void createLevelButtons(){
        for (int i = 0; i < DataBase.getNumberOfLevels(); ++i) {
            levelButtons[i] = new levelButton(i);
            levelButtons[i].setIcon(DataBase.getLevelIcons()[i]);
            levelButtons[i].setBorderPainted(false);
            levelButtons[i].setFocusPainted(false);
        }
    }

    public static void update() {
        for (int i = 0; i < levelButtons.length; i++) {
            if (!GameState.getIsAvailable(i)) {
                levelButtons[i].setEnabled(false);
            } else {
                levelButtons[i].setEnabled(true);
                levelButtons[i].addMouseListener(levelButtons[i].mouseAdapter);
            }
        }
    }

    public void focusReleased(Component component)
    {
        component.setFocusable(false);
        this.setFocusable(true);
        this.requestFocus();
    }
    // listner для секретного уровня
    // уровни
    // кнопка закрытия приложения
    private JButton closeButton = new JButton("Close");
    // кнопка возврата в меню
    private JButton returnButton = new JButton("Return to Main menu");

    public static LevelSelectWindow getSelectWindow() {
        return selectWindow;
    }

    private LevelSelectWindow() {
        //настройки основного окна
        super("Пятнашки");
        levelButtons = new levelButton[DataBase.getNumberOfLevels()];
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // настройки размера окна
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = 800;
        int sizeHeight = 700;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        this.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        // инициализация mouseListner для closeButton
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
                focusReleased(closeButton);
            }
        });
        // инициализация mouseListner для returnButton
        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectWindow.setVisible(false);
                MainWindow.getUI().showUI();
                focusReleased(returnButton);
            }
        });
        // инициализация массива levelButtons
        createLevelButtons();

        //// Настройки внешнего вида окна
        //Основная панель, на ней размещены другие панели
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setSize(this.getSize());

        // Панель с кнопками
        JPanel levelPanel = new JPanel();
        levelPanel.setBounds(mainPanel.getX(),mainPanel.getY(),mainPanel.getWidth() - 10,mainPanel.getHeight() - 100);
        GridBagLayout layout = new GridBagLayout();
        levelPanel.setLayout(layout);

        // первоначальные настройки Сетки
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,10,10,10);
        //Добавление кнопок на панель
        for (int i = 0; i < levelButtons.length; i++) {
             {
                if (i % 2 == 1) {
                    levelPanel.add(levelButtons[i], gbc);
                    gbc.gridx = 0;
                    gbc.gridy++;
                } else {
                    levelPanel.add(levelButtons[i], gbc);
                    gbc.gridx++;
                }
                if (!GameState.getIsAvailable(i)) {
                    levelButtons[i].setEnabled(false);
                } else {
                    levelButtons[i].setEnabled(true);
                    levelButtons[i].addMouseListener(levelButtons[i].mouseAdapter);
                }
            }
        }
        // Обертка в JScrollPane
        JScrollPane levelScroll = new JScrollPane(levelPanel);
        levelScroll.setBounds(levelPanel.getBounds());
        levelScroll.createVerticalScrollBar();
        levelScroll.getVerticalScrollBar().setUnitIncrement(10);
        levelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // добавление кнопок уровня на mainPanel
        mainPanel.add(levelScroll,BorderLayout.CENTER);

        // панель для кнопок навигации меню
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBounds(levelPanel.getX(), levelPanel.getHeight() -10,
                levelPanel.getWidth(),mainPanel.getHeight() - levelPanel.getHeight());
        // настройка расположения кнопок
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.ipadx = 0;
        gbc.ipady = 15;
        gbc.insets = new Insets(0,10,20,10);
        gbc.anchor = GridBagConstraints.CENTER;
        menuPanel.add(closeButton,gbc);
        gbc.gridx = 1;
        gbc.weightx = 2;
        menuPanel.add(returnButton, gbc);
        mainPanel.add(menuPanel);
        this.getContentPane().add(mainPanel);
        for (int i = 0; i < levelButtons.length; i++) {
            levelButtons[i].setSize(330,330);
            levelButtons[i].setIcon(new ImageIcon(DataBase.getLevelIcons()[i].getImage().getScaledInstance(levelButtons[i].getWidth(),levelButtons[i].getHeight(),Image.SCALE_SMOOTH)));
            levelButtons[i].setContentAreaFilled(false);
        }
        focusReleased(mainPanel);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                GameState.secretTyped(e.getKeyChar());
                if (GameState.isCodeActivated()) {
                    selectWindow.setVisible(false);
                    DataBase.getSecretLevel().setVisible(true);
                    DataBase.randomizeSecretPuzzle();

                }
            }
        });

    }
}
