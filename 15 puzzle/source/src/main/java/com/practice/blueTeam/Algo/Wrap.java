package com.practice.blueTeam.Algo;

import java.util.ArrayList;
import java.util.List;

public class Wrap {

    private static Puzzle puzzle = new Puzzle();

    private static ArrayList<int[]> states = new ArrayList<int[]>();
    private static ArrayList<Puzzle> solution = null;
    private static boolean isStateSolved = false;

    private static int currentState = -1;

    private static final int numOfShuffles = 25;

    public static int[] showSolution() {
        puzzle = new Puzzle();
        if (solution != null) { clearSolution(); }
        clearStates();
        int[] temp = GetTilesArray(puzzle);
        addState(temp);
        return temp;
    }

    private static int[] GetTilesArray(Puzzle puzzle) {
        int[] out = new int[16];
        int c = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                out[c] = puzzle.getTiles()[i][j];
                c++;
            }
        }
        return out;
    }

    private static void clearStates() {
        states.clear();
        states.trimToSize();
        currentState = -1;
    }

    private static void addState (int[] state) {
        states.add(state);
        currentState++;
    }

    private static void addSolution() {
        while (solution == null) {

            solution = new ArrayList<Puzzle>(puzzle.aStarSolve());

            if (solution == null) {
                solution.trimToSize();
                solution = new ArrayList<Puzzle>(puzzle.dijkstraSolve());
            }
        }
        isStateSolved = true;
;    }

    private static void clearSolution() {
        solution.clear();
        solution.trimToSize();
        solution = null;
        isStateSolved = false;
    }

    public static int[] RandomizePuzzle() {
        puzzle = new Puzzle(numOfShuffles);
        clearStates();
        if (solution != null) {clearSolution();}
        int[] temp = GetTilesArray(puzzle);
        addState(temp);
        return temp;
    }

    public static void setPuzzle (int[] data) {
        if (solution != null) { clearSolution(); }
        clearStates();
        int[][] buff = new int[4][4];
        int n = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buff[i][j] = data[n];
                n++;
            }
        }
        puzzle.setTiles(buff);
        int[] tmp = GetTilesArray(puzzle);
        addState(tmp);
    }

    public static int[] MoveTile (int numOfTile) {
        if (solution != null) { clearSolution(); }
        if (currentState == states.size() - 1){
            if (!puzzle.isValidMove(puzzle.whereIs(numOfTile)))
                return states.get(currentState);
            puzzle.move(puzzle.whereIs(numOfTile));
        } else {
            int[][] buff = new int[4][4];
            int n = 0;
            int[] s = states.get(currentState);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    buff[i][j] = s[n];
                    n++;
                }
            }
            Puzzle test = new Puzzle();
            test.setTiles(buff);
            if (!test.isValidMove(test.whereIs(numOfTile)))
                return states.get(currentState);
            states.removeAll(states.subList(currentState + 1, states.size()));
            states.trimToSize();
            puzzle.setTiles(buff);
            puzzle.move(puzzle.whereIs(numOfTile));
        }
        int[] temp = GetTilesArray(puzzle);
        addState(temp);
        return temp;
    }

    public static int[] StepBack() {
        if(currentState > 0) {
            currentState--;
            return states.get(currentState);
        } else {
            return states.get(0);
        }
    }

    public static int[] StepForward() {
        if (currentState < states.size() - 1) {
            currentState++;
            return states.get(currentState);
        } else {
            if (puzzle.isSolved()) {
                return states.get(currentState);
            }
            if (!isStateSolved) {
                addSolution();
            }

            Puzzle temp = new Puzzle(solution.remove(0));
            while (puzzle.equals(temp)) {
                temp = new Puzzle(solution.remove(0));
            }
            solution.trimToSize();
            puzzle = new Puzzle(temp);
            int[] s = GetTilesArray(puzzle);
            addState(s);
            currentState = states.size() - 1;
            return s;
        }
    }

    public static boolean isSolved() {
        int[][] buff = new int[4][4];
        int n = 0;
        int[] s = states.get(currentState);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buff[i][j] = s[n];
                n++;
            }
        }
        Puzzle test = new Puzzle();
        test.setTiles(buff);
        if (test.isSolved()) {
//            solution.clear();
//            solution = null;
            return true;
        } else {
            return false;
        }
    }

    public static List<int[]> GetStates() {
        return states;
    }

}