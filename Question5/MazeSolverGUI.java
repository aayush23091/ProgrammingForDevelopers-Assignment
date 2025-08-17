package Question5;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Cell {
    int row, col;
    boolean visited = false;
    boolean[] walls = {true, true, true, true}; // top, right, bottom, left
    Cell parent = null;

    Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

public class MazeSolverGUI extends JFrame {
    private final int rows = 20, cols = 20, cellSize = 30;
    private Cell[][] maze;
    private java.util.List<Cell> path = new ArrayList<>();
    private JButton dfsButton, bfsButton, resetButton;

    public MazeSolverGUI() {
        setTitle("Maze Solver GUI");
        setSize(cols * cellSize + 20, rows * cellSize + 70);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        dfsButton = new JButton("Solve with DFS");
        bfsButton = new JButton("Solve with BFS");
        resetButton = new JButton("Reset Maze");

        buttonPanel.add(dfsButton);
        buttonPanel.add(bfsButton);
        buttonPanel.add(resetButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Generate solvable maze
        generateMaze();

        dfsButton.addActionListener(e -> solveDFS());
        bfsButton.addActionListener(e -> solveBFS());
        resetButton.addActionListener(e -> {
            generateMaze();
            path.clear();
            repaint();
        });
    }

    private void generateMaze() {
        maze = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                maze[r][c] = new Cell(r, c);
            }
        }

        // Recursive backtracking generator
        Stack<Cell> stack = new Stack<>();
        Cell start = maze[0][0];
        start.visited = true;
        stack.push(start);

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            Cell next = getUnvisitedNeighbor(current);

            if (next != null) {
                removeWalls(current, next);
                next.visited = true;
                stack.push(next);
            } else {
                stack.pop();
            }
        }

        // Reset visited flags for solving
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                maze[r][c].visited = false;
    }

    private Cell getUnvisitedNeighbor(Cell cell) {
        java.util.List<Cell> neighbors = new ArrayList<>();
        int r = cell.row, c = cell.col;

        if (r > 0 && !maze[r - 1][c].visited) neighbors.add(maze[r - 1][c]);
        if (c < cols - 1 && !maze[r][c + 1].visited) neighbors.add(maze[r][c + 1]);
        if (r < rows - 1 && !maze[r + 1][c].visited) neighbors.add(maze[r + 1][c]);
        if (c > 0 && !maze[r][c - 1].visited) neighbors.add(maze[r][c - 1]);

        if (neighbors.isEmpty()) return null;
        return neighbors.get(new Random().nextInt(neighbors.size()));
    }

    private void removeWalls(Cell a, Cell b) {
        int dx = a.col - b.col;
        int dy = a.row - b.row;

        if (dx == 1) { // b is left of a
            a.walls[3] = false;
            b.walls[1] = false;
        } else if (dx == -1) { // b is right of a
            a.walls[1] = false;
            b.walls[3] = false;
        } else if (dy == 1) { // b is above a
            a.walls[0] = false;
            b.walls[2] = false;
        } else if (dy == -1) { // b is below a
            a.walls[2] = false;
            b.walls[0] = false;
        }
    }

    private void solveDFS() {
        resetVisited();
        path.clear();

        Stack<Cell> stack = new Stack<>();
        Cell start = maze[0][0];
        Cell end = maze[rows - 1][cols - 1];

        stack.push(start);
        start.visited = true;

        while (!stack.isEmpty()) {
            Cell current = stack.pop();

            if (current == end) break;

            for (Cell neighbor : getNeighbors(current)) {
                if (!neighbor.visited) {
                    neighbor.visited = true;
                    neighbor.parent = current;
                    stack.push(neighbor);
                }
            }
        }

        buildPath(end);
    }

    private void solveBFS() {
        resetVisited();
        path.clear();

        Queue<Cell> queue = new LinkedList<>();
        Cell start = maze[0][0];
        Cell end = maze[rows - 1][cols - 1];

        queue.add(start);
        start.visited = true;

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            if (current == end) break;

            for (Cell neighbor : getNeighbors(current)) {
                if (!neighbor.visited) {
                    neighbor.visited = true;
                    neighbor.parent = current;
                    queue.add(neighbor);
                }
            }
        }

        buildPath(end);
    }

    private void buildPath(Cell end) {
        if (!end.visited) {
            JOptionPane.showMessageDialog(this, "No path found!");
            repaint();
            return;
        }
        for (Cell c = end; c != null; c = c.parent)
            path.add(c);
        Collections.reverse(path);
        repaint();
    }

    private void resetVisited() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                maze[r][c].visited = false;
                maze[r][c].parent = null;
            }
    }

    private java.util.List<Cell> getNeighbors(Cell cell) {
        java.util.List<Cell> neighbors = new ArrayList<>();
        int r = cell.row, c = cell.col;

        if (!cell.walls[0] && r > 0) neighbors.add(maze[r - 1][c]); // top
        if (!cell.walls[1] && c < cols - 1) neighbors.add(maze[r][c + 1]); // right
        if (!cell.walls[2] && r < rows - 1) neighbors.add(maze[r + 1][c]); // bottom
        if (!cell.walls[3] && c > 0) neighbors.add(maze[r][c - 1]); // left

        return neighbors;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.translate(10, 40);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = maze[r][c];
                int x = c * cellSize;
                int y = r * cellSize;

                g.setColor(Color.WHITE);
                g.fillRect(x, y, cellSize, cellSize);

                g.setColor(Color.BLACK);
                if (cell.walls[0]) g.drawLine(x, y, x + cellSize, y); // top
                if (cell.walls[1]) g.drawLine(x + cellSize, y, x + cellSize, y + cellSize); // right
                if (cell.walls[2]) g.drawLine(x, y + cellSize, x + cellSize, y + cellSize); // bottom
                if (cell.walls[3]) g.drawLine(x, y, x, y + cellSize); // left
            }
        }

        // Draw path
        g.setColor(Color.RED);
        for (Cell c : path) {
            g.fillRect(c.col * cellSize + cellSize / 4, c.row * cellSize + cellSize / 4,
                    cellSize / 2, cellSize / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MazeSolverGUI().setVisible(true);
        });
    }
}
