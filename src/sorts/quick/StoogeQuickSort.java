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
public class StoogeQuickSort extends Sort {
    public StoogeQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stooge Quick");
        this.setRunAllSortsName("Stooge Quick Sort");
        this.setRunSortName("Stooge Quicksort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int t;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            t = i1;
            i1 = i0;
        } else t = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, t, i2, 1, true) > 0) return t;
            return i2;
        }
        return i1;
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0))
                b = m;
            else
                a = m + 1;
        }
        return a;
    }
    
    protected int pivCmpHelper(int v, int piv) {
        int c = Reads.compareValues(v, piv);
        return c > 0 ? 1 : (c < 0 ? -1 : 0);
    }

    protected int pivCmp(int[] array, int a, int b, int piv, boolean eqLower) {
        Highlights.markArray(1, a);
        Highlights.markArray(2, b);
        Delays.sleep(0.005);
        int c1 = pivCmpHelper(array[a], piv);
        int c2 = pivCmpHelper(array[b], piv);
        int biasType = eqLower ? 1 : 0;
        return (c1 >= biasType && c2 < biasType ? 1 : c1 < biasType && c2 >= biasType ? -1 : 0);
    }

    private void stoogeSort(int[] array, int start, int end, int piv, boolean eqLower) {
        if (pivCmp(array, start, end, piv, eqLower) > 0) {
            Writes.swap(array, start, end, 0.005, true, false);
        }
        
        if (end - start + 1 >= 3) {
            int t = (end - start + 1) / 3;
    
            this.stoogeSort(array, start, end-t, piv, eqLower);
            this.stoogeSort(array, start+t, end, piv, eqLower);
            this.stoogeSort(array, start, end-t, piv, eqLower);
        }
    }

    protected int partition(int[] array, int start, int end, int piv, boolean eqLower) {
        stoogeSort(array, start, end - 1, piv, eqLower);
        return binSearch(array, start, end, piv, !eqLower);
    }

    protected void sortHelper(int[] array, int a, int b) {
        while (b - a > 2) {
            int pivIdx = medOf3(array, a, a + (b - a) / 2, b - 1);
            int m = partition(array, a, b, array[pivIdx], false);
            if (m == a) {
                a = partition(array, a, b, array[pivIdx], true);
                continue;
            }
            if (b - m < m - a) {
                sortHelper(array, m, b);
                b = m;
            } else {
                sortHelper(array, a, m);
                a = m;
            }
        }
        if (b - a == 2) {
            if (Reads.compareIndices(array, a, a + 1, 1, true) > 0)
                Writes.swap(array, a, a + 1, 1, true, false);
        }
    }
    
    public void quickSort(int[] array, int a, int b) {
        sortHelper(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);
    }
}
