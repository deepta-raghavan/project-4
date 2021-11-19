package cs1501_p4;

public class NetworkEdge {
    private static final double FLOATING_POINT_EPSILON = 1E-10;
    private final int COPPER_SPEED = 230000000;
    private final int OPTIC_SPEED = 200000000;

    private final int v;
    private final int w;
    private int bandwidth;
    private int length;
    private double time;

    enum Material {
        COPPER, OPTICAL
    }

    private Material type;

    public NetworkEdge(int v, int w, int bandwidth, String type, int length) {
        if (v < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (w < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (!(bandwidth >= 0.0)) throw new IllegalArgumentException("edge bandwidth must be non-negative");
        this.v = v;
        this.w = w;
        this.bandwidth = bandwidth;
        this.length = length;
        this.type = type.equals("copper") ? Material.COPPER : Material.OPTICAL;
        this.time = calculateTime();
    }

    public NetworkEdge(NetworkEdge e) {
        this.v = e.v;
        this.w = e.w;
        this.bandwidth = e.bandwidth;
        this.type = e.type;
        this.length = e.length;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public int bandwidth() {
        return bandwidth;
    }

    public double time() {
        return time;
    }

    public Material type() {
        return type;
    }

    private double calculateTime() {
        double secondsPerMeter;
        if (this.type == Material.COPPER) {
            secondsPerMeter = ((double) 1) / COPPER_SPEED;
        } else {
            secondsPerMeter = ((double) 1) / OPTIC_SPEED;
        }
        // d = rt, t = d/r
        return length * secondsPerMeter * Math.pow(10, 9); // convert to nanoseconds
    }


    public int compareTo(NetworkEdge otherEdge) {
        if (time > otherEdge.time()) {
            return 1;
        } else if (time == otherEdge.time()) {
            return 0;
        } else {
            return -1;
        }
    }

    public String toString() {
        return v + "->" + w + " /" + bandwidth;
    }

}