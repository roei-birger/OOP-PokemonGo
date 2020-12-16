import api.*;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {
    private Arena a = new Arena();
    private directed_weighted_graph g;

    @Test
    void setPokemons() {
        List<CL_Pokemon> _pokemons = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            _pokemons.add(new CL_Pokemon(new Point3D(i, i, i), i, i, i, null));
        }
        a.setPokemons(_pokemons);
        assertEquals(_pokemons.toString(), a.getPokemons().toString(), "SetPokemon didn't change the list successfully");
    }

    @Test
    void setAgents() {
        g = graph_creator();
        List<CL_Agent> _agent = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            _agent.add(new CL_Agent(g, 0));
        }
        a.setAgents(_agent);
        assertEquals(_agent.toString(), a.getAgents().toString(), "SetAgent didn't change the list successfully");

    }

    @Test
    void setGraph() {
        g = graph_creator();
        a.setGraph(g);
        assertEquals(g, a.getGraph(), "SetGraph didn't change the graph successfully");
    }

    @Test
    void getAgents() {
        g = graph_creator();
        List<CL_Agent> _agent = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            _agent.add(new CL_Agent(g, 0));
        }
        a.setAgents(_agent);
        assertEquals(_agent.toString(), a.getAgents().toString(), "getAgent didn't return the list successfully");

    }

    @Test
    void getPokemons() {
        List<CL_Pokemon> _pokemons = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            _pokemons.add(new CL_Pokemon(new Point3D(i, i, i), i, i, i, null));
        }
        a.setPokemons(_pokemons);
        assertEquals(_pokemons.toString(), a.getPokemons().toString(), "getPokemon didn't return the list successfully");

    }

    @Test
    void getGraph() {
        g = graph_creator();
        a.setGraph(g);
        assertEquals(g, a.getGraph(), "getGraph returns the wrong graph");

    }


    @Test
    void get_info() {
        List<String> _info = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            _info.add("" + i);
        }
        a.set_info(_info);
        assertEquals(_info.toString(), a.get_info().toString(), "getInfo didn't change the list successfully");
    }

    @Test
    void set_info() {
        List<String> _info = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            _info.add("" + i);
        }
        a.set_info(_info);
        assertEquals(_info.toString(), a.get_info().toString(), "setInfo didn't return the list successfully");
    }

    public directed_weighted_graph graph_creator() {
        directed_weighted_graph g = new DWGraph_DS();


        g.addNode(new NodeData(-101));
        g.addNode(new NodeData(-15));
        g.addNode(new NodeData(-1));
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(3));
        g.addNode(new NodeData(13));
        g.addNode(new NodeData(53));
        g.addNode(new NodeData(66));
        g.addNode(new NodeData(555));


        g.connect(53, 13, 6.0);
        g.connect(53, 555, 2.1);
        g.connect(13, 3, 8.9);
        g.connect(3, 13, 8.9);
        g.connect(3, -15, 0.1);
        g.connect(-15, -1, 10);
        g.connect(-1, -101, 20);
        g.connect(-101, 3, 7.6);
        g.connect(-101, 0, 12);
        g.connect(0, -101, 21);
        g.connect(0, 555, 3.2);
        g.connect(555, 66, 2);
        g.connect(66, 0, 3.3);

        return g;
    }

}