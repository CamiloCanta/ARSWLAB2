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

    private final ArrayList<Integer> primes;

    private Control() {
        super();
        this.pft = new PrimeFinderThread[NTHREADS];
        int i;
        this.primes = new ArrayList<Integer>();
        for (i = 0; i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i * NDATA, (i + 1) * NDATA, primes);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i * NDATA, MAXVALUE + 1,primes);
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
        System.out.println("Numero primos encontrados" + " " + primes.size() + "\n");




    }


    private void wakeUpThreads() {
        //notifica que todos lo pueden usar
        synchronized (primes){
            primes.notifyAll();
        }


    }


    @Override
    public void run() {

        //correr hilos
        for (int i = 0; i < NTHREADS; i++) {
            pft[i].start();

        }


        //parar de acuerdo a un tiempo

        Scanner sc = new Scanner(System.in);


        long time = System.currentTimeMillis() + TMILISECONDS;
        boolean continueProccess = true;
        while (IsAlive()) {


            if (System.currentTimeMillis() >= time) {
                try {
                    OutputNumberPrimesPartials();
                    continueProccess = false;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            while (!continueProccess) {
                System.out.println("PRESIONE ENTER PARA CONTINUAR");
                if (sc.nextLine().equals("")) {
                    wakeUpThreads();
                    continueProccess = true;
                }

            }



        }
        System.out.println("NUMEROS EN TOTAL :" + primes.size());
        System.out.println("fin");
    }



}

