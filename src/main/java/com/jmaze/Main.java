package com.jmaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        drawFrame();
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

    protected void drawFrame() {
        if (Cell.cells.isEmpty()) {
            return;
        }

        for (Cell cell : Cell.cells) {
            if (cell.open) {
                graphics.setColor(Color.WHITE);
                graphics.fillRect(cell.position.x * CELL_SIZE + offsetX, cell.position.y * CELL_SIZE + offsetY, CELL_SIZE, CELL_SIZE);
            }
            graphics.setColor(Color.GRAY);
            graphics.drawRect(cell.position.x * CELL_SIZE + offsetX, cell.position.y * CELL_SIZE + offsetY, CELL_SIZE, CELL_SIZE);
        }
    }

    private Point GetClickedCell(Point ClickPos) {
        int CellPosX = ((ClickPos.x - offsetX) / CELL_SIZE) * CELL_SIZE;
        int CellPosY = ((ClickPos.y - offsetY) / CELL_SIZE) * CELL_SIZE;
        return new Point(CellPosX, CellPosY);
    }

    private void RenderOpenCells(Point ClickPos) {
        Point clickedCell = GetClickedCell(ClickPos);
        System.out.println(clickedCell);
        for (Cell cell : Cell.cells) {
            if (cell.getRealPos().equals(clickedCell)) {
                cell.open = true;
            }
        }
        repaint();
    }
}
