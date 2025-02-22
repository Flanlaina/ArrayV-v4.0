package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and yuji

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author yuji
 *
 */
public final class AdaptiveBPMSort extends Sort {

    public AdaptiveBPMSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Buffer Partition Merge");
        this.setRunAllSortsName("Adaptive Buffer Partition Merge Sort");
        this.setRunSortName("Adaptive Buffer Partition Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public static int getMinLevel(int n) {
        while(n >= 32) n = (n+1)/2;
        return n;
    }
    
    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b)
            Writes.swap(array, a, b, pause, mark, aux);
    }
    
    void medianOfThree(int[] array, int a, int b) {
        int m = a + (b - 1 - a) / 2;
        if (Reads.compareIndices(array, a, m, 1, true) > 0)
            swap(array, a, m, 1, true, false);
        if (Reads.compareIndices(array, m, b - 1, 1, true) > 0) {
            swap(array, m, b - 1, 1, true, false);
            if (Reads.compareIndices(array, a, m, 1, true) > 0)
                return;
        }
        swap(array, a, m, 1, true, false);
    }
    
    int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }
    
    void medianOfMedians(int[] array, int a, int b) {
        while (b-a > 2) {
            int m = a, i = a;
            
            for (; i+2 < b; i += 3)
                swap(array, m++, this.medOf3(array, i, i+1, i+2), 1, true, false);
            while (i < b)
                swap(array, m++, i++, 1, true, false);
            
            b = m;
        }
    }
    
    void multiSwap(int[] array, int a, int b, int len) {
        if (a != b)
            for (int i = 0; i < len; i++)
                Writes.swap(array, a + i, b + i, 1, true, false);
    }
    
    void rotate(int[] array, int a, int m, int b) {
        int l = m-a, r = b-m;
        
        while (l > 0 && r > 0) {
            if (r < l) {
                this.multiSwap(array, m-r, m, r);
                b -= r;
                m -= r;
                l -= r;
            } else {
                this.multiSwap(array, a, m, l);
                a += l;
                m += l;
                r -= l;
            }
        }
    }
    
    int partition(int[] array, int a, int b) {
        int i = a, j = b;
        Highlights.markArray(3, a);
        
        while(true) {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            } while (i < j && Reads.compareIndices(array, i, a, 0, false) < 0);
            
            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            } while (j >= i && Reads.compareIndices(array, j, a, 0, false) > 0);
            
            if (i < j) Writes.swap(array, i, j, 1, true, false);
            else return j;
        }
    }
    
    int quickSelect(int[] array, int a, int b, int m) {
        boolean badPartition = false, mom = false;
        int m1 = m+(b-m+1)/2;
        
        while (true) {
            if (badPartition) {
                this.medianOfMedians(array, a, b);
                mom = true;
            } else this.medianOfThree(array, a, b);
            
            int p = this.partition(array, a, b);
            swap(array, a, p, 1, true, false);
            
            int l = p - a;
            int r = b - (p + 1);
            badPartition = !mom && (Math.min(l, r) < Math.max(l, r) / 16);
            
            if (p >= m && p < m1) return p;
            else if (p < m) a = p+1;
            else b = p;
        }
    }
    
    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
    }
    
    protected void mergeTo(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;
        
        while (i < m && j < b) {
            if (Reads.compareValues(array[i], array[j]) <= 0)
                Writes.swap(array, p++, i++, 1, true, false);
            else 
                Writes.swap(array, p++, j++, 1, true, false);
        }
        while (i < m) Writes.swap(array, p++, i++, 1, true, false);
        while (j < b) Writes.swap(array, p++, j++, 1, true, false);
    }
    
    protected void mergeFW(int[] array, int a, int m, int b, int p) {
        int pLen = m - a;
        multiSwap(array, a, p, pLen);
        int i = 0, j = m, k = a;
        while (i < pLen && j < b) {
            if (Reads.compareValues(array[p + i], array[j]) <= 0)
                swap(array, k++, p + (i++), 1, true, false);
            else
                swap(array, k++, j++, 1, true, false);
        }
        while (i < pLen) swap(array, k++, p + (i++), 1, true, false);
    }

    protected void mergeBW(int[] array, int a, int m, int b, int p) {
        int pLen = b - m;
        multiSwap(array, m, p, pLen);
        int i = pLen - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a) {
            if (Reads.compareValues(array[p + i], array[j]) >= 0)
                swap(array, k--, p + (i--), 1, true, false);
            else
                swap(array, k--, j--, 1, true, false);
        }
        while (i >= 0) swap(array, k--, p + (i--), 1, true, false);
    }
    
    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = leftExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b)
                break;
            a = leftExpSearch(array, a, m, array[m], false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = rightExpSearch(array, a, m, array[b - 1], false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a)
                break;
            b = rightExpSearch(array, m, b, array[m - 1], true);
        }
    }

    public void merge(int[] array, int a, int m, int b, int p) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (b - m < m - a)
            mergeBW(array, a, m, b, p);
        else
            mergeFW(array, a, m, b, p);
    }

    void lazyBufferedMergeFW(int[] array, int a, int m, int b, int p) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        int pLen = m - a;
        multiSwap(array, a, p, pLen);
        int i = 0, j = m, k = a;
        while (i < pLen && j < b) {
            if (Reads.compareValues(array[j], array[p+i]) < 0) {
                int q = leftExpSearch(array, j, b, array[p+i], true);
                while (j < q) swap(array, k++, j++, 1, true, false);
            }
            swap(array, k++, p+(i++), 1, true, false);
        }
        while (i < pLen) swap(array, k++, p+(i++), 1, true, false);
    }
    
    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }
    
    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                if (i - j < 4) Writes.swap(array, j, i - 1, 1.0, true, false);
                else Writes.reversal(array, j, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, rightExpSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }
    
    public void inPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (m - a > b - m)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
    }
    
    public void mergeSort(int[] array, int a, int b, int p) {
        int length = b-a;
        if(length < 2) return;
        
        int i, j = getMinLevel(length);
        
        if(buildRuns(array, a, b, j)) return;
        
        while(j < length) {
            for(i = a; i+2*j <= b; i+=2*j)
                this.merge(array, i, i+j, i+2*j, p);
            if(i + j < b)
                this.merge(array, i, i+j, b, p);
            j *= 2;
        }
    }
    
    public void sort(int[] array, int a, int b) {
        if (b - a <= 32) {
            buildRuns(array, a, b, b - a);
            return;
        }
        int minLvl = (int)Math.sqrt(b-a);
        int m = b - (b - a) / 2;
        mergeSort(array, m, b, a);
        while (m-a > minLvl) {
            int m1 = m - (m - a) / 2;
            m1 = this.quickSelect(array, a, m, m1);
            this.mergeSort(array, m1, m, a);
            lazyBufferedMergeFW(array, m1, m, b, a);
            m = m1;
        }
        buildRuns(array, a, m, m - a);
        inPlaceMerge(array, a, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
