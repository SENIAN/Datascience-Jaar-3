package Chart;

import Algorithm.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.renderer.xy.XYItemRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by Mohammed on 4/16/2017.
 */
@SuppressWarnings("Duplicates")
public class Chart extends Application {

    Settings settings = new Settings();
    List<Centroid> centroidList;
    List<Centroid> pointList;

    SimpleKMeans kMeans = new SimpleKMeans();

    public List<Centroid> runKmeans() {
            kMeans.AssignEachPointToClosestCluster();
            kMeans.AssignNewCentroidLocation();
            kMeans.calculateLowestSSE();

        return kMeans.clustersList;
    }

    public List runParameters(int iterations, int clustersSize) {
        kMeans.clientObs = settings.getDataStream();
        kMeans.createClusters(clustersSize);
        if(iterations==0) {
            centroidList = runKmeans();
        }
            while (iterations-- != 0) {
                centroidList.clear();
                centroidList.addAll(runKmeans());
            }
            return centroidList;
        }



    @Override
    public void start(Stage primaryStage) throws Exception {
        pointList = runParameters(0, 4);
        primaryStage.setTitle("Scatter Chart");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Total Customers");
        yAxis.setLabel("Total Wine Offers");
        //creating the chart
        final ScatterChart<Number,Number> scatterChart =
                new ScatterChart<>(xAxis,yAxis);

        scatterChart.setTitle("K-Means Clustering");
        //defining a series
        XYChart.Series points = new XYChart.Series();
        XYChart.Series clusters = new XYChart.Series();
        points.setName("Wine Offers");
        clusters.setName("Clusters");


        VBox vBoxChart = new VBox();
        VBox vBox = new VBox();
        Button button = new Button("Run K Means");
        TextField iterations = new TextField();
        iterations.setPromptText("Amount of Iterations");
        TextField countOfClusters = new TextField();
        countOfClusters.setPromptText("Cluster Size");
        Label label  = new Label("Sum of Squared Errors");
        vBox.getChildren().addAll(iterations, countOfClusters, label,button);

        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clusters.getData().clear();
                List<Centroid> centroidList = runParameters(Integer.parseInt(iterations.getText()), Integer.parseInt(countOfClusters.getText()));
                for(Centroid centroid : centroidList) {
                    clusters.getData().add(new XYChart.Data(centroid.getCentroid().getX(), centroid.getCentroid().getY()));
                }
                centroidList.clear();
           }
        });

        pointList.forEach(k-> {
            k.getOfferList().forEach(v-> {
                points.getData().add(new XYChart.Data(v.getXylocation().getX(), v.getXylocation().getY()));
                {
                }
            });
        });



        scatterChart.getData().addAll(points,clusters);
        vBoxChart.getChildren().addAll(scatterChart, vBox);
        //populating the series with data



        Scene scene  = new Scene(vBoxChart,800,600);


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
