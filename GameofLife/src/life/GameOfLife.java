package life;

public class GameOfLife {

    private final int rows;
    private final int cols;
    private boolean[][] board;

    public GameOfLife(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new boolean[rows][cols];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isAlive(int r, int c) {
        return board[r][c];
    }

    public void toggle(int r, int c) {
        board[r][c] = !board[r][c];
    }

    public void step() {
        boolean[][] next = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                int neighbors = countNeighbors(r, c);

                if (board[r][c]) {      // cell is alive
                    next[r][c] = (neighbors == 2 || neighbors == 3);
                } else {               // cell is dead
                    next[r][c] = (neighbors == 3);
                }
            }
        }

        board = next;
    }

    private int countNeighbors(int r, int c) {
        int count = 0;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {

                if (dr == 0 && dc == 0) continue;

                int nr = r + dr;
                int nc = c + dc;

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    if (board[nr][nc]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    public void randomize(double probability) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = Math.random() < probability;
            }
        }
    }

}
