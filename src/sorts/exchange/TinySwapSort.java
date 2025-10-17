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

public class TinySwapSort extends Sort {
    public TinySwapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Tinyswap");
        this.setRunAllSortsName("Tinyswap Sort");
        this.setRunSortName("Tinyswap Sort");
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
        for (int i = 0; i < currentLength-1; i++)
            for (int j = i+1; j < currentLength; j++) {
                Writes.swap(array, i, j, 0.5, true, false);
                if (Reads.compareIndices(array, i, j, 0.25, true) > 0) j = i;
            }
    }
}
