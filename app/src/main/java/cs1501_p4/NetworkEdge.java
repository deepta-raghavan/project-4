package cs1501_p4;

public class NetworkEdge {
    private static final double FLOATING_POINT_EPSILON = 1E-10;
    private final int COPPER_SPEED = 230000000;
    private final int OPTIC_SPEED = 200000000;

    private final int v;             // from
    private final int w;             // to
    private String cableType;
    private int length;
    private double time;

    enum Material {
        COPPER, OPTICAL
    }

    private Material type;

    public NetworkEdge(int v, int w, String cable, int length) {
        if (v < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (w < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        this.v = v;
        this.w = w;
        cableType = cable;
        this.length = length;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    private double calculateTime() {
        double secondsPerMeter;
        if(this.cableType == "copper") {
            secondsPerMeter = ((double) 1)/COPPER_SPEED;
        } else {
            secondsPerMeter = ((double) 1)/OPTIC_SPEED;
        }
        // d = rt, t = d/r
        return length * secondsPerMeter * Math.pow(10, 9); // convert to nanoseconds
    }

    public double time() {
        return time;
    }

}