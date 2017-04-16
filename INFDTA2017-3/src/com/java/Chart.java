import Algorithm.ForeCastingAlgorithm;
import Algorithm.Model;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import util.GenericFileParser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed on 4/15/2017.
 */
public class Chart extends Application {

        List<Model> xyAxis;
        List<Model> xyAxisSes;
        ForeCastingAlgorithm foreCastingAlgorithm = new ForeCastingAlgorithm();

        @Override
        public void start(Stage stage) {
            xyAxis = foreCastingAlgorithm.getDemandList();
            xyAxisSes = foreCastingAlgorithm.runSesAlgorithm(0.659100047);

            foreCastingAlgorithm.runDesAlgorithm();
            stage.setTitle("Line Chart");
            //defining the axes
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();

            xAxis.setLabel("Month Number");
            yAxis.setLabel("Demand");
            //creating the chart
            final LineChart<Number,Number> lineChart =
                    new LineChart<Number,Number>(xAxis,yAxis);

            lineChart.setTitle("Sword Sell Forcasting");
            //defining a series
            XYChart.Series demand = new XYChart.Series();
            XYChart.Series forecastSes = new XYChart.Series();
            XYChart.Series forecastDes = new XYChart.Series();

            demand.setName("Swords Demand");
            forecastSes.setName("ForeCasting SES");
            forecastDes.setName("ForeCasting Des");
            //populating the series with data
            for(Model model : xyAxis) {
                demand.getData().add(new XYChart.Data(model.getX(), model.getY()));
            }
            for(Model model : xyAxisSes) {
                forecastSes.getData().add(new XYChart.Data(model.getX(), model.getY()));
            }

            Scene scene  = new Scene(lineChart,800,600);
            lineChart.getData().addAll(demand, forecastSes,forecastDes);


            stage.setScene(scene);
            stage.show();
        }
        public static void main(String[] args) {
            launch(args);
        }
    }

