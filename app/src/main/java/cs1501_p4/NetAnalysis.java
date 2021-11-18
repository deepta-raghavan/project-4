package cs1501_p4;

import java.io.*;
import java.io.IOException;
import java.util.*;

public class NetAnalysis implements NetAnalysis_Inter{

    public static ComputerNetwork g;
    public static Scanner scanner;
    public static DijkstraAllPairsSP shortestPaths;
    private int numVertices;
    private int point1;
    private int point2;
    private String cableType;
    private int cableLength;

    public NetAnalysis(String fileName){
        try (Scanner s = new Scanner(new File(fileName))) {
            if (s.hasNextLine()) {
                String line = s.nextLine();
                numVertices = Integer.parseInt(line);

                while (s.hasNext()) {
                    line = s.nextLine();

                    String[] attributes = line.split(" ");
                    point1 = Integer.parseInt(attributes[0]);
                    point2 = Integer.parseInt(attributes[1]);
                    cableType = attributes[2];
                    cableLength = Integer.parseInt(attributes[3]);

                    NetworkEdge edge = new NetworkEdge(point1, point2, cableType, cableLength);

                }
            }
        } catch (IOException e) {
            System.out.println("An Error Occurred");
            e.printStackTrace();
        }
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
