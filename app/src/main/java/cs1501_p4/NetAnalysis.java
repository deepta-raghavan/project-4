package cs1501_p4;

import java.io.*;
import java.io.IOException;
import java.util.*;

public class NetAnalysis {
    public static ComputerNetwork g;
    public static Scanner scanner;
    public static DijkstraAllPairsSP shortestPaths;
    public static ArrayList<Integer> latent = new ArrayList<>();
    public static int minBandwidth = 0;
    public static ArrayList<NetworkEdge> minspan;
    public static double lal;
    private ArrayList<NetworkEdge> edges;

    public NetAnalysis(String filename) {
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
        return new NetworkEdge(from, to, capacity, type, length);
    }


    public static boolean connectedTwoVertFail() {
        int V = g.V();
        if (V <= 3) {
            return false;
        }
        boolean doesNotFail = true;
        for (int v1 = 0; v1 < V; v1++) {
            for (int v2 = v1 + 1; v2 < V; v2++) {
                if (!checkIfFailsOnTwoVertices(v1, v2)) {
                    doesNotFail = false;
                    return false;
                }
            }
        }
        if (doesNotFail) {
            return true;
        }
        return true;
    }

    public static boolean checkIfFailsOnTwoVertices(int v1, int v2) {
        boolean[] marked = new boolean[g.V()]; // marked[v] = is there an s-v path
        Queue<Integer> q = new LinkedList<Integer>();
        marked[v1] = true;
        marked[v2] = true;
        int start = 0;
        if (start == v1) start = 1;
        if (start == v2) start = 2;
        marked[start] = true;
        q.add(start);

        int count = 1;
        while (!q.isEmpty()) {
            int v = q.remove();
            for (NetworkEdge e : g.adj(v)) {
                int w = e.to();
                if (!marked[w]) {
                    marked[w] = true;
                    count++;
                    q.add(w);
                }
            }
        }
        return count + 2 == g.V();
    }


    public static ArrayList<Integer> lowestLatencyPath(int u, int w) {
        boolean validVertices = false;
        while (!validVertices) {
            validVertices = u >= 0 && w >= 0;
            calculateLowestPath(u, w);
        }
        return latent;
    }

    public static void calculateLowestPath(int u, int w) {
        if (shortestPaths.hasPath(u, w)) {
            latent.add(u);
            minBandwidth = Integer.MAX_VALUE;
            for (NetworkEdge e : shortestPaths.path(u, w)) {
                minBandwidth = Math.min(minBandwidth, e.bandwidth());
                latent.add(e.to());
            }
        }
    }

    public int bandwidthAlongPath(ArrayList<Integer> p) throws IllegalArgumentException {
        int minbwith = 0;
        if (shortestPaths.hasPath(p.get(0), p.get(p.size() - 1))) {
            minbwith = Integer.MAX_VALUE;
            for (NetworkEdge e : shortestPaths.path(p.get(0), p.get(p.size() - 1))) {
                minbwith = Math.min(minBandwidth, e.bandwidth());
            }
            return minbwith;
        }
        throw new IllegalArgumentException("Path not valid");
    }


    public boolean copperOnlyConnected() {
        int V = g.V();
        int count = bfs(0);
        boolean onlyCopperConnected = count == V;
        if (onlyCopperConnected) {
            return true;
        } else {
            return false;
        }
    }

    private static int bfs(int s) {
        boolean[] marked = new boolean[g.V()]; // marked[v] = is there an s-v path
        Queue<Integer> q = new LinkedList<Integer>();
        marked[s] = true;
        q.add(s);
        int count = 1;
        while (!q.isEmpty()) {
            int v = q.remove();
            for (NetworkEdge e : g.adj(v)) {
                int w = e.to();
                if (!marked[w] && e.type() == NetworkEdge.Material.COPPER) {
                    marked[w] = true;
                    count++;
                    q.add(w);
                }
            }
        }
        return count;
    }

    public ArrayList<STE> lowestAvgLatST() {
        ArrayList<STE> arr = new ArrayList<>();
        if (minspan == null) {
            lal = KruskalMST() / minspan.size();
        }

        for (NetworkEdge e : minspan) {
            STE addthis = new STE(e.from(), e.to());
            arr.add(addthis);
        }
        return arr;
    }

    private double KruskalMST() {
        for(NetworkEdge hi:g.edges()){
            edges.add(hi);
        }
        int[] parent = new int[g.V()];
        byte[] rank = new byte[g.V()];
        for (int i = 0; i < g.V(); i++) {
            parent[i] = i;
            rank[i] = 0;
        }


        Collections.sort(edges, (e1, e2) -> e1.compareTo(e2));

        minspan = new ArrayList<NetworkEdge>();
        double weight = 0.0;


        int currEdge = 0;
        while (currEdge != edges.size() - 1 && minspan.size() < g.V() - 1) {
            NetworkEdge e = edges.get(currEdge);
            int v = e.from();
            int w = e.to();
            if (!connected(v, w, parent)) {
                union(v, w, parent, rank);
                minspan.add(e);
                weight += e.time();
            }

            currEdge++;
        }

        return weight;
    }

    private boolean connected(int p, int q, int[] parent) {
        return find(p, parent) == find(q, parent);
    }


    private int find(int p, int[] parent) {
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    private void union(int p, int q, int[] parent, byte[] rank) {
        int rootP = find(p, parent);
        int rootQ = find(q, parent);
        if (rootP == rootQ) return;


        if (rank[rootP] < rank[rootQ]) parent[rootP] = rootQ;
        else if (rank[rootP] > rank[rootQ]) parent[rootQ] = rootP;
        else {
            parent[rootQ] = rootP;
            rank[rootP]++;
        }
    }
}