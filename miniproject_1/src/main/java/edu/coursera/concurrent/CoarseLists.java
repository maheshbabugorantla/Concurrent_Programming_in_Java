package edu.coursera.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Wrapper class for two lock-based concurrent list implementations.
 */
public final class CoarseLists {
    /**
     * An implementation of the ListSet interface that uses Java locks to
     * protect against concurrent accesses.
     *
     * TODO Implement the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java ReentrantLock object
     * to protect against those concurrent accesses. You may refer to
     * SyncList.java for help understanding the list management logic, and for
     * guidance in understanding where to place lock-based synchronization.
     */
    public static final class CoarseList extends ListSet {
        /*
         * TODO Declare a lock for this class to be used in implementing the
         * concurrent add, remove, and contains methods below.
         */

        ReentrantLock reentrantLock = new ReentrantLock();

        /**
         * Default constructor.
         */
        public CoarseList() {
            super();
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a lock to protect against concurrent access.
         */
        @Override
        boolean add(final Integer object) {

            reentrantLock.lock(); // Acquiring the Lock

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (!object.equals(curr.object)) {
                    final Entry entry = new Entry(object);
                    entry.next = curr;
                    pred.next = entry;
                    return true;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
            return false;
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a lock to protect against concurrent access.
         */
        @Override
        boolean remove(final Integer object) {
            reentrantLock.lock(); // Creates a Blocking Lock
            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    pred.next = curr.next;
                    return true;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
            return false;
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a lock to protect against concurrent access.
         */
        @Override
        boolean contains(final Integer object) {

            reentrantLock.lock();

            try {
                Entry curr = this.head.next;

                while (curr.object.compareTo(object) < 0) {
                    curr = curr.next;
                }
                return object.equals(curr.object);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
            return false;
        }
    }

    /**
     * An implementation of the ListSet interface that uses Java read-write
     * locks to protect against concurrent accesses.
     *
     * TODO Implement the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java
     * ReentrantReadWriteLock object to protect against those concurrent
     * accesses. You may refer to SyncList.java for help understanding the list
     * management logic, and for guidance in understanding where to place
     * lock-based synchronization.
     */
    public static final class RWCoarseList extends ListSet {
        /*
         * TODO Declare a read-write lock for this class to be used in
         * implementing the concurrent add, remove, and contains methods below.
         */

        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
//        private final Lock readLock;
//        private final Lock writeLock;

        /**
         * Default constructor.
         */
        public RWCoarseList() {
            super();
//            this.readLock = reentrantReadWriteLock.readLock();
//            this.writeLock = reentrantReadWriteLock.writeLock();
        }

        /**
         * {@inheritDoc}
         *
         * TODO Use a read-write lock to protect against concurrent access.
         */
        @Override
        boolean add(final Integer object) {

            reentrantReadWriteLock.writeLock().lock();

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (!object.equals(curr.object)) {
                    final Entry entry = new Entry(object);
                    entry.next = curr;
                    pred.next = entry;
                    return true;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
            return false;
        }

        /**
         * {@inheritDoc}
         *
         * TODO Use a read-write lock to protect against concurrent access.
         */
        @Override
        boolean remove(final Integer object) {

            reentrantReadWriteLock.writeLock().lock();

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    pred.next = curr.next;
                    return true;
                }
            } catch(NullPointerException e) {
                e.printStackTrace();
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
            return false;
        }

        /**
         * {@inheritDoc}
         *
         * TODO Use a read-write lock to protect against concurrent access.
         */
        @Override
        boolean contains(final Integer object) {

            reentrantReadWriteLock.readLock().lock();

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }
                return object.equals(curr.object);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                reentrantReadWriteLock.readLock().unlock();
            }
            return false;
        }
    }
}
