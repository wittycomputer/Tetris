package test;

public class TetrisData {
    public static final int ROW = 20;
    public static final int COL = 10;

    private int data[][];
    private int line;

    public TetrisData() {
        data = new int[ROW][COL];
    }

    public int getAt(int x, int y) {
        if (x < 0 || x >= ROW || y < 0 || y >= COL)
            return 0;
        return data[x][y];
    }

    public void setAt(int x, int y, int v) {
        data[x][y] = v;
    }

    public int getLine() {
        return line;
    }

    public synchronized int removeLines() {
        int linesRemoved = 0;
        NEXT: for (int i = ROW - 1; i >= 0; i--) {
            boolean done = true;
            for (int k = 0; k < COL; k++) {
                if (data[i][k] == 0) {
                    done = false;
                    continue NEXT;
                }
            }
            if (done) {
                linesRemoved++;
                line++;
                for (int x = i; x > 0; x--) {
                    for (int y = 0; y < COL; y++) {
                        data[x][y] = data[x - 1][y];
                    }
                }
                for (int y = 0; y < COL; y++) {
                    data[0][y] = 0;
                }
                i++; // recheck this row after shifting
            }
        }
        return linesRemoved;
    }

    public synchronized int removeBottomLines(int n) {
        int linesRemoved = 0;
        for (int i = 0; i < n; i++) {
            for (int y = 0; y < COL; y++) {
                data[ROW - 1 - i][y] = 0;
            }
            linesRemoved++;
        }
        for (int i = ROW - 1 - n; i >= 0; i--) {
            for (int y = 0; y < COL; y++) {
                data[i + n][y] = data[i][y];
            }
        }
        for (int i = 0; i < n; i++) {
            for (int y = 0; y < COL; y++) {
                data[i][y] = 0;
            }
        }
        return linesRemoved;
    }

    public void clear() { // data 배열 초기화
        for (int i = 0; i < ROW; i++) {
            for (int k = 0; k < COL; k++) {
                data[i][k] = 0;
            }
        }
    }

    public void dump() { // 배열 내용 출력
        for (int i = 0; i < ROW; i++) {
            for (int k = 0; k < COL; k++) {
                System.out.print(data[i][k] + " ");
            }
            System.out.println();
        }
    }
}
