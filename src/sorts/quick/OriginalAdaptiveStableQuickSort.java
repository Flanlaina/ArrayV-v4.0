package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Scandum
 * @author Yuri-chan
 *
 */
public final class OriginalAdaptiveStableQuickSort extends Sort {

    public OriginalAdaptiveStableQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Stable Quick (Original)");
        this.setRunAllSortsName("Original Adaptive Stable Quick Sort");
        this.setRunSortName("Original Adaptive Stable Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // slightly tweaked insertion sort
    public void insertionSort(int[] array, int a, int b, double sleep, boolean pd, boolean aux) {
        if (b - a < 2) return;
        int i = a + 1;
        if (pd) {
            if (Reads.compareIndices(array, i - 1, i++, sleep, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, sleep, true) >  0) i++;
                Writes.reversal(array, a, i - 1, sleep, true, aux);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, sleep, true) <= 0) i++;
            Highlights.clearMark(2);
        }
        while (i < b) {
            int current = array[i];
            int pos = i - 1;
            while (pos >= a && Reads.compareValues(array[pos], current) > 0) {
                Writes.write(array, pos + 1, array[pos], sleep, true, aux);
                pos--;
            }
            if (pos + 1 < i) Writes.write(array, pos + 1, current, sleep, true, aux);
            i++;
        }
    }

    int twinSwap(int[] array, int left, int nmemb) {
        int index, start, end;
        index = 0;
        end = nmemb - 2;
        while (index <= end) {
            if (Reads.compareIndices(array, index + left, index + 1 + left, 1, true) <= 0) {
                index += 2;
                continue;
            }
            start = index;
            index += 2;
            while (true) {
                if (index > end) {
                    if (start == 0) {
                        if (nmemb % 2 == 0
                                || Reads.compareIndices(array, index - 1 + left, index + left, 1, true) > 0) {
                            // the entire array was reversed
                            end = nmemb - 1;
                            Writes.reversal(array, start + left, end + left, 1, true, false);
                            return 1;
                        }
                    }
                    break;
                }
                if (Reads.compareIndices(array, index + left, index + 1 + left, 1, true) > 0) {
                    if (Reads.compareIndices(array, index - 1 + left, index + left, 1, true) > 0) {
                        index += 2;
                        continue;
                    }
                    Writes.swap(array, index + left, index + 1 + left, 1, true, false);
                }
                break;
            }
            end = index - 1;
            Writes.reversal(array, start + left, end + left, 1, true, false);
            end = nmemb - 2;
            index += 2;
        }
        return 0;
    }

    // by Scandum
    void tailMerge(int[] array, int left, int[] swap, int nmemb, int block) {
        int offset;
        int a, s, c, c_max, d, d_max, e;
        s = 0;
        while (block < nmemb) {
            for (offset = 0; offset + block < nmemb; offset += block * 2) {
                a = offset;
                e = a + block - 1;
                if (Reads.compareIndices(array, e + left, e + 1 + left, 1, true) <= 0)
                    continue;
                if (offset + block * 2 <= nmemb) {
                    c_max = s + block;
                    d_max = a + block * 2;
                } else {
                    c_max = s + nmemb - (offset + block);
                    d_max = 0 + nmemb;
                }
                d = d_max - 1;
                while (Reads.compareIndices(array, e + left, d + left, 1, true) <= 0) {
                    d_max--;
                    d--;
                    c_max--;
                }
                c = s;
                d = a + block;
                Highlights.clearMark(2);
                while (c < c_max) {
                    Writes.write(swap, c++, array[d + left], 0, false, true);
                    Highlights.markArray(1, (d++) + left);
                    Delays.sleep(1);
                }
                c--;
                d = a + block - 1;
                e = d_max - 1;
                if (Reads.compareIndices(array, a + left, a + block + left, 1, true) <= 0) {
                    Highlights.clearMark(2);
                    Writes.write(array, (e--) + left, array[(d--) + left], 1, true, false);
                    while (c >= s) {
                        while (Reads.compareValues(array[d + left], swap[c]) > 0) {
                            Highlights.markArray(2, c + left + offset);
                            Writes.write(array, (e--) + left, array[(d--) + left], 1, true, false);
                        }
                        Highlights.markArray(2, c + left + offset);
                        Writes.write(array, (e--) + left, swap[c--], 1, true, false);
                    }
                } else {
                    Highlights.clearMark(2);
                    Writes.write(array, (e--) + left, array[(d--) + left], 1, true, false);
                    while (d >= a) {
                        while (Reads.compareValues(array[d + left], swap[c]) <= 0) {
                            Highlights.markArray(2, c + left + offset);
                            Writes.write(array, (e--) + left, swap[c--], 1, true, false);
                        }
                        Writes.write(array, (e--) + left, array[(d--) + left], 1, true, false);
                    }
                    while (c >= s) {
                        Highlights.markArray(2, c + left + offset);
                        Writes.write(array, (e--) + left, swap[c--], 1, true, false);
                    }
                }
            }
            block *= 2;
        }
    }

    private int medianOfN(int[] array, int[] temp, int start, int end, int n) {
        int gap = (end - start) / n;
        for (int i = 0; i < n; i++) {
            Writes.write(temp, i, array[start + i * gap], 0, false, true);
        }
        insertionSort(temp, 0, n, 0, false, true);
        return temp[n / 2];
    }

    protected int partition(int[] array, int[] temp, int start, int end, int piv) {
        int pta = start, pts = 0;
        for (int ptx = start; ptx < end; ptx++) {
            if (Reads.compareValues(array[ptx], piv) <= 0)
                Writes.write(array, pta++, array[ptx], 0.5, true, false);
            else
                Writes.write(temp, pts++, array[ptx], 0.5, true, true);
        }
        int tempCnt = pts;
        Writes.arraycopy(temp, 0, array, pta, tempCnt, 0.5, true, false);
        return pta;
    }

    public boolean getSortedRuns(int[] array, int start, int end) {
        Highlights.clearAllMarks();
        boolean reverseSorted = true;
        boolean sorted = true;
        int comp;
        for (int i = start; i < end - 1; i++) {
            comp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            if (comp > 0)
                sorted = false;
            else
                reverseSorted = false;
            if ((!reverseSorted) && (!sorted))
                return false;
        }
        if (reverseSorted && !sorted) {
            Writes.reversal(array, start, end - 1, 1, true, false);
            sorted = true;
        }
        return sorted;
    }

    public void twinSort(int[] array, int[] swap, int start, int end) {
        int nmemb = end - start;
        if (twinSwap(array, start, nmemb) == 0)
            tailMerge(array, start, swap, nmemb, 2);
    }

    protected void sort(int[] array, int[] temp, int start, int end) {
        while (end - start > 16) {
            if (getSortedRuns(array, start, end))
                return;
            int piv = medianOfN(array, temp, start, end, 5);
            int p = partition(array, temp, start, end, piv);
            int left = p - start;
            int right = end - p;
            if ((left == 0 || right == 0) || (left/right >= 16 || right/left >= 16)) {
                twinSort(array, temp, start, end);
                return;
            }
            sort(array, temp, p, end);
            end = p;
        }
        insertionSort(array, start, end, 0.5, true, false);
    }

    public void customSort(int[] array, int start, int end) {
        int[] temp = Writes.createExternalArray(end - start);
        sort(array, temp, start, end);
        Writes.deleteExternalArray(temp);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int[] temp = Writes.createExternalArray(sortLength);
        sort(array, temp, 0, sortLength);
        Writes.deleteExternalArray(temp);

    }

}
