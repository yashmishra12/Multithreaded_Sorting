import java.util.Arrays;

import java.util.Random;

import java.util.Scanner;

class Sorter implements Runnable {

   private int[] a;

   private int threadCount;

   public Sorter(int[] a, int threadCount) {

       this.a = a;

       this.threadCount = threadCount;

   }

   public void run() {

       ThreadedMergeSort.parallelMergeSort(a, threadCount);

   }

}

public class ThreadedMergeSort {

   private static final Random RAND = new Random(42); // random number generator

   private static int threadCount;

   public ThreadedMergeSort(int threadCount) {

       this.threadCount = threadCount;

   }

   public static void main(String[] args) throws Throwable {

       Scanner sc = new Scanner(System.in);

       int threads, n;

       System.out.println("Enter the number of threads: ");

       threads = sc.nextInt();

       System.out.println("Enter the number of elements in the array : ");

       n = sc.nextInt();

       int[] arr = new int[n];

       System.out.println("Enter the array elements (Space separated) : ");

       for(int i=0; i<n; i++) {

           arr[i] = sc.nextInt();

       }

       int[] copy = Arrays.copyOf(arr, arr.length);

       ThreadedMergeSort sorter = new ThreadedMergeSort(threads);

       long start, end;

       start = System.currentTimeMillis();

       sorter.parallelMergeSort(arr);

       end = System.currentTimeMillis();

       System.out.println("Threaded merge sort took: " + (end-start) + " milliseconds.");

       arr = Arrays.copyOf(copy, copy.length);

       start = System.currentTimeMillis();

       sorter.mergeSort(arr);

       end = System.currentTimeMillis();

       System.out.println("Normal merge sort took: " + (end-start) + " milliseconds.");

   }

   public static void parallelMergeSort(int[] a) {

       // int cores = Runtime.getRuntime().availableProcessors();

       parallelMergeSort(a, threadCount);

   }

   public static void parallelMergeSort(int[] a, int threadCount) {

       if (threadCount <= 1) {

           mergeSort(a);

       } else if (a.length >= 2) {

           // split array in half

           int[] left = Arrays.copyOfRange(a, 0, a.length / 2);

           int[] right = Arrays.copyOfRange(a, a.length / 2, a.length);

           // sort the halves

           // mergeSort(left);

           // mergeSort(right);

           Thread lThread = new Thread(new Sorter(left, threadCount / 2));

           Thread rThread = new Thread(new Sorter(right, threadCount / 2));

           lThread.start();

           rThread.start();

           try {

               lThread.join();

               rThread.join();

           } catch (InterruptedException ie) {}

           // merge them back together

           merge(left, right, a);

       }

   }

   // Arranges the elements of the given array into sorted order

   // using the "merge sort" algorithm, which splits the array in half,

   // recursively sorts the halves, then merges the sorted halves.

   // It is O(N log N) for all inputs.

   public static void mergeSort(int[] a) {

       if (a.length >= 2) {

           // split array in half

           int[] left = Arrays.copyOfRange(a, 0, a.length / 2);

           int[] right = Arrays.copyOfRange(a, a.length / 2, a.length);

           // sort the halves

           mergeSort(left);

           mergeSort(right);

           // merge them back together

           merge(left, right, a);

       }

   }

   // Combines the contents of sorted left/right arrays into output array a.

   // Assumes that left.length + right.length == a.length.

   public static void merge(int[] left, int[] right, int[] a) {

       int i1 = 0;

       int i2 = 0;

       for (int i = 0; i < a.length; i++) {

           if (i2 >= right.length || (i1 < left.length && left[i1] < right[i2])) {

               a[i] = left[i1];

               i1++;

           } else {

               a[i] = right[i2];

               i2++;

           }

       }

   }

   // Swaps the values at the two given indexes in the given array.

   public static final void swap(int[] a, int i, int j) {

       if (i != j) {

           int temp = a[i];

           a[i] = a[j];

           a[j] = temp;

       }

   }

   // Randomly rearranges the elements of the given array.

   public static void shuffle(int[] a) {

       for (int i = 0; i < a.length; i++) {

           // move element i to a random index in [i .. length-1]

           int randomIndex = (int) (Math.random() * a.length - i);

           swap(a, i, i + randomIndex);

       }

   }

   // Returns true if the given array is in sorted ascending order.

   public static boolean isSorted(int[] a) {

       for (int i = 0; i < a.length - 1; i++) {

           if (a[i] > a[i + 1]) {

               return false;

           }

       }

       return true;

   }

   // Creates an array of the given length, fills it with random

   // non-negative integers, and returns it.

   public static int[] createRandomArray(int length) {

       int[] a = new int[length];

       for (int i = 0; i < a.length; i++) {

           a[i] = RAND.nextInt(1000000);

           // a[i] = RAND.nextInt(40);

       }

       return a;

   }

}

