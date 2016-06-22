package pers.crazymouse.algorithm.std;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Stack;

/**
 * Created by crazymouse on 6/21/16.
 */
public class Maze {
    public static final int WALL = 1;
    public static final int BLANK = 0;
    public static final int OCC = -1;

    private SimpleIntegerProperty map[][];
    private Mouse begin;
    private Mouse end;
    private Stack<Mouse> stack = new Stack<>();
    private Stack<Stack<Integer>> turnStack = new Stack<>(); //TODO record path

    /**
     * Initialize the size of maze.
     * Initialize the begin and end Mouse for (0, 0)
     *
     * @param width  set width of maze
     * @param height set height of maze
     */
    public Maze(int width, int height) {
        map = new SimpleIntegerProperty[width][height];
        begin = new Mouse(0, 0);
        end = new Mouse(0, 0);
    }

    public void setMap(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                this.map[i][j] = new SimpleIntegerProperty(map[i][j]);
            }
        }
    }

    public void setMap(SimpleIntegerProperty[][] map) {
        this.map = map;
    }

    public SimpleIntegerProperty[][] getMap() {
        return this.map;
    }

    public void searchPath(int beginX, int beginY, int endX, int endY) {
        searchPath(new Mouse(beginX, beginY), new Mouse(endX, endY));
    }

    public void searchPath() {
        searchPath(begin, end);
    }

    public int getValue(int x, int y) {
        try {
            return map[x][y].getValue();
        } catch (ArrayIndexOutOfBoundsException ex) {
            return WALL;
        } catch (NullPointerException ex) {
            return WALL;
        }
    }

    public int getValue(Mouse e) {
        return getValue(e.x, e.y);
    }

    private void searchPath(Mouse begin, Mouse end) {
        setBegin(begin);
        setEnd(end);
        while (singleStep()) ;
    }

    public void setBegin(Mouse begin) {
        this.begin = begin;
        init();
    }

    public void setBegin(int x, int y) {
        setBegin(new Mouse(x, y));
    }

    public void setEnd(Mouse end) {
        this.end = end;
    }

    public void setEnd(int x, int y) {
        setEnd(new Mouse(x, y));
    }

    private void init() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j].getValue() == OCC)
                    map[i][j].setValue(BLANK);
            }
        }
        stack.clear();
        stack.add(begin);
    }

    public boolean singleStep() {
        if (!stack.empty()) {
            Mouse p = stack.peek();
            map[p.x][p.y].setValue(OCC);
            if (p.equals(end)) {
                p.turn.clear();
            }
            if (!p.hasChoice()) {
                map[p.x][p.y].setValue(BLANK);
                stack.pop();
            } else {
                Mouse np = p.Turn();
                if (getValue(np) == BLANK)
                    stack.add(np);
            }
        }
        return !stack.empty();
    }

    class Mouse implements Comparable<Mouse> {
        int x;
        int y;
        Stack<Integer> turn = new Stack<>();

        Mouse() {
            this(0, 0);
        }

        Mouse(int x, int y) {
            this.x = x;
            this.y = y;
            for (int i = 0; i < 4; i++)
                turn.add(i);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }

        @Override
        public int compareTo(Mouse o) {
            if (x - o.x == 0)
                return y - o.y;
            else
                return x - o.x;
        }

        @Override
        public boolean equals(Object obj) {
            return compareTo((Mouse) obj) == 0;
        }

        /**
         * Test whether there is any direction can walk
         *
         * @return <code>true</code> there are way; <code>false</code> no way.
         */
        boolean hasChoice() {
            return !turn.empty();
        }

        /**
         * @return
         */
        Mouse Turn() {
            Mouse e = new Mouse();
            if (hasChoice()) {
                /**
                 * It's a trick :)
                 */
                e.x = x + (turn.peek() - 1) % 2;
                e.y = y + (turn.peek() - 2) % 2;
                e.turn.remove((turn.pop() + 2) % 4);
            } else {
                e = null;
            }
            return e;
        }
    }

}
