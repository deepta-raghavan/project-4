/**
 * A driver for CS1501 Project 4
 * @author	Dr. Farnan
 */
package cs1501_p4;

import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        NetAnalysis na = new NetAnalysis("app/build/resources/main/network_data2.txt");
        ArrayList<Integer> p=na.lowestLatencyPath(0,7);
        for(int i:p){
            System.out.println(i);
        }
        System.out.println(na.bandwidthAlongPath(p));
        System.out.println(na.connectedTwoVertFail());

        ArrayList<STE> q = na.lowestAvgLatST();
        /*for(STE i:q){
            System.out.println(i.toString());
        }*/
    }
}
