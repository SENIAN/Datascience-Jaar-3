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

        URL url = ClassLoader.getSystemResource("SwordForeCasting.csv");
    GenericFileParser genericFileParser = new GenericFileParser();
    List<Model> xyAxis;
    List<Model> xySesList;
    //endregion

    //region formula's
    public double sessFormula(double smoothingAlpha, double demand, double initialSmoothingFactorSt) {

        double smoothingSt = smoothingAlpha * demand + (1 - smoothingAlpha) * initialSmoothingFactorSt;
        this.lastSmoothingFactorSt = smoothingSt;
        return smoothingSt;
    }
    public double desFormula(double smoothingAlpha, double demand, double initialSmoothingFactorSt, double smoothingBeta) {
        double St = smoothingAlpha * demand +
                (1 - smoothingAlpha) * (initialSmoothingFactorSt
                        + initialBetaFactorBt);
        double Bt = smoothingBeta * (St - initialSmoothingFactorSt) + (1 - smoothingBeta) * initialBetaFactorBt;
        return St + Bt;

    }
    //endregion

    //region Ses and Des algorithm
    public List<Model> runSesAlgorithm(double pickSmoothingFactor, int totalMonthsForcast) {
        this.smoothingAlpha = pickSmoothingFactor;
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
            if(j-1==0) {
                xyAxisSes.get(j-1).setOneStepForeCastError(getInitialSmoothingFactorSt());
            }
            xyAxisSes.get(j).setOneStepForeCastError(xyAxisSes.get(j-1).getY());
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
        int currentMonth = xyAxisSes.size()-1;
        for(int i=1; i <= totalMonthsForcast; i++) {
                foreCastModel = new Model();
                int nextMonth = currentMonth+i;
                foreCastModel.setX(nextMonth);
                foreCastModel.setY(xyAxisSes.get(currentMonth).getY());
                xyAxisSes.add(foreCastModel);
        }
        //endregion
        return xyAxisSes;
    }

    public List<Model> runDesAlgorithm(double pickSmoothingFactor, int totalMonthsForecast) {
        this.smoothingAlpha = pickSmoothingFactor;
        xyAxis = genericFileParser.readDataFile(new File(url.getFile()));
        xySesList = runSesAlgorithm(pickSmoothingFactor, 12);
        HashMap<Model, Model> desList = new HashMap<>();
        setInitialBetaFactorBt(xyAxis.get(1).getY() - xyAxis.get(0).getY());
        for (int i = 0; i < xyAxis.size(); i++) {
            desList.put(xyAxis.get(i), xySesList.get(i));
        }
        return new ArrayList<>();
    }
    //endregion

    //region Minimize Error function
    public void standardError() {
        double st = Math.sqrt(sumOfSquaredErrors / xyAxis.size()-1);
        this.standardError =  st;
    }
    //endregion

    //region Util
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
    //endregion
}
