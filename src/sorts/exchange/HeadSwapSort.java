package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

STG-26 is coming. You don't have much time left.

 */

/**
 * @author Flanlaina
 * @author gooflang
 * 
 */
public class HeadSwapSort extends Sort {
    public HeadSwapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Head Swap");
        this.setRunAllSortsName("Head Swap Sort");
        this.setRunSortName("Head Swap Sort");
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
                Writes.swap(array, i-1, i, 0.5, true, false);
                Writes.swap(array, 0, i-1, 0.5, true, false);
                i = 1;
            } else i++;
        }
    }
}
