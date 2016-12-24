import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by jack on 2016/12/16.
 */
public class DashAniUtil {
    private static BasicStroke[] m_strokes = null;

    private static BasicStroke[] GetStrokes(){
        if(m_strokes == null){
            m_strokes = new BasicStroke[13];
            float[] dashArray = new float[2];
            dashArray[0] = 8.0f;
            dashArray[1]=6.0f;
            for(int i = 0; i < m_strokes.length; i++){
                //TODO 最后一个参数是i还是-i待定
                m_strokes[i] = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dashArray, i);
            }
        }
        return m_strokes;
    }

    /**
     * 给定指定的GeneralPath数组和偏移，绘制虚线
     * @param paths
     * @param g
     * @param m_curStrokeIndex
     */
    public static void DrawStaticDashLine(GeneralPath[] paths, Graphics g, int m_curStrokeIndex){
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setStroke( GetStrokes()[m_curStrokeIndex]);
        for(int i = 0; i < paths.length; i++){
            g2.draw(paths[i]);
        }
        g2.dispose();
    }

    private static GeneralPath[] m_paths;
    private static Rectangle[] m_updateRectangles;

    /**
     * 如下例子所示，表示第一段线由3个点组成，第二段线由2个点组成
     * @param g
     * @param m_curStrokeIndex
     * @param points [(1,1)(2,2)(3,3)   (3,4)(2,3)]
     * @param pointSpliter [3,2]
     */
    public static void DrawDashLineWithPoints(Graphics g, int m_curStrokeIndex, Point[] points, int[] pointSpliter){
        m_paths = new GeneralPath[pointSpliter.length];
        m_updateRectangles = new Rectangle[points.length - pointSpliter.length];
        int rectIndex = 0;
        int curIndex = 0;
        for(int i = 0; i < pointSpliter.length; i++){
            m_paths[i] = new GeneralPath();
            m_paths[i].moveTo(points[curIndex].getX(), points[curIndex].getY());
            for(int j = 1; j < pointSpliter[i]; j++){
                m_paths[i].lineTo(points[curIndex + j].getX(), points[curIndex + j].getY());
                double deltaX = (points[curIndex + j - 1].getX() - points[curIndex + j].getX() == 0) ? 1 : Math.abs(points[curIndex + j - 1].getX() - points[curIndex + j].getX());
                double deltaY = (points[curIndex + j - 1].getY() - points[curIndex + j].getY() == 0) ? 1 : Math.abs(points[curIndex + j - 1].getY() - points[curIndex + j].getY());
                m_updateRectangles[rectIndex++] = (points[curIndex + j - 1].getX() <= points[curIndex + j].getX() && points[curIndex + j - 1].getY() <= points[curIndex + j].getY()) ?
                        new Rectangle((int)points[curIndex + j - 1].getX(), (int)points[curIndex + j - 1].getY(), (int)deltaX, (int)deltaY) :
                        new Rectangle((int)points[curIndex + j].getX(), (int)points[curIndex + j].getY(), (int)deltaX, (int)deltaY);
            }
            curIndex += pointSpliter[i];
        }
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setStroke( GetStrokes()[m_curStrokeIndex]);
        for(int i = 0; i < m_paths.length; i++){
            g2.draw(m_paths[i]);
        }
        g2.dispose();
    }

    public static Rectangle[] GetUpdateRectangles(){
        return m_updateRectangles;
    }
}
