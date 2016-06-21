package pers.crazymouse.algorithm.std;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Stack;

/**
 * Created by crazymouse on 6/21/16.
 */
public class Maze {
    static final int WALL = 1;
    static final int BLANK = 0;
    static final int OCC = -1;
    private SimpleIntegerProperty map[][];

    public Maze(int width, int height) {
        map = new SimpleIntegerProperty[width][height];
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
        int count = 0;
        Stack<Mouse> stack = new Stack<>();
        stack.add(begin);
        while (!stack.empty()) {
            Mouse p = stack.peek();
            map[p.x][p.y].setValue(OCC);
            if (p.equals(end)) {
                p.turn.clear();
                System.out.println(++count);
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
