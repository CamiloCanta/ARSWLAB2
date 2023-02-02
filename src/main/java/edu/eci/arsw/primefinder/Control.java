/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {

    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];

    private Control() {
        super();
        this.pft = new PrimeFinderThread[NTHREADS];

        int i;
        for (i = 0; i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i * NDATA, (i + 1) * NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i * NDATA, MAXVALUE + 1);
    }

    public static Control newControl() {
        return new Control();
    }


    private boolean IsAlive() {

        for (PrimeFinderThread primeThread : pft){
            if (primeThread.isAlive()) {
                //System.out.print("a");
                return true;
            }
        }
        return false;
    }


    private  void OutputNumberPrimesPartials() throws InterruptedException {
        for (int i = 0; i < NTHREADS; i++) {
            int id = i+1;
            System.out.println("Numero primos encontrados por el thread #" + id + " " + pft[i].getPrimes().size() + "\n");
            pft[i].wait();
        }
    }


    private synchronized void wakeUpThreads(){
        for(int i = 0; i < NTHREADS;i++ ){
            System.out.println("Hilo # " + i+1 + " se desperto");
            pft[i].notify();
        }
    }



    @Override
    public void run() {

        //correr hilos
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();

        }


        //parar de acuerdo a un tiempo

        Scanner sc = new Scanner(System.in);


        long time = System.currentTimeMillis() + TMILISECONDS;

        System.out.println(IsAlive());
        while(IsAlive()){
            boolean continueProccess = true;

            if(System.currentTimeMillis() >= time){
                try {
                    OutputNumberPrimesPartials();
                    continueProccess = false;


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            while(!continueProccess){
                System.out.println("PRESIONE ENTER PARA CONTINUAR");
                if(sc.nextLine().equals("")){
                    wakeUpThreads();
                }
                continueProccess = true;


            }
        }
        System.out.println("fin");

    }

}