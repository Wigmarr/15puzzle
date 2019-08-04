package com.practice.blueTeam.Algo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PuzzleTest {
    public Puzzle temp;

    public class TilePos {
        public int x, y;

        public TilePos (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private int[][] tiles;
    private Puzzle.TilePos blank;
    @Before
    public void setUp(){
        temp = new Puzzle();
    }

    @Test
    public void move1() {
        Puzzle buf = new Puzzle(temp);
        buf.move(buf.whereIs(16));
        assertEquals(buf, temp);
    }

    @Test
    public void move2() {
        Puzzle buf = new Puzzle(temp);
        buf.move(buf.whereIs(-1));
        assertEquals(buf, temp);
    }

    @Test
    public void move3() {
        Puzzle buf = new Puzzle(temp);
        buf.move(buf.whereIs(11));
        assertNotEquals(buf, temp);
    }

    @Test
    public void move4() {
        Puzzle buf = new Puzzle(temp);
        buf.move(buf.whereIs(14));
        assertNotEquals(buf, temp);
    }

    @Test
    public void shuffle1() {
        Puzzle buf = new Puzzle(temp);
        buf.shuffle(3);
        assertNotSame(buf, temp);
    }

    @Test
    public void shuffle2() {
        Puzzle buf = new Puzzle(temp);
        buf.shuffle(25);
        assertNotSame(buf, temp);
    }

    @Test
    public void isVaildMove1(){
        assertTrue(temp.isValidMove(temp.whereIs(14)));
    }

    @Test
    public void isVaildMove2(){
        assertTrue(temp.isValidMove(temp.whereIs(11)));
    }
    @Test
    public void isNotValidMove1(){
        assertFalse(temp.isValidMove(temp.whereIs(-6)));

    }
    @Test
    public void isNotValidMove2(){
        assertFalse(temp.isValidMove(temp.whereIs(25)));
    }
    @Test
    public void isNotValidMove3(){
        assertFalse(temp.isValidMove(temp.whereIs(4)));
    }
    @Test
    public void puz(){
        assertEquals(Puzzle.SOLVED, new Puzzle());
    }
    @Test
    public void  estimateError1(){
        assertEquals(0, temp.estimateError());
    }
    @Test
    public void  estimateError2(){
        temp.shuffle(5);
        assertNotEquals(0, temp.estimateError());
    }
    @Test
    public void getTile(){
        assertEquals(15, temp.getTile(temp.whereIs(15)));
    }
}