package Algorithm;

import Algorithm.Centroid;

import java.awt.*;

/**
 * Created by Mohammed on 2/28/2017.
 */
public class Offer {

    public int OfferID;
    public Point point;
    public double finalDistance;
    public int ClusterID;
    public Offer(int offerID, Point point, Centroid centroid) {
        this.OfferID = offerID;
        this.point = point;
    }

    public Offer() {

    }

    public void setClusterID(int clusterID) {
        ClusterID = clusterID;
    }

    public int getClusterID() {
        return ClusterID;
    }

    public void setFinalDistance(double finalDistance) {
        this.finalDistance = finalDistance;
    }

    public double getFinalDistance() {
        return finalDistance;
    }

    public int getOfferID() {
        return OfferID;
    }

    public void setOfferID(int offerID) {
        OfferID = offerID;
    }

    public Point getXylocation() {
        return point;
    }

    public void setXylocation(Point xylocation) {
        this.point = xylocation;
    }
}
