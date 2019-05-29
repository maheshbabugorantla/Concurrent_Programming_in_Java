package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 *
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     *
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */

    public SieveActor() {
        super();
    }

    @Override
    public int countPrimes(final int limit) {
        final SieveActorActor sieveActorActor = new SieveActorActor(2);

        int totalPrimes = 1; // Starting with 2

        finish(() -> {
            for(int number = 3; number <= limit; number+=2) {
                final Object msg = number;
                sieveActorActor.send(msg);
            }
            sieveActorActor.send(0);
        });

        SieveActorActor currentActor = sieveActorActor;

        while(currentActor != null) {
            totalPrimes += currentActor.getNumPrimesCount();
            currentActor = currentActor.nextActor;
        }

        return totalPrimes;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {
        /**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */

        private static final int max_local_primes = 100;
        private final int[] localPrimes;
        private int numPrimesCount;
        private SieveActorActor nextActor;

        public SieveActorActor(int primeNumber) {
            this.localPrimes = new int[max_local_primes];
            this.numPrimesCount = 0;
            this.nextActor = null;
        }

        public int getNumPrimesCount() {
            return numPrimesCount;
        }

        @Override
        public void process(final Object msg) {

            Integer candidate = (Integer) msg;

            if (candidate <= 0) {
                if (nextActor != null) {
                    nextActor.send(msg);
                }
                return;
            }

            final boolean isCandidatePrime = isLocallyPrime(candidate);

            if (isCandidatePrime) {
                if(numPrimesCount < max_local_primes) {
                    localPrimes[numPrimesCount] = candidate;
                    numPrimesCount += 1;
                } else if(nextActor == null) {
                    this.nextActor = new SieveActorActor(candidate);
                    this.nextActor.send(msg);
                } else {
                    nextActor.send(msg);
                }
            }
        }

        private boolean isLocallyPrime(Integer number) {

            for (int index = 0; index < this.numPrimesCount; index++) {
                if (number % this.localPrimes[index] == 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
