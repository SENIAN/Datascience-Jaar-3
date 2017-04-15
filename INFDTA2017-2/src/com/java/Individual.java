import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Mohammed on 3/1/2017.
 */

@Data
public class Individual {

    private int BirthEgg;
    private double fitness;
    private String chronosome;
    private double changeToBePicked;
    private int IndividualID;

    public void setIndividualID(int individualID) {
        IndividualID = individualID;
    }
    public int getIndividualID() {
        return IndividualID;
    }
    public Individual() {

    }
    public Individual(int birthEgg) {
        this.BirthEgg = birthEgg;
    }
    public Individual(String chronosome) {}
    public double getChangeToBePicked() {
        return changeToBePicked;
    }
    public void setChangeToBePicked(double changeToBePicked) {
        this.changeToBePicked = changeToBePicked;
    }
    public void setBirthEgg(int birthEgg) {
        BirthEgg = birthEgg;
    }
    public int getBirthEgg() {
        return BirthEgg;
    }
    public double getFitness() {
        return fitness;
    }
    public String getChronosome() {
        return chronosome;
    }
    public void setChronosome(String chronosome) {
        this.chronosome = chronosome;
    }
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

}