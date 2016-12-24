import java.awt.*;

/**
 * Created by jack on 2016/12/16.
 */
public class ColorUtil {
    private static int[] m_temps = {20, 30, 35, 40, 45, 50, 55, 60, 65, 70, 80, 90, Integer.MAX_VALUE};
    private static Color[] m_colors = {new Color(5, 10, 65), new Color(6, 13, 146), new Color(75, 129, 227),
            new Color(15, 135, 161), new Color(14, 241, 153), new Color(254, 246, 1),
            new Color(240, 184, 11), new Color(236, 139, 53), new Color(194, 67, 17),
            new Color(224, 113, 150), new Color(237, 4, 92), new Color(251, 0, 0),
            new Color(186, 4, 4)};
    public static Color GetColor(double temp){
        for(int i = 0; i < m_temps.length; i++){
            if(temp <= m_temps[i]){
                return m_colors[i];
            }
        }
        return Color.WHITE;
    }
}
