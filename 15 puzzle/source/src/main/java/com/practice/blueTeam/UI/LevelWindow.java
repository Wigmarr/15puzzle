package com.practice.blueTeam.UI;

import com.practice.blueTeam.Algo.Main;
import com.practice.blueTeam.Algo.Puzzle;
import com.practice.blueTeam.Algo.Wrap;
import com.practice.blueTeam.DataBase.DataBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

public class LevelWindow extends JFrame
        implements ActionListener, PropertyChangeListener {
    private JButton closeButton = new JButton("Close");
    private JButton randomButton = new JButton("Random");
    private JButton buildButton = new JButton("Show Solution");

    public JButton getNextButton() {
        return nextButton;
    }

    private JButton nextButton = new JButton("Next");
    private JButton prevButton = new JButton("Prev");
    private JButton returnButton = new JButton("Return to Main menu");
    private JPanel tilesPanel = new JPanel();
    private int levelNumber;
    private Tile[] tiles;

    private Task task;
    private JProgressBar progressBar;
    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            progressBar.setVisible(true);
            Random random = new Random();
            nextButton.setEnabled(false);
            int progress = 0;
            //Initialize progress property.
            setProgress(0);

            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(100));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(50);
                setProgress(Math.min(progress, 100));
            }

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            DataBase.nextStep(levelNumber);
            nextButton.setEnabled(true);
            progressBar.setVisible(false);
            if (DataBase.getIsSolved(levelNumber))
                nextButton.setEnabled(false);
            else
                nextButton.setEnabled(true);
            setTiles();
            setCursor(null); //turn off the wait cursor
        }
    }

    public void setTiles() {
        for (int i = 0; i < 16; i++)
        {
            tiles[i] = new Tile(DataBase.getTilesOrderOnTheScreen(levelNumber)[i].numberOfTile, levelNumber);
            tiles[i].setIcon(DataBase.getLevelTiles(levelNumber)[DataBase.getTilesOrderOnTheScreen(levelNumber)[i].numberOfTile]);
            tiles[i].setBorderPainted(false);
            tiles[i].setFocusPainted(false);

        }
        tilesPanel.removeAll();
        for (int i = 0; i < 16; i++) {
            tilesPanel.add(tiles[i]);
            tiles[i].setIcon(new ImageIcon(DataBase.getLevelTiles(levelNumber)[tiles[i].numberOfTile].getImage().getScaledInstance(tilesPanel.getWidth()/4, tilesPanel.getHeight()/4, Image.SCALE_SMOOTH)));
        }
    }
    public LevelWindow(int levelNumber) {

        super("Пятнашки");
        this.levelNumber = levelNumber;
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        tiles = new Tile[16];

        progressBar = new JProgressBar(0,100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);


        int sizeWidth = 800;
        int sizeHeight = 700;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        this.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        JPanel mainPanel = new JPanel();

        tilesPanel = new JPanel();
        this.setContentPane(mainPanel);
        mainPanel.setLayout(null);
        mainPanel.setSize(this.getSize());
        progressBar.setBounds(mainPanel.getWidth()/5, mainPanel.getHeight()/10, 200, 30);

        mainPanel.add(progressBar);
        tilesPanel.setLayout(new GridLayout(4,4,0,0));
        tilesPanel.setLocation(mainPanel.getWidth()/16,mainPanel.getHeight()/6 );
        tilesPanel.setSize(400, 400);
        mainPanel.add(tilesPanel);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLocation(tilesPanel.getWidth() + 100, tilesPanel.getY() -10);
        buttonsPanel.setSize(200,300);
        buttonsPanel.setLayout(new GridLayout(5,1,5,5));
        buttonsPanel.add(randomButton);
        buttonsPanel.add(buildButton);
        JPanel nextPrevPanel = new JPanel();
        nextPrevPanel.setLayout(new GridLayout(1,2,0,0));
        nextPrevPanel.add(prevButton);
        nextPrevPanel.add(nextButton);
        buttonsPanel.add(nextPrevPanel);
        buttonsPanel.add(closeButton);
        buttonsPanel.add(returnButton);
        mainPanel.add(buttonsPanel);
        buildButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DataBase.AutoSolve(levelNumber);
                if (DataBase.getIsSolved(levelNumber))
                    nextButton.setEnabled(false);
                else
                    nextButton.setEnabled(true);
            }
        });
        nextButton.setActionCommand("start");
        nextButton.addActionListener(this);
        prevButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DataBase.prevStep(levelNumber);
                if (DataBase.getIsSolved(levelNumber))
                    nextButton.setEnabled(false);
                else
                    nextButton.setEnabled(true);
            }
        });
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });
        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DataBase.getLevelWindows(levelNumber).setVisible(false);
                MainWindow.getUI().setVisible(true);
            }
        });
        randomButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DataBase.randomizePuzzle(levelNumber);
                if (DataBase.getIsSolved(levelNumber))
                    nextButton.setEnabled(false);
                else
                    nextButton.setEnabled(true);
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task();

        task.addPropertyChangeListener(this);
        task.execute();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }
}
