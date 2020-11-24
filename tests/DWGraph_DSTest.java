package ex2.tests;

import ex2.src.api.node_data;
import ex2.src.api.*;
import ex2.src.api.DWGraph_DS;
import ex2.src.api.directed_weighted_graph;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {
    private directed_weighted_graph g;
    static long start = new Date().getTime();

    @BeforeAll
    public static void BeforeAll() {
        System.out.println("Start");
    }

    //reset graph to default
    @BeforeEach
    public void BeforeEach() {
        g = graph_creator();
    }

    @Test
    void copyConstructorBase() {
        directed_weighted_graph g1 = new DWGraph_DS(g);
        assertEquals(g.toString(), g1.toString(), "copyConstructor didn't make a deep copy");
    }

    @Test
    void copyConstructorError() {
        directed_weighted_graph g1 = new DWGraph_DS(g);
        g1.removeNode(3);
        assertNotEquals(g, g1, "copyConstructor didn't make a deep copy");
    }

    @Test
    void getNodeBase() {
        assertEquals(0, g.getNode(0).getKey(), "getNode didn't return node from the default constructor");
    }

    @Test
    void getNodeNegativeKey() {
        assertEquals(-1, g.getNode(-1).getKey(), "getNode didn't return node from the default constructor");
    }


    @Test
    void getNodeNull() {
        assertNull(g.getNode(9), "getNode didn't return null if the node didn't in the graph");
    }

    @Test
    void hasEdgeBasic() {
        assertTrue(((DWGraph_DS) g).hasEdge(555, 66), "hasEdge didn't true about connect nodes");
        assertFalse(((DWGraph_DS) g).hasEdge(66, 555), "hasEdge didn't false about unconnected nodes");
    }

    @Test
    void hasEdgeBasicFalse() {
        assertFalse(((DWGraph_DS) g).hasEdge(3, 555), "hasEdge didn't false about unconnected nodes");
    }

    @Test
    void hasEdgeOneNoteExist() {
        assertFalse(((DWGraph_DS) g).hasEdge(9, 555), "hasEdge didn't false about one not exist node");
    }

    @Test
    void hasEdgeTwoNoteExist() {
        assertFalse((((DWGraph_DS) g).hasEdge(9, -200)), "hasEdge didn't false about two not exist nodes");
    }

    @Test
    void hasEdgeSameNode() {
        assertFalse(((DWGraph_DS) g).hasEdge(3, 3), "hasEdge didn't true about node with himself");
    }

    @Test
    void getEdgeBase() {
        assertEquals(21, g.getEdge(0, -101).getWeight(), "getEdge didn't return the correct weight");
    }

    @Test
    void getEdgeNotHave() {
        assertNull(g.getEdge(0, 3), "getEdge didn't return null if didn't have edge");
    }

    @Test
    void getEdgeOneNoteExist() {
        assertNull(g.getEdge(0, 9), "getEdge didn't return -1 about one not exist node");
    }

    @Test
    void getEdgeTwoNoteExist() {
        assertNull(g.getEdge(5, 9), "getEdge didn't return -1 about two not exist node");
    }

    @Test
    void addNodePos() {
        int temp1 = g.nodeSize();
        g.addNode(new NodeData(60));
        int temp2 = g.nodeSize();
        assertEquals(60, g.getNode(60).getKey(), "addNode didn't added a positive node");
        assertEquals(temp1 + 1, temp2, "addNode didn't added a positive node");
    }

    @Test
    void addNodeNeg() {
        int temp1 = g.nodeSize();
        g.addNode(new NodeData(-60));
        int temp2 = g.nodeSize();
        assertEquals(-60, g.getNode(-60).getKey(), "addNode didn't added a negative node");
        assertEquals(temp1 + 1, temp2, "addNode didn't update nodeSize");
    }


    @Test
    void connectBase() {
        g.connect(-1, 0, 0.7);
        assertTrue(((DWGraph_DS) g).hasEdge(-1, 0), "connect didn't insert edge between the nodes");
        assertEquals(0.7, g.getEdge(-1, 0).getWeight(), "connect didn't update correct weight");
    }

    @Test
    void connectNegativeWeight() {
        g.connect(-1, 0, -0.7);
        assertFalse(((DWGraph_DS) g).hasEdge(-1, 0), "connect insert edge with negative weight");
    }

    @Test
    void connectUpdateNegativeWeight() {
        g.connect(66, 0, -0.7);
        assertEquals(3.3, g.getEdge(66, 0).getWeight(), "connect update a negative weight between exist nodes");
    }

    @Test
    void connectRightNum() {
        g.connect(-1, 0, 0.7);
        assertTrue(((DWGraph_DS) g).hasEdge(-1, 0), "connect didn't insert edge between the nodes");
        assertEquals(0.7, g.getEdge(-1, 0).getWeight(), "connect didn't insert the right edge weight between the nodes");
    }

    @Test
    void connectNotExistNode() {
        g.connect(3, 4, 0.5);
        assertFalse(((DWGraph_DS) g).hasEdge(3, 4), "connect insert edge between not exist node to exist node");
    }

    @Test
    void connectNotExistNodes() {
        g.connect(6, 4, 0.7);
        assertFalse(((DWGraph_DS) g).hasEdge(6, 4), "connect insert edge between not exist nodes");
    }

    @Test
    void connectAddEdgeSize() {
        int temp1 = g.edgeSize();
        g.connect(-1, 0, 0.7);
        int temp2 = g.edgeSize();
        assertEquals(temp1 + 1, temp2, "connect  didn't update edgeSize");
    }

    @Test
    void connectNotAddEdgeSize() {
        int temp1 = g.edgeSize();
        g.connect(9, 6, 0.7);
        g.connect(3, -15, 0.7);
        g.connect(3, 0, -0.7);
        int temp2 = g.edgeSize();
        assertEquals(temp1, temp2, "connect update edgeSize when he shouldn't need");
    }

    @Test
    void getEBase() {
        Collection<node_data> temp = new ArrayList<>();
        temp.add(g.getNode(13));
        temp.add(g.getNode(-15));
        assertEquals(temp.toString(), g.getE(3).toString(), "getV didn't return correct collection");
    }

    @Test
    void getVBase() {
        Collection<node_data> temp = new ArrayList<>();
        temp.add(g.getNode(-1));
        temp.add(g.getNode(0));
        temp.add(g.getNode(66));
        temp.add(g.getNode(3));
        temp.add(g.getNode(-101));
        temp.add(g.getNode(53));
        temp.add(g.getNode(555));
        temp.add(g.getNode(13));
        temp.add(g.getNode(-15));

        assertEquals(temp.toString(), g.getV().toString(), "getV didn't return correct collection");
    }

    @Test
    void getVSize() {
        Collection<node_data> temp = new ArrayList<>();
        temp.add(g.getNode(-1));
        temp.add(g.getNode(0));
        temp.add(g.getNode(66));
        temp.add(g.getNode(3));
        temp.add(g.getNode(-101));
        temp.add(g.getNode(53));
        temp.add(g.getNode(555));
        temp.add(g.getNode(13));
        temp.add(g.getNode(-15));
        assertEquals(temp.size(), g.getV().size(), "getV didn't return correct size of collection");
    }

    @Test
    void testGetV2() {
        Collection<node_data> temp = new ArrayList<>();
        temp.add(g.getNode(555));
        temp.add(g.getNode(13));
        assertEquals(temp.toString(), ((DWGraph_DS) g).getV(g.getNode(53).getKey()).toString(), "getV didn't return correct collection");
    }

    @Test
    void removeNodeBase() {
        assertEquals(555, g.removeNode(555).getKey(), "removeNode didn't return correct node");
    }

    @Test
    void removeNodeChekSizes() {
        int nodeSize = g.nodeSize();
        int edgeSize = g.edgeSize();
        assertEquals(555, g.removeNode(555).getKey(), "removeNode didn't return correct node");
        assertEquals(nodeSize - 1, g.nodeSize(), "removeNode didn't update correct nodeSize");
        assertEquals(edgeSize - 3, g.edgeSize(), "removeNode didn't update correct edgeSize");
    }

    @Test
    void removeNodeNotExist() {
        assertNull(g.removeNode(9), "removeNode didn't return null when remove not exist node");
    }

    @Test
    void removeNodeNotExistChekSizes() {
        int nodeSize = g.nodeSize();
        int edgeSize = g.edgeSize();
        assertNull(g.removeNode(9), "removeNode didn't return null when remove not exist node");
        assertEquals(nodeSize, g.nodeSize(), "removeNode didn't update correct nodeSize");
        assertEquals(edgeSize, g.edgeSize(), "removeNode didn't update correct edgeSize");
    }

    @Test
    void removeNodeChekNeighbors() {
        assertEquals(555, g.removeNode(555).getKey(), "removeNode didn't return correct node");
        assertFalse(((DWGraph_DS) g).getV(g.getNode(53).getKey()).contains(g.getNode(555)), "removeNode didn't remove the node from the nei's nei lise");
        assertFalse(((DWGraph_DS) g).getV(g.getNode(0).getKey()).contains(g.getNode(555)), "removeNode didn't remove the node from the nei's nei lise");
    }

    @Test
    void removeEdgeBase() {
        int edgeSize = g.edgeSize();
        g.removeEdge(-1, -101);
        assertFalse(((DWGraph_DS) g).hasEdge(-1, -101), "removeEdge didn't remove the edge");
        assertEquals(edgeSize - 1, g.edgeSize(), "removeNode didn't update edgeSize");
    }

    @Test
    void removeEdgeNotExistEdge() {
        int edgeSize = g.edgeSize();
        g.removeEdge(-1, 0);
        assertEquals(edgeSize, g.edgeSize(), "removeEdge update edgeSize when remove not exist edge");
    }

    @Test
    void removeEdgeNotExistNode() {
        int edgeSize = g.edgeSize();
        g.removeEdge(6, 4);
        assertEquals(edgeSize, g.edgeSize(), "removeEdge update edgeSize when remove not exist nodes");
    }

    @Test
    void removeEdgeWithHimself() {
        int edgeSize = g.edgeSize();
        g.removeEdge(3, 3);
        assertEquals(edgeSize, g.edgeSize(), "removeEdge update edgeSize when remove edge between node to himself");
    }

    @Test
    void nodeSizeBase() {
        assertEquals(9, g.nodeSize(), "nodeSize didn't return correct size");
    }

    @Test
    void nodeSizeAfterRemoveNode() {
        g.removeNode(0);
        g.removeNode(555);
        assertEquals(7, g.nodeSize(), "nodeSize didn't update nodeSize after remove");
    }

    @Test
    void nodeSizeAfterAddNode() {
        g.addNode(new NodeData(10));
        g.addNode(new NodeData(16));
        g.addNode(new NodeData(3));
        assertEquals(11, g.nodeSize(), "nodeSize didn't update nodeSize after add");
    }

    @Test
    void edgeSizeBase() {
        assertEquals(13, g.edgeSize(), "edgeSize didn't return correct size");
    }

    @Test
    void nodeSizeAfterRemoveEdge() {
        g.removeEdge(-1, -101);
        g.removeEdge(6, -101);
        g.removeEdge(3, -15);
        assertEquals(11, g.edgeSize(), "edgeSize didn't update edgeSize after remove");
    }

    @Test
    void nodeSizeAfterAddEdge() {
        g.connect(0, -101, 9);
        g.connect(3, -1, 0);
        assertEquals(14, g.edgeSize(), "edgeSize didn't update edgeSize after add");
    }

    @Test
    void getMCBase() {
        assertEquals(22, g.getMC(), "getMC didn't return correct size");
    }

    @Test
    void getMCAfterRemoveNode() {
        g.removeNode(0);
        g.removeNode(555);
        assertEquals(30, g.getMC(), "getMC didn't update MC after remove node");
    }


    @Test
    void getMCAfterAddNode() {
        g.addNode(new NodeData(10));
        g.addNode(new NodeData(16));
        g.addNode(new NodeData(0));
        assertEquals(24, g.getMC(), "getMC didn't update MC after add node");
    }

    @Test
    void getMCAfterAddEdge() {
        g.connect(0, -101, 21);
        g.connect(3, -1, 0);
        assertEquals(23, g.getMC(), "getMC didn't update MC after add Edge");
    }

    @Test
    void getMCAfterRemoveEdge() {
        g.removeEdge(-1, -101);
        g.removeEdge(6, -101);
        g.removeEdge(3, -15);
        assertEquals(24, g.getMC(), "getMC didn't update MC after remove Edge");
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