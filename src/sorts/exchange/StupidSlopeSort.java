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

tiny gnomesort but as a sorting network

 */

public final class StupidSlopeSort extends Sort {
    public StupidSlopeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stupid Slope");
        this.setRunAllSortsName("Stupid Slope Sort");
        this.setRunSortName("Stupid Slopesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i < currentLength; i++)
            for (int j = i; j > 0; j--)
                for (int k = 1; k <= j; k++)
                    if (Reads.compareIndices(array, k-1, k, 0.25, true) > 0)
                        Writes.swap(array, k-1, k, 0.5, true, false);
    }
}
