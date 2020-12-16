package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a GUI class to present the game.
 * The JFrame combine with JPanel.
 * The frame includes:
 * - timer.
 * - grade per agent.
 * - resizeable frame.
 */
public class MyFrame extends JFrame {
    private int _ind;
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private long timeToEnd = 60000;
    private int numLevel;
    private int n1;
    private Ex2.Entry entryScreen;

    /**
     * A default constructor
     */
    MyFrame(String a) {
        super(a);
    }

    /**
     * Constructs a MyFrame with received data.
     *
     * @param a
     * @param w
     * @param h
     * @param n1
     */
    public MyFrame(String a, int w, int h, int n1) {
        super(a);
        this.setSize(new Dimension(w, h));
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.n1 = n1;
        this.setVisible(true);

    }

    /**
     * Updates the arena data in the frame.
     *
     * @param ar
     */
    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();

    }

    /**
     * Repaints the graphics
     *
     * @param g
     */
    public void paint(Graphics g) {
        if (_ar != null) {
            initPanel();
            this.revalidate();
        }
    }

    /**
     * Updates the size of the frame,
     * in order to make the frame resizeable
     */
    private void updateFrame() {
        Range rx = new Range(200, this.getWidth() - 30);
        Range ry = new Range(this.getHeight() - 80, 165);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _w2f = Arena.w2f(g, frame);
    }

    /**
     * Creates a myPanel according to MyFrame
     */
    private void initPanel() {
        MyPanel myPanel = new MyPanel();
        this.add(myPanel);
        JLabel title = new JLabel();
        ImageIcon imageT = new ImageIcon("./data/titel.png");
        title.setIcon(imageT);
        myPanel.add(title);


    }

    /**
     * Sets the timeToEnd parameter of the MyFrame.
     *
     * @param time
     */
    public void setTimeToEnd(long time) {
        this.timeToEnd = time;
    }

    /**
     * Sets the numLevel parameter of the MyFrame.
     *
     * @param level
     */
    public void setLevel(int level) {
        this.numLevel = level;
    }

    /**
     * Draws the graph.
     *
     * @param g
     */
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.black);//קודקוד
            drawNode(n, 7, g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.BLACK);//הצלעות של כל קודקוד
                drawEdge(e, g);
            }
        }
    }

    /**
     * Draws all the nodes on the graph.
     *
     * @param n
     * @param r
     * @param g
     */
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = _w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.setColor(Color.yellow.darker().darker());
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * r);
    }

    /**
     * Draws all the edges on the graph.
     *
     * @param e
     * @param g
     */
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = _w2f.world2frame(s);
        geo_location d0 = _w2f.world2frame(d);
        Graphics2D hh = (Graphics2D) g;
        hh.setStroke(new BasicStroke(2));
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

    /**
     * Creates a login screen.
     */
    public void initLogin() {
        entryScreen = new Ex2.Entry();
        this.add(entryScreen);
        entryScreen.setVisible(true);
        this.setVisible(true);
        ImageIcon imageB = new ImageIcon("./data/pokeball.png");
        setIconImage(imageB.getImage());
    }

    /**
     * This class represents a JPanel class to present the game
     * in the JFrame.
     */
    public class MyPanel extends JPanel {
        /**
         * a default constructor
         */
        public MyPanel() {
        }

        /**
         * Draws all the grades per agent.
         *
         * @param g
         */
        private void drawValues(Graphics g) {
            g.setColor(Color.GREEN.darker());
            g.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
            g.drawString("Grades:", 60, 65);
            double sum = 0;
            for (CL_Agent i : _ar.getAgents()) {
                sum = i.getValue();
                g.setColor(Color.GREEN.darker().darker());
                g.setFont(new Font("Lucida Handwriting", Font.ITALIC, 14));
                g.drawString("Agent " + i.getID() + ":  " + sum, 40, 95 + (20 * i.getID()));
            }
        }

        /**
         * Draws the timer of the game.
         *
         * @param g
         */
        private void drawTimer(Graphics g) {
            g.setColor(Color.GREEN.darker().darker());
            g.setFont(new Font("MV Boli", Font.BOLD, 18));
            g.drawString("Time to end " + (float) timeToEnd / 100, 15, 95 + (20 * (_ar.getAgents().size() + 1)));

        }

        /**
         * Draws the level of the game.
         *
         * @param g
         */
        private void drawLevel(Graphics g) {
            g.setColor(Color.GREEN.darker().darker());
            g.setFont(new Font("MV Boli", Font.BOLD, 25));
            g.drawString("level " + numLevel, 40, 35);
        }

        /**
         * Repaints the graphics
         *
         * @param g
         */
        public void paintComponent(Graphics g) {
            int w = this.getWidth();
            int h = this.getHeight();
            g.clearRect(0, 0, w, h);
            if (_ar != null) {
                updateFrame();
                drawGraph(g);
                drawPokemons(g);
                drawAgents(g);
                drawInfo(g);
                //drawTitle(g);
                drawValues(g);
                drawTimer(g);
                drawLevel(g);
            }
            this.revalidate();
        }

        /**
         * Draws the info of the game.
         *
         * @param g
         */
        private void drawInfo(Graphics g) {
            List<String> str = _ar.get_info();
            String dt = "none";
            for (int i = 0; i < str.size(); i++) {
                g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
            }
        }

        /**
         * Draws all the pokemons on the graph.
         * @param g
         */
        private void drawPokemons(Graphics g) {
            ImageIcon imageP = new ImageIcon("./data/pokemon.png");
            ImageIcon imageG = new ImageIcon("./data/green.png");
            List<CL_Pokemon> fs = _ar.getPokemons();

            if (fs != null) {
                Iterator<CL_Pokemon> itr = fs.iterator();

                while (itr.hasNext()) {

                    CL_Pokemon f = itr.next();
                    Point3D c = f.getLocation();
                    int r = 10;
                    if (c != null) {

                        geo_location fp = _w2f.world2frame(c);

                        if (f.getType() < 0) {
                            g.drawImage(imageG.getImage(), (int) fp.x() - r - 10, (int) fp.y() - r - 10, this);
                        } else
                            g.drawImage(imageP.getImage(), (int) fp.x() - r - 20, (int) fp.y() - r - 20, this);

                    }
                }
            }
        }

        /**
         * Draws all the agents on the graph.
         * @param g
         */
        private void drawAgents(Graphics g) {
            ImageIcon imageB = new ImageIcon("./data/pokeball.png");
            setIconImage(imageB.getImage());

            List<CL_Agent> rs = _ar.getAgents();
            //	Iterator<OOP_Point3D> itr = rs.iterator();
            g.setColor(Color.red);//אולי נשנה
            int i = 0;
            while (rs != null && i < rs.size()) {
                geo_location c = rs.get(i).getLocation();
                int r = 8;
                i++;
                if (c != null) {

                    geo_location fp = _w2f.world2frame(c);
                    g.drawImage(imageB.getImage(), (int) fp.x() - r - 10, (int) fp.y() - r - 10, this);
                }
            }
        }


    }
}
