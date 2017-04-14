import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Mohammed on 2/28/2017.
 */

public class SimpleKMeans {

    static int MaxXValue = 32;
    static int MinXValue = 1;
    static int MaxYValue = 100;
    static int MinYValue = 1;

    public List<Centroid> clustersList = new ArrayList();
    public List<Centroid> lastClusterList;
    private Centroid centroid;
    public List<Offer> clientObs;
    private int numofclusters;
    HashMap<Integer, Double> sseWithIterationMoment =  new HashMap<>();
    public int iterations = 0;

    public static Point createRandomCentroidPoint() {
        Random r = new Random();
        double x = MinXValue + (MaxXValue - MinXValue) * r.nextDouble();
        double y = MinYValue + (MaxYValue - MinYValue) * r.nextDouble();
        return new Point((int) x, (int) y);
    }

    public List<Centroid> createClusters(int NUM_OF_CLUSTERS) {
        numofclusters = NUM_OF_CLUSTERS;
        for (int i = 0; i < NUM_OF_CLUSTERS; i++) {
            centroid = new Centroid(i);
            Point p = createRandomCentroidPoint();
            centroid.setClusterID(i);
            centroid.setCentroid(p);
            clustersList.add(centroid);
            System.out.println("Initial Centroid Location:" + " clusterID: " + i + "  " + clustersList.get(i).getCentroid());
        }
        return clustersList;
    }

    public double calculateEuclideanDistance(Point centroid, Point clientObs) {
        return Math.sqrt(Math.pow((clientObs.getX() - centroid.getX()), 2) + Math.pow((clientObs.getY() - centroid.getY()), 2));
    }


    public double countSSE(List<Offer> list) {
        //formula SSE = (x - mean of X)2
        double sse = 0.0;
        for (Offer offer : list) {
            sse += Math.pow((offer.getFinalDistance()), 2);
        }
        return (int) Math.sqrt(sse);
    }


    public void AssignEachPointToClosestCluster() {
        double max = Double.MAX_VALUE;
        double min = max;
        double distance = 0.0;
        double sse = 0.0;
        int clusterID = 0;
        iterations++;

        //Foreach observation calculate the distance to all clusters.
        for (Offer offer : clientObs) {
            min = max;
            for (int i = 0; i < clustersList.size(); i++) {
                Centroid centroid = clustersList.get(i);
                distance = calculateEuclideanDistance(centroid.getCentroid(), offer.getXylocation());
                // Find the closest centroid and assign the offer to the centroid.
                if (distance < min) {
                    min = distance;
                    clusterID = i;
                    offer.setFinalDistance(min);
                    offer.setClusterID(clusterID);
                }
            }
            // assign the point to the closest centroid
            clustersList.get(clusterID).getOfferList().add(offer);
        }
        List<Offer> list  = new ArrayList<>();
        clustersList.forEach(k-> {
            list.addAll(k.getOfferList());
        });
        sseWithIterationMoment.put(iterations, countSSE(list));
    }

    public void AssignNewCentroidLocation() {
        double sumOfX = 0.0;
        double sumOfY = 0.0;
        double meanX = 0.0;
        double meanY = 0.0;
        int size;
        int changed=0;
        List<Offer> offersList;
       for (int i = 0; i < clustersList.size(); i++) {
            sumOfX = 0.0;
            sumOfY = 0.0;
            meanX = 0;
            meanY = 0;
            offersList = clustersList.get(i).getOfferList();
            size = offersList.size();
            for (int z = 0; z < offersList.size(); z++) {
                sumOfX += offersList.get(z).getXylocation().getX();
                sumOfY += offersList.get(z).getXylocation().getY();
            }
            meanX = sumOfX / size;
            meanY = sumOfY / size;

            // setting new centroid location based on MeanTotal
            clustersList.get(i).getCentroid().setLocation(meanX, meanY);
       }

        for (Centroid centroid1 : clustersList) {
            System.out.println("--------------New Centroid Location-------------");
            System.out.println("Centroid ID:  " + centroid1.getClusterID());
            System.out.println(centroid1.getCentroid().getX() + "   X  " +  "  Y  " + centroid1.getCentroid().getY());
        }

    }

    public double calculateLowestSSE() {
        double lastSSE = Double.MAX_VALUE;
        double newSSE = lastSSE;
        for(Double d : sseWithIterationMoment.values()) {
            lastSSE = d;
            if(newSSE>lastSSE) {
                newSSE = lastSSE;
            }
        }

        System.out.println("Iteration count:");
        System.out.println(sseWithIterationMoment.keySet());
        System.out.println("SSE per Iteration count: ");
        System.out.println(sseWithIterationMoment.values());
        System.out.println("Lowest SSE = " + newSSE);
        return newSSE;
    }
}


