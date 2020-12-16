package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

/**
 * This class is responsible for starting the game.
 */
public class Ex2 implements Runnable {
    private static MyFrame _win;
    private static MyFrame _login;
    private static Arena _ar;
    private static HashMap<Integer, Integer> busy = new HashMap<>();
    private static String lg;
    private static long dt;
    private static int n0, n1 = -1;
    private static long fullTime = 60000;
    private static Thread client = new Thread(new Ex2());

    /**
     * The main class that starts the game.
     *
     * @param a
     */
    public static void main(String[] a) {
        if (a.length == 2) {
            n0 = Integer.parseInt(a[0]);
            n1 = Integer.parseInt(a[1]);
            client.start();
        } else {
            _win = new MyFrame("Enter Pokemon game", 420, 300, n1);
            _win.initLogin();

        }
    }

    /**
     * Starts the game's algorithm by loading the data from JSON file.
     * The function also starts the game's graphics.
     * In addition sends the results to the server.
     */
    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(n1); // you have [0,23] games
        int id = n0;
        game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();

        try {
            File f = new File("my_graph");
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(g);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        dw_graph_algorithms temp = new DWGraph_Algo();
        temp.load("my_graph");
        directed_weighted_graph gg = temp.getGraph();
        init(game);


        //Start game

        game.startGame();
        fullTime = game.timeToEnd();
        _win.setTitle("Ex2 - OOP: " + game.toString());
        int ind = 0;
        dt = 110;

        while (game.isRunning()) {
            if (dt == 50)
                dt = 90;
            else if (dt == 70)
                dt = 90;
            else if (game.timeToEnd() <= (0.1666666) * fullTime)
                dt = 85;
            else if (game.timeToEnd() <= (0.3333333) * fullTime)
                dt = 90;
            else if (game.timeToEnd() <= (0.5) * fullTime)
                dt = 96;
            else if (game.timeToEnd() <= (0.6666666) * fullTime)
                dt = 100;
            _win.setTimeToEnd(game.timeToEnd() / 10);
            _win.setLevel(n1);
            _win.setTitle("Ex2 - OOP: " + game.toString());
            moveAgents(game, gg);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     *
     * @param game
     * @param gg
     * @param
     */
    private void moveAgents(game_service game, directed_weighted_graph gg) {
        lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (dest == -1) {
                if (busy.containsKey(id)) {
                    busy.remove(id);
                }
                dest = nextNode(gg, src, game, id);
                busy.put(id, dest);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
                if (game.timeToEnd() < (0.6666666) * fullTime) {
                    for (edge_data j : gg.getE(src)) {
                        if (j.getWeight() < 0.4) {
                            dt = 50;
                            break;

                        } else if (j.getWeight() < 0.5) {
                            dt = 70;
                        }
                    }
                }
            }
        }
    }

    /**
     * This function chooses the given agent (by receiving the agent's ID) his next destination
     * by using shortestPathDist function in order to find the
     * shortest way to the closest Pokemon.
     *
     * @param g
     * @param src
     * @return
     */
    private int nextNode(directed_weighted_graph g, int src, game_service game, int id) {
        int ans = -1;
        List<CL_Pokemon> pkList = _ar.json2Pokemons(game.getPokemons());
        dw_graph_algorithms gA = new DWGraph_Algo();
        gA.init(g);
        int pokDest;
        double min = Double.POSITIVE_INFINITY;
        int minDest = src;
        int minSrc = src;
        double path;
        if (busy.containsKey(id)) minSrc = busy.get(id);
        else {
            for (int i = 0; i < pkList.size(); i++) {
                _ar.updateEdge(pkList.get(i), g);
                if (pkList.get(i).get_edge() != null) {
                    int thisPokSRC = pkList.get(i).get_edge().getSrc();
                    if (thisPokSRC == src) {
                        return pkList.get(i).get_edge().getDest();
                    }

                    if (!busy.containsValue(thisPokSRC)) {
                        pokDest = pkList.get(i).get_edge().getDest();
                        path = gA.shortestPathDist(src, thisPokSRC);
                        if (path < min) {
                            min = path;
                            minSrc = thisPokSRC;
                            minDest = pokDest;
                        }
                    }
                }


            }
            if (minSrc == src)
                return nextNode(g, src);
        }


        List<node_data> finalList;
        if (minSrc == src) {
            finalList = gA.shortestPath(src, minDest);
        } else {
            finalList = gA.shortestPath(src, minSrc);
        }
        if (finalList.size() == 1)
            ans = minSrc;
        else ans = finalList.get(1).getKey();
        return ans;
    }


    /**
     * This function chooses to the given agent (by the received agent ID) his next destination
     * by using a very simple random implementation!
     * The function is used for agents that don't have a Pokemon close to them
     * because all the other Pokemons are already linked to closer agents.
     *
     * @param g
     * @param src
     * @return
     */
    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int) (Math.random() * s);
        int i = 0;
        while (i < r) {
            itr.next();
            i++;
        }
        ans = itr.next().getDest();
        return ans;
    }

    /**
     * Creates a frame from the received data.
     *
     * @param game
     */
    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);


        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            Collections.sort(cl_fs);
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), gg);
            }
            for (int a = 0; a < rs; a++) {
                int ind = a % cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) {
                    nn = c.get_edge().getSrc();
                }

                game.addAgent(nn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This class represents a GUI class to present the login to the game.
     * The JFrame is combined with JPanel.
     * The frame includes:
     * - intake values from the user
     * - resizeable frame.
     */
    public static class Entry extends JPanel {
        /**
         * a default constructor
         */
        public Entry() {
            super();
            this.setLayout(null);
            input();
            title();

        }

        /**
         * Draws the graphics on the frame.
         *
         */
        private void title() {
            JLabel title = new JLabel("Welcome!");
            JLabel sub_title = new JLabel("Please enter:");
            title.setFont(new Font("MV Boli", Font.BOLD, 25));
            sub_title.setFont(new Font("MV Boli", Font.BOLD, 18));
            title.setForeground(Color.black);
            sub_title.setForeground(Color.black);
            title.setBounds(120, 22, 350, 40);
            sub_title.setBounds(120, 52, 380, 40);
            add(title);
            add(sub_title);
        }

        /**
         * This function creates all the files responsible to intake
         * values from the user and update the relevant parameter in the game.
         */
        private void input() {
            JLabel game_num = new JLabel("Game Level");
            game_num.setBounds(50, 142, 80, 25);
            game_num.setForeground(Color.black);
            game_num.setBackground(Color.white);
            game_num.setFont(new Font("Ariel", Font.BOLD, 13));
            game_num.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            game_num.setOpaque(true);

            this.add(game_num);

            JTextField gameInput = new JTextField();
            gameInput.setBounds(150, 142, 165, 25);
            gameInput.setFont(new Font("Ariel", Font.BOLD, 13));
            gameInput.setForeground(Color.black);

            this.add(gameInput);

            JLabel id = new JLabel(" ID Number");
            id.setBounds(50, 102, 80, 25);
            id.setForeground(Color.black);
            id.setBackground(Color.white);
            id.setFont(new Font("Ariel", Font.BOLD, 13));
            id.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            id.setOpaque(true);

            add(id);

            JTextField userInput = new JTextField();
            userInput.setBounds(150, 102, 165, 25);
            userInput.setFont(new Font("Ariel", Font.BOLD, 13));
            userInput.setForeground(Color.black);


            this.add(userInput);


            JButton button1 = new JButton("Start");
            button1.setBounds(120, 182, 80, 25);
            button1.setForeground(Color.black);
            button1.setBackground(Color.white);
            button1.setFont(new Font("Ariel", Font.BOLD, 15));
            button1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            this.add(button1);
            button1.addActionListener(e ->
                    n1 = Integer.parseInt(gameInput.getText()));
            button1.addActionListener(e ->
                    n0 = Integer.parseInt(userInput.getText()));
            button1.addActionListener(e -> client.start());
            button1.addActionListener(e -> _win.setVisible(false));

        }

    }
}
