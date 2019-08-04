package com.practice.blueTeam.DataBase;

import com.practice.blueTeam.Algo.Puzzle;
import com.practice.blueTeam.Algo.Wrap;
import com.practice.blueTeam.GameState.GameState;
import com.practice.blueTeam.UI.LevelSelectWindow;
import com.practice.blueTeam.UI.LevelWindow;
import com.practice.blueTeam.UI.SecretLevel;
import com.practice.blueTeam.UI.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.Data;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

// Используется синглтон, чтобы на протижении всего процесса везде использовалась одна база данных
public class DataBase {
    private static DataBase ourInstance = new DataBase();
    public static DataBase getInstance() {
        return ourInstance;
    }

    public static boolean getIsSolved(int index) {
        return isSolved[index];
    }
    public static boolean getIsSolved(){
        return isSecretSolved;
    }
    private  static boolean isSecretSolved;
    private static boolean[] isSolved;
    // API для работы с алгоритмом
    public static void randomizePuzzle(int index) {
        setTilesOrderOnTheScreen(Wrap.RandomizePuzzle(), index);
        levelWindows[index].setTiles();
        if (Wrap.isSolved()) {
            isSolved[index] = true;
        }
        else {
            isSolved[index] = false;
        }
    }

    public static void nextStep(int index) {
        setTilesOrderOnTheScreen(Wrap.StepForward(),index);
        levelWindows[index].setTiles();
        if (Wrap.isSolved()) {
            isSolved[index] = true;
        }
        else {
            isSolved[index] = false;
        }
    }

    public static void prevStep(int index) {
        setTilesOrderOnTheScreen(Wrap.StepBack(),index);
        levelWindows[index].setTiles();
        if (Wrap.isSolved()) {
            isSolved[index] = true;
        }
        else {
            isSolved[index] = false;
        }
    }

    public static void Move (int numberOfTile, int index){
        setTilesOrderOnTheScreen(Wrap.MoveTile(numberOfTile), index);
        levelWindows[index].setTiles();
        if (Wrap.isSolved()) {
            GameState.setIsAvailable(index+1);
            LevelSelectWindow.update();
            isSolved[index] = true;
        }
        else {
            isSolved[index] = false;
        }
    }

    public static void AutoSolve() {
        setSecretTilesOrderOnTheScreen(Wrap.showSolution());
        secretLevel.setTiles();
        if (Wrap.isSolved()) {
            isSecretSolved = true;
        }
        else {
            isSecretSolved = false;
        }
    }

    public static void AutoSolve(int index) {
        setTilesOrderOnTheScreen(Wrap.showSolution(), index);
        levelWindows[index].setTiles();
        if (Wrap.isSolved()) {
            isSolved[index] = true;
        }
        else {
            isSolved[index] = false;
        }
    }

    public static void Move (int numberOfTile){
//        Wrap.setPuzzle(converTilesIntoINT(getTilesOrderOnTheScreen(index)));
        setSecretTilesOrderOnTheScreen(Wrap.MoveTile(numberOfTile));
        secretLevel.setTiles();
        if (Wrap.isSolved()) {
            isSecretSolved = true;
        }
        else {
            isSecretSolved = false;
        }
    }

    public static void randomizeSecretPuzzle() {
        setSecretTilesOrderOnTheScreen(Wrap.RandomizePuzzle());
        secretLevel.setTiles();
    }

    public static void secretNextStep() {
        setSecretTilesOrderOnTheScreen(Wrap.StepForward());
        secretLevel.setTiles();
        if (Wrap.isSolved()) {
            isSecretSolved = true;
        }
        else {
            isSecretSolved = false;
        }
    }

    public static void secretPrevStep() {
        setSecretTilesOrderOnTheScreen(Wrap.StepBack());
        secretLevel.setTiles();
        if (Wrap.isSolved()) {
            isSecretSolved = true;
        }
        else {
            isSecretSolved = false;
        }
    }

    // количество уровней
    private static int numberOfLevels;
    public static int getNumberOfLevels() {
        return numberOfLevels;
    }
    public static void setNumberOfLevels(int numberOfLevels) {
        DataBase.numberOfLevels = numberOfLevels;
    }
    // Текущий уровень
//    private static int currentLevel;
//    public static int getCurrentLevel() {
//        return currentLevel;
    //    }
    // Иконки уровней
//    public static void setCurrentLevel(int currentLevel) {
//        DataBase.currentLevel = currentLevel;
//    }
    private static ImageIcon[] levelIcons;
    public static ImageIcon[] getLevelIcons() {
        return levelIcons;
    }
    public static void setLevelIcons(ImageIcon[] levelIcons) {
        DataBase.levelIcons = levelIcons;
    }
    // Рисунки Тайлов
    private static ImageIcon[][] tilesIcons;
    public static ImageIcon[][] getTiles() {
        return tilesIcons;
    }
    public static ImageIcon[] getLevelTiles(int levelNumber) {
        return tilesIcons[levelNumber];
    }
    // порядок Тайлов
    private static Tile[][] tilesOrderOnTheScreen;
    public static Tile[] getTilesOrderOnTheScreen(int i) {
        return tilesOrderOnTheScreen[i];
    }
    public static void setTilesOrderOnTheScreen(int[] tilesOrderOnTheScreen, int index) {
        for (int i = 0; i < 16; i++) {
            DataBase.tilesOrderOnTheScreen[index][i] = new Tile(tilesOrderOnTheScreen[i],index);
            DataBase.tilesOrderOnTheScreen[index][i].setIcon(DataBase.tilesIcons[index][i]);
        }
    }
     //порядок секретных тайлов
    private static Tile[] secretTilesOrderOnTheScreen;
    public static Tile[] getSecretTilesOrderOnTheScreen() {
        return secretTilesOrderOnTheScreen;
    }
    public static void setSecretTilesOrderOnTheScreen(int[] tilesOrderOnTheScreen) {
        for (int i = 0; i < 16; i++) {
            DataBase.secretTilesOrderOnTheScreen[i] = new Tile(tilesOrderOnTheScreen[i], 0);
            DataBase.secretTilesOrderOnTheScreen[i].setIcon(DataBase.secretTilesIcons[i]);
        }
    }
    // Уровни
    private static LevelWindow[] levelWindows;
    public static LevelWindow getLevelWindows(int i) {
        return levelWindows[i];
    }
    // Секретный уровень
    private static SecretLevel secretLevel;
    public static SecretLevel getSecretLevel() {
        return secretLevel;
    }
    // Рисунки тайлов секретного уровня
    private static ImageIcon[] secretTilesIcons;
    public static ImageIcon[] getSecretTiles() {
        return secretTilesIcons;
    }

    // Рисунок секретного уровня
    private static  ImageIcon secretLevelImage;
    public static ImageIcon getSecretLevelImage() {
        return secretLevelImage;
    }
    // конструктор
    private DataBase() {
        numberOfLevels = 3;
        //
        // waiting for functional part to get tiles
        //
        isSolved = new boolean[numberOfLevels];
        levelIcons = new ImageIcon[numberOfLevels];
        tilesIcons = new ImageIcon[numberOfLevels][16];
        levelWindows = new LevelWindow[numberOfLevels];
        tilesOrderOnTheScreen = new Tile[numberOfLevels][16];
        secretTilesOrderOnTheScreen = new Tile[16];
        secretTilesIcons = new ImageIcon[16];
        for (int i = 0; i < numberOfLevels+1; i++){
            if (i == numberOfLevels) {

                String path = new String("/SecretLevel/Image.jpeg");
                URL url = this.getClass().getResource(path);
                secretLevelImage = new ImageIcon(url);
                for (int j = 0; j < 16; j++) {
                    String path2 = new String( "/SecretLevel/images/Image_" + (j+1) +".jpg");
                    url = this.getClass().getResource(path2);
                    secretTilesIcons[j] = new ImageIcon(url);
                }

                secretLevel = new SecretLevel();
            //    randomizeSecretPuzzle();
                break;
            }
            String path = new String("/Level"+ (i+1)+ "/Image.jpeg");
            URL url = this.getClass().getResource(path);
            levelIcons[i] = new ImageIcon(url);
            for (int j = 0; j < 16; j++) {
                String path2 = new String( "/Level" + (i+1) + "/images/Image_" + (j+1) +".jpg");
                url = this.getClass().getResource(path2);
                tilesIcons[i][j] = new ImageIcon(url);
            }
        }

        for (int i = 0; i < numberOfLevels; i++) {
            levelWindows[i] = new LevelWindow(i);
            randomizePuzzle(i);
        }
    }
}
