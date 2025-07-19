package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author frankblob
 * @author PiotrGrochowski
 *
 */
public final class ShoveSort extends Sort {

    public ShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Shove");
        setRunAllSortsName("Shove Sort");
        setRunSortName("Shove Sort");
        setCategory("Impractical Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(512);
        setBogoSort(false);
    }

    private void shovesort(int[] array, int start, int end, double sleep) {
        int i = start;
        while (i < end - 1) {
            if (Reads.compareIndices(array, i, i + 1, sleep, true) > 0) {
                Writes.multiSwap(array, i, end - 1, sleep, true, false);
                if (i > start) i--;
            } else i++;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        shovesort(array, 0, length, 0.125D);

    }

}
