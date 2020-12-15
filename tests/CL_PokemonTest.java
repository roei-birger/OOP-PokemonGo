import api.DWGraph_DS;
import api.NodeData;
import api.directed_weighted_graph;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CL_PokemonTest {

    private CL_Pokemon myPokemon;
    private directed_weighted_graph g;

    @Test
    void get_edge() {
        g = graph_creator();
        myPokemon = new CL_Pokemon(new Point3D(35, 32, 0.0), 6, 5.0, 5.0, g.getEdge(53, 555));
        assertEquals(g.getEdge(53, 555), myPokemon.get_edge(), "get_edge didn't return successfully the edge");
    }

    @Test
    void set_edge() {
        g = graph_creator();
        myPokemon = new CL_Pokemon(new Point3D(35, 32, 0.0), 6, 5.0, 5.0, g.getEdge(53, 555));
        myPokemon.set_edge(g.getEdge(66, 0));
        assertEquals(g.getEdge(66, 0), myPokemon.get_edge(), "set_edge didn't set's successfully the edge");
    }

    @Test
    void getLocation() {
        g = graph_creator();
        myPokemon = new CL_Pokemon(new Point3D(35, 32, 0.0), 6, 5.0, 5.0, g.getEdge(53, 555));
        assertEquals(new Point3D(35, 32, 0.0), myPokemon.getLocation(), "getLocation didn't return successfully the Location");
    }

    @Test
    void getType() {
        g = graph_creator();
        myPokemon = new CL_Pokemon(new Point3D(35, 32, 0.0), 6, 5.0, 5.0, g.getEdge(53, 555));
        assertEquals(6, myPokemon.getType(), "getType didn't return successfully the Type");
    }

    @Test
    void getValue() {
        g = graph_creator();
        myPokemon = new CL_Pokemon(new Point3D(35, 32, 0.0), 6, 5.0, 5.0, g.getEdge(53, 555));
        assertEquals(5.0, myPokemon.getValue(), "getValue didn't return successfully the Value");
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