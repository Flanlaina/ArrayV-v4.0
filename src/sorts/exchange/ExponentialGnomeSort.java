package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Kiriko-chan
 *
 */
public final class ExponentialGnomeSort extends Sort {

    public ExponentialGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exponential Gnome");
        this.setRunAllSortsName("Optimized Gnome Sort + Exponential Search");
        this.setRunSortName("Optimized Gnomesort + Exponential Search");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int rightBinSearch(int[] array, int a, int b, int val, double sleep) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(0, a);
            Highlights.markArray(1, m);
            Highlights.markArray(2, b);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    private int rightExpSearch(int[] array, int a, int b, int val, double sleep) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) {
            i *= 2;
        }
        return rightBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val, sleep);
    }

    private void gnomeSort(int[] array, int a, int b, double compSleep, double writeSleep) {
        for (int i = a + 1; i < b; i++) {
            Writes.multiSwap(array, i, rightExpSearch(array, a, i, array[i], compSleep), writeSleep, true, false);
        }
    }
    
    public void customSort(int[] array, int a, int b, double sleep) {
        gnomeSort(array, a, b, sleep / 2.0, sleep);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        gnomeSort(array, 0, sortLength, 4, 0.8);

    }

}
