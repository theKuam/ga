package com.ngm.tsp;

import com.ngm.ga.Chromosome;
import com.ngm.ga.Population;
import com.ngm.utils.ConstantString;
import com.ngm.utils.Sorting;
import com.ngm.utils.TSPFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class CityPopulation extends Population {
    private static final int DOMAIN = 250; //Coordinates domain.
    private int cityCount;  //Number of cities.
    private ArrayList<City> map; //City map.
    private ArrayList<Double> aX;   //X-coordinate list.
    private ArrayList<Double> aY;   //Y-coordinate list.
    private String fitnessType;
    private Chromosome bestChromosome;
    private double bestFitness;

    public CityPopulation(String filename) {
        cityCount = 0;
        bestFitness = Double.MAX_VALUE;
        bestChromosome = new Chromosome();
        map = new ArrayList<>();
        aX = new ArrayList<>();
        aY = new ArrayList<>();
        initializeMap(filename);
//        printMap();
    }

    public ArrayList<City> getMap() {
        return map;
    }

    public static int getDOMAIN() {
        return DOMAIN;
    }

    public int getCityCount() {
        return cityCount;
    }

    public void setCityCount(int cityCount) {
        this.cityCount = cityCount;
    }

    public ArrayList<Double> getaX() {
        return aX;
    }

    public void setaX(ArrayList<Double> aX) {
        this.aX = aX;
    }

    public ArrayList<Double> getaY() {
        return aY;
    }

    public void setaY(ArrayList<Double> aY) {
        this.aY = aY;
    }

    public double getaXByIndex(int index) {
        return aX.get(index);
    }

    public void setaXByIndex(int index, double value) {
        if(index > aX.size() - 1) aX.add(value);
        else aX.set(index, value);
    }

    public double getaYByIndex(int index) {
        return aY.get(index);
    }

    public void setaYByIndex(int index, double value) {
        if(index > aY.size() - 1) aY.add(value);
        else aY.set(index, value);
    }

    public String getFitnessType() {
        return fitnessType;
    }

    public void setFitnessType(String fitnessType) {
        this.fitnessType = fitnessType;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(double bestFitness) {
        this.bestFitness = bestFitness;
    }

    public Chromosome getBestChromosome() {
        return bestChromosome;
    }

    public void setBestChromosome(Chromosome bestChromosome) {
        this.bestChromosome = bestChromosome;
    }

    //Initialize a map with cities.
    private void initializeMap(String filename) {
        if(filename != null) {
            TSPFile.readFile(filename, this);
        }
        else {
            System.out.print("Number of Cities: ");
            Scanner sc = new Scanner(System.in);
            cityCount = sc.nextInt();
            int randomX, randomY;

            //Create city list without concurrent points.
            boolean duplicate;
            int count = 0;
            while(aX.size() < cityCount) {
                do {
                    randomX = new Random().nextInt(DOMAIN);
                    if (aX.indexOf(randomX) == -1) {
                        duplicate = false;
                        setaXByIndex(count, randomX);
                        randomY = new Random().nextInt(DOMAIN);
                        setaXByIndex(count, randomY);
                        count++;
                    } else duplicate = true;
                } while (duplicate);
            }
        }

        //Add city into the map.
        for (int i = 0; i < cityCount; i++) {
            City city = new City();
            city.setcX(aX.get(i));
            city.setcY(aY.get(i));
            map.add(city);
        }
    }

    //Show location of cities (their coordinates).
    public void printMap() {
        for (int i = 0; i < cityCount; i++) {
            System.out.print("City " + i + ": [" + map.get(i).getcX()
                    + ", " + map.get(i).getcY() + "] \n");
        }
    }

    //Initialize Population.
    private void initializePopulation() {
        for (int i = 0; i < CHROMOSOME_COUNT; i++) {
            Chromosome newChromosome = new Chromosome();
            //We set init candidate is {0 .. cityCount-1}.
            for (int j = 0; j < this.getCityCount(); j++) {
                newChromosome.setDataByIndex(j, j);
            }
            //add it into chromosome list.
            chromosomes.add(newChromosome);
            //Rearranging cities to create new candidate.
            for (int j = 0; j < CHROMOSOME_COUNT; j++) {
                randomlyArrange(chromosomes.indexOf(newChromosome));
            }
            //Evaluate fitness value.
            calculateFitnessValue(chromosomes.indexOf(newChromosome), chromosomes);
        }
    }

    //Swapping two random cities to create new candidate.
    private void randomlyArrange(int index) {
        int firstCity = new Random().nextInt(cityCount);
        int secondCity = 0;
        boolean done = false;
        //Must be two different cities.
        while(!done) {
            secondCity = new Random().nextInt(cityCount);
            if(secondCity != firstCity) {
                done = true;
            }
        }
        //Swapping..
        switchValues(index, secondCity, firstCity);
    }

    private void switchValues(int index, int secondCity, int firstCity) {
        int temp = chromosomes.get(index).getDataByIndex(firstCity);
        chromosomes.get(index).setDataByIndex(firstCity,
                chromosomes.get(index).getDataByIndex(secondCity));
        chromosomes.get(index).setDataByIndex(secondCity, temp);
    }

    @Override
    protected void calculateFitnessValue(int index, ArrayList<Chromosome> c) {
        super.calculateFitnessValue(index, c);
        Chromosome thisChromosome = c.get(index);
        thisChromosome.setFitness(0.0);
        for (int i = 0; i < cityCount; i++) {
            if(i == cityCount -1)
                thisChromosome.setFitness(thisChromosome.getFitness() +
                        getDistance(fitnessType, thisChromosome.getDataByIndex(cityCount - 1),
                                thisChromosome.getDataByIndex(0))); //Back to the first city. Complete trip.
            else
                thisChromosome.setFitness(thisChromosome.getFitness() +
                        getDistance(fitnessType, thisChromosome.getDataByIndex(i),
                                thisChromosome.getDataByIndex(i+1)));
        }
    }

    private double getDistance(String fitnessType, int firstCity, int secondCity) {
        if(fitnessType.equals(ConstantString.EUC_2D)) {
            City cityA = map.get(firstCity);
            City cityB = map.get(secondCity);
            return Math.sqrt(
                    (cityA.getcX()-cityB.getcX())*(cityA.getcX()-cityB.getcX())
                            +
                            (cityA.getcY()-cityB.getcY())*(cityA.getcY()-cityB.getcY())
            ); //Euclid distance.
        }
        else if(fitnessType.equals(ConstantString.ATT)) {
            City cityA = map.get(firstCity);
            City cityB = map.get(secondCity);
            double xd = cityA.getcX()-cityB.getcX();
            double yd = cityA.getcY()-cityB.getcY();
            double rAB = Math.sqrt((xd*xd+yd*yd)/10.0);
            double tAB = Math.round(rAB);
            double dAB = 0;
            if(tAB < rAB) dAB = tAB + 1;
            else dAB = tAB;
            return dAB;
            //Pseudo Euclid distance.
        }
        else return -1;
    }

    public void GAAlgorithm() {
        Chromosome aChromosome;
        int iteration = 0;
        int counter = 0;
        boolean done = false;
        ArrayList<Chromosome> newPopulation = new ArrayList<>();

        //First, Initialize Population.
        initializePopulation();
        //Then, find the best solution using loop.
        while (!done) {
            /*
            Three condition can end this loop:
                If the maximum number of iterations has been reached, or,
                If the target value has been found, or,
                If the fitness has not been changed for a long time (eg. five iterations).
            */
            if(iteration < MAX_ITERATIONS) {
                for (int i = 0; i < CHROMOSOME_COUNT; i++) {
                    //For each iteration, show a trip and its total distance.
                    aChromosome = chromosomes.get(i);
//                    System.out.print("Route " + i + ": ");
//                    for (int j = 0; j < cityCount; j++) {
//                        System.out.print(aChromosome.getDataByIndex(j) + ", ");
//                        if (j == cityCount - 1)
//                            System.out.print(aChromosome.getDataByIndex(0) + ", ");
//                    } //A complete trip.
                    calculateFitnessValue(i, chromosomes);
//                    System.out.print("Distance: " + aChromosome.getFitness() + "\n");
                    //The cost must not be grater than the target.
                    if(aChromosome.getFitness() <= FITNESS_TARGET) {
                        //The target value has been reached.
                        done = true;
                    }
                }
                //Select parents to pair.
                ArrayList<Chromosome> parents = new ArrayList<>(selectParent());
                ArrayList<Chromosome> offspring = new ArrayList<>();
                while(parents.size() != 0) {
                    //Do mating..
                    int[] soulMates = mating(parents);
                    //If crossover is applied to the selected parents.
                    if(crossoverApplied(soulMates, parents)) {
                        Chromosome[] crossChildren = crossOver(soulMates, parents);
                        for (Chromosome crossChild : crossChildren) {
                            offspring.add(crossChild);
                            //For each child, if mutation is applied.
                            if (mutationApplied(crossChild)) {
                                offspring.add(mutation(crossChild));
                            }
                        }
                    }
                    if(soulMates[0] > soulMates[1]) {
                        parents.remove(soulMates[0]);
                        parents.remove(soulMates[1]);
                    }
                    else {
                        parents.remove(soulMates[1]);
                        parents.remove(soulMates[0]);
                    }
                }
                newPopulation.addAll(chromosomes);
                newPopulation.addAll(offspring);
                Sorting.mergeSort(0, newPopulation.size() - 1, newPopulation);
                calculateFitnessValue(0, newPopulation);
                counter++;
                if(newPopulation.get(0).getFitness() < bestFitness) {
                    bestChromosome = newPopulation.get(0);
                    bestFitness = newPopulation.get(0).getFitness();
                    counter = 0;
                }
                //The best fitness value is not changed for a long time.
                if(counter == MAX_LAPSE) {
                    done = true;
                }
                updateChromosomes(newPopulation);

                //Step #
                // System.out.println("Step: " + iteration + ", Record: " + bestFitness);
                iteration++;
            }
            else {
                //The maximum number of iterations has been reached.
                done = true;
            }
        }
    }

    private void updateChromosomes(ArrayList<Chromosome> newPopulation) {
        for (int i = 0; i < CHROMOSOME_COUNT; i++) {
            if(i < Math.floor(CHROMOSOME_COUNT*BEST_CHROMOSOME_COUNT)) {
                chromosomes.set(i, newPopulation.get(i));
            }
            else {
                chromosomes.set(i, newPopulation.get(
                        (int)Math.floor(CHROMOSOME_COUNT*BEST_CHROMOSOME_COUNT)
                                + (int)Math.floor(new Random().nextDouble()
                                *((newPopulation.size() - (int)Math.floor(CHROMOSOME_COUNT*BEST_CHROMOSOME_COUNT))))));
            }
        }
    }

    private Chromosome mutation(Chromosome crossChild) {
        Chromosome mutantChild = new Chromosome();
        mutantChild.setData(crossChild.getData());
        int firstCity = new Random().nextInt(cityCount);
        int secondCity = 0;
        boolean done = false;
        //Must be two different cities.
        while(!done) {
            secondCity = new Random().nextInt(cityCount);
            if(secondCity != firstCity) {
                done = true;
            }
        }
        //Swapping..
        int temp = mutantChild.getDataByIndex(firstCity);
        mutantChild.setDataByIndex(firstCity,
                mutantChild.getDataByIndex(secondCity));
        mutantChild.setDataByIndex(secondCity, temp);
        return mutantChild;
    }

    private boolean mutationApplied(Chromosome crossChild) {
        return new Random().nextDouble() <= MUTATION_PROBABILITY;
    }

    private Chromosome[] crossOver(int[] soulMates, ArrayList<Chromosome> parents) {
        //PMX
        int firstCutIndex = new Random().nextInt(cityCount-2);
        int secondCutIndex = 0;
        boolean done = false;
        //Must be two different chromosome.
        while(!done) {
            secondCutIndex = new Random().nextInt(cityCount - 1);
            if (secondCutIndex >= firstCutIndex) {
                done = true;
            }
        }
        Chromosome parentA = parents.get(soulMates[0]);
        Chromosome parentB = parents.get(soulMates[1]);
        int[] segmentA = createSegment(firstCutIndex, secondCutIndex, parentA);
        int[] segmentB = createSegment(firstCutIndex, secondCutIndex, parentB);
        Chromosome[] returnCrossChildren = new Chromosome[2];
        Integer[] childAData = insertSegment(firstCutIndex, secondCutIndex, segmentA);
        Integer[] childBData = insertSegment(firstCutIndex, secondCutIndex, segmentB);
        cross(firstCutIndex, secondCutIndex, childAData, parentB, parentA);
        cross(firstCutIndex, secondCutIndex, childBData, parentA, parentB);
        returnCrossChildren[0] = new Chromosome();
        returnCrossChildren[0].setData(new ArrayList<>(Arrays.asList(childAData)));
        returnCrossChildren[1] = new Chromosome();
        returnCrossChildren[1].setData(new ArrayList<>(Arrays.asList(childBData)));
        return returnCrossChildren;
    }

    private void cross(int firstCutIndex, int secondCutIndex, Integer[] childData,
                       Chromosome notAlikeParent, Chromosome alikeParent) {
        for (int i = firstCutIndex; i <= secondCutIndex; i++) {
            boolean alreadyThere = false;
            for (int j = firstCutIndex; j <= secondCutIndex; j++) {
                if(childData[j] == notAlikeParent.getDataByIndex(i)) {
                    alreadyThere = true;
                    break;
                }
            }
            if(!alreadyThere) {
                int index = i;
                int notAlreadyThereValue = notAlikeParent.getDataByIndex(index);
                while(true) {
                    int sameIndexValue = alikeParent.getDataByIndex(index);
                    int sameValueIndex = notAlikeParent.getData().indexOf(sameIndexValue);
                    if ((sameValueIndex < firstCutIndex) || (sameValueIndex > secondCutIndex)) {
                        childData[sameValueIndex] = notAlreadyThereValue;
                        break;
                    }
                    else {
                        index = sameValueIndex;
                    }
                }
            }
        }
        for (int i = 0; i < cityCount; i++) {
            if ((childData[i] == -1)) {
                childData[i] = notAlikeParent.getDataByIndex(i);
            }
        }
    }

    private Integer[] insertSegment(int firstCutIndex, int secondCutIndex, int[] segment) {
        Integer[] returnChildData = new Integer[cityCount];
        int segmentIndex = 0;
        for (int i = 0; i < cityCount; i++) {
            if ((i >= firstCutIndex) && (i <= secondCutIndex)) {
                returnChildData[i] = segment[segmentIndex];
                segmentIndex++;
            }
            else {
                returnChildData[i] = -1;
            }
        }
        return returnChildData;
    }

    private int[] createSegment(int firstCutIndex, int secondCutIndex, Chromosome parent) {
        int capacityOfSegment = (secondCutIndex - firstCutIndex) + 1;
        int[] returnSegment = new int[capacityOfSegment];
        ArrayList<Integer> parentData = new ArrayList<>(parent.getData());
        int segmentIndex = 0;
        for (int i = 0; i < cityCount; i++) {
            if ((i >= firstCutIndex) && (i <= secondCutIndex)) {
                returnSegment[segmentIndex] = parentData.get(i);
                segmentIndex++;
            }
        }
        return returnSegment;
    }

    private boolean crossoverApplied(int[] soulMates, ArrayList<Chromosome> parents) {
        return new Random().nextDouble() <= CROSSOVER_PROBABILITY;
    }

    private int[] mating(ArrayList<Chromosome> parents) {
        int parentsSize = parents.size();
        int randomA = new Random().nextInt(parentsSize);
        int randomB = 0;
        boolean done = false;
        //Must be two different chromosome.
        while(!done) {
            randomB = new Random().nextInt(parentsSize);
            if(randomB != randomA) {
                done = true;
            }
        }
        return new int[]{randomA, randomB};
    }

    private ArrayList<Chromosome> selectParent() {
        //Apply Roulette selection.
        ArrayList<Chromosome> parents = new ArrayList<>();
        double fitnessSum = 0;
        //Calculate fitness sum.
        for (int i = 0; i < CHROMOSOME_COUNT; i++) {
            fitnessSum += chromosomes.get(i).getFitness();
        }
        for(int i = 0; i < CHROMOSOME_COUNT; i++) {
            if((chromosomes.get(i).getFitness())/fitnessSum
                    <= SELECTION_PROBABILITY) {
                parents.add(chromosomes.get(i));
            }
        }
        return parents;
    }


    public void printBestSolution()
    {
        if(bestFitness <= FITNESS_TARGET){
            //Print it.
            System.out.println("Target reached.");
        }else{
            System.out.println("Target not reached");
        }
        System.out.print("Shortest Route: ");
        for(int j = 0; j < cityCount; j++)
        {
            System.out.print(bestChromosome.getDataByIndex(j) + ", ");
            if(j == cityCount - 1)
                System.out.print(bestChromosome.getDataByIndex(0) + ", ");
        }
        System.out.print("Distance: " + bestFitness + "\n");
    }

}
