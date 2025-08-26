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
 * @author frankblob
 * @author Flanlaina
 *
 */
public class BadShoveSort extends Sort {
    public BadShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Bad Shove");
        setRunAllSortsName("Bad Shove Sort");
        setRunSortName("Bad Shove Sort");
        setCategory("Impractical Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(512);
        setBogoSort(false);
    }

    public void shovesort(int[] array, int start, int end, double sleep) {
        int i = start;
        while (i < end - 1) {
            if (Reads.compareIndices(array, i, i + 1, sleep, true) > 0) {
                Writes.multiSwap(array, i, end - 1, sleep, true, false);
                i = start;
            } else i++;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        shovesort(array, 0, sortLength, 0.125);
    }
}
