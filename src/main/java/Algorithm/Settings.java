package Algorithm;

import util.GenericFileParser;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;


/**
 * Created by Mohammed on 2/21/2017.
 */
public class Settings {





    private List<Offer> offerObs;
    private List<Offer> genericOfferObs;
    Point point;
    File file;
    Offer offer;
    Model genericSettingModel = new Model();
    private GenericFileParser genericFileParser = new GenericFileParser();

    public List<Offer> getGenericDataStream() {
        List<Model> modelList;
        Offer offer;
        Point point;
        genericOfferObs = new ArrayList();
        URL path = ClassLoader.getSystemResource("");
        modelList = genericFileParser.readDataFile(new File(path.getFile()));
        int i =0;
        for(Model model: modelList) {
            offer = new Offer();
            point = new Point();
            point.setLocation(model.getX(), model.getY());
            offer.setXylocation(point);
            offer.setOfferID(i++);
            genericOfferObs.add(offer);
        }
        return genericOfferObs;

    }

    public List<Offer> getDataStream() {

        try {
            URL path = ClassLoader.getSystemResource("WineData.csv");
            file = new File(path.toURI());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            int i = 0;
            int x = 0;
            offerObs = new ArrayList();
            while ((line = br.readLine()) != null) {
                String[] tempLine = line.split(",");
                i++;
                for (int y = 0; y < tempLine.length; y++) {

                    point = new Point();
                    offer = new Offer();
                    if ((Integer.valueOf(tempLine[y]) != 0)) {
                        point.setLocation((double) i, y);
                        offer.setOfferID(x++);
                        offer.setXylocation(point);
                        offerObs.add(offer);
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return offerObs;
    }

    public static void main(String[] args) {
        Settings settings = new Settings();
        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.clientObs = settings.getDataStream();
        int i = 0;
        kMeans.createClusters(4);

        while (i++ != 10) {
            kMeans.AssignEachPointToClosestCluster();
            kMeans.AssignNewCentroidLocation();
            kMeans.clustersList.forEach(k -> {
                k.getOfferList().clear();
            });
        }
        kMeans.calculateLowestSSE();
    }
}