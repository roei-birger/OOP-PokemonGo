
import api.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    private directed_weighted_graph g;
    private dw_graph_algorithms ga;
    static long start = new Date().getTime();

    @BeforeAll
    public static void BeforeAll() {
        System.out.println("Start");
    }

    //reset graph to default
    @BeforeEach
    public void BeforeEach() {
        g = graph_creator();
        ga = new DWGraph_Algo();
        ga.init(g);

    }

    @Test
    void initBase() {
        ga.init(g);
    }

    @Test
    void getGraphBase() {
        directed_weighted_graph g1 = ga.getGraph();
        assertEquals(g1, g, "getGraph doesn't return the same graph");
    }

    @Test
    void copyBase() {
        directed_weighted_graph g1 = ga.copy();
        assertEquals(g.edgeSize(), g1.edgeSize(), "copy doesn't make a deep copy at edgeSize");
        assertEquals(g.nodeSize(), g1.nodeSize(), "copy doesn't make a deep copy at nodeSize");
        assertEquals(g, g1, "copy doesn't make a deep copy");
    }

    @Test
    void copyCheckMC() {
        g.addNode(new NodeData(17));
        g.removeNode(17);
        directed_weighted_graph g1 = ga.copy();
        assertEquals(22, g1.getMC(), "copy doesn't reset the MC");
    }

    @Test
    void copyCheckAfterRemove() {
        directed_weighted_graph g1 = ga.copy();
        assertEquals(g, g1, "copy doesn't make a deep copy");
        g1.addNode(new NodeData(5));
        assertNotEquals(g1, g, "the copied graph stay the same after add node");
        g1.removeNode(5);
        assertEquals(g1, g, "the copied graph stay the same after remove node");
    }

    @Test
    void copyOneNode() {
        directed_weighted_graph g2 = new DWGraph_DS();
        g2.addNode(new NodeData(2));
        ga.init(g2);
        directed_weighted_graph g1 = ga.copy();
        assertEquals(g2, g1, "copy doesn't make a deep copy");
    }

    @Test
    void copyNull() {
        ga.init(null);
        directed_weighted_graph g1 = ga.copy();
        assertTrue(g1.nodeSize() == 0 & g1.nodeSize() == 0 & g1.getMC() == 0, "copy doesn't reset the parameters for null graph");
        assertNull(g1.getV(), "copy make copy for null graph");
    }

    @Test
    void isConnectedBase() {
        g.connect(555, 53, 2);
        assertTrue(ga.isConnected(), "isConnected return false when connected");
    }

    @Test
    void isConnectedNull() {
        ga.init(null);
        assertFalse(ga.isConnected(), "isConnected return True to null graph");
    }

    @Test
    void isConnectedLonelyNode() {
        directed_weighted_graph g1 = new DWGraph_DS();
        g1.addNode(new NodeData(1));
        ga.init(g1);
        assertTrue(ga.isConnected(), "isConnected return false to graph with one node");
    }

    @Test
    void isConnectedNotConnected() {
        assertFalse(ga.isConnected(), "isConnected return true when unconnected");
    }

    @Test
    void shortestPathDistBase() {
        g.connect(13, -1, 40);
        ga.init(g);
        assertEquals(28.4, ga.shortestPathDist(53, -101), "shortestPathDist return uncorrected dist");
        assertEquals(19, ga.shortestPathDist(13, -1), "shortestPathDist return uncorrected dist");
        assertEquals(6.5, ga.shortestPathDist(66, 555), "shortestPathDist return uncorrected dist");
    }

    @Test
    void shortestPathDistNodeToHimself() {
        assertEquals(0, ga.shortestPathDist(0, 0), "shortestPathDist return uncorrected dist from node to himself");
    }

    @Test
    void shortestPathDistNull() {
        ga.init(null);
        assertEquals(-1, ga.shortestPathDist(0, -1), "shortestPathDist return uncorrected dist at null graph");
    }

    @Test
    void shortestPathDistNodeNotExist() {
        assertEquals(-1, ga.shortestPathDist(0, -11), "shortestPathDist return uncorrected dist at one node not exist");
        assertEquals(-1, ga.shortestPathDist(50, -11), "shortestPathDist return uncorrected dist at two node not exist");
    }

    @Test
    void shortestPathDistNoDist() {
        g.addNode(new NodeData(16));
        assertEquals(-1, ga.shortestPathDist(0, 16), "shortestPathDist return uncorrected dist");
    }

    @Test
    void shortestPathBase() {
        g.connect(13, -1, 40);
        ga.init(g);
        LinkedList<node_data> finalList = new LinkedList<>();
        finalList.addFirst(g.getNode(-101));
        finalList.addFirst(g.getNode(0));
        finalList.addFirst(g.getNode(66));
        finalList.addFirst(g.getNode(555));
        finalList.addFirst(g.getNode(53));
        assertEquals(finalList.toString(), ga.shortestPath(53, -101).toString(), "shortestPath return uncorrected list");
        finalList = new LinkedList<>();
        finalList.addFirst(g.getNode(-1));
        finalList.addFirst(g.getNode(-15));
        finalList.addFirst(g.getNode(3));
        finalList.addFirst(g.getNode(13));
        assertEquals(finalList.toString(), ga.shortestPath(13, -1).toString(), "shortestPath return uncorrected list");
        finalList = new LinkedList<>();
        finalList.addFirst(g.getNode(555));
        finalList.addFirst(g.getNode(0));
        finalList.addFirst(g.getNode(66));
        assertEquals(finalList.toString(), ga.shortestPath(66, 555).toString(), "shortestPath return uncorrected list");

    }

    @Test
    void shortestPathNodeToHimself() {
        LinkedList<node_data> finalList = new LinkedList<>();
        finalList.addFirst(g.getNode(0));
        assertEquals(finalList.toString(), ga.shortestPath(0, 0).toString(), "shortestPath return uncorrected list at node to himself");
    }

    @Test
    void shortestPathNullGraph() {
        ga.init(null);
        assertNull(ga.shortestPath(0, 4), "shortestPath return uncorrected list at null graph");
    }

    @Test
    void saveBase() {
        try {
            ga.save("file");
            assertTrue(ga.save("file"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadBase() {

        ga.save("file");
        dw_graph_algorithms ga2 = new DWGraph_Algo();
        try {
            ga2.load("file");

        } catch (IOException e) {
            e.printStackTrace();
        }
        directed_weighted_graph g1 = ga2.getGraph();
        assertEquals(g, g1, "load return different graph");
    }


    @Test
    void loadAfterChange() {
        ga.save("file");
        dw_graph_algorithms t1 = new DWGraph_Algo();
        try {
            t1.load("file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        directed_weighted_graph g9 = t1.getGraph();
        g9.removeNode(-1);
        assertNotEquals(g, g9, "load return different graph");
    }

    @Test
    void runTime2MillionNodes() {
        long start = new Date().getTime();

        for (int i = 0; i < 4000000; i++) {
            g.addNode(new NodeData(i++));
        }

        double w = 0.7;

        for (int i = 0, j = 1; i < 1000000; i++, j++) {
            w += 0.9;
            g.connect(i, j, w);
            g.connect(0, j, w);
            g.connect(1, i, w);
            g.connect(2, j, w);
            g.connect(3, i, w);
            g.connect(4, j, w);
            g.connect(5, i, w);
            g.connect(6, j, w);
            g.connect(7, i, w);
            g.connect(8, j, w);
            g.connect(9, i, w);
            g.connect(10, j, w);


        }
        ga.init(g);
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        assertTrue(dt < 10);
        System.out.println("runTimeTwoMillionNodes: " + dt);

    }


    @AfterAll
    public static void AfterAll() {
        System.out.println("Finish succeed");
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        System.out.println("All program runTime: " + dt);
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