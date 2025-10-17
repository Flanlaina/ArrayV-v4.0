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
public class ReverseDistributiveSort extends Sort {
    public ReverseDistributiveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Reverse Distributive");
        this.setRunAllSortsName("Reverse Distributive Sort");
        this.setRunSortName("Reverse Distributive Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void exchangeHalver(int[] array, int a, int m, int b) {
        for (int i = a; i < m; i++) {
            for (int j = b - 1; j >= m; j--) {
                if (Reads.compareIndices(array, i, j, 0.05, true) > 0)
                    Writes.swap(array, i, j, 0.05, true, false);
            }
        }
    }

    public void sort(int[] array, int a, int b) {
        if (b - a < 2) return;
        int m = a + (b - a) / 2;
        exchangeHalver(array, a, m, b);
        sort(array, a, m);
        sort(array, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        sort(array, 0, sortLength);
    }
}

