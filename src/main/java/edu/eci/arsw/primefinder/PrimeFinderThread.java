package edu.eci.arsw.primefinder;

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

    public PrimeFinderThread(int a, int b) {
        super();
        this.primes = new LinkedList<>();
        this.a = a;
        this.b = b;
    }

    private synchronized  void addPrime(){
        for (int i= a;i < b;i++){
            if (isPrime(i)){
                primes.add(i);
            }
        }
    }

    @Override
    public void run(){
        addPrime();
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