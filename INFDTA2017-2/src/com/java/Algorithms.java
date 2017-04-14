
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;
import java.util.Map;

/**
 * Created by Mohammed on 3/1/2017.
 */
public class Algorithms {

    public double mutationRate;
    public double crossoverRate;
    public boolean useElitism;
    public int numOfIterations;
    public int popSize;
    List<Individual> initialPopulation = new ArrayList<>();

    public Algorithms(double mutationRate, double crossoverRate, boolean useElitism, int numOfIterations, int popSize) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.useElitism = useElitism;
        this.numOfIterations = numOfIterations;
        this.popSize = popSize;

    }
    /*Initial Population creation*/
    public List<Individual> createFirstPopulationSetting() {
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

    /*Initial Populations with chronosomes set*/
    public List<Individual> populateToMakeChronosome(List<Individual> currPopulation) {
        List<Individual> allChronosomes = new ArrayList<>();
        currPopulation.forEach(n -> {
            String ChronosomeValue = String.format("%5s", Integer.toBinaryString(n.getBirthEgg())).replace(' ', '0');
            n.setChronosome(ChronosomeValue);
            allChronosomes.add(n);
        });
        return allChronosomes;
    }

    /*Evaluate the fitness of each Individual and returns list of indivuals and their fitnesses*/
    public List<Individual> getAllFitnesses(List<Individual> initialPopulation) {
        /*Evaluating the fitnesses of all chronosomes and setting them accordingly*/
        List<Individual> individualsWithFitness = new ArrayList<>();
        double fitness;
        for(Individual individual: initialPopulation) {
            fitness =  computeFitness(individual.getChronosome());
            individual.setFitness(fitness);
            individualsWithFitness.add(individual);
        }
        return individualsWithFitness;
    }

    public List<Individual> selectTwoParents(List<Individual> listOffIndividuals) {
        List<Individual> TwoParents = new ArrayList<>();
        int totalFitness = 0;
        Random r = new Random();
           for(Individual individual: listOffIndividuals) {
            totalFitness += individual.getFitness();
        }
        for(Individual individual: listOffIndividuals) {
            double chance = individual.getFitness() / totalFitness;
            System.out.println(individual.getFitness() / totalFitness);
            individual.setChangeToBePicked(chance);
        }
        // Who wins the roulette wins the game as being the parent of the child;
        r.setSeed(totalFitness);
        double extractionValue = r.nextDouble();
        double spinRouletteWheel = 0.0;
        for(Individual individual: listOffIndividuals) {
           spinRouletteWheel += individual.getChangeToBePicked();
           if(checkInRange(spinRouletteWheel, extractionValue)) {
               TwoParents.add(individual);
           }
        }
        return TwoParents;
    }

    public boolean checkInRange(double spinRouletteWheel, double extractionValue) {

        Math.round(extractionValue);
        if(spinRouletteWheel < extractionValue) {
            System.out.println("roulette wheel is out of range of extraction value");
            System.out.println(spinRouletteWheel);
            System.out.println(extractionValue);
            return false;

        }
        if(spinRouletteWheel >= extractionValue) {
            return true;
        }
        return false;
    }

    public List<Individual> populateToMakeChronosomeWithElitism(List<Individual> currPopulationElitism) {
        List<Individual> allChronosomesElitism = new ArrayList<>();
        currPopulationElitism.forEach(n -> {
            String ChronosomeValue = String.format("%5s", Integer.toBinaryString(n.getBirthEgg())).replace(' ', '0');
            n.setChronosome(ChronosomeValue);
            allChronosomesElitism.add(n);
        });
        getFittestChronosomes(allChronosomesElitism);
        return allChronosomesElitism;
    }
    public List<Individual> getFittestChronosomes(List<Individual> chronosomes) {
        double fitness = 0;
        List<Individual> fittestChronosomes = new ArrayList<>();
        for (Individual chronosome : chronosomes) {
          //  Long decimal = Long.parseLong(chronosome.getIndividual(), 2);
          //  fitness = computeFitness((double) decimal.intValue());
            if (fitness > 0) {
           //     fittestChronosomes.add(chronosome, Double.toString(fitness));
                //       System.out.println(chronosome + "Individual belongs to the fittest added for next rounds");
            } else {
                //       System.out.println(chronosome + " Individual isn't the fitt enough they fall out");
            }
        }
        /*Map.Entry<Individual<Integer>, Integer> BestIndividual = getFittestFromFittestList(fittestChronosomes);
        System.out.println("<---------------------------Best Individual------------------------->");
        System.out.println(BestIndividual.getKey() + "  Fitness:" + BestIndividual.getValue());*/
        return fittestChronosomes;
    }
    public double computeFitness(String binary) {
        /*Max-One*/
        double fitness = 0;

        for (int i = 0; i < binary.length(); i++) {
            char s =  binary.charAt(i);
            if(s == '1') {
                fitness++;
            }
        }
        return fitness;
    }
    public List<Individual> getFittestFromFittestList(List<Individual> fittestOnes) {

        int fitness = 0;
        List<Individual> fittestOne = new ArrayList<>();
        List<Individual> newParent = new ArrayList<>();
/*
        for (Individual individual : fittestOne) {
            Long decimal = Long.parseLong(entry.getKey().getIndividual(), 2);
            fitness = (int) computeFitness(decimal.intValue());

        }


//        return Collections.max(fittestOne.entrySet(),
  //              (p1, p2) -> p1.getKey().getIndividual().compareTo(p2.getValue()));
    }
    */

        return new ArrayList<>();
    }
    public Individual russianRoulette(List<Individual> population) {
        int fitness = 0;
        int newFitness = 0;
        for (Individual first : population) {
        //    double thisValue = computeFitness(first.getIndividual());
         //   fitness = (int) thisValue;
        }

        int r = new Random().nextInt(fitness);

        for (Individual sec : population) {
      //      newFitness += computeFitness((sec.getIndividual()));
            if (newFitness >= r) {
                return sec;
            }
        }
        return null;
    }
    public void getTwoParents(List<Individual> chronosomes) {
        Individual firstParent = russianRoulette(chronosomes);
        Individual secondParrent = russianRoulette(chronosomes);
        crossoverAndMutation(firstParent, secondParrent);

    }
    public Individual createNewChild(Individual parentOne, Individual parentTwo) {
        //splitting the dna

        System.out.println("<---------------------------Spliting DNA------------------------->");
        String MatchDna1 = String.format("%5s", Integer.toBinaryString(parentOne.getBirthEgg())).replace(' ', '0');
        String MatchDna2 = String.format("%5s", Integer.toBinaryString(parentTwo.getBirthEgg())).replace(' ', '0');

        System.out.println("<---------------------------Parent DNA" + "    " + MatchDna1 + MatchDna2  +  "------------------------->");

        int aRandomValue = new Random().nextInt(MatchDna1.length());
        String Splitted1 =  MatchDna1.substring(0, aRandomValue);
        String Splitted2 =  MatchDna2.substring(aRandomValue, MatchDna2.length());
        System.out.println("<---------------------------New DNA    " + Splitted1+Splitted2 +"------->");
        //       return new Individual<>(Integer.parseInt(Splitted1+Splitted2, 2));

        return null;
    }
    public List<Individual> crossoverAndMutation(Individual parentOne, Individual parentTwo) {
        Random r = new Random();
        Individual justAChild;
        List<Individual> childPopulation = new ArrayList<>();
        if (r.nextDouble() < crossoverRate) {
            justAChild = createNewChild(parentOne, parentTwo);
            childPopulation.add(justAChild);
        }
        childPopulation.add(parentOne);
        if (r.nextDouble() < mutationRate) {
            justAChild = createNewChild(parentOne, parentTwo);
        }

        childPopulation.add(parentOne);
        initialPopulation = childPopulation;
        //initialPopulation.forEach(n-> System.out.println(" Child Fitness Value:   " + n +  "    From ParentOne Fitness:   " + parentOne.getIndividual() + " ParentTwo Fitness:  " + parentTwo.getIndividual()));
        return null;
    }
    public void getAllChronosomes(List chronosomes) {
        //      chronosomes.forEach(n -> System.out.println(n));
    }
    public void getAllChronosomes(Map map) {
        //    getFittestFromFittestList(map);
        //   map.forEach((key, value) -> {
        //      System.out.println("Chronosome:  " + key + "     Fitness:    " + value);
        // });
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