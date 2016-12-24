import javax.swing.*;
import java.awt.*;

/**
 * Created by jack on 12/18/16.
 */
public class VelocimeterUtil {
    private static ImageIcon m_imgIconPan = new ImageIcon(Class.class.getClass().getResource("/img/pan.png"));
    private static ImageIcon m_imgIconPanArrow = new ImageIcon(Class.class.getClass().getResource("/img/panarrow.png"));

    private static int m_x;
    private static int m_y;
    private static int m_width;
    /**
     *
     * @param g
     * @param x 左上角的x值
     * @param y 左上角的y值
     * @param width 表盘的宽度
     * @param min 代表的最小值
     * @param max 代表的最大值
     * @param current 当前的值
     */
    public static void DrawVelocimeter(Graphics g, int x, int y, int width, double min, double max, double current){
        m_x = x;
        m_y = y;
        m_width = width;
        Graphics2D g2 = (Graphics2D) g.create();
        Image img3 = m_imgIconPan.getImage();
        g2.drawImage(img3, x, y, width, width, m_imgIconPan.getImageObserver());

        g2.translate(x + (int)(width / 2), y + (int)(width / 2));
        g2.rotate((current - (max + min)/2) / ((max - min) * 0.23));
        Image img = m_imgIconPanArrow.getImage();
        g2.drawImage(img, -(int)(width / 100 * 10), -(int)(width / 100 * 38), (int)(width / 100 * 20), (int)(width / 100 * 48), m_imgIconPanArrow.getImageObserver());
        g2.dispose();
    }

    /**
     * 在已经调用了上面方法绘制的前提下，调用repaint（此Rectange）来进行局部的重绘
     * @return
     */
    public static Rectangle GetUpdateArea(){
        return new Rectangle(m_x, m_y, m_width, m_width);
    }
}
