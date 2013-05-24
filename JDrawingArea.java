import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDrawingArea extends JPanel {
    final int fpsLimit = 60;

    long oldNanoTime;
    long prevFrameNanoT;

    Timer timer;
    GameEngine engine;

    JDrawingArea(GameEngine ge) {
        super();
        oldNanoTime = prevFrameNanoT = System.nanoTime();

        final JDrawingArea this_l = this;

        timer = new Timer(1000/fpsLimit, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(System.nanoTime() - oldNanoTime < 1000000000 / fpsLimit * 2)
                    this_l.paintImmediately(0, 0, this_l.getWidth(), this_l.getHeight());
                oldNanoTime = System.nanoTime();
            }
        });
        timer.start();
        engine = ge;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        long now = System.nanoTime();
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        engine.nextFrame(now - prevFrameNanoT, g2d);
        prevFrameNanoT = now;
    }
}
