package com.ngm.ga;

import com.ngm.tsp.CityPopulation;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            CityPopulation cp = new CityPopulation("input\\st70.tsp");
            System.out.println("Attemp #" + i + ":");
            cp.GAAlgorithm();
            cp.printBestSolution();
        }
    }
}
