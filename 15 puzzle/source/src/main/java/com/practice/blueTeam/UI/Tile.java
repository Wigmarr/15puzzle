package com.practice.blueTeam.UI;

import com.practice.blueTeam.DataBase.DataBase;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tile extends JButton {
    // является ли последним тайлом
    private boolean last;
    public boolean isLast() {
        return last;
    }
    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumberOfTile() {
        return numberOfTile;
    }

    // номер тайла
    int numberOfTile;
    int levelNumber;
    // конструктор
    public Tile(int numberOfTile, int levelNumber) {
        this.last = false;
        this.numberOfTile = numberOfTile;
        if (numberOfTile == 15)
            this.last = true;
        else
            this.last = false;

        setSize(50,50);
        if (this.last)
            setVisible(false);
        else
            setVisible(true);
        if (levelNumber == DataBase.getNumberOfLevels() + 1) {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    DataBase.Move(numberOfTile);
                    DataBase.getSecretLevel().setTiles();
                    if (DataBase.getIsSolved())
                        DataBase.getSecretLevel().getNextButton().setEnabled(false);
                    else
                        DataBase.getSecretLevel().getNextButton().setEnabled(true);
                }
            });
        } else {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    DataBase.Move(numberOfTile, levelNumber);
                    DataBase.getLevelWindows(levelNumber).setTiles();
                    if (DataBase.getIsSolved(levelNumber))
                        DataBase.getLevelWindows(levelNumber).getNextButton().setEnabled(false);
                    else
                        DataBase.getLevelWindows(levelNumber).getNextButton().setEnabled(true);
                }
            });
        }
    }
}
