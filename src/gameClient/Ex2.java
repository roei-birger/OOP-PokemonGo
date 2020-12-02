package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ex2 implements Runnable {
    private static MyFrame _win;
    private static Arena _ar;
    private static HashMap<Integer, Integer> busy = new HashMap<>(); //<dest,agID>

    public static void main(String[] a) {
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        int scenario_num = 17;// לבדוק האם צריך לקלוט מהמשתמש
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
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

        _win.setTitle("Ex2 - OOP: " + game.toString());// כותרת של המשחק - לשנות בגרפיקה

        int ind = 0;
        long dt = 100;

        while (game.isRunning()) {
            _win.setTitle("Ex2 - OOP: " + game.toString());// כותרת של המשחק - לשנות בגרפיקה
            moveAgents(game, gg);//הכי יעיל שאפשר
            try {
                if (ind % 3 == 0) {
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
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
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
            if (dest == -1) {//אם הסוכן נמצא על קודקוד ואין לו ידע מעודכן
                if (busy.containsKey(id)) {
                    busy.remove(id);
                }
                dest = nextNode(gg, src, game, id);//מחפשים את היעד שלו
                busy.put(id, dest);
                game.chooseNextEdge(ag.getID(), dest);//מעדכנים המידע של הסוכן את היעד הבא שלו
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
    }


    /**
     * a very simple random walk implementation!
     *
     * @param g
     * @param src
     * @return
     */
    private static int nextNode(directed_weighted_graph g, int src, game_service game, int id) {
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

                    pokDest = pkList.get(i).get_edge().getDest();
                    path = gA.shortestPathDist(src, pokDest);
                    if (path < min && !busy.containsValue(pkList.get(i).get_edge().getSrc())) {
                        min = path;
                        minSrc = pkList.get(i).get_edge().getSrc();
                        minDest = pokDest;

                    }
                } else return ((DWGraph_DS) g).getV(src).iterator().next().getKey();

            }
            busy.put(id,minSrc);
        }

        List<node_data> finalList = gA.shortestPath(src, minDest);
        if (finalList.size() == 1)
            ans = minSrc;
        else ans = finalList.get(1).getKey();
        return ans;
    }

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


}
