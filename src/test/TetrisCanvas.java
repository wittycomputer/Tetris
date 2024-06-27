package test;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TetrisCanvas extends JPanel implements Runnable, KeyListener {

    protected Thread worker;
    protected Color colors[];
    protected int w = 25;
    protected TetrisData data;
    protected int margin = 20;
    protected boolean stop, makeNew;
    protected Piece current, nextPiece; // 다음 블록을 저장할 변수 추가
    protected int interval = 2000;
    protected int level = 2;
    protected int score = 0; // 점수 변수 추가
    protected int bombs = 3; // 폭탄 아이템 개수 추가
    protected HighScore highScore;
    protected JFrame parentFrame; // 부모 프레임

    public TetrisCanvas(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        data = new TetrisData();
        highScore = new HighScore();
        addKeyListener(this);
        colors = new Color[8]; // 테트리스 배경 및 조각 색
        colors[0] = new Color(80, 80, 80); // 배경색 (검정)
        colors[1] = new Color(255, 0, 0); // 빨간색
        colors[2] = new Color(0, 255, 0); // 녹색
        colors[3] = new Color(0, 200, 255); // 노란색
        colors[4] = new Color(255, 255, 0); // 하늘색
        colors[5] = new Color(255, 150, 0); // 황토색
        colors[6] = new Color(210, 0, 240); // 보라색
        colors[7] = new Color(40, 0, 240); // 파란색
    }

    public void start() { // 게임 시작
        data.clear();
        worker = new Thread(this);
        worker.start();
        makeNew = true;
        stop = false;
        score = 0; // 점수 초기화
        bombs = 3; // 폭탄 개수 초기화
        current = getRandomPiece(); // 첫 번째 블록 설정
        nextPiece = getRandomPiece(); // 다음 블록 설정
        requestFocus();
        repaint();
    }

    public void stop() {
        stop = true;
        current = null;
    }

    public void paint(Graphics g) {
        super.paint(g);
        // 쌓인 조각들 그리기
        for (int i = 0; i < TetrisData.ROW; i++) {
            for (int k = 0; k < TetrisData.COL; k++) {
                if (data.getAt(i, k) == 0) {
                    g.setColor(colors[data.getAt(i, k)]);
                    g.draw3DRect(margin / 2 + w * k, margin / 2 + w * i, w, w, true);
                } else {
                    g.setColor(colors[data.getAt(i, k)]);
                    g.fill3DRect(margin / 2 + w * k, margin / 2 + w * i, w, w, true);
                }
            }
        }
        // 현재 내려오고 있는 테트리스 조각 그리기
        if (current != null) {
            for (int i = 0; i < 4; i++) {
                g.setColor(colors[current.getType()]);
                g.fill3DRect(margin / 2 + w * (current.getX() + current.c[i]), margin / 2 + w * (current.getY() + current.r[i]), w, w, true);
            }
        }
        // 다음에 나올 테트리스 조각 그리기
        if (nextPiece != null) {
            g.drawString("Next:", TetrisData.COL * w + margin, margin);
            for (int i = 0; i < 4; i++) {
                g.setColor(colors[nextPiece.getType()]);
                g.fill3DRect(TetrisData.COL * w + margin + w * (nextPiece.c[i] + 1), margin + w * (nextPiece.r[i] + 1), w, w, true);
            }
        }
        // 점수 표시
        g.setColor(Color.BLUE); // 점수 색상을 파란색으로 고정
        g.setFont(new Font("Arial", Font.BOLD, 20)); // 점수 표시를 더 크게
        g.drawString("Score: " + score, TetrisData.COL * w + margin, margin + 100);

        // 폭탄 개수 표시
        g.drawString("Bombs: " + bombs, TetrisData.COL * w + margin, margin + 150);
    }

    public Dimension getPreferredSize() { // 테트리스 판 크기 결정
        int tw = w * (TetrisData.COL + 5) + margin; // 오른쪽에 공간 추가
        int th = w * TetrisData.ROW + margin;
        return new Dimension(tw, th);
    }

    public void run() {
        while (!stop) {
            try {
                if (makeNew) { // 새로운 테트리스 조각 만들기
                    current = nextPiece; // 다음 조각을 현재 조각으로 설정
                    nextPiece = getRandomPiece(); // 새로운 다음 조각 생성
                    makeNew = false;
                } else { // 현재 만들어진 테트리스 조각 아래로 이동
                    if (current.moveDown()) {
                        makeNew = true;
                        if (current.copy()) {
                            stop();
                            showGameOverDialog(); // 게임 오버 다이얼로그 표시
                        }
                        current = null;
                    }
                    int linesCleared = data.removeLines();
                    if (linesCleared > 0) {
                        updateScore(linesCleared);
                    }
                }
                repaint();
                Thread.sleep(interval / level);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 점수 업데이트 메서드
    private void updateScore(int linesCleared) {
        int pointsPerLine = 100;
        score += linesCleared * pointsPerLine;
    }

    // 폭탄 사용 메서드
    private void useBomb() {
        if (bombs > 0) {
            int linesCleared = data.removeBottomLines(3); // 맨 아래 3개의 줄 제거
            if (linesCleared > 0) {
                updateScore(linesCleared);
            }
            bombs--; // 폭탄 개수 감소
            repaint();
        }
    }

    // 게임 설명 다이얼로그 표시 메서드
    public void showGameInstructions() {
        JDialog dialog = new JDialog(parentFrame, "게임 설명", true);
        dialog.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText(
            "게임 조작법:\n\n" +
            "방향키 왼쪽: 블록을 왼쪽으로 이동\n" +
            "방향키 오른쪽: 블록을 오른쪽으로 이동\n" +
            "방향키 위쪽: 블록을 회전\n" +
            "방향키 아래쪽: 블록을 빠르게 내리기\n" +
            "스페이스바: 블록을 즉시 아래로 내리기\n" +
            "B 키: 폭탄 사용 (맨 아래 3줄 제거)\n\n" +
            "폭탄 아이템 효과:\n" +
            "폭탄을 사용하면 맨 아래 3개의 줄이 제거되고 점수를 획득합니다.\n"
        );

        dialog.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton okButton = new JButton("확인");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(okButton, BorderLayout.SOUTH);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    // 게임 오버 다이얼로그 표시
    private void showGameOverDialog() {
        JDialog dialog = new JDialog(parentFrame, "Game Over", true);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel nameLabel = new JLabel("닉네임 입력:");
        JTextField nameField = new JTextField();

        JButton submitButton = new JButton("점수 제출");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                highScore.addScore(name, score);
                dialog.dispose();
                showHighScores();
            }
        });

        JButton retryButton = new JButton("다시하기");
        retryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                start(); // 게임 다시 시작
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(submitButton);
        panel.add(retryButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    // 점수판 표시
    public void showHighScores() { 
        JDialog dialog = new JDialog(parentFrame, "High Scores", true);
        dialog.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        ArrayList<HighScore.Score> scores = highScore.getScores();
        for (int i = 0; i < scores.size(); i++) {
            HighScore.Score score = scores.get(i);
            textArea.append((i + 1) + ". " + score.getName() + ": " + score.getScore() + "\n");
        }

        dialog.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(okButton, BorderLayout.SOUTH);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    // 랜덤 테트리스 조각 생성 메서드
    private Piece getRandomPiece() {
        int random = (int) (Math.random() * 7); // 7개의 조각 중 하나 선택
        switch (random) {
            case 0: return new Bar(data);
            case 1: return new Tee(data);
            case 2: return new EI(data);
            case 3: return new J(data); // J 조각 추가
            case 4: return new L(data); // L 조각 추가
            case 5: return new O(data); // O 조각 추가
            case 6: return new S(data); // S 조각 추가
            case 7: return new Z(data); // Z 조각 추가
        }
        return new Bar(data); // 기본적으로 Bar 조각 반환
    }

    // 키보드를 이용해서 테트리스 조각 이동
    public void keyPressed(KeyEvent e) {
        if (current == null) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: // 왼쪽 화살표
                current.moveLeft();
                repaint();
                break;
            case KeyEvent.VK_RIGHT: // 오른쪽 화살표
                current.moveRight();
                repaint();
                break;
            case KeyEvent.VK_UP: // 위쪽 화살표
                current.rotate();
                repaint();
                break;
            case KeyEvent.VK_DOWN: // 아래쪽 화살표
                boolean temp = current.moveDown();
                if (temp) {
                    makeNew = true;
                    if (current.copy()) {
                        stop();
                        showGameOverDialog(); // 게임 오버 다이얼로그 표시
                    }
                }
                int linesCleared = data.removeLines();
                if (linesCleared > 0) {
                    updateScore(linesCleared);
                }
                repaint();
                break;
            case KeyEvent.VK_SPACE: // 스페이스바를 눌렀을 때
                while (!current.moveDown()) {}
                makeNew = true;
                if (current.copy()) {
                    stop();
                    showGameOverDialog(); // 게임 오버 다이얼로그 표시
                }
                int linesClearedInstant = data.removeLines();
                if (linesClearedInstant > 0) {
                    updateScore(linesClearedInstant);
                }
                repaint();
                break;
            case KeyEvent.VK_B: // B 키를 눌렀을 때 폭탄 사용
                useBomb();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}
}
