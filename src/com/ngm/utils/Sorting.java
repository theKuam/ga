package com.ngm.utils;

import com.ngm.ga.Chromosome;

import java.util.ArrayList;

public class Sorting {
    //Merge sort, for particle list.
    public static void mergeSort(int p, int r, ArrayList<Chromosome> chromosomes) {
        if(p < r) {
            int  q = Math.floorDiv((p + r), 2);
            mergeSort(p, q, chromosomes);
            mergeSort(q + 1, r, chromosomes);
            merge(p, q, r, chromosomes);
        }
    }

    private static void merge(int p, int q, int r,  ArrayList<Chromosome> chromosomes) {
        Chromosome maxChromosome = new Chromosome();
        maxChromosome.setFitness(Integer.MAX_VALUE);
        ArrayList<Chromosome> lChromosomes
                = new ArrayList<>(chromosomes.subList(p, q + 1));
        lChromosomes.add(maxChromosome);
        ArrayList<Chromosome> rChromosomes
                = new ArrayList<>(chromosomes.subList(q + 1, r + 1));
        rChromosomes.add(maxChromosome);
        int i = 0;
        int j = 0;
        for (int k = p; k < r + 1; k++) {
            if(lChromosomes.get(i).compareTo(rChromosomes.get(j)) - 1 != 0) {
                chromosomes.set(k, lChromosomes.get(i));
                i++;
            }
            else {
                chromosomes.set(k, rChromosomes.get(j));
                j++;
            }
        }
    }
}
