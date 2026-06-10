package com.jmaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {

    private Graphics graphics;

    protected final static int FIELD_WIDTH = 50;
    protected final static int FIELD_HEIGHT = 50;
    protected final static int CELL_SIZE = 20;
    private final int SPEED = 5;

    private int offsetX;
    private int offsetY;
    private Point startCell;
    private Point GenHead;

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
        if (startCell != null) {
            graphics.fillRect(startCell.x + offsetX, startCell.y + offsetY, CELL_SIZE, CELL_SIZE);
            graphics.setColor(Color.black);
            graphics.fillRect(GenHead.x + offsetX, GenHead.y + offsetY, CELL_SIZE, CELL_SIZE);
        }
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
            graphics.setColor(Color.gray);
            graphics.fillRect(cell.x + offsetX, cell.y + offsetY, CELL_SIZE, CELL_SIZE);
            graphics.setColor(Color.darkGray);
            graphics.drawRect(cell.x + offsetX, cell.y + offsetY, CELL_SIZE, CELL_SIZE);
        }
        for (Point cell : Cell.OpenCells) {
            graphics.setColor(Color.darkGray);
            graphics.fillRect(cell.x + offsetX, cell.y + offsetY, CELL_SIZE, CELL_SIZE);
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
        Point clickedCell = GetClickedCell(ClickPos);
        if (Cell.OpenCellsCount >= 1) {
            return;
        }
        if (clickedCell == null) {
            return;
        }

        if (Cell.cells.contains(clickedCell)) {
            Cell.OpenCellsCount++;
            startCell = clickedCell;
            final Point finalStart = startCell;
            new Thread(() -> Generate(clickedCell, finalStart)).start();
        }
        repaint();
    }

    private Set<Point> GetClosedNeighbors(Point base) {
        Set<Point> neighbors = new HashSet<>();
        //NOTE: checks left
        if (Cell.cells.contains(new Point(base.x - CELL_SIZE * 2, base.y))) {
            if (!Cell.OpenCells.contains(new Point(base.x - CELL_SIZE * 2, base.y))) {
                neighbors.add(new Point(base.x - CELL_SIZE * 2, base.y));
            }
        }
        //NOTE: checks right
        if (Cell.cells.contains(new Point(base.x + CELL_SIZE * 2, base.y))) {
            if (!Cell.OpenCells.contains(new Point(base.x + CELL_SIZE * 2, base.y))) {
                neighbors.add(new Point(base.x + CELL_SIZE * 2, base.y));
            }
        }
        //NOTE: checks below
        if (Cell.cells.contains(new Point(base.x, base.y - CELL_SIZE * 2))) {
            if (!Cell.OpenCells.contains(new Point(base.x, base.y - CELL_SIZE * 2))) {
                neighbors.add(new Point(base.x, base.y - CELL_SIZE * 2));
            }
        }
        //NOTE: checks above
        if (Cell.cells.contains(new Point(base.x, base.y + CELL_SIZE * 2))) {
            if (!Cell.OpenCells.contains(new Point(base.x, base.y + CELL_SIZE * 2))) {
                neighbors.add(new Point(base.x, base.y + CELL_SIZE * 2));
            }
        }
        return neighbors;
    }

    private void Generate(Point start, Point finish) {

        Deque<Point> path = new ArrayDeque<>();
        Set<Point> visited = new HashSet<>();

        path.push(start);
        visited.add(start);
        Cell.OpenCells.add(start);

        while (!path.isEmpty()) {
            Point current = path.peek();
            GenHead = path.getFirst();

            List<Point> neighbors = new ArrayList<>(GetClosedNeighbors(current));
            neighbors.removeIf(visited::contains);

            if (neighbors.isEmpty()) {
                path.pop();
            } else {
                int r = random(0, neighbors.size() - 1);
                Point chosen = neighbors.get(r);

                visited.add(chosen);
                path.push(chosen);
                Cell.OpenCells.add(chosen);

                int dx = current.x - chosen.x;
                int dy = current.y - chosen.y;
                Point middle = new Point(chosen.x + dx / 2, chosen.y + dy / 2);
                Cell.OpenCells.add(middle);

            }

            repaint();
            try {
                Thread.sleep(SPEED);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        repaint();
    }

    private int random(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
