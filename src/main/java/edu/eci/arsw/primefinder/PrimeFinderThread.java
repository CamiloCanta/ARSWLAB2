package edu.eci.arsw.primefinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{


    int a,b;
    private boolean sleep;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setPrimes(List<Integer> primes) {
        this.primes = primes;
    }

    private List<Integer> primes;
    private static Integer count = 0;


    public static Integer getCount() {
        return count;
    }

    public static void setCount(Integer count) {
        PrimeFinderThread.count = count;
    }

    public PrimeFinderThread(int a, int b) {
        super();
        this.primes = new LinkedList<>();
        this.a = a;
        this.b = b;
        sleep = false;
    }




    private void addPrime() throws InterruptedException {
            for (int i = a; i < b; i++) {
                if(sleep){
                    waitPrime();
                }
                else{
                    {
                        if (isPrime(i)) {
                            primes.add(i);
                            synchronized (count){
                                count ++;
                            }
                        }
                    }
                }


            }

    }


    private  synchronized void waitPrime() throws InterruptedException {
        this.wait();
    }

    public synchronized void notifyPrime(){
        sleep = false;
        this.notify();
    }

    public void sleepExecute(){
        sleep = true;

    }





    @Override
    public void run(){
        try {
            addPrime();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isPrime(int n) {
        boolean ans;
        if (n > 2) {
            ans = n%2 != 0;
            for(int i = 3;ans && i*i <= n; i+=2 ) {
                ans = n % i != 0;
            }
        } else {
            ans = n == 2;
        }
        return ans;
    }

    public List<Integer> getPrimes() {
        return primes;
    }

}