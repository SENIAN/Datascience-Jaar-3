
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
    private List<Individual> initialPopulation = new ArrayList<>();
    private List<Individual> listWithChronosomes = new ArrayList<>();
    private List<Individual> individualsWithFitness = new ArrayList<>();
    private List<Individual> twoParents = new ArrayList<>();
    private List<Individual> lastPopulation = new ArrayList<>();


    /*Initial Population creation  + initialization of chronosomes */
    private List<Individual> createFirstPopulationSetting() {
        int maximumIntegerValue = 31;
        Random r = new Random();
        Individual individual;
        for (int i = 0; i < popSize; i++) {
            int s = r.nextInt(maximumIntegerValue);
            individual = new Individual();
            individual.setBirthEgg(s);
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
                i++;
                if (checkInRange(spinRouletteWheel, extractionValue)) {
                    if (twoParents.size() <= 1) {
                        twoParents.add(individual);
                    }
                    break;
                }
            }
        }
        return twoParents;
    }

    //Crossover rate from parents to individual
    public List<Individual> runOnePointCrossover(List<Individual> twoParents) {
        SecureRandom r = new SecureRandom();
        Individual parentOne;
        Individual parentTwo;
        Individual childOne = new Individual();
        Individual childTwo = new Individual();

        if (twoParents.size() == 2) {
            parentOne = twoParents.get(0);
            parentTwo = twoParents.get(1);
            double crossoverProb = r.nextDouble() * 1;
            if (crossoverProb < crossoverRate) {
                System.out.println("<---------------------------Spliting DNA------------------------->");
                String MatchDna1 = String.format("%5s", Integer.toBinaryString(parentOne.getBirthEgg())).replace(' ', '0');
                String MatchDna2 = String.format("%5s", Integer.toBinaryString(parentTwo.getBirthEgg())).replace(' ', '0');
                System.out.println("<---------------------------Parent DNA" + "    " + MatchDna1 + MatchDna2 + "------------------------->");
                // Minimum minimum 1 max 4 for crossover point. 5 is out of bound for usefullness 1-4
                int aRandomValue = r.nextInt(MatchDna1.length()-1) + 1 ;
                String child1Split1 = MatchDna1.substring(0, aRandomValue);
                String child1Split2 = MatchDna2.substring(aRandomValue, MatchDna2.length());
                System.out.println("<---------------------------New DNA of child 1    " + child1Split1 + child1Split2 + "------->");
                String child2Split1 = MatchDna2.substring(0, aRandomValue);
                String child2Split2 = MatchDna1.substring(aRandomValue, MatchDna1.length());
                System.out.println("<---------------------------New DNA of Child 2    " + child2Split1 + child2Split2 + "------->");
                childOne.setChronosome(child1Split1+child1Split2);
                childTwo.setChronosome(child2Split1+child2Split2);
                lastPopulation.add(childOne);
                lastPopulation.add(childTwo);
            } else {
                /*Crossover not performed */
                return twoParents;
            }
        } else {
            System.out.println("Please provide algorithm with 2 parents to do the crossover");
        }
        return null;
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

    public double computeFitness(String binary) {
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

/*
    public Individual<Integer> mutation(Individual<Integer> mutate) {
        String ChronosomeValue = String.format("%5s", Integer.toBinaryString(mutate.getIndividual())).replace(' ', '0');
        int mutationFactor = new Random().nextInt(ChronosomeValue.length());
        char mutationReference = ChronosomeValue.charAt(mutationFactor);
        char mutationArray[];
        if (mutationReference == '0') {
            mutationArray = ChronosomeValue.toCharArray();
            mutationArray[mutationFactor] = '1';
            return new Individual<Integer>(Integer.parseInt(String.valueOf(mutationArray), 2));
        }
        mutationArray = ChronosomeValue.toCharArray();
        mutationArray[mutationFactor] = '0';
        return new Individual<Integer>(Integer.parseInt(String.valueOf(mutationArray), 2));

    }*/


  /*  public double getAverageFitness(List<Individual> population) {
        double returnResult = 0;
        for (Individual value : population) {
            returnResult = (float) computeFitness;
        }
        returnResult = returnResult / population.size();
        System.out.println(returnResult);
        return returnResult;
    }*/

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

/*
ALGORITHM – MAIN LOOP
The main loop of the genetic algorithm is explained in the slides of the course.
Moreover, a C# sample containing only the main loop of a generic genetic algorithm is available and can be used as a
starting point. If you use this code, you will have to program by yourself some specific functions to make it work. The
functions are:
 Func<Ind> createIndividual ==> input is nothing, output is a new individual;
 Func<Ind,double> computeFitness ==> input is one individual, output is its fitness;
 Func<Ind[],double[],Func<Tuple<Ind,Ind>>> selectTwoParents ==> input is an array of individuals (population)
and an array of corresponding fitnesses, output is a function which (without any input) returns a tuple with two
individuals (parents);
 Func<Tuple<Ind, Ind>, Tuple<Ind, Ind>> crossover ==> input is a tuple with two individuals (parents), output is a
tuple with two individuals (offspring/children);
 Func<Ind, double, Ind> mutation ==> input is one individual and mutation rate, output is the mutated individual
where Ind is the data structure which encodes the individual. You need to define concretely this data structure by
yourself
 */