/**
 * Created by Mohammed on 3/1/2017.
 */
public class ApplicationSettings {
        public static void main(String[] args) {
            GeneticStarter geneticStarter = new GeneticStarter();
            geneticStarter.initializeAlgorithm(0.34, 0.42,
                    true,
                    100,
                    100);
            geneticStarter.startAlgorithm();
        }
}
