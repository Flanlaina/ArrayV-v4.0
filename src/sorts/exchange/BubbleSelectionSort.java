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

i guess it's a way to find the min element...

 */

public final class BubbleSelectionSort extends Sort {
    public BubbleSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bubble Selection");
        this.setRunAllSortsName("Bubble Selection Sort");
        this.setRunSortName("Bubble Selection Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength-1; i++) {
            for (int j = i; j < currentLength-1; j++)
                if (Reads.compareIndices(array, j, j+1, 0.25, true) < 0)
                    Writes.swap(array, j, j+1, 0.5, true, false);
            Writes.swap(array, i, currentLength-1, 0.5, true, false);
        }
    }
}
