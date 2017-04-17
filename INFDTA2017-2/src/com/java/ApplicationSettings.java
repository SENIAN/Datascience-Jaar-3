/**
 * Created by Mohammed on 3/1/2017.
 */
public class ApplicationSettings {
        public static void main(String[] args) {
            GeneticStarter geneticStarter = new GeneticStarter();
            geneticStarter.initializeAlgorithm(0.02, 0.95,
                    true,
                    100,
                    100);
            geneticStarter.startAlgorithm();
        }
}
