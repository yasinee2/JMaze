package com.jmaze;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Cell {

    protected static Set<Point> cells = new HashSet<>();
    protected static Set<Point> OpenCells = new HashSet<>();
    protected static Main main;
    private static final int size = main.CELL_SIZE;
    private static final int field_height = main.FIELD_HEIGHT;
    private static final int field_width = main.FIELD_WIDTH;
    protected static int OpenCellsCount = 0;

    protected Point position;
    protected boolean open = false;
    protected Set<Cell> OpenNeighbors = new HashSet<>();

    protected void open(Point position) {
        OpenCells.add(position);
        OpenCellsCount++;
    }

    protected static void initHashSets() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < (field_height * field_width) - 1; i++) { //NOTE: -1 bc 0,0
            if (x < field_width) {
                cells.add(new Point(x * size, y * size));
                x++;
            } else {
                x = 0;
                y++;
            }
        }
        main.repaint();
    }
}
