package com.jmaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {

    private Graphics graphics;

    protected final static int FIELD_WIDTH = 50;
    protected final static int FIELD_HEIGHT = 50;
    protected final static int CELL_SIZE = 20;

    private int offsetX;
    private int offsetY;
    private boolean FieldInitialed = false;
    private Cell startCell;
    private Cell endCell;

    public static void main(String[] args) {
        initWindow();
    }

    public Main() {
        initListeners();
        Cell.main = this;
        Cell.initHashSets();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        this.graphics = graphics;
        super.paintComponent(graphics);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        offsetX = (getWidth() / 2) - (CELL_SIZE * FIELD_WIDTH) / 2;
        offsetY = (getHeight() / 2) - (CELL_SIZE * FIELD_HEIGHT) / 2;
        drawField();
    }

    private void initListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                RenderOpenCells(new Point(e.getX(), e.getY()));
            }
        });
    }

    private static void initWindow() {
        JFrame frame = new JFrame();
        frame.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
        frame.add(new Main());
        frame.setTitle("JMaze");
        frame.setVisible(true);
    }

    protected void drawField() {
        if (Cell.cells.isEmpty()) {
            return;
        }

        for (Point cell : Cell.cells) {
            graphics.setColor(Color.GRAY);
            graphics.drawRect(cell.x + offsetX, cell.y + offsetY, CELL_SIZE, CELL_SIZE);
        }
        for (Point cell : Cell.OpenCells) {
            graphics.setColor(Color.GRAY);
            graphics.fillRect(cell.x + offsetX, cell.y + offsetY, CELL_SIZE, CELL_SIZE);
            graphics.setColor(Color.WHITE);
            //graphics.drawString(cell.x + ", " + cell.y, cell.x + offsetX, cell.y + offsetY);
        }
    }

    private Point GetClickedCell(Point ClickPos) {
        int CellPosX = ((ClickPos.x - offsetX) / CELL_SIZE) * CELL_SIZE;
        int CellPosY = ((ClickPos.y - offsetY) / CELL_SIZE) * CELL_SIZE;
        Point output = new Point(CellPosX, CellPosY);
        if (Cell.cells.contains(output)) {
            return output;
        } else {
            return null;
        }
    }

    private void RenderOpenCells(Point ClickPos) {
        Point startPoint = new Point();
        Point clickedCell = GetClickedCell(ClickPos);
        System.out.println(clickedCell);
        if (Cell.OpenCellsCount >= 2) {
            return;
        }
        if (clickedCell == null) {
            return;
        }
        if (Cell.OpenCellsCount == 1) {
            startPoint = clickedCell;
        }
        if (Cell.cells.contains(clickedCell)) {
            Cell.OpenCells.add(clickedCell);
            Cell.OpenCellsCount++;
        }
        if (Cell.OpenCellsCount == 2) {
            Generate(startPoint, clickedCell);
        }
        repaint();
    }

    private Set<Point> GetClosedNeighbors(Point base) {
        Set<Point> neighbors = new HashSet<>();
        //NOTE: checks left
        if (Cell.cells.contains(new Point(base.x - CELL_SIZE * 2, base.y))) {
            if (!Cell.OpenCells.contains(new Point(base.x - CELL_SIZE * 2, base.y))) {
                neighbors.add(new Point(base.x - CELL_SIZE * 2, base.y));
                System.out.println("left");
            }
        }
        //NOTE: checks right
        if (Cell.cells.contains(new Point(base.x + CELL_SIZE * 2, base.y))) {
            if (!Cell.OpenCells.contains(new Point(base.x + CELL_SIZE * 2, base.y))) {
                neighbors.add(new Point(base.x + CELL_SIZE * 2, base.y));
                System.out.println("right");
            }
        }
        //NOTE: checks below
        if (Cell.cells.contains(new Point(base.x, base.y - CELL_SIZE * 2))) {
            if (!Cell.OpenCells.contains(new Point(base.x, base.y - CELL_SIZE * 2))) {
                neighbors.add(new Point(base.x, base.y - CELL_SIZE * 2));
                System.out.println("below");
            }
        }
        //NOTE: checks above
        if (Cell.cells.contains(new Point(base.x, base.y + CELL_SIZE * 2))) {
            if (!Cell.OpenCells.contains(new Point(base.x, base.y + CELL_SIZE * 2))) {
                neighbors.add(new Point(base.x, base.y + CELL_SIZE * 2));
                System.out.println("above");
            }
        }
        System.out.println(neighbors);
        return neighbors;
    }

    private void Generate(Point start, Point finish) {
        Point[] neighborArray = new Point[4];
        int index = 0;
        for (Point neighbor : GetClosedNeighbors(start)) {
            System.out.println(index);
            neighborArray[index] = neighbor;
            index++;
        }
        int random = random(0, neighborArray.length - 1);
        if (neighborArray.length != 0) {
            Cell.OpenCells.add(neighborArray[random]);

            int dx = start.x - neighborArray[random].x;
            int dy = start.y - neighborArray[random].y;

            Point middle = new Point(neighborArray[random].x + dx / 2, neighborArray[random].y + dy / 2);
            Cell.OpenCells.add(middle);
            System.out.println("middle: " + middle);
        }

        repaint();
    }

    private int random(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
