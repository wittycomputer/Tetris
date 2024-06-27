package test;

public class L extends Piece {
    public L(TetrisData data) {
        super(data);
        c[0] = 0;   r[0] = 0;  // 중심 블록
        c[1] = -1;  r[1] = 0;  // 왼쪽 블록
        c[2] = 1;   r[2] = 0;  // 오른쪽 블록
        c[3] = 1;   r[3] = 1;  // 아래 오른쪽 블록
    }

    public int getType() {
        return 5;
    }

    public int rotateType() {
        return 4;
    }
}
