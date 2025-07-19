package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan
 * @author fungamer2
 *
 */
public final class PDFibonacciInsertionSort extends Sort {

    public PDFibonacciInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Fibonacci Insertion");
        this.setRunAllSortsName("Pattern-Defeating Fibonacci Insertion Sort");
        this.setRunSortName("Pattern-Defeating Fibonacci Insertion Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int fibonacciSearch(int[] array, int start, int end, int item) {
        int fibM2 = 0;
        int fibM1 = 1;
        int fibM = 1;
        while (fibM <= end - start) {
            fibM2 = fibM1;
            fibM1 = fibM;
            fibM = fibM2 + fibM1;
        }
        int offset = start - 1;
        while (fibM > 1) {
            int i = Math.min(offset + fibM2, end);
            Highlights.markArray(1, offset + 1);
            Highlights.markArray(2, i);
            if (Reads.compareValues(array[i], item) <= 0) {
                fibM = fibM1;
                fibM1 = fibM2;
                fibM2 = fibM - fibM1;
                offset = i;
            } else {
                fibM = fibM2;
                fibM1 -= fibM2;
                fibM2 = fibM - fibM1;
            }
            Delays.sleep(0.6);
        }
        int position = ++offset;
        if (Reads.compareValues(array[position], item) <= 0) {
            position++;
        }
        return position;
    }

    public void fibonacciInsertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (Reads.compareIndices(array, i - 1, i++, 0.6, true) == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.6, true) == 1)
                i++;
            Writes.reversal(array, a, i - 1, 0.15, true, false);
        } else
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.6, true) <= 0)
                i++;
        for(; i < b; i++) {
            int tmp = array[i];
            int position = this.fibonacciSearch(array, a, i - 1, tmp);
            int j = i - 1;
            while (j >= position) {
                Writes.write(array, j + 1, array[j--], 0.15, true, false);
            }
            Writes.write(array, j + 1, tmp, 0.15, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        fibonacciInsertSort(array, 0, sortLength);

    }

}
