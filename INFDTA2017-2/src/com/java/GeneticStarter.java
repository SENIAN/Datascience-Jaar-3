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
            mutation();
            useElitism();
        }else {
            clearAllIndividuals();
            createFirstPopulationSetting();
            populateToMakeChronosome();
            getAllFitnesses();
            selectTwoParents();
            runOnePointCrossover();
            mutation();
        }
    }


    public void startAlgorithm() {
        int iteration = getNumOfIterations();
        int i = 0;
              while (i++ < iteration) {
                  choiceElitism();
                  if (i == iteration) {
                      System.out.println("<----------------------Iteration Count:[" + i + "]------------------>");
                      System.out.println("<----------------------Creating First Population----------------->");
                      System.out.println("<----------------------Population Size:[" + getPopSize() + "]------------------>");
                      System.out.println("<---------------------Best Individual this Iteration--------------->");
                      convergenceCheck();
                  }
              }
    }
}
