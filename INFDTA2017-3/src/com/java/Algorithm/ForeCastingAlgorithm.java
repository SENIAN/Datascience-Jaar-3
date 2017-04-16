package Algorithm;

import Exceptions.ValueOutOfBoundException;

/**
 * Created by Mohammed on 3/1/2017.
 */
public class ForeCastingAlgorithm {


    double initialSmoothingFactorSt;
    double lastSmoothingFactorSt;
    double lastSmoothingFactorBt;
    double initialBetaFactorBt;
    double smoothingAlpha;
    double smoothingBeta;
    double standardError;

    public ForeCastingAlgorithm foreCastingAlgorithm;


    public double sessFormula(double smoothingAlpha, double demand, double initialSmoothingFactorSt) {

        double smoothingSt = smoothingAlpha*demand+(1 - smoothingAlpha)*initialSmoothingFactorSt;
        System.out.println(this.lastSmoothingFactorSt = smoothingSt);
        return smoothingSt;
    }
    public double desFormula(double smoothingAlpha, double demand, double initialSmoothingFactorSt, double smoothingBeta) {
       double St = smoothingAlpha * demand +
               (1-smoothingAlpha) * (initialSmoothingFactorSt
                                  + initialBetaFactorBt);
       double Bt = smoothingBeta * (smoothingAlpha - initialSmoothingFactorSt) + (1- smoothingBeta) * initialBetaFactorBt;
       return St+Bt;

    }


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
        if(smoothingAlpha >= min && smoothingAlpha <= max) {
            this.smoothingAlpha = smoothingAlpha;
        }else {
            throw new ValueOutOfBoundException();
        }
    }
    public void setSmoothingBeta(double smoothingBeta) {
        this.smoothingBeta = smoothingBeta;
    }
    //endregion
}
