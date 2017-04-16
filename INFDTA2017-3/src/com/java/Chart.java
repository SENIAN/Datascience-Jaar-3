import Algorithm.ForeCastingAlgorithm;
import Algorithm.Model;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.jfree.chart.plot.CategoryPlot;
import util.Utility;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed on 4/15/2017.
 */
public class Chart extends Application {

        URL url = ClassLoader.getSystemResource("SwordForeCasting.csv");
        Utility utility = new Utility();
        List<Model> xyAxis;
        ForeCastingAlgorithm foreCastingAlgorithm = new ForeCastingAlgorithm();
        List<Model> xyAxisSes;

        public List<Model> runSesAlgorithm() {
            List<Model> xyAxisSes = new ArrayList<>();
            xyAxis = utility.readDataFile(new File(url.getFile()));
            double initialSes = 0;
            for(int i=0; i < 12; i++) {
                initialSes += xyAxis.get(i).getY();
            }
            initialSes  = initialSes / 12;
            foreCastingAlgorithm.setInitialSmoothingFactorSt(initialSes);
            Model model;
            for(int j=0; j < xyAxis.size(); j++) {
                if(j==0) {
                    model = new Model();
                    model.setX(xyAxis.get(j).getX());
                    model.setY(foreCastingAlgorithm.sessFormula(0.73, xyAxis.get(j).getY(), initialSes));
                    xyAxisSes.add(model);
                }else{
                    model = new Model();
                    model.setX(xyAxis.get(j).getX());
                    model.setY(foreCastingAlgorithm.sessFormula(0.73, xyAxis.get(j).getY(), initialSes));
                    xyAxisSes.add(model);
                }
            }
            //region Test Region
            Model model1 = new Model();
            model1.setX(37);
            model1.setY(270);
            Model model2 = new Model();
            model2.setX(38);
            model2.setY(273);
            Model model3 = new Model();
            model3.setX(39);
            model3.setY(274);
            xyAxisSes.add(model1);
            xyAxisSes.add(model2);
            xyAxisSes.add(model3);
            //endregion
            return xyAxisSes;
        }

        @Override
        public void start(Stage stage) {
            xyAxis = utility.readDataFile(new File(url.getFile()));
            xyAxisSes = runSesAlgorithm();
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

