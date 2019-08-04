package com.practice.blueTeam.Algo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Puzzle {

    public class TilePos {
        public int x, y;

        public TilePos (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private int[][] tiles;
    private TilePos blank;

    public Puzzle () {
        setTiles(new int[4][4]);
        int num = 0;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                getTiles()[i][j] = num;
                num++;
            }
        }
        blank = new TilePos(3,3);
    }

    public Puzzle (Puzzle from) {
        this();
        for (TilePos p: allTilePos()) {
            getTiles()[p.x][p.y] = from.getTile(p);
        }
        blank = from.getBlank();
    }

    public Puzzle (int number) {
        this();
        this.shuffle(number);
    }

    public final static Puzzle SOLVED = new Puzzle();

    public int[][] getTiles() {
        return tiles;
    }

    public void setTiles(int[][] tiles) {
        this.tiles = tiles;
        this.blank = whereIs(15);
    }

    public List<TilePos> allTilePos() {
        ArrayList<TilePos> list = new ArrayList<TilePos>();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                list.add(new TilePos(i,j));
            }
        }

        return list;
    }

    public int getTile(TilePos p) {
        return getTiles()[p.x][p.y];
    }

    public TilePos getBlank() {
        return whereIs(15);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Puzzle) {
            for(TilePos p: allTilePos()) {
                if( this.getTile(p) != ((Puzzle) o).getTile(p)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int out=0;
        for(TilePos p: allTilePos()) {
            out= (out*4*4) + this.getTile(p);
        }
        return out;
    }

    public List<TilePos> allValidMoves() {
        ArrayList<TilePos> out = new ArrayList<TilePos>();
        for(int dx = -1; dx < 2; dx++) {
            for(int dy = -1; dy < 2; dy++) {
                TilePos tp = new TilePos(blank.x + dx, blank.y + dy);
                if( isValidMove(tp) ) {
                    out.add(tp);
                }
            }
        }
        return out;
    }

    public TilePos whereIs(int a) {
        for(TilePos p: allTilePos()) {
            if(getTile(p) == a) {
                return p;
            }
        }
        return null;
    }

    public boolean isValidMove(TilePos p) {
        if (p == null) {
            return false;
        }
        if( ( p.x < 0) || (p.x >= 4) ) {
            return false;
        }
        if( ( p.y < 0) || (p.y >= 4) ) {
            return false;
        }
        int dx = blank.x - p.x;
        int dy = blank.y - p.y;
        if( (Math.abs(dx) + Math.abs(dy) != 1 ) || (dx*dy != 0) ) {
            return false;
        }
        return true;
    }

    public void move(TilePos p) {
        if (isValidMove(p)) {
            getTiles()[blank.x][blank.y] = getTiles()[p.x][p.y];
            getTiles()[p.x][p.y] = 15;
            blank = p;
        }
    }

    public Puzzle moveClone(TilePos p) {
        Puzzle out = new Puzzle(this);
        out.move(p);
        return out;
    }

    public int numberMisplacedTiles() {
        int wrong = 0;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if( (getTiles()[i][j] < 15) && ( getTiles()[i][j] != SOLVED.getTiles()[i][j] ) ){
                    wrong++;
                }
            }
        }
        return wrong;
    }

    public boolean isSolved() {
        return numberMisplacedTiles() == 0;
    }

    public int estimateError() {
        return this.numberMisplacedTiles();
    }

    public List<Puzzle> allAdjacentPuzzles() {
        ArrayList<Puzzle> out = new ArrayList<Puzzle>();
        for (TilePos move : allValidMoves()) {
            out.add(moveClone(move));
        }
        return out;
    }

    public void shuffle(int number) {
        for(int i = 0; i < number; i++) {
            List<TilePos> possible = allValidMoves();
            int which =  (int) (Math.random() * possible.size());
            TilePos move = possible.get(which);
            this.move(move);
        }
    }

    public List<Puzzle> aStarSolve() {
        HashMap<Puzzle, Puzzle> predcessor = new HashMap<Puzzle, Puzzle>();
        HashMap<Puzzle, Integer> depth = new HashMap<Puzzle, Integer>();
        final HashMap<Puzzle, Integer> score = new HashMap<Puzzle, Integer>();
        Comparator<Puzzle> comparator = new Comparator<Puzzle>() {
            @Override
            public int compare(Puzzle a, Puzzle b) {
                return score.get(a) - score.get(b);
            }
        };
        PriorityQueue<Puzzle> toVisit = new PriorityQueue<Puzzle>(10000, comparator);

        predcessor.put(this, null);
        depth.put(this, 0);
        score.put(this, this.estimateError());
        toVisit.add(this);
        int cnt = 0;
        while (toVisit.size() > 0) {
            Puzzle candidate = toVisit.remove();
            cnt++;
            if( candidate.isSolved() ) {
                LinkedList<Puzzle> solution = new LinkedList<Puzzle>();
                Puzzle backtrace = candidate;
                while( backtrace != null ) {
                    solution.addFirst(backtrace);
                    backtrace = predcessor.get(backtrace);
                }
                return solution;
            }
            for(Puzzle fp: candidate.allAdjacentPuzzles()) {
                if( !predcessor.containsKey(fp) ) {
                    predcessor.put(fp, candidate);
                    depth.put(fp, depth.get(candidate)+1);
                    int estimate = fp.estimateError();
                    score.put(fp, depth.get(candidate) + 1 + estimate);
                    toVisit.add(fp);
                }
            }
        }
        return null;
    }

    public List<Puzzle> dijkstraSolve() {
        Queue<Puzzle> toVisit = new LinkedList<Puzzle>();
        HashMap<Puzzle,Puzzle> predecessor = new HashMap<Puzzle,Puzzle>();
        toVisit.add(this);
        predecessor.put(this, null);
        int cnt = 0;
        while( toVisit.size() > 0) {
            Puzzle candidate = toVisit.remove();
            cnt++;
            if( candidate.isSolved() ) {
                LinkedList<Puzzle> solution = new LinkedList<Puzzle>();
                Puzzle backtrace=candidate;
                while( backtrace != null ) {
                    solution.addFirst(backtrace);
                    backtrace = predecessor.get(backtrace);
                }
                return solution;
            }
            for(Puzzle fp: candidate.allAdjacentPuzzles()) {
                if( !predecessor.containsKey(fp) ) {
                    predecessor.put(fp,candidate);
                    toVisit.add(fp);
                }
            }
        }
        return null;
    }

}