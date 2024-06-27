package test;

public class O extends Piece {
    public O(TetrisData data) {
        super(data);
        c[0] = 0;   r[0] = 0;  // 중심 블록
        c[1] = 1;   r[1] = 0;  // 오른쪽 블록
        c[2] = 0;   r[2] = 1;  // 아래 블록
        c[3] = 1;   r[3] = 1;  // 오른쪽 아래 블록
    }

    public int getType() {
        return 6;
    }

    public int rotateType() {
        return 1;  // 회전하지 않는 조각
    }

    public void rotate() {
        // O 조각은 회전하지 않음
    }
}
