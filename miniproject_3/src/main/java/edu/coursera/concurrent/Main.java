package edu.coursera.concurrent;

public class Main {

    public static void main(String[] args) {
        int limit = 400000;

        new SieveActor().countPrimes(limit); // warmup
        System.gc();
        new SieveActor().countPrimes(limit); // warmup
        System.gc();
        new SieveActor().countPrimes(limit); // warmup
        System.gc();

        SieveActor actor = new SieveActor();
        SieveSequential sieveSequential = new SieveSequential();
        int primes_less_than_50_seq = sieveSequential.countPrimes(limit);
        int primes_less_than_50_par = actor.countPrimes(limit);
        System.out.println("Count of primes in first " + limit + " numbers (Sequential): " + primes_less_than_50_seq);
        System.out.println("Count of primes in first " + limit + " numbers (Parallel): " + primes_less_than_50_par);
    }
}
