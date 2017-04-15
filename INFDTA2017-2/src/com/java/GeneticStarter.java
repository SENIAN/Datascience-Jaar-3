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

    public void startAlgorithm() {
        int iteration = getNumOfIterations();
        while (iteration-- != 0) {
            if (useElitism) {
                clearAllIndividuals();
                System.out.println("<----------------------Iteration Count:[" + iteration  +"]------------------>");
                System.out.println("<----------------------Creating First Population----------------->");
                System.out.println("<----------------------Population Size:[" + getPopSize() + "]------------------>");
                System.out.println("<----------------------Population Size:------------------>");
                runOnePointCrossover(selectTwoParents(getAllFitnesses(populateToMakeChronosome(createFirstPopulationSetting()))));
                System.out.println("<----------------------New Childs created:---------------------->");

            }
        }
    }
}
