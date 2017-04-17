
import lombok.Data;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by Mohammed on 3/1/2017.
 */

@Data
public class GeneticAlgorithm {

    private double mutationRate;
    private double crossoverRate;
    private boolean useElitism;
    private int numOfIterations;
    private int popSize;
    private int childId = 0;
    public List<Individual> initialPopulation = new ArrayList<>();
    private List<Individual> listWithChronosomes = new ArrayList<>();
    private List<Individual> individualsWithFitness = new ArrayList<>();
    private List<Individual> twoParents = new ArrayList<>();
    public List<Individual> lastPopulation = new ArrayList<>();


    /*Initial Population creation  + initialization of chronosomes */
    public List<Individual> createFirstPopulationSetting() {
        int maximumIntegerValue = 31;
        Random r = new Random();
        Individual individual;
        for (int i = 0; i < popSize; i++) {
            int s = r.nextInt(maximumIntegerValue);
            individual = new Individual();
            individual.setBirthEgg(s);
            individual.setIndividualID(i);
            initialPopulation.add(individual);
        }
        return initialPopulation;
    }
    /*Chronosomes set*/
    public List<Individual> populateToMakeChronosome() {
        initialPopulation.forEach(n -> {
            String ChronosomeValue = String.format("%5s", Integer.toBinaryString(n.getBirthEgg())).replace(' ', '0');
            n.setChronosome(ChronosomeValue);
            listWithChronosomes.add(n);
        });
        return listWithChronosomes;
    }
    /*Evaluate the fitness of each Individual and returns list of indivuals and their fitnesses*/
    public List<Individual> getAllFitnesses() {
        /*Evaluating the fitnesses of all chronosomes and setting them accordingly*/
        double fitness;
        for (Individual individual : listWithChronosomes) {
            fitness = computeFitness(individual.getChronosome());
            individual.setFitness(fitness);
            individualsWithFitness.add(individual);
        }
        return individualsWithFitness;
    }
    // Selects 2 parents based on Russian Roulette SelectionModel
    public List<Individual> selectTwoParents() {
        double totalFitness = 0.0;
        Random r = new Random();
        while (twoParents.size() != 2) {
            totalFitness = 0.0;
            for (Individual individual : individualsWithFitness) {
                totalFitness += individual.getFitness();
            }
            for (Individual individual : individualsWithFitness) {
                double chance = round(individual.getFitness() / totalFitness, 2);
                individual.setChangeToBePicked(chance);
            }
            int i = 0;
            // Who wins the roulette wins the game as being the parent of the child;
            double extractionValue = getExtractionValue();
            double spinRouletteWheel = 0.0;
            for (Individual individual : individualsWithFitness) {
                spinRouletteWheel += individual.getChangeToBePicked();
                if (checkInRange(spinRouletteWheel, extractionValue)) {
                    if (twoParents.size() <= 1) {
                        twoParents.add(individual);
                    }
                }
            }
        }
        return twoParents;
    }
    //Crossover rate from parents to individual
    public void runOnePointCrossover() {
        Individual parentOne;
        Individual parentTwo;
        Individual childOne = new Individual();
        Individual childTwo = new Individual();

        if (twoParents.size() == 2) {
            parentOne = twoParents.get(0);
            parentTwo = twoParents.get(1);
            SecureRandom r = new SecureRandom();
            double crossoverProb = r.nextDouble() * 1;
            if (crossoverProb < crossoverRate || crossoverProb == 1) {
                String MatchDna1 = String.format("%5s", Integer.toBinaryString(parentOne.getBirthEgg())).replace(' ', '0');
                String MatchDna2 = String.format("%5s", Integer.toBinaryString(parentTwo.getBirthEgg())).replace(' ', '0');

                // Minimum minimum 1 max 4 for crossover point. 5 is out of bound for usefullness 1-4
                int aRandomValue =  new SecureRandom().nextInt(MatchDna1.length()-1)+ (int) crossoverProb + 1;

                /*Creating childs*/
                String child1 = MatchDna1.substring(0, aRandomValue) + MatchDna2.substring(aRandomValue, MatchDna2.length());
                String child2 = MatchDna2.substring(0, aRandomValue) + MatchDna1.substring(aRandomValue, MatchDna1.length());

                childOne.setChronosome(child1);
                childOne.setFitness(computeFitness(child1));
                childOne.setBirthEgg(Integer.parseInt(child1, 2));
                childOne.setIndividualID(childId++);

                childTwo.setChronosome(child2);
                childTwo.setFitness(computeFitness(child2));
                childTwo.setBirthEgg(Integer.parseInt(child2, 2));
                childTwo.setIndividualID(childId++);

                lastPopulation.add(childOne);
                lastPopulation.add(childTwo);
            } else {
                /*Crossover not performed */
                parentOne.setIndividualID(childId++);
                parentTwo.setIndividualID(childId++);
                lastPopulation.add(parentOne);
                lastPopulation.add(parentTwo);
            }
        } else {
            System.out.println("Please provide algorithm with 2 parents to do the crossover");
        }
    }
    //Mutate based on mutationRate
    public List<Individual> mutation() {
        Random r = new Random();
        double test = r.nextDouble() * 0.1;
        for (Individual individual : lastPopulation) {
            if (mutationRate > r.nextDouble() * 0.1) {
                String ChronosomeValue = individual.getChronosome();
                int mutationFactor = new Random().nextInt(ChronosomeValue.length());
                char mutationReference = ChronosomeValue.charAt(mutationFactor);
                char mutationArray[];
                if (mutationReference == '0') {
                    mutationArray = ChronosomeValue.toCharArray();
                    mutationArray[mutationFactor] = '1';
                    individual.setChronosome(String.valueOf(mutationArray));
                }
                if(mutationReference == '1') {
                    mutationArray = ChronosomeValue.toCharArray();
                    mutationArray[mutationFactor] = '0';
                    individual.setChronosome(String.valueOf(mutationArray));
                }
            }
        }
        return lastPopulation;
    }
    //Use elitism yes or no
    public List<Individual> useElitism() {
            individualsWithFitness.forEach(k -> {
                if (k.getFitness() >= 3) {
                    lastPopulation.add(k);
                }
            });
            return lastPopulation;
    }
    //Accept new offspring as new population
    public boolean convergenceCheck() {
        int f = 0;
        int f1 = 0;
        int sizeOfLastPop = lastPopulation.size();
        int sizeOfInitPop = initialPopulation.size();
        double fitnessMin = 0;
        Individual bestIndividual = new Individual();
        for(Individual individual: lastPopulation) {
             f+= individual.getFitness();

             if(individual.getFitness() > fitnessMin) {
                 fitnessMin = individual.getFitness();
                 bestIndividual = individual;
             }
        }
        System.out.println("Best Individual in the population:  " + " Fitness: " + bestIndividual.getFitness() +
                " Individual ID:  " + bestIndividual.getIndividualID());

        for(Individual individual : initialPopulation) {
            f1+= individual.getFitness();
        }

        double averageInitialPopulation = f1 / sizeOfInitPop;
        double averageLastPopulation = f / sizeOfLastPop;
        System.out.println("Average Fitness Of Initial Population:  "  + averageInitialPopulation);
        System.out.println("Average Fitness Of Last Population:  " + averageLastPopulation);

         if(averageLastPopulation > averageInitialPopulation) {
            return true;
        }
        lastPopulation.clear();

        return false;
    }



    /*Util methods for different kind of calculations*/
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private double getExtractionValue() {
        SecureRandom r = new SecureRandom();
        double extractionValue = round(1 * r.nextDouble(), 1);
        while (!(extractionValue > 0.10 & extractionValue < 0.90)) {
            extractionValue = round(1 * r.nextDouble(), 1);
        }
        return extractionValue;
    }
    private boolean checkInRange(double spinRouletteWheel, double extractionValue) {
        if (spinRouletteWheel < extractionValue) {
            return false;
        }
        double inRange = extractionValue / 100 * 20; // 5 precent difference
        if (spinRouletteWheel > extractionValue && spinRouletteWheel <= extractionValue + inRange) {
            return true;
        }
        if (spinRouletteWheel > extractionValue + inRange) {
            return false;
        }
        return false;
    }
    private double computeFitness(String binary) {
        /*Max-One*/
        double fitness = 0;

        for (int i = 0; i < binary.length(); i++) {
            char s = binary.charAt(i);
            if (s == '1') {
                fitness++;
            }
        }
        return fitness;
    }
    public void clearAllIndividuals() {
        initialPopulation.clear();
        individualsWithFitness.clear();
        listWithChronosomes.clear();
        twoParents.clear();
    }


    public double getCrossoverRate() {
        return crossoverRate;
    }
    public double getMutationRate() {
        return mutationRate;
    }
    public int getNumOfIterations() {
        return numOfIterations;
    }
    public int getPopSize() {
        return popSize;
    }
    public boolean isUseElitism() {
        return useElitism;
    }
    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }
    public void setInitialPopulation(List<Individual> initialPopulation) {
        this.initialPopulation = initialPopulation;
    }
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
    public void setNumOfIterations(int numOfIterations) {
        this.numOfIterations = numOfIterations;
    }
    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }
    public void setUseElitism(boolean useElitism) {
        this.useElitism = useElitism;
    }
}