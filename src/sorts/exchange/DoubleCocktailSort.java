package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Flanlaina
extending code by Potassium

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author Potassium
 *
 */
public class DoubleCocktailSort extends Sort {
    public DoubleCocktailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Double Cocktail Shaker");
        this.setRunAllSortsName("Double Cocktail Shaker Sort");
        this.setRunSortName("Double Cocktail Shakersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /**
     * Sorts the range {@code [left, right)} of {@code array} using a modified
     * version of Cocktail Shaker Sort.
     * 
     * @param array the array
     * @param left  the start of the range, inclusive
     * @param right the end of the range, exclusive
     */
    public void cocktailSort(int[] array, int left, int right) {
        int s = 0, e = right - left;
        while (e - s > 1) {
            boolean sorted = true;
            for (int j = left + s, k = right - s - 1; j < right - s - 1; j++, k--) {
                if (Reads.compareIndices(array, j, j + 1, 0.025, true) > 0) {
                    Writes.swap(array, j, j + 1, 0.075, true, false);
                    sorted = false;
                }
                if (Reads.compareIndices(array, k, k - 1, 0.025, true) < 0) {
                    Writes.swap(array, k, k - 1, 0.075, true, false);
                    sorted = false;
                }
            }
            if (sorted) break;
            s++;
            e--;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        cocktailSort(array, 0, sortLength);
    }
}
