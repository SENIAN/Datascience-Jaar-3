import java.util.List;

/**
 * Created by Mohammed on 4/15/2017.
 */
public class GeneticStarter extends GeneticAlgorithm {

    public void initializeAlgorithm(double mutationRate,
                                         double crossOverRate,
                                         boolean elitism, int numberIterations, int initialPopsize) {
        setMutationRate(mutationRate);
        setCrossoverRate(crossOverRate);
        setUseElitism(elitism);
        setNumOfIterations(numberIterations);
        setPopSize(initialPopsize);
    }

    public void choiceElitism() {
        if(isUseElitism()) {
            clearAllIndividuals();
            createFirstPopulationSetting();
            populateToMakeChronosome();
            getAllFitnesses();
            selectTwoParents();
            runOnePointCrossover();
            mutate();
            useElitism();
            convergenceCheck();
        }else {
            clearAllIndividuals();
            createFirstPopulationSetting();
            populateToMakeChronosome();
            getAllFitnesses();
            selectTwoParents();
            runOnePointCrossover();
            mutate();
            convergenceCheck();
        }
    }


    public void startAlgorithm() {
        int iteration = getNumOfIterations();
        while (iteration-- != 0) {
            System.out.println("<----------------------Iteration Count:[" + iteration  +"]------------------>");
            System.out.println("<----------------------Creating First Population----------------->");
            System.out.println("<----------------------Population Size:[" + getPopSize() + "]------------------>");
            choiceElitism();
        }
    }
}