package cs1501_p4;

import java.io.*;
import java.io.IOException;
import java.util.*;

public class NetAnalysis implements NetAnalysis_Inter{

    public static ComputerNetwork g;
    public static Scanner scanner;
    public static DijkstraAllPairsSP shortestPaths;

    public NetAnalysis(String filename){
        System.out.println("Loading graph....");

        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            int V = Integer.parseInt(in.readLine());
            g = new ComputerNetwork(V);
            String line;
            while ((line = in.readLine()) != null) {
                String[] edgeData = line.split(" ");
                NetworkEdge e = createEdge(edgeData);
                g.addEdge(e);
                String tmpVertex = edgeData[0];
                edgeData[0] = edgeData[1];
                edgeData[1] = tmpVertex;
                NetworkEdge otherE = createEdge(edgeData);
                g.addEdge(otherE);
                shortestPaths = new DijkstraAllPairsSP(g);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not load file.");
        } catch (IOException e) {
            System.out.println("Error processing file.");
        }
    }

    public static NetworkEdge createEdge(String[] edgeData) {
        int from = Integer.parseInt(edgeData[0]);
        int to = Integer.parseInt(edgeData[1]);
        String type = edgeData[2];
        int capacity = Integer.parseInt(edgeData[3]);
        int length = Integer.parseInt(edgeData[4]);
        return new NetworkEdge(from, to, capacity, length, type);
    }


    public ArrayList<Integer> lowestLatencyPath(int u, int w) {
            Iterable<NetworkEdge> itr = shortestPaths.path(u,w);
            ArrayList<Integer> path = new ArrayList<Integer>();
            for (NetworkEdge x : itr)
            {
                path.add(x.from());
        }
        return path;
    }

    @Override
    public int bandwidthAlongPath(ArrayList<Integer> p) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public boolean copperOnlyConnected() {
        return false;
    }

    @Override
    public boolean connectedTwoVertFail() {
        return false;
    }

    @Override
    public ArrayList<STE> lowestAvgLatST() {
        return null;
    }
}
