package com.jmaze;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Cell {

    protected static Set<Cell> cells = new HashSet<>();
    protected static Main main;
    private static final int size = main.CELL_SIZE;
    private static final int field_height = main.FIELD_HEIGHT;
    private static final int field_width = main.FIELD_WIDTH;

    protected Point position;
    protected boolean open = false;
    protected int OpenNeighbors;
    protected int ClosedNeighbors = 8 - OpenNeighbors;

    protected Cell create(Point position, int OpenNeighbors, boolean open) {
        this.position = position;
        this.open = open;
        this.OpenNeighbors = OpenNeighbors;
        return this;
    }

    protected Point getRealPos() {
        return new Point(position.x * size, position.y * size);
    }

    protected boolean getState() {
        return open;
    }

    protected static void initHashSets() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < (field_height * field_width) - 1; i++) { //NOTE: -1 bc 0,0
            if (x < field_width) {
                cells.add(new Cell().create(new Point(x, y), 0, false));
                x++;
            } else {
                x = 0;
                y++;
            }
        }
        main.repaint();
    }
}
