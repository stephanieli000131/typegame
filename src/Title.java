import javax.swing.*;
import java.awt.*;
import java.util.Random;

class Title extends JPanel implements Runnable {
    int width = 500;
    int height = 250;
    int N = 4;
    int[] x = new int[N];
    int[] y = new int[N];
    String[] strs = new String[]{"  Typing", "", "Game", ""};

    Title() {

        setBounds(0, 0, width, height);
        setOpaque(false);
        setplace();
    }

    void setplace() {
        for (int i = 0; i < N; i++) {
            x[i] = (int) (width * 0.15 + i * 0.2 * width);
            y[i] = 10;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Random r = new Random();
        g.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        for (int i = 0; i < N; i++) {
            g.drawString(strs[i], x[i], y[i]);
            y[i]++;
            if (y[i] > height - 80) {
                y[i] = height - 80;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }
}