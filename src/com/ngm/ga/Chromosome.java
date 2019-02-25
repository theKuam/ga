package com.ngm.ga;

import java.util.ArrayList;

public class Chromosome implements Comparable<Chromosome> {
    private ArrayList<Integer> data; //Chromosome's data.
    private double fitness; //Chromosome's fitness value.

    public Chromosome() {
        this.data = new ArrayList<>();
        this.fitness = 0.0;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }

    public int getDataByIndex(int index) {
        return this.data.get(index);
    }

    public void setDataByIndex(int index, int value) {
        if(index > this.data.size() - 1) this.data.add(value);
        else this.data.set(index, value);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    //Compare two chromosome's fitness values.
    @Override
    public int compareTo(Chromosome that) {
        return Double.compare(this.getFitness(), that.getFitness());
        //this < that ? -1 : this > that ? 1 : 0.
    }
}
