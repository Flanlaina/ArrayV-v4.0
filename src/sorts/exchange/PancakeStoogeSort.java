package sorts.exchange;

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
public class PancakeStoogeSort extends Sort {
    public PancakeStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pancake Stooge");
        this.setRunAllSortsName("Pancake Stooge Sort");
        this.setRunSortName("Pancake Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    // Easy patch to avoid self-reversals and the "reversals can be done in a single
    // swap" notes.
    protected void reverse(int[] array, int a, int b, double sleep, boolean mark, boolean aux) {
        if (b <= a) return;
        if (b - a >= 3) Writes.reversal(array, a, b, sleep, mark, aux);
        else Writes.swap(array, a, b, sleep, mark, aux);
    }

    public void stooge(int[] arr, int len, boolean invert) {
        if (len == 2) {
            if (invert ^ (Reads.compareIndices(arr, 0, 1, 0.05, true) > 0))
                reverse(arr, 0, 1, 0.05, true, false);
        } else if (len > 2) {
            int third = len / 3;
            stooge(arr, len - third, invert);
            reverse(arr, 0, len - 1, 0.05, true, false);
            stooge(arr, len - third, !invert);
            reverse(arr, 0, len - 1, 0.05, true, false);
            stooge(arr, len - third, invert);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        stooge(array, sortLength, false);
    }
}
