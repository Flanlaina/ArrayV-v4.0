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
 * @author gooflang
 *
 */
public class PancakeHeadReversalSort extends Sort {
    public PancakeHeadReversalSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pancake Head Reversal");
        this.setRunAllSortsName("Pancake Head Reversal Sort");
        this.setRunSortName("Pancake Head Reversal Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i < currentLength;) {
            if (Reads.compareIndices(array, i-1, i, 0.25, true) > 0) {
                Writes.reversal(array, 0, i, 0.25, true, false);
                Writes.reversal(array, 0, 1, 0.25, true, false);
                Writes.reversal(array, 0, i, 0.25, true, false);
                Writes.reversal(array, 0, i-1, 0.25, true, false);
                i = 1;
            } else i++;
        }
    }
}
