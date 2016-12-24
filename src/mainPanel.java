import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;

/**
 * Created by jack on 2016/12/15.
 */
public class mainPanel extends JPanel {
    mainPanel m_panel;
    boolean m_running = false;
    GeneralPath[] m_paths;
    int m_curStrokeIndex = 0;
    double m_curRotating = 0;
    double m_temp = 0;
    double m_speed;
    JLabel m_tempLabel;
    JLabel m_speedLabel;
    ImageIcon m_imgIconBack = new ImageIcon(getClass().getResource("/img/cengxi.png"));
    ImageIcon m_imgIconColor = new ImageIcon(getClass().getResource("/img/color.png"));
    ImageIcon m_imgIconZhuanlun = new ImageIcon(getClass().getResource("/img/zhuanlun.png"));
    ImageIcon m_imgIconFamen1 = new ImageIcon(getClass().getResource("/img/famen.png"));
    ImageIcon m_imgIconFamen2 = new ImageIcon(getClass().getResource("/img/famen2.png"));
    Thread m_t;

    public mainPanel() {
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        AddButton();
        DrawBackgroundLabel();
        AddSliders();
    }

    /**
     * 添加开始结束按钮
     */
    private void AddButton() {
        final JButton button = new JButton("开始");
        button.setBounds(30, 500, 60, 30);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (m_running == true) {
                    m_t.interrupt();
                } else {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (m_running == true) {
                                    m_curStrokeIndex = ++m_curStrokeIndex >= 13 ? 0 : m_curStrokeIndex;
                                    m_curRotating -= m_speed;
                                    //更新转轮
                                    m_panel.repaint(446, 312, 48, 48);
                                    Rectangle[] rects = DashAniUtil.GetUpdateRectangles();
                                    for(int t = 0; t < rects.length; t++){
                                        m_panel.repaint(rects[t]);
                                    }
                                    Thread.sleep(50);
                                }
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }

                        }
                    };
                    m_t = new Thread(runnable);
                    m_t.start();
                }
                m_running = !m_running;
                button.setText(m_running == true ? "停止" : "开始");
                DrawFamenImg(m_panel.getGraphics());
                //更新两个阀门
                m_panel.repaint(387, 601, 35, 30);
                m_panel.repaint(600, 601, 35, 30);
                m_panel.repaint(174, 443, 40, 40);
            }
        });
        this.add(button, 0);
    }


    /**
     * 添加silders
     */
    private void AddSliders() {
        JLabel label1 = new JLabel("速度:");
        label1.setBounds(810, 480, 150, 20);
        JSlider slider = new JSlider(0, 100);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(false);
        slider.setBackground(Color.WHITE);
        slider.setBounds(800, 500, 150, 40);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider j = (JSlider) (e.getSource());
                m_speed = (double) (j.getValue()) / 150;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        m_speedLabel.setText((int)(m_speed * 150) / 10.0 + "BH/V");
                        if (m_panel != null) {
                            m_panel.repaint(802, 120, 150, 80);
                            m_panel.repaint(VelocimeterUtil.GetUpdateArea());
                        }
                    }
                }).run();
            }
        });
        slider.setValue(2);
        this.add(label1);
        this.add(slider);

        JLabel label2 = new JLabel("温度:");
        label2.setBounds(810, 550, 150, 20);
        JSlider slider2 = new JSlider(0, 100);
        slider2.setMajorTickSpacing(10);
        slider2.setMinorTickSpacing(1);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(false);
        slider2.setBackground(Color.WHITE);
        slider2.setBounds(800, 570, 150, 40);
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider j = (JSlider) (e.getSource());
                m_temp = j.getValue();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        m_tempLabel.setText(m_temp + "℃");
                        if (m_panel != null) {
                            DrawWaters(m_panel.getGraphics());
                            m_panel.repaint(820, 103, 150, 40);
                        }
                    }
                }).run();
            }
        });
        slider2.setValue(55);
        this.add(label2);
        this.add(slider2);
    }

    /**
     * 绘制面板上的JLabel,新建面板时加载
     */
    private void DrawBackgroundLabel() {
        JLabel lb1 = new JLabel("示数窗口");
        lb1.setFont(new Font("宋体", Font.PLAIN, 24));
        lb1.setBounds(831, 62, 150, 40);
        this.add(lb1);

        JLabel lb2 = new JLabel("料液温度");
        lb2.setFont(new Font("宋体", Font.PLAIN, 18));
        lb2.setBounds(820, 103, 150, 40);
        this.add(lb2);

        JLabel lb3 = new JLabel("洗脱液流速");
        lb3.setFont(new Font("宋体", Font.PLAIN, 18));
        lb3.setBounds(802, 120, 150, 80);
        this.add(lb3);

        m_tempLabel = new JLabel();
        m_tempLabel.setFont(new Font("宋体", Font.PLAIN, 18));
        m_tempLabel.setBounds(900, 103, 150, 40);
        this.add(m_tempLabel);

        m_speedLabel = new JLabel();
        m_speedLabel.setFont(new Font("宋体", Font.PLAIN, 18));
        m_speedLabel.setBounds(900, 120, 150, 80);
        this.add(m_speedLabel);
    }

    public void paint(Graphics g) {
        super.paint(g);
        m_panel = this;
        DrawStaticBackgroundImg(g);
        DrawFamenImg(g);
        DrawZhuanlunImg(g);
        DrawWaters(g);
        Point[] points = new Point[]{ new Point(343, 91),new Point(343, 61),new Point(251, 61),
                new Point(466, 363),new Point(466, 596), new Point(343, 596),new Point(343, 554),
                new Point(571, 90),new Point(571, 61),new Point(466, 61),new Point(466, 309),
                new Point(714, 595),new Point(571, 595),new Point(571, 554)};

        int[] arr = new int[]{3, 4, 4, 3};
        DashAniUtil.DrawDashLineWithPoints(g, m_curStrokeIndex, points, arr);
        UpdateIndicator(g);
        VelocimeterUtil.DrawVelocimeter(g, 800, 200, 150, 0, 100, m_speed * 150);
    }

    /**
     * 绘制容器里的水
     *
     * @param g
     */
    private void DrawWaters(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Color color = ColorUtil.GetColor(m_temp);
        g2.setColor(color);
        g2.fillRect(312, 128, 52, 392);
        g2.fillRect(541, 130, 51, 390);
        g2.dispose();
    }

    /**
     * 绘制指示灯
     *
     * @param g
     */
    private void UpdateIndicator(Graphics g) {
        Color c = g.getColor();
        Color target = m_running == true ? Color.GREEN : Color.RED;
        g.setColor(target);
        g.fillOval(124, 493, 40, 40);
        g.setColor(c);
    }

    /**
     * 绘制阀门，一次性改变
     *
     * @param g
     */
    public void DrawFamenImg(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Image img1 = m_running == true ? m_imgIconFamen1.getImage() : m_imgIconFamen2.getImage();
        g2.drawImage(img1, 387, 601, 35, 30, m_running == true ? m_imgIconFamen1.getImageObserver() : m_imgIconFamen2.getImageObserver());
        g2.drawImage(img1, 600, 601, 35, 30, m_running == true ? m_imgIconFamen1.getImageObserver() : m_imgIconFamen2.getImageObserver());
        g2.dispose();
    }

    /**
     * 绘制转轮，实时更新
     *
     * @param g
     */
    public void DrawZhuanlunImg(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Image img3 = m_imgIconZhuanlun.getImage();
        g2.translate(470, 336);
        g2.rotate(m_curRotating);
        g2.drawImage(img3, -24, -24, 48, 48, m_imgIconZhuanlun.getImageObserver());
        g2.dispose();
    }

    /**
     * 绘制面板上的静态Image，主要是色度带和背景
     *
     * @param g
     */
    private void DrawStaticBackgroundImg(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Image img1 = m_imgIconBack.getImage();
        g2.drawImage(img1, 236, 0, 564, 700, m_imgIconBack.getImageObserver());

        Image img2 = m_imgIconColor.getImage();
        g2.drawImage(img2, 20, 30, 131, 449, m_imgIconColor.getImageObserver());
        g2.dispose();
    }
}
