package Algorithm;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Created by Mohammed on 2/28/2017.
 */
public class Centroid {

    public int ClusterID;
    public Point centroid;
    public Centroid(int clusterID) {
        this.ClusterID = clusterID;
    }
    public List<Offer> offerList = new ArrayList<>();

    public List<Offer> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<Offer> offerList) {
        this.offerList = offerList;
    }

    public void setCluster(int clusterID) {
        ClusterID = clusterID;
    }

    public void setClusterID(int clusterID) {
        ClusterID = clusterID;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void updateOffersList(List<Offer> offers) {
        this.offerList = offers;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public int getClusterID() {
        return ClusterID;
    }
}
