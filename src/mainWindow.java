import javafx.stage.Screen;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jack on 2016/12/15.
 */
public class mainWindow {
    public static void main(String[] argv) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("层析演示");
                mainPanel mp = new mainPanel();
                frame.setContentPane(mp);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setBackground(Color.WHITE);
                frame.pack();
                frame.setSize(1000, 710);//setSize要在pack之后
                frame.setLocation(100, 0);
                frame.setVisible(true);
            }
        });

    }
}