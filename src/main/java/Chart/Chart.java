package Chart;

import Algorithm.Centroid;
import Algorithm.Model;
import Algorithm.Settings;
import Algorithm.SimpleKMeans;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Mohammed on 4/16/2017.
 */
@SuppressWarnings("Duplicates")
public class Chart extends Application {

    Settings settings = new Settings();

    public List<Centroid> runKmeans() {
        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.clientObs = settings.getDataStream();
        int i = 0;
        kMeans.createClusters(4);
        kMeans.AssignEachPointToClosestCluster();
        kMeans.AssignNewCentroidLocation();
        kMeans.calculateLowestSSE();
        return kMeans.clustersList;
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        List<Centroid> centroidList = runKmeans();

        primaryStage.setTitle("Scatter Chart");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Total Customers");
        yAxis.setLabel("Total Wine Offers");
        //creating the chart
        final ScatterChart<Number,Number> scatterChart =
                new ScatterChart<Number, Number>(xAxis,yAxis);

        scatterChart.setTitle("K-Means Clustering");
        //defining a series
        XYChart.Series points = new XYChart.Series();
        XYChart.Series clusters = new XYChart.Series();
        points.setName("Wine Offers");
        clusters.setName("Clusters");

        //populating the series with data
        centroidList.forEach(k-> {
            k.getOfferList().forEach(v-> {
                points.getData().add(new XYChart.Data(v.getXylocation().getX(), v.getXylocation().getY()));
            });
        });

        centroidList.forEach(k-> {
                clusters.getData().add(new XYChart.Data(k.getCentroid().getX(), k.getCentroid().getY()));
        });

        Scene scene  = new Scene(scatterChart,800,600);
        scatterChart.getData().addAll(points,clusters);


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
