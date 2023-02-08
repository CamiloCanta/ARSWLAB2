package edu.eci.arsw.primefinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{


    int a,b;

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


    private ArrayList<Integer> count;
    public PrimeFinderThread(int a, int b) {
        super();
        this.primes = new LinkedList<>();
        this.a = a;
        this.b = b;
    }

    public PrimeFinderThread(int a, int b,ArrayList<Integer> count) {
        super();
        this.primes = new LinkedList<>();
        this.a = a;
        this.b = b;
        this.count = count;

    }

    private void addPrime() throws InterruptedException {
        synchronized (count) {
            for (int i = a; i < b; i++) {
                if (isPrime(i)) {
                    primes.add(i);
                    count.add(i);
                }
            }
            count.wait();
        }
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