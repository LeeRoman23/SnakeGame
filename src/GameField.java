import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    // Constructor generates main properties
    public GameField() {
        setBackground(Color.BLACK);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        timer = new Timer(250, this);
        timer.start();
    }

    // Method that creates main snake and apple
    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        createApple();
    }

    // Method generates an apple in random place
    public void createApple() {
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }

    // Method uploads images
    public void loadImages() {
        // If you don't see any images, describe full path to the image
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    // Method to repaint images if we are still didn't lose
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
            // if we lost we print our score and key to restart
        } else {
            timer.setDelay(250);
            String gameOver = "Game Over";
            String score = "Your score: " + (dots - 3);
            String restart = "Press 'Space' to restart";
            Font f = new Font("Arial", Font.BOLD, 14);
            g.setFont(f);
            g.setColor(Color.WHITE);
            g.drawString(gameOver, 110, SIZE / 2);
            g.drawString(score, 105, SIZE / 3);
            g.drawString(restart, 63, 220);
        }
    }

    // Method to make snake move
    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    // Check if we ate apple, then we increase our size
    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            // Speed increases if we ate enough apples
            if (dots - 3 == 5) {
                timer.setDelay(230);
            }
            if (dots - 3 == 10) {
                timer.setDelay(200);
            }
            if (dots - 3 == 15) {
                timer.setDelay(150);
            }
            if (dots - 3 == 20) {
                timer.setDelay(100);
            }
            if (dots - 3 >= 30) {
                timer.setDelay(50);
            }
            createApple();
        }
    }

    // Method to check if we bump into the wall or into ourself
    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] > SIZE) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SIZE) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }

    // If we are still playing calls methods checkApple(), checkCollisions(), move()
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    // class to control our snake
    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                left = false;
                up = true;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                left = false;
                right = false;
                down = true;
            }
            if (!inGame && key == KeyEvent.VK_SPACE) {
                inGame = true;
                right = true;
                up = false;
                down = false;
                left = false;
                initGame();
            }
        }
    }
}
