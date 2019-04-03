//Isaac Lichter
package pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

public class Pong extends JFrame {

    public PongPanel panel;
    JButton startButton;
//    LinkedList<HighScore> highScoreList;
    boolean hasStarted;

    public static void main(String[] args) {
        Pong pong = new Pong("");
    }

    public Pong(String title) {

        panel = new PongPanel();
        panel.setSize(600, 400);
        add(panel, BorderLayout.CENTER);

        if(title.length() == 0){
            setTitle("Pong");
            panel.isOnePlayerGame = true;
        }
        else{
            setTitle(title);
            panel.isOnePlayerGame = false;
            if (title.equals("Client"))
                panel.delta = new Point(-12, 12);
        }
        setSize(800, 500);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel statusBar = new JLabel("Welcome to Pong!");
        add(statusBar, BorderLayout.SOUTH);

        startButton = new JButton();
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                panel.startGame();
            }
        });

//        try {
//
//            FileInputStream file = new FileInputStream("highScor.ser");
//            ObjectInputStream in = new ObjectInputStream(file);
//
//            highScoreList = (LinkedList<HighScore>) in.readObject();
//
//            in.close();
//            file.close();
//
//        } catch (FileNotFoundException e1) {
//            highScoreList = new LinkedList<HighScore>();
//            while (highScoreList.size() < 10) {
//                highScoreList.add(new HighScore());
//            }
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        } catch (ClassNotFoundException e1) {
//            e1.printStackTrace();
//        }

        startButton.setText("Start");
        startButton.setSize(this.getWidth(), 40);
        add(startButton, BorderLayout.SOUTH);

        JPanel scorePanel = new JPanel();
        JLabel player1Score = new JLabel("Player1: ");
        JLabel player2Score = new JLabel("Player2: ");

        Timer t = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                player1Score.setText("Player1: " + panel.getPlayerOneScore() + " ");
                player2Score.setText("Player2: " + panel.getPlayerTwoScore() + " ");
            }
        });
        t.start();

        scorePanel.setLayout(new GridLayout(1, 12));
        scorePanel.add(player1Score);
        scorePanel.add(player2Score);
//        for (HighScore score : highScoreList) {
//            scorePanel.add(new JLabel(score.getName() + ": " + score.getScore()));
//        }

        add(scorePanel, BorderLayout.NORTH);

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

//                try {
//                    FileOutputStream file = new FileOutputStream("highScor.ser");
//                    ObjectOutputStream out = new ObjectOutputStream(file);
//
//                    out.writeObject(highScoreList);
//
//                    out.close();
//                    file.close();
//
//                } catch (IOException ex) {
//                    System.out.println("IOException is caught");
//                }
                System.exit(0);
            }

        });
        setVisible(true);
    }

//    class HighScore implements Comparable<HighScore>, Serializable {
//
//        private static final long serialVersionUID = 7829136421241571165L;
//
//        private final String name;
//        private final int score;
//
//        public int getScore() {
//            return score;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public HighScore() {
//            name = "ZZZ";
//            score = 0;
//        }
//
//        public HighScore(String playerName, int playerScore) {
//            name = playerName;
//            score = playerScore;
//        }
//
//        @Override
//        public int compareTo(HighScore that) {
//            return this.score - that.getScore();
//        }
//
//    }

//    public void addPossibleHighScore(int score) {
//        if (score < highScoreList.getLast().getScore()) {
//            return;
//        }
//        System.out.println("Enter a three-letter name: ");
//        Scanner kb = new Scanner(System.in);
//        String name = "";
//        while (name.length() < 3) {
//            name = kb.next();
//        }
//        name = name.substring(0, 3).toUpperCase();
//        HighScore playerScore = new HighScore(name, score);
//        int x;
//        for (x = 9; x > 0; x--) {
//            if (highScoreList.get(x - 1).compareTo(playerScore) >= 0) {
//                break;
//            }
//        }
//        highScoreList.add(x, playerScore);
//        highScoreList.remove(10);
//    }

    class PongPanel extends JPanel {

        private Timer timer;
        private Point ball = new Point(350, 200), delta = new Point(12, 12);
        private int playerOneScore;
        private int playerTwoScore;
        private boolean isMovingUp;
        int yPositionPlayerOne;
        int yPositionPlayerTwo;
        boolean didHitBall;
        boolean isOnePlayerGame;

        public PongPanel() {

            didHitBall = true;
            yPositionPlayerOne = 170;
            isOnePlayerGame = true;

            playerOneScore = 0;
            playerTwoScore = 0;

            super.addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (e.getUnitsToScroll() > 0) {
                        isMovingUp = false;
                    }
                    if (e.getUnitsToScroll() < 0) {
                        isMovingUp = true;
                    }

                }
            });
            super.addKeyListener(new KeyAdapter() {

                                     @Override
                                     public void keyPressed(KeyEvent e) {
                                         if (e.getKeyCode() == KeyEvent.VK_UP) {
                                             isMovingUp = true;
                                         }
                                         if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                             isMovingUp = false;
                                         }
                                     }

                                 }
            );

            timer = new Timer(100, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    addNotify();
                    requestFocus();
                    if (isMovingUp) {
                        yPositionPlayerOne -= 10;
                    } else {
                        yPositionPlayerOne += 10;
                    }
                    if (yPositionPlayerOne < 0) {
                        yPositionPlayerOne = 0;
                    }
                    if (yPositionPlayerOne > getHeight() - 65) {
                        yPositionPlayerOne = getHeight() - 65;
                    }
                    updateBall();
                    if (isOnePlayerGame) {
                        yPositionPlayerTwo = ball.y - 15;
                    }
                    repaint();
                }
            });

        }

        public void startGame() {
            ball = new Point(this.getWidth() / 2 - 10, this.getHeight() / 2);
            playerOneScore = 0;
            playerTwoScore = 0;
            hasStarted = true;
            timer.start();
        }

        public void endGame() {
            hasStarted = false;
            timer.stop();
            ball.x = this.getWidth() / 2;
            ball.y = this.getHeight() / 2;

//            addPossibleHighScore(playerOneScore);
            playerOneScore = 0;
            playerTwoScore = 0;

            if (getTitle().equals("Client"))
                delta.x = -12;
            else delta.x = 12;
            didHitBall = true;
        }

        private void updateBall() {
            if (ball.y <= 0 || ball.y > this.getHeight() - 30) {
                delta.y = -delta.y;
            }
            if (ball.x > this.getWidth() - 70) {
                if (ball.y > yPositionPlayerOne - 30 && ball.y < yPositionPlayerOne + 60) {
                    delta.x = -delta.x;
                    playerOneScore++;
                } else {
                    didHitBall = false;
                    endGame();
                }
            }
            if (ball.x <= 30) {
                delta.x = -delta.x;
                playerTwoScore++;
            }
            ball.x += delta.x;
            ball.y += delta.y;
        }

        @Override
        public void paint(Graphics g) {

            super.paint(g);
            g.drawRect(this.getWidth() - 40, yPositionPlayerOne, 10, 60);
            g.drawRect(30, yPositionPlayerTwo, 10, 60);
            g.fillOval(ball.x, ball.y, 30, 30);
        }

        public int getPlayerOneScore() {
            return playerOneScore;
        }

        public int getPlayerTwoScore() {
            return playerTwoScore;
        }

    }

    static class DataToCommunicate implements Serializable {

        private static final long serialVersionUID = 4563712390874575378L;

        boolean successfulHit;
        int paddlePosition;
        boolean startButtonPressed;

        public DataToCommunicate(boolean successfulHit,
                int paddlePosition,
                boolean startButtonPressed) {
            this. successfulHit = successfulHit; //edu.touro.mco364.Pong.this.panel.didHitBall;
            this.paddlePosition =paddlePosition;// edu.touro.mco364.Pong.this.panel.yPositionPlayerOne;
            this.startButtonPressed =startButtonPressed;// hasStarted;
        }
    }

}