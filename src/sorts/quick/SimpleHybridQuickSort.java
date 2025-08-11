package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author Flanlaina
 *
 */
public class SimpleHybridQuickSort extends Sort {

    public SimpleHybridQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Simple Hybrid Quick");
        setRunAllSortsName("Simple Hybrid Quick Sort");
        setRunSortName("Simple Hybrid Quicksort");
        setCategory("Quick Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    static int log2(int val) {
        return 31 - Integer.numberOfLeadingZeros(val);
    }

    private int medianOfThree(int[] array, int i0, int i1, int i2) {
        int tmp;
        if(Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if(Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if(Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    private int ninther(int[] array, int a, int b) {
        int s = (b - a) / 9;

        int a1 = medianOfThree(array, a, a + s, a + 2 * s);
        int m1 = medianOfThree(array, a + 3 * s, a + 4 * s, a + 5 * s);
        int b1 = medianOfThree(array, a + 6 * s, a + 7 * s, a + 8 * s);

        return medianOfThree(array, a1, m1, b1);
    }

    private int medianOfThreeNinthers(int[] array, int a, int b) {
        int s = (b - a + 2) / 3;

        int a1 = ninther(array, a, a + s);
        int m1 = ninther(array, a + s, a + 2 * s);
        int b1 = ninther(array, a + 2 * s, b);

        return medianOfThree(array, a1, m1, b1);
    }

    private int partition(int[] array, int a, int b, int val) {
        int i = a, j = b;
        while (i <= j) {
            while (Reads.compareValues(array[i], val) < 0) {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5D);
            }
            while (Reads.compareValues(array[j], val) > 0) {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5D);
            }

            if (i <= j) {
                if (i != j) Writes.swap(array, i, j, 1.0D, true, false);
                i++; j--;
            }

        }
        return i;
    }

    public void insertSort(int[] array, int a, int b, double delay) {
        for (int i = a + 1; i < b; i++) {
            int j = i;
            int t = array[i];
            while (j > a && Reads.compareValueIndex(array, t, j - 1, delay, true) < 0) {
                Writes.write(array, j, array[j - 1], delay, true, false);
                j--;
            }
            if (j != i)Writes.write(array, j, t, delay, true, false);
        }
    }

    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (4 * i + 1 < n) {
            int max = val;
            int next = i, child = 4 * i + 1;
            for (int j = child; j < Math.min(child + 4, n); j++) {
                if (Reads.compareValues(array[p + j], max) > 0) {
                    max = array[p + j];
                    next = j;
                }
            }
            if (next == i) break;
            Writes.write(array, p + i, max, 1, true, false);
            i = next;
        }
        Writes.write(array, p + i, val, 1, true, false);
    }

    public void heapSort(int[] array, int a, int b) {
        int n = b - a;
        for (int i = (n - 1) / 4; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
    }

    private void sort(int[] array, int a, int b, int depthLimit) {
        while (b - a > 16) {
            if (depthLimit == 0) {
                heapSort(array, a, b);
                return;
            }
            int piv = medianOfThreeNinthers(array, a, b - 1);
            int p = partition(array, a, b - 1, array[piv]);
            depthLimit--;
            if (b - p < p - a) {
                sort(array, p, b, depthLimit);
                b = p;
            } else {
                sort(array, a, p, depthLimit);
                a = p;
            }
        }
        insertSort(array, a, b, 0.5D);
    }
    
    public void quickSort(int[] array, int a, int b) {
        sort(array, a, b, 2 * log2(b - a));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
