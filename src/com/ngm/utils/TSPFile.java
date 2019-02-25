package com.ngm.utils;

import com.ngm.tsp.CityPopulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TSPFile {
    //Read a TSP file.
    public static void readFile(String filename, CityPopulation cp) {
        File file = new File(filename);
        Scanner sc;
        StringTokenizer st;
        try {
            sc = new Scanner(file);
            System.out.println(sc.nextLine()); //File name.
            System.out.println(sc.nextLine()); //File type: TSP.
            System.out.println(sc.nextLine()); //Comment for file.
            //Get problem dimension.
            st = new StringTokenizer(sc.nextLine());
            while(st.hasMoreTokens()) {
                st.nextToken(": ");
                cp.setCityCount(Integer.parseInt(st.nextToken()));
            }
            //Get problem's fitness value type.
            st = new StringTokenizer(sc.nextLine());
            while(st.hasMoreTokens()) {
                st.nextToken(": ");
                cp.setFitnessType(st.nextToken());
            }
            //Type of weight.
            if(sc.nextLine().equals(ConstantString.NODE_COORD_SECTION)) {
                //Get point coordinate.
                for (int i = 0; i < cp.getCityCount(); i++) {
                    sc.nextInt();
                    cp.setaXByIndex(i, sc.nextDouble());
                    cp.setaYByIndex(i, sc.nextDouble());
                }
            }
            //else..

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
