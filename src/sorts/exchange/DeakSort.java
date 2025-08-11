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

This sort was made completely on accident.

 */

public final class DeakSort extends Sort {
    public DeakSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Deak");
        this.setRunAllSortsName("Deak Sort");
        this.setRunSortName("Deaksort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void deak(int[] array, int a, int b, int depth) {
        if (b <= a) return;
        if (Reads.compareIndices(array, a, b, 0.1, true) > 0) Writes.swap(array, a, b, 0.1, true, false);
        int m = a + (b - a) / 2;
        Writes.recordDepth(depth);
        Writes.recursion();
        deak(array, a, m, depth+1);
        Writes.recursion();
        deak(array, m+1, b, depth+1);
    }

    private boolean isSorted(int[] array, int a, int b) {
        for (int i = a+1; i < b; i++) if (Reads.compareIndices(array, i, i - 1, 0.1, true) < 0) return false;
        return true;
    }

    private int segmentCount(int[] array, int length){
        int count = 1;
        for (int i = 0; i < length; i++) {
            if (Reads.compareIndices(array, i, i + 1, 0.1, true) > 0){
                count++;
            }
        }
        return count;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isSorted(array, 0, currentLength)) {
            for (int i = 0; i <= currentLength; i++) {
                deak(array, 0, i, 0);
            }
            if (segmentCount(array, currentLength) == 2) break; 
        }
        for (int i = 1; i < currentLength; i++){
            if (Reads.compareIndices(array, i, i+1, 0.1, true) > 0){
                Writes.swap(array, i, i+1, 0.1, true, false);
            } else {
                break;
            }
        }
    }
}
