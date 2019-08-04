package com.practice.blueTeam.Algo;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        /* Only way to create new Puzzle is to Wrap.RandomizePuzzle it */
        int[] state = Wrap.RandomizePuzzle();

        printState(state);

        int index = 0;

        /*
         Wrap.isSolved returns true or false
         Wrap.Step Back and Forward is for navigation, MoveTile for changing
         All those methods returns current state throught int[]
        */
        while (!Wrap.isSolved()) {
            index = in.nextInt();
            if (index == -1) {
                break;
            }
            if (index == -2){
                state = Wrap.StepBack();
                printState(state);
                printStates();
                continue;
            }
            if (index == -3){
                state = Wrap.StepForward();
                printState(state);
                printStates();
                continue;
            }
            state = Wrap.MoveTile(index);
            printStates();
        }

    }

    public static void printStates() {
        for (int[] t : Wrap.GetStates()) {
            System.out.print("\t");
            for (int i : t) {
                System.out.print(i);
            }
        }
        System.out.println();
    }

    public static void printState(int[] state) {
        System.out.println();
        for (int t : state) {
            System.out.print(t);
        }
        System.out.println();
    }
}