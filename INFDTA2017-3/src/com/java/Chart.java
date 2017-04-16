import Algorithm.ForeCastingAlgorithm;
import Algorithm.Model;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Mohammed on 4/15/2017.
 */
public class Chart extends Application {

        List<Model> xyAxis;
        List<Model> xyAxisSes;
        ForeCastingAlgorithm foreCastingAlgorithm = new ForeCastingAlgorithm();

        public List<Model> controlHandler(double smoothingFactor, int totalMonthsForecast) {
           return foreCastingAlgorithm.runSesAlgorithm(smoothingFactor, totalMonthsForecast);
        }

        @Override
        public void start(Stage stage) {
            xyAxis = foreCastingAlgorithm.getDemandList();
            xyAxisSes = foreCastingAlgorithm.runSesAlgorithm(0.659100047,12);

            foreCastingAlgorithm.runDesAlgorithm(0.659100047, 12);
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

            VBox vBox = new VBox();
            VBox vbox1 = new VBox();

            vBox.setPadding(new Insets(10));
            vBox.setSpacing(8);
            Text StandardError = new Text( " Standard Error " + foreCastingAlgorithm.getStandardError() + "");
            Text AlphaFactor = new Text(" Alpha Value " + foreCastingAlgorithm.getSmoothingAlpha() + "");
            TextField smoothingInput = new TextField();
            smoothingInput.setPromptText("Fill in the Alpha Value desired");
            smoothingInput.setMaxWidth(200);
            TextField foreCastingMonths = new TextField();
            foreCastingMonths.setMaxWidth(200);
            foreCastingMonths.setPromptText("Total Months u want to forecast");
            Button button = new Button("(Re)-Run Algorithm");

            button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    vbox1.getChildren().clear();
                    forecastSes.getData().clear();
                    xyAxisSes = controlHandler(Double.parseDouble(smoothingInput.getText()), Integer.parseInt(foreCastingMonths.getText()));
                    for(Model model : xyAxisSes) {
                        forecastSes.getData().add(new XYChart.Data(model.getX(), model.getY()));
                    }
                    Text StandardError = new Text( " Standard Error " + foreCastingAlgorithm.getStandardError() + "");
                    Text AlphaFactor = new Text(" Alpha Value " + foreCastingAlgorithm.getSmoothingAlpha() + "");
                    vbox1.getChildren().addAll(StandardError,AlphaFactor);
                }
            });
            vBox.getChildren().addAll(StandardError, AlphaFactor, smoothingInput, button, foreCastingMonths,vbox1);
            vBox.getChildren().add(lineChart);
            Scene scene  = new Scene(vBox,800,600);
            lineChart.getData().addAll(demand, forecastSes,forecastDes);
            stage.setScene(scene);
            stage.show();
        }
        public static void main(String[] args) {
            launch(args);
        }
    }

