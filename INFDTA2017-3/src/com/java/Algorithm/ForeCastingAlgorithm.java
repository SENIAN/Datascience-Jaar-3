package Algorithm;

import Exceptions.ValueOutOfBoundException;
import util.GenericFileParser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mohammed on 3/1/2017.
 */
@SuppressWarnings("duplicated")
public class ForeCastingAlgorithm {

    double sumOfSquaredErrors = 0;

    //region instance variables
    double initialSmoothingFactorSt;
    double lastSmoothingFactorSt;
    double lastSmoothingFactorBt;
    double initialBetaFactorBt;
    double smoothingAlpha;
    double smoothingBeta;
    double standardError;
    double initialTrend;

    URL url = ClassLoader.getSystemResource("SwordForeCasting.csv");
    GenericFileParser genericFileParser = new GenericFileParser();
    List<Model> xyAxis;
    //endregion

    //region formula's
    public double sessFormula(double smoothingAlpha, double demand, double initialSmoothingFactorSt) {

        double smoothingSt = smoothingAlpha * demand + (1 - smoothingAlpha) * initialSmoothingFactorSt;
        this.lastSmoothingFactorSt = smoothingSt;
        return smoothingSt;
    }

    public double StUpdatedDes(double smoothingAlpha, double demand, double initialSmoothingFactorSt, double initialTrend) {
        return smoothingAlpha * demand + (1 - smoothingAlpha) * (initialSmoothingFactorSt + initialTrend);
    }

    public double BtUpdatedDes(double smoothingBeta, double updatedStDes, double lastSmoothingFactorSt, double lastSmoothingFactorBt) {
        return smoothingBeta * (updatedStDes - lastSmoothingFactorSt) + (1 - smoothingBeta) * lastSmoothingFactorBt;
    }
    //endregion

    //region Ses and Des algorithm
    public List<Model> runSesAlgorithm(double pickSmoothingFactor, int totalMonthsForcast) {
        setSmoothingAlpha(pickSmoothingFactor);
        List<Model> xyAxisSes = new ArrayList<>();
        xyAxis = genericFileParser.readDataFile(new File(url.getFile()));
        Model model;
        Model foreCastModel;
        double foreCast;
        /*Setting the level estimate e.g. St*/
        for (int j = 0; j < xyAxis.size(); j++) {
            if (j == 0) {
                foreCast = sessFormula(pickSmoothingFactor, xyAxis.get(j).getY(), initialSes());
                model = new Model();
                model.setX(xyAxis.get(j).getX());
                model.setDemandY(xyAxis.get(j).getY());
                model.setY(foreCast);
                xyAxisSes.add(model);
            } else {
                foreCast = sessFormula(pickSmoothingFactor, xyAxis.get(j).getY(), getLastSmoothingFactorSt());
                model = new Model();
                model.setX(xyAxis.get(j).getX());
                model.setY(foreCast);
                model.setDemandY(xyAxis.get(j).getY());
                xyAxisSes.add(model);
            }
        }

        //region One-Step forecast error
        /* Set the One-Step Forecast Error*/
        for (int j = 1; j < xyAxisSes.size(); j++) {
            /*Set Initial Smoothing factor as ForeCast error j-1==0;*/
            if (j - 1 == 0) {
                xyAxisSes.get(j - 1).setOneStepForeCastError(getInitialSmoothingFactorSt());
            }
            xyAxisSes.get(j).setOneStepForeCastError(xyAxisSes.get(j - 1).getY());
        }
        //endregion
        //region Setting the foreCast error + sum of Squared error
        double forecastError;
        this.sumOfSquaredErrors = 0;
        for (int i = 0; i < xyAxisSes.size(); i++) {
            forecastError = xyAxisSes.get(i).getDemandY() - xyAxisSes.get(i).getOneStepForeCastError();
            xyAxisSes.get(i).setForeCastError(forecastError);
            double SquaredErrors = Math.pow(forecastError, 2);
            xyAxisSes.get(i).setSSE(SquaredErrors);
            this.sumOfSquaredErrors += SquaredErrors;
        }
        //endregion
        //region forecast coming months region
        int currentMonth = xyAxisSes.size() - 1;
        for (int i = 1; i <= totalMonthsForcast; i++) {
            foreCastModel = new Model();
            int nextMonth = currentMonth + i;
            foreCastModel.setX(nextMonth);
            foreCastModel.setY(xyAxisSes.get(currentMonth).getY());
            xyAxisSes.add(foreCastModel);
        }
        //endregion
        return xyAxisSes;
    }

    public List<Model> runDesAlgorithm(double pickSmoothingFactorA, int totalMonthsForecast, double pickSmoothingFactorB) {
        setSmoothingAlpha(pickSmoothingFactorA);
        setSmoothingBeta(pickSmoothingFactorB);
        xyAxis = genericFileParser.readDataFile(new File(url.getFile()));
        List<Model> xyAxisDesList = new ArrayList<>();
        Model model;
        Model foreCastModelDes;
        double forecastDes;
        for (int i = 0; i < xyAxis.size(); i++) {
            if (i == 0) {
                /*Insert empty row*/
                model = new Model();
                model.setX(xyAxis.get(i).getX());
                model.setY(xyAxis.get(i).getY());
                xyAxisDesList.add(model);
            } else if (i == 1) {
                /*Des initialization*/
                model = new Model();
                model.setX(xyAxis.get(i).getX());
                model.setY(xyAxis.get(i).getY());
                model.setDesYSt(xyAxis.get(i).getY());
                model.setDesYBt(initialTrend());
                setInitialTrend(initialTrend());
                model.setDemandY(xyAxis.get(i).getY());
                xyAxisDesList.add(model);
            } else {
                /*Forecast starts here third row*/
                model = new Model();
                model.setX(xyAxis.get(i).getX());
                model.setDemandY(xyAxis.get(i).getY());
                model.setDesYSt(StUpdatedDes(smoothingAlpha, xyAxis.get(i).getY(), xyAxisDesList.get(i - 1).getDesYSt(), xyAxisDesList.get(i - 1).getDesYBt()));
                model.setDesYBt(BtUpdatedDes(smoothingBeta, model.getDesYSt(), xyAxisDesList.get(i - 1).getDesYSt(), xyAxisDesList.get(i - 1).getDesYBt()));
                model.setY(model.getDesYSt() + model.getDesYBt());
                xyAxisDesList.add(model);
            }
         }

            int size = xyAxisDesList.size();
            int currentmonth = size -1;
            int nextmonth;
            //region forecast coming months region
            for (int j = 1; j <= totalMonthsForecast; j++) {
                foreCastModelDes = new Model();
                nextmonth = currentmonth + j;
                forecastDes = foreCastFbm(xyAxisDesList.get(currentmonth).getDesYSt(),currentmonth, nextmonth, xyAxisDesList.get(currentmonth).getDesYBt()) ;
                foreCastModelDes.setX(nextmonth);
                foreCastModelDes.setY(forecastDes);
                xyAxisDesList.add(foreCastModelDes);
            }
            //endregion

        return xyAxisDesList;
    }
    //endregion

    //region Minimize Error function
    public void standardError() {
        double st = Math.sqrt(sumOfSquaredErrors / xyAxis.size() - 1);
        this.standardError = st;
    }
    //endregion

    //region Util
    public double initialTrend() {
        return xyAxis.get(1).Y - xyAxis.get(0).Y;
    }

    public double foreCastFbm(double Sn , double bm, double bm1, double Bn) {
        return Sn + (bm1 - bm) * Bn;
    }

    public List<Model> getDemandList() {
        List<Model> demand;
        demand = genericFileParser.readDataFile(new File(url.getFile()));
        return demand;
    }

    public double initialSes() {
        double topUpVal = 0;
        double initialSes = 0;
        for (int i = 0; i < 12; i++) {
            topUpVal += xyAxis.get(i).getY();
        }
        initialSes = topUpVal / 12;

        return this.initialSmoothingFactorSt = initialSes;
    }


    //endregion

    //region Getters and Setters

    public void setLastSmoothingFactorBt(double lastSmoothingFactorBt) {
        this.lastSmoothingFactorBt = lastSmoothingFactorBt;
    }

    public void setLastSmoothingFactorSt(double lastSmoothingFactorSt) {
        this.lastSmoothingFactorSt = lastSmoothingFactorSt;
    }

    public double getInitialBetaFactorBt() {
        return initialBetaFactorBt;
    }

    public double getLastSmoothingFactorBt() {
        return lastSmoothingFactorBt;
    }

    public double getLastSmoothingFactorSt() {
        return lastSmoothingFactorSt;
    }

    public double getStandardError() {
        standardError();
        return standardError;
    }

    public void setInitialBetaFactorBt(double initialBetaFactorBt) {
        this.initialBetaFactorBt = initialBetaFactorBt;
    }

    public void setStandardError(double standardError) {
        this.standardError = standardError;
    }

    public double getInitialSmoothingFactorSt() {
        return initialSmoothingFactorSt;
    }

    public double getSmoothingAlpha() {
        return smoothingAlpha;
    }

    public double getSmoothingBeta() {
        return smoothingBeta;
    }

    public void setInitialSmoothingFactorSt(double initialSmoothingFactorSt) {
        this.initialSmoothingFactorSt = initialSmoothingFactorSt;
    }

    public void setSmoothingAlpha(double smoothingAlpha) {
        double min = 0.0;
        double max = 1.0;
        if (smoothingAlpha >= min && smoothingAlpha <= max) {
            this.smoothingAlpha = smoothingAlpha;
        } else {
            throw new ValueOutOfBoundException();
        }
    }

    public void setSmoothingBeta(double smoothingBeta) {
        this.smoothingBeta = smoothingBeta;
    }

    public void setInitialTrend(double initialTrend) {
        this.initialTrend = initialTrend;
    }

    public double getInitialTrend() {
        return initialTrend;
    }
    //endregion
}
