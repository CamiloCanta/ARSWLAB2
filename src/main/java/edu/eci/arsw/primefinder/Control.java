/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {

    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread[] pft;

    private int suma ;




    private Control() {
        super();
        this.pft = new PrimeFinderThread[NTHREADS];
        int i;
        for (i = 0; i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i * NDATA, (i + 1) * NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i * NDATA, MAXVALUE + 1);
        suma = 0;
    }

    public static Control newControl() {
        return new Control();
    }


    private boolean IsAlive() {
        for (PrimeFinderThread primeThread : pft) {
            if (primeThread.isAlive()) {
                return true;
            }
        }
        return false;
    }


    private void OutputNumberPrimesPartials() throws InterruptedException {
        System.out.println("NUMEROS CALCULADOS HASTA EL MOMENTOS " + PrimeFinderThread.getCount());
        for(PrimeFinderThread pt: pft) {
            pt.sleepExecute();
        }







    }


    private void wakeUpThreads() {
        for(PrimeFinderThread pt: pft) {
            pt.notifyPrime();
        }
    }


    @Override
    public void run() {

        //correr hilos
        for (int i = 0; i < NTHREADS; i++) {
            pft[i].start();

        }
        Scanner sc = new Scanner(System.in);

        long time = System.currentTimeMillis() + TMILISECONDS;

        boolean calculos = true;
        while(IsAlive()){
            if (System.currentTimeMillis() >= time) {
                try {
                    OutputNumberPrimesPartials();
                    calculos = false;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            while(!calculos){
                System.out.println("DIGITE ENTER PARA CONTINUAR");
                if(sc.nextLine().equals("")){
                    wakeUpThreads();
                    calculos = true;
                }
                time = System.currentTimeMillis() + TMILISECONDS;
            }
        }
        System.out.println("NUMEROS PRIMOS EN TOTAL " + PrimeFinderThread.getCount());
        System.exit(0);
    }

}


