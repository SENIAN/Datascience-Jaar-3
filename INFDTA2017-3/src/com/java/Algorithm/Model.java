package Algorithm;

/**
 * Created by Mohammed on 4/15/2017.
 */
public class Model {

    //region instance variables
    double X; // X Axis
    double Y; // Y Axis
    double DemandY; // Demand Axis
    double desYSt; // Updated equation for des
    double OSFE; // One Step forcast error
    double FE; // ForeCast error
    double SSE; // Sum of Squared Errors
    //endregion
    //region getters and setters
    public double getDesYSt() {
        return desYSt;
    }
    public void setDesYSt(double desYSt) {
        this.desYSt = desYSt;
    }
    public double getDemandY() {
        return DemandY;
    }
    public void setDemandY(double demandY) {
        DemandY = demandY;
    }
    public double getSumOfSquaredErrors() {
        return SSE;
    }
    public void setSSE(double sumOfSquaredErrors) {
        this.SSE = sumOfSquaredErrors;
    }
    public void setX(double x) {
        X = x;
    }
    public double getForeCastError() {
        return FE;
    }
    public void setForeCastError(double foreCastError) {
        this.FE = foreCastError;
    }
    public double getOneStepForeCastError() {
        return OSFE;
    }
    public void setOneStepForeCastError(double oneStepForeCastError) {
        this.OSFE = oneStepForeCastError;
    }
    public double getY() {
        return Y;
    }
    public double getX() {
        return X;
    }
    public void setY(double y) {
        Y = y;
    }
    //endregion
}

