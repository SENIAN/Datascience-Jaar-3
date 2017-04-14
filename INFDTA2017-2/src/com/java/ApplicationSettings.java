import java.util.List;

/**
 * Created by Mohammed on 3/1/2017.
 */
public class ApplicationSettings {
        public static void main(String[] args) {

            Algorithms algorithms = new Algorithms(0.34, 0.91, true, 10, 40);
            int startingPoint = -1;
            for (int i = 0; i <= algorithms.numOfIterations; i++) {
                if (algorithms.useElitism) {

                    System.out.println("<----------------------Iteration Count:[" + i +"]------------------>");

                    System.out.println("<----------------------Creating First Population----------------->");
                    System.out.println("<----------------------Population Size:[" + algorithms.popSize + "]------------------>");
                    algorithms.selectTwoParents(algorithms.getAllFitnesses(
                            algorithms.populateToMakeChronosome(
                            algorithms.createFirstPopulationSetting())));
                    System.out.println("<----------------------Average of Population------------------->");
                   // algorithms.getAverageFitness(initialPoint);
                    System.out.println("<----------------------PopulateChronosomes With Elitism------------------->");
                   // List<Individual<String>> pointWithElitism = algorithms.populateToMakeChronosomeWithElitism(initialPoint);


                    startingPoint = 1;
                } else {
                    System.out.println("<----------------------Creating First Population------------------->");
                   // List<Individual<Integer>> initialPoint = algorithms.createFirstPopulationSetting();
                    System.out.println("<----------------------Population Size   " + algorithms.popSize + " ------------------>");
                    System.out.println("<----------------------Average of Population------------------->");
                  //  algorithms.getAverageFitness(initialPoint);
                    System.out.println("<----------------------PopulateChronosomes Without Elitism------------------->");

                    startingPoint = 0;
                }
            }

        }
}
