package test;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MyTetris extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private TetrisCanvas tetrisCanvas; // TetrisCanvas 객체 추가

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MyTetris frame = new MyTetris();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MyTetris() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 449, 600);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnNewMenu = new JMenu("게임");
        menuBar.add(mnNewMenu);

        JMenuItem mntmNewMenuItem = new JMenuItem("시작");
        mnNewMenu.add(mntmNewMenuItem);

        JMenuItem mntmNewMenuItem_1 = new JMenuItem("종료");
        mnNewMenu.add(mntmNewMenuItem_1);
        
        JMenuItem mntmNewMenuItem_2 = new JMenuItem("최고점수");
        mnNewMenu.add(mntmNewMenuItem_2);
        
        JMenuItem mntmNewMenuItem_3 = new JMenuItem("게임 설명");
        mnNewMenu.add(mntmNewMenuItem_3);

        mntmNewMenuItem_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null); // Absolute layout 사용
        setContentPane(contentPane);

        // TetrisCanvas 초기화 및 추가
        tetrisCanvas = new TetrisCanvas(this);
        tetrisCanvas.setBounds(12, 10, 409, 540); // 위치 및 크기 설정
        contentPane.add(tetrisCanvas);

        // 시작 메뉴 항목에 ActionListener 추가
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tetrisCanvas.start(); // 게임 시작
            }
        });

        // 최고 점수 메뉴 항목에 ActionListener 추가
        mntmNewMenuItem_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tetrisCanvas.showHighScores(); // 최고 점수 표시
            }
        });

        // 게임 설명 메뉴 항목에 ActionListener 추가
        mntmNewMenuItem_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tetrisCanvas.showGameInstructions(); // 게임 설명 표시
            }
        });
    }
}
