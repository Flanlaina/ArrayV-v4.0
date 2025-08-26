package sorts.select;

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
 * @author JouperNordin
 *
 */
public class IndexRankSort extends Sort {
    public IndexRankSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Index Rank");
        this.setRunAllSortsName("Index Rank Sort");
        this.setRunSortName("Index Ranksort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void sort(int[] array, int a, int b) {
        int length = b - a;
        int[] keys = Writes.createExternalArray(length);
        
        double sleep = 2d / length;
        
        // find sorted indices
        
        for(int j = a; j < b; j++) {
            int c = 0;
            
            for(int i = a; i < b; i++) {
                if(i == j) continue;
                int cmp = Reads.compareIndices(array, i, j, sleep, true);
                if(cmp < 0 || (cmp == 0 && i < j)) c++;
            }
            Writes.write(keys, j-a, c, 0, false, true);
        }

        for (int i = 0; i < length; i++) {
            while (Reads.compareOriginalValues(i, keys[i]) != 0) {
                Writes.swap(array, a + i, a + keys[i], 0, true, false);
                Writes.swap(keys, i, keys[i], 1, false, true);
            }
        }
        Writes.deleteExternalArray(keys);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
