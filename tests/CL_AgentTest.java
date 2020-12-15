import api.DWGraph_DS;
import api.NodeData;
import api.directed_weighted_graph;
import gameClient.CL_Agent;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CL_AgentTest {

    private String agentJSON = "{\"Agent\":{\"id\":0,\"value\":13.0,\"src\":3,\"dest\":-1,\"speed\":1.0,\"pos\":\"35.21277812104085,32.10516650840175,0.0\"}}";
    private CL_Agent myAgent;
    private directed_weighted_graph g;

    @Test
    void update() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        assertEquals(agentJSON, myAgent.toJSON(), "update didn't update successfully the agent");
    }

    @Test
    void getSrcNode() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        assertEquals(3, myAgent.getSrcNode(), "getSrcNode didn't return successfully the SrcNode");
    }

    @Test
    void toJSON() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        assertEquals(agentJSON, myAgent.toJSON(), "toJSON didn't return successfully the agent");
    }

    @Test
    void setNextNode() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        myAgent.setNextNode(-1);
        assertEquals(-1, myAgent.getNextNode(), "setNextNode didn't set's successfully the NextNode");
    }

    @Test
    void setCurrNode() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.setCurrNode(3);
        assertEquals(3, myAgent.getSrcNode(), "setCurrNode didn't set's successfully the CurrNode");
    }

    @Test
    void isMoving() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        assertFalse(myAgent.isMoving(), "isMoving didn't return successfully the ans");
    }

    @Test
    void getID() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        assertEquals(0, myAgent.getID(), "getID didn't return successfully the ID");
    }

    @Test
    void getLocation() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        assertEquals(new Point3D(35.21277812104085, 32.10516650840175, 0.0), myAgent.getLocation(), "getLocation didn't return successfully the Location");
    }

    @Test
    void getValue() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        assertEquals(13.0, myAgent.getValue(), "getValue didn't return successfully the Value");
    }

    @Test
    void getNextNode() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        myAgent.setNextNode(-1);
        assertEquals(-1, myAgent.getNextNode(), "getNextNode didn't set's successfully the NextNode");
    }

    @Test
    void getSpeed() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        assertEquals(1.0, myAgent.getSpeed(), "getSpeed didn't return successfully the Speed");
    }

    @Test
    void setSpeed() {
        g = graph_creator();
        myAgent = new CL_Agent(g, 0);
        myAgent.update(agentJSON);
        myAgent.setSpeed(2);
        assertEquals(2.0, myAgent.getSpeed(), "setSpeed didn't set's successfully the Speed");
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