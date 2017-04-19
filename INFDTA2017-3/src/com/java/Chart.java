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
        List<Model> xyAxisDes;
        ForeCastingAlgorithm foreCastingAlgorithm = new ForeCastingAlgorithm();

        public List<Model> controlHandler(double smoothingFactor, int totalMonthsForecast) {
           return foreCastingAlgorithm.runSesAlgorithm(smoothingFactor, totalMonthsForecast);
        }
        public List<Model> controlHandlerDes(double smoothingA, int totalMonths, double smoothingB) {
            return foreCastingAlgorithm.runDesAlgorithm(smoothingA, totalMonths, smoothingB);
        }

        @Override
        public void start(Stage stage) {
            xyAxis = foreCastingAlgorithm.getDemandList();
            xyAxisSes = foreCastingAlgorithm.runSesAlgorithm(0.659100047,12);

            xyAxisDes = foreCastingAlgorithm.runDesAlgorithm(0.73, 12, 0.001);
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
            forecastDes.setName("ForeCasting DES");

            //populating the series with data
            for(Model model : xyAxis) {
                demand.getData().add(new XYChart.Data(model.getX(), model.getY()));
            }
            for(Model model : xyAxisSes) {
                forecastSes.getData().add(new XYChart.Data(model.getX(), model.getY()));
            }
            for(Model model : xyAxisDes) {
                forecastDes.getData().add(new XYChart.Data(model.getX(), model.getY()));
            }
            VBox vBox = new VBox();
            VBox vbox1 = new VBox();

            vBox.setPadding(new Insets(10));
            vBox.setSpacing(8);
            Text StandardError = new Text( " Standard Error " + foreCastingAlgorithm.getStandardError() + "");
            Text AlphaFactor = new Text(" Alpha Value " + foreCastingAlgorithm.getSmoothingAlpha() + "");
            Text BetaFactor = new Text("Gamma value " + foreCastingAlgorithm.getSmoothingBeta() + "");
            TextField smoothingInput = new TextField();
            TextField betaInput = new TextField();
            TextField betaSmoothingInput = new TextField();
            smoothingInput.setPromptText("Fill in the Alpha Value desired for the SES");
            smoothingInput.setMaxWidth(300);
            betaSmoothingInput.setPromptText("Fill the Alpha value desired for the DES");
            betaSmoothingInput.setMaxWidth(300);
            betaInput.setPromptText("Fill in the gamma value desired");
            betaInput.setMaxWidth(300);
            TextField foreCastingMonths = new TextField();
            foreCastingMonths.setMaxWidth(300);
            foreCastingMonths.setPromptText("Total Months u want to forecast");
            Button button = new Button("(Re)-Run Algorithm");

            button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    vbox1.getChildren().clear();
                    forecastSes.getData().clear();
                    forecastDes.getData().clear();
                    if(!smoothingInput.getText().isEmpty()) {
                        xyAxisSes = controlHandler(Double.parseDouble(smoothingInput.getText()), Integer.parseInt(foreCastingMonths.getText()));
                        for (Model model : xyAxisSes) {
                            forecastSes.getData().add(new XYChart.Data(model.getX(), model.getY()));
                        }
                    }
                    if(!betaSmoothingInput.getText().isEmpty()) {
                        xyAxisDes = controlHandlerDes(Double.parseDouble(betaSmoothingInput.getText()), Integer.parseInt(foreCastingMonths.getText()), Double.parseDouble(betaInput.getText()));

                        for (Model model : xyAxisDes) {
                            forecastDes.getData().add(new XYChart.Data(model.getX(), model.getY()));
                        }
                        Text BetaFactor = new Text("Gamma value" +  foreCastingAlgorithm.getSmoothingBeta() + "");
                        vbox1.getChildren().add(BetaFactor);
                    }
                    Text StandardError = new Text( " Standard Error " + foreCastingAlgorithm.getStandardError() + "");
                    Text AlphaFactor = new Text(" Alpha Value " + foreCastingAlgorithm.getSmoothingAlpha() + "");
                    vbox1.getChildren().addAll(StandardError,AlphaFactor);
                }
            });
            vBox.getChildren().addAll(StandardError, AlphaFactor, smoothingInput, BetaFactor, betaSmoothingInput, foreCastingMonths, betaInput, button, vbox1);
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

