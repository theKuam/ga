package com.ngm.ga;

import java.util.ArrayList;

public class Population {
    protected ArrayList<Chromosome> chromosomes; //list of chromosomes
    protected static final int CHROMOSOME_COUNT = 100;
    protected static final double FITNESS_TARGET = 1000.0;
    protected static final double SELECTION_PROBABILITY = 0.3;
    protected static final double CROSSOVER_PROBABILITY = 0.2;
    protected static final double MUTATION_PROBABILITY = 0.05;
    protected static final double BEST_CHROMOSOME_COUNT = 0.75;
    protected static final long MAX_ITERATIONS = 10000;
    protected static final long MAX_LAPSE = 100;

    public Population() {
        this.chromosomes = new ArrayList<>();
    }

    public ArrayList<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(ArrayList<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    protected void calculateFitnessValue(int index,  ArrayList<Chromosome> c) {
    }
}
