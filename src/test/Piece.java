package test;

import java.awt.Point;

public abstract class Piece {
    final int DOWN = 0;
    final int LEFT = 1;
    final int RIGHT = 2;
    protected int r[];
    protected int c[];
    protected TetrisData data;
    protected Point center;

    public Piece(TetrisData data) {
        r = new int[4];
        c = new int[4];
        this.data = data;
        center = new Point(5, 0);
    }

    public abstract int getType();
    public abstract int rotateType();

    public int getX() {
        return center.x;
    }

    public int getY() {
        return center.y;
    }

    public boolean copy() {
        boolean value = false;
        int x = getX();
        int y = getY();
        if (getMinY() + y <= 0) { // 게임 종료 상황
            value = true;
        }
        for (int i = 0; i < 4; i++) {
            data.setAt(y + r[i], x + c[i], getType());
        }
        return value;
    }

    public boolean isOverlap(int dir) { // 다른 조각과 겹치는지 확인
        int x = getX();
        int y = getY();
        switch (dir) {
            case DOWN: // 아래
                for (int i = 0; i < r.length; i++) {
                    if (data.getAt(y + r[i] + 1, x + c[i]) != 0) {
                        return true;
                    }
                }
                break;
            case LEFT: // 왼쪽
                for (int i = 0; i < r.length; i++) {
                    if (data.getAt(y + r[i], x + c[i] - 1) != 0) {
                        return true;
                    }
                }
                break;
            case RIGHT: // 오른쪽
                for (int i = 0; i < r.length; i++) {
                    if (data.getAt(y + r[i], x + c[i] + 1) != 0) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public int getMinX() {
        int min = c[0];
        for (int i = 1; i < c.length; i++) {
            if (c[i] < min) {
                min = c[i];
            }
        }
        return min;
    }

    public int getMaxX() {
        int max = c[0];
        for (int i = 1; i < c.length; i++) {
            if (c[i] > max) {
                max = c[i];
            }
        }
        return max;
    }

    public int getMinY() {
        int min = r[0];
        for (int i = 1; i < r.length; i++) {
            if (r[i] < min) {
                min = r[i];
            }
        }
        return min;
    }

    public int getMaxY() {
        int max = r[0];
        for (int i = 1; i < r.length; i++) {
            if (r[i] > max) {
                max = r[i];
            }
        }
        return max;
    }

    public boolean moveDown() {
        if (center.y + getMaxY() + 1 < TetrisData.ROW) {
            if (!isOverlap(DOWN)) {
                center.y++;
            } else {
                return true;
            }
        } else {
            return true;
        }

        return false;
    }

    public void moveLeft() { // 왼쪽으로 이동
        if (center.x + getMinX() - 1 >= 0) {
            if (!isOverlap(LEFT)) {
                center.x--;
            } else {
                return;
            }
        }
    }

    public void moveRight() { // 오른쪽으로 이동
        if (center.x + getMaxX() + 1 < TetrisData.COL) {
            if (!isOverlap(RIGHT)) {
                center.x++;
            } else {
                return;
            }
        }
    }

    public void rotate() { // 조각 회전
        int rc = rotateType();
        if (rc <= 1) return;

        int[] tempR = new int[4];
        int[] tempC = new int[4];

        for (int i = 0; i < 4; i++) {
            tempR[i] = -c[i];
            tempC[i] = r[i];
        }

        // 벽 충돌 검사 및 조정
        for (int i = 0; i < 4; i++) {
            if (center.x + tempC[i] < 0) { // 왼쪽 벽을 벗어남
                center.x++;
            } else if (center.x + tempC[i] >= TetrisData.COL) { // 오른쪽 벽을 벗어남
                center.x--;
            }
            if (center.y + tempR[i] < 0) { // 상단을 벗어남
                center.y++;
            } else if (center.y + tempR[i] >= TetrisData.ROW) { // 하단을 벗어남
                center.y--;
            }
        }

        // 위치가 조정된 후 충돌 검사
        boolean valid = true;
        for (int i = 0; i < 4; i++) {
            if (data.getAt(center.y + tempR[i], center.x + tempC[i]) != 0) {
                valid = false;
                break;
            }
        }

        if (valid) {
            for (int i = 0; i < 4; i++) {
                r[i] = tempR[i];
                c[i] = tempC[i];
            }
        }
    }

    public void rotate4() {
        for (int i = 0; i < 4; i++) {
            int temp = c[i];
            c[i] = -r[i];
            r[i] = temp;
        }
    }
}
