package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

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
 * A Fluxsort variant with Logsort and Logota Sort.
 * <p>
 * To use this algorithm in another, use {@code blockMergeSort()} from a reference
 * instance.
 *
 * @author Flanlaina
 * @author aphitorite
 * @author Scandum
 *
 */
public class PeachSort extends Sort {
    public PeachSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Peach");
        this.setRunAllSortsName("Peach Sort");
        this.setRunSortName("Peachsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Set block size (default: calculates minimum block length for current length)", 1);
    }

    static final int PARTIAL_INSERT_LIMIT = 8;
    static final int MERGESORT_MIN_INSERT = 16;
    static final int QUICKSORT_INSERT_THRESHOLD = 32;

    /*
     * 1st return value: W(n)
     * 2nd return value: first power of two greater than or equal to W(n)
     */
    public static int[] productLog2(int n) {
        int r = 1;
        while((r<<r)+r-1 < n) r++;
        int q = 0;
        while(1<<q < r) q++;
        return new int[] {r, 1<<q};
    }

    public static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    private static int branchlessEqual(int a, int b) {
        return ((a - b) >> 31) + ((b - a) >> 31) + 1;
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if(Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if(Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if(Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    public int ninther(int[] array, int a, int b) {
        if (b - a <= 9) return a + (b - a) / 2;
        int len = b - a, half = len / 2, quart = len / 4, eight = len / 8;
        int c = medOf3(array, a, a + eight, a + quart);
        int d = medOf3(array, a + quart + eight, a + half, a + half + eight);
        int e = medOf3(array, b - quart, b - eight, b - 1);
        return medOf3(array, c, d, e);
    }

    // Median of 3 ninthers
    public int pseudomo27(int[] array, int a, int b) {
        if (b - a < 64) return this.ninther(array, a, b);
        int d = (b - a + 1) / 8;
        int m0 = this.ninther(array, a, a + 2 * d);
        int m1 = this.ninther(array, a + 3 * d, a + 5 * d);
        int m2 = this.ninther(array, a + 6 * d, b);
        return this.medOf3(array, m0, m1, m2);
    }

    // Ninther of 9 ninthers
    public int pseudomo81(int[] array, int a, int b) {
        if (b - a < 256) return this.pseudomo27(array, a, b);
        int d = (b - a + 1) / 24;
        int m0 = this.ninther(array, a, a + 2 * d);
        int m1 = this.ninther(array, a + 3 * d, a + 5 * d);
        int m2 = this.ninther(array, a + 6 * d, a + 8 * d);
        int m3 = this.ninther(array, a + 9 * d, a + 11 * d);
        int m4 = this.ninther(array, a + 12 * d, a + 14 * d);
        int m5 = this.ninther(array, a + 15 * d, a + 17 * d);
        int m6 = this.ninther(array, a + 18 * d, a + 20 * d);
        int m7 = this.ninther(array, a + 19 * d, a + 21 * d);
        int m8 = this.ninther(array, a + 22 * d, b);
        return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5),
                this.medOf3(array, m6, m7, m8));
    }

    // Ninther of 9 medians of 3 ninthers
    public int pseudomo243(int[] array, int a, int b) {
        if (b - a < 16384) return this.pseudomo81(array, a, b);
        int d = (b - a + 1) / 24;
        int m0 = this.pseudomo27(array, a, a + 2 * d);
        int m1 = this.pseudomo27(array, a + 3 * d, a + 5 * d);
        int m2 = this.pseudomo27(array, a + 6 * d, a + 8 * d);
        int m3 = this.pseudomo27(array, a + 9 * d, a + 11 * d);
        int m4 = this.pseudomo27(array, a + 12 * d, a + 14 * d);
        int m5 = this.pseudomo27(array, a + 15 * d, a + 17 * d);
        int m6 = this.pseudomo27(array, a + 18 * d, a + 20 * d);
        int m7 = this.pseudomo27(array, a + 19 * d, a + 21 * d);
        int m8 = this.pseudomo27(array, a + 22 * d, b);
        return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5),
                this.medOf3(array, m6, m7, m8));
    }

    public void segmentReversal(int[] array, int start, int end, double delay, boolean mark, boolean aux) {
        for (int i = start; i < end; i++) {
            int left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, delay, true) == 0) i++;
            int right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, delay * 2, mark, aux);
                else Writes.reversal(array, left, right, delay * 2, mark, aux);
            }
        }
    }

    //Refactored from PDQSorting.java
    protected boolean partialInsert(int[] array, int a, int b) {
        if (a == b) return true;
        double sleep = 0.25;
        int c = 0;
        for (int i = a + 1; i < b; i++) {
            if (c > PARTIAL_INSERT_LIMIT) return false;
            if (Reads.compareIndices(array, i - 1, i, sleep, true) > 0) {
                int t = array[i];
                int j = i;
                do {
                    Writes.write(array, j, array[j - 1], sleep, true, false);
                    j--;
                } while (j - 1 >= a && Reads.compareValues(array[j - 1], t) > 0);
                Writes.write(array, j, t, sleep, true, false);
                c += i - j;
            }
        }
        return true;
    }

    protected void blockSwap(int[] array, int a, int b, int len) {
        if (a == b) return;
        for (int i = 0; i < len; i++) Writes.swap(array, a + i, b + i, 1, true, false);
    }

    protected void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], sleep, true, false);
        if (a != b) Writes.write(array, b, temp, sleep, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    boolean pivCmp(int v, int piv, int eqLower) {
        int c = Reads.compareValues(v, piv);
        return c < 0 || ((eqLower == 1) && c == 0);
    }

    void pivBufXor(int[] array, int pa, int pb, int v, int wLen) {
        while (wLen-- > 0) {
            if ((v & 1) == 1) Writes.swap(array, pa + wLen, pb + wLen, 1, true, false);
            v >>= 1;
        }
    }

    // @param bit - < pivot means this bit
    int pivBufGet(int[] array, int pa, int piv, int eqLower, int wLen, int bit) {
        int r = 0;
        while (wLen-- > 0) {
            r <<= 1;
            r |= (this.pivCmp(array[pa++], piv, eqLower) ? 0 : 1) ^ bit;
        }
        return r;
    }

    protected void blockCycle(int[] array, int a, int n, int tagStart, int bLen, int wLen, int piv, int eqLower,
            int bit) {
        for (int i = 0, aPtr = a, tPtr = tagStart; i < n; i++, aPtr += bLen, tPtr += bLen) {
            int dest = this.pivBufGet(array, aPtr, piv, eqLower, wLen, bit);
            while (dest != i) {
                this.blockSwap(array, aPtr, a + dest * bLen, bLen);
                dest = this.pivBufGet(array, aPtr, piv, eqLower, wLen, bit);
            }
            this.pivBufXor(array, aPtr, tPtr, i, wLen);
        }
    }

    protected int[] partition(int[] array, int[] buf, int a, int b, int bLen, int piv, int bias) {
        // determines which elements do not need to be moved
        for(; a < b; a++) {
            Highlights.markArray(1, a);
            Delays.sleep(0.25);
            if(!this.pivCmp(array[a], piv, bias)) break;
        }
        for(; b > a; b--) {
            Highlights.markArray(1, b-1);
            Delays.sleep(0.25);
            if(this.pivCmp(array[b-1], piv, bias)) break;
        }
        boolean alreadyParted = b == a;/* , opposing = true */
        if (b - a <= bLen) {
            int j = a, k = 0;
            for (int i = a; i < b; i++) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.25, true);
                boolean loPart = cmp < 0 || ((bias == 1) && cmp == 0);
                // opposing &= (!((bias == 0) ^ loPart) || cmp == 0);
                if (loPart) {
                    if (j != i) Writes.write(array, j, array[i], 0.5, true, false);
                    j++;
                } else Writes.write(buf, k++, array[i], 0.5, false, true);
            }
            Writes.arraycopy(buf, 0, array, j, k, 0.5, true, false);
            return new int[] { j, (alreadyParted ? 1 : 0) /* | (opposing ? 2 : 0) */ };
        }

        // sort blocks and type blocks
        int p = a;
        int l = 0, r = 0;
        int lb = 0, rb = 0;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.25, true);
            boolean loPart = cmp < 0 || ((bias == 1) && cmp == 0);
            // opposing &= (!((bias == 0) ^ loPart) || cmp == 0);
            if (loPart) {
                Writes.write(array, p + l++, array[i], 0.25, true, false);
                if(l == bLen) {
                    l = 0;
                    lb++;
                    p += bLen;
                }
            } else {
                Writes.write(buf, r++, array[i], 0.25, false, true);
                if(r == bLen) {
                    Writes.arraycopy(array, p, array, p+bLen, l, 0.5, true, false);
                    Writes.arraycopy(buf, 0, array, p, bLen, 0.5, true, false);
                    r = 0;
                    rb++;
                    p += bLen;
                }
            }
        }

        // sort blocks
        int min = Math.min(lb, rb);
        int m = a + lb * bLen;
        if (min > 0) {
            int wLen = 32 - Integer.numberOfLeadingZeros(min - 1); // ceil(log2(min))
            for (int i = 0, j = a, k = a; i < min; i++) { // set bit buffers
                while (!this.pivCmp(array[j + wLen], piv, bias)) j += bLen;
                while (this.pivCmp(array[k + wLen], piv, bias)) k += bLen;
                this.pivBufXor(array, j, k, i, wLen);
                j += bLen; k += bLen;
            }
            if (lb < rb) {
                for (int i = p - bLen, j = p; i >= a; i -= bLen) { // swap right to left
                    if (!pivCmp(array[i + wLen], piv, bias)) {
                        j -= bLen;
                        blockSwap(array, i, j, bLen);
                    }
                }
                this.blockCycle(array, a, lb, m, bLen, wLen, piv, bias, 0);
            } else {
                for (int i = a, j = a; i < p; i += bLen) { // swap left to right
                    if (pivCmp(array[i + wLen], piv, bias)) {
                        blockSwap(array, i, j, bLen);
                        j += bLen;
                    }
                }
                this.blockCycle(array, m, rb, a, bLen, wLen, piv, bias, 1);
            }
        }

        // handle leftover
        Writes.arraycopy(buf, 0, array, b - r, r, 1, true, false);
        if (l > 0) {
            Highlights.clearMark(2);
            Writes.arraycopy(array, b - r - l, buf, 0, l, 1, false, true);
            Writes.arraycopy(array, m, array, m + l, rb * bLen, 1, true, false);
            Writes.arraycopy(buf, 0, array, m, l, 1, true, false);
        }
        return new int[] { m + l, (alreadyParted ? 1 : 0) /* | (opposing ? 2 : 0) */ };
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

    protected int minExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int maxExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
    }

    public int findRun(int[] array, int start, int end) {
        int i = start + 1;
        if (i >= end) return i;
        boolean lessunique = false;
        boolean different = false;
        int cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
        while (cmp == 0 && i < end) {
            lessunique = true;
            i++;
            if (i < end) cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
        }
        if (cmp > 0) {
            while (cmp >= 0 && i < end) {
                if (cmp == 0) lessunique = true;
                else different = true;
                i++;
                if (i < end) cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
            }
            if (i - start > 1 && different) {
                if (i - start < 4) Writes.swap(array, start, i - 1, 0.75, true, false);
                else Writes.reversal(array, start, i - 1, 0.75, true, false);
                if (lessunique) segmentReversal(array, start, i - 1, 0.75, true, false);
            }
        } else {
            while (cmp <= 0 && i < end) {
                i++;
                if (i < end) cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
            }
        }
        return i;
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, binSearch(array, j, i, array[i], false), 0.5);
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    public void insertSort(int[] array, int a, int b) {
        buildRuns(array, a, b, b - a);
    }

    private void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = m-a;
        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);
        int i = 0, j = m;
        while(i < s && j < b) {
            Highlights.markArray(2, j);

            if(Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        }
        Highlights.clearAllMarks();
        while(i < s) Writes.write(array, a++, tmp[i++], 1, true, false);
    }
    private void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = b-m;
        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);
        int i = s-1, j = m-1;
        while(i >= 0 && j >= a) {
            Highlights.markArray(2, j);

            if(Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        }
        Highlights.clearAllMarks();
        while(i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
    }

    private void blockMergeHelper(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
        if(m-a <= 2*bLen) {
            this.mergeFWExt(array, swap, a, m, b);
            return;
        }

        int bCnt = 0, wLen = log2((b-a)/bLen-3)+1;
        int i = a, j = m, k = 0;
        int l = 0, r = 0, c = 0;

        for(; c < 2*bLen; c++) { //merge 2 blocks into buffer to create 2 buffers
            if(Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.write(swap, k++, array[i++], 1, true, true);
                l++;
            } else {
                Writes.write(swap, k++, array[j++], 1, true, true);
                r++;
            }
        }

        int t = 0, pc = p;
        boolean left = l >= r;
        k = left ? i-l : j-r;
        c = 0;

        do {
            if(j == b || Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.write(array, k++, array[i++], 1, true, false);
                l++;
            } else {
                Writes.write(array, k++, array[j++], 1, true, false);
                r++;
            }
            if(++c == bLen) { //change buffer after every block
                this.pivBufXor(array, k-bLen, pc, t++, wLen);
                pc += bLen;
                if (left) l -= bLen;
                else      r -= bLen;
                left = l >= r;
                k = left ? i-l : j-r;
                c = 0;
                bCnt++;
            }
        } while(i < m);

        int b1 = j-c;
        Writes.arraycopy(array, k-c, array, b1, c, 1, true, false); //swap remainder to end (r buffer)
        r -= c;
        l = Math.min(l, m-a-l);

        //l and r buffers are divisible by bLen
        Writes.arraycopy(array, a,   array, m-l,  l, 1, true, false); //swap l buffer to front
        Writes.arraycopy(array, a+l, array, b1-r, r, 1, true, false); //swap r buffer to front
        Writes.arraycopy(swap,  0, array, a, 2*bLen, 1, true, false); //swap first merged elements to correct position in front

        this.blockCycle(array, a+2*bLen, bCnt, p, bLen, wLen, piv, pCmp, bit);
    }

    private void blockMergeEasy(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return;
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if(b-m <= 2*bLen) {
            this.mergeBWExt(array, swap, a, m, b);
            return;
        }
        a = minExpSearch(array, a, m, array[m], false);
        if(m-a <= 2*bLen) {
            this.mergeFWExt(array, swap, a, m, b);
            return;
        }

        int a1 = a+(m-a)%bLen;
        this.blockMergeHelper(array, swap, a1, m, b, p, bLen, piv, pCmp, bit);
        this.mergeFWExt(array, swap, a, a1, b);
    }

    public void blockMerge(int[] array, int[] swap, int a, int m, int b, int bLen) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return;
        b = maxExpSearch(array, m, b, array[m - 1], true);
        a = minExpSearch(array, a, m, array[m], false);
        int l = m - a, r = b - m;
        int lCnt = (l + r + 1) / 2, med;

        // find lower ceil((A+B)/2) elements and then find max of halves to get median
        // binary search is used for O(log n) performance
        if (r < l) {
            if (r <= bLen) {
                this.mergeBWExt(array, swap, a, m, b);
                return;
            }
            int la = 0, lb = r;
            while (la < lb) {
                int lm = (la + lb) >>> 1;
                if (Reads.compareIndices(array, m + lm, a + (lCnt - lm) - 1, 0.25, true) <= 0) la = lm + 1;
                else lb = lm;
            }
            if (la == 0) med = array[a + lCnt - 1];
            else med = Reads.compareIndices(array, m + la - 1, a + (lCnt - la) - 1, 0.25, true) > 0
                     ? array[m + la - 1] : array[a + (lCnt - la) - 1];
        } else {
            if (l <= bLen) {
                this.mergeFWExt(array, swap, a, m, b);
                return;
            }
            int la = 0, lb = l;
            while (la < lb) {
                int lm = (la + lb) >>> 1;
                if (Reads.compareIndices(array, a + lm, m + (lCnt - lm) - 1, 0.25, true) < 0) la = lm + 1;
                else lb = lm;
            }
            if (l == r && la == l) med = array[m - 1];
            else if (la == 0)      med = array[m + lCnt - 1];
            else  med = Reads.compareIndices(array, a + la - 1, m + (lCnt - la) - 1, 0.25, true) >= 0
                    ? array[a + la - 1] : array[m + (lCnt - la) - 1];
        }

        // stable ternary partition around median: [ < ][ = ][ > ]
        int m1 = this.binSearch(array, a, m, med, true);
        int m2 = this.binSearch(array, m, b, med, false);
        int ms2 = m - this.binSearch(array, m1, m, med, false);
        int ms1 = this.binSearch(array, m, m2, med, true) - m;
        this.rotate(array, m - ms2, m, m2); // ABCABC -> ABABCC
        this.rotate(array, m1, m - ms2, m + ms1 - ms2); // ABABCC -> AABBCC

        if (m1 > a && ms1 > 0) this.blockMergeEasy(array, swap, a, m1, m1 + ms1, a + lCnt, bLen, med, 0, 0);
        if (m2 < b && ms2 > 0) this.blockMergeEasy(array, swap, m2 - ms2, m2, b, a, bLen, med, 1, 1);
    }

    public void blockMergeSort(int[] array, int[] swap, int left, int right, int bLen) {
        int j = MERGESORT_MIN_INSERT, length = right - left;
        if (buildRuns(array, left, right, j)) return;
        for(; j < length; j *= 2)
            for(int i = left; i+j < right; i += 2*j)
                this.blockMerge(array, swap, i, i+j, Math.min(right, i+2*j), bLen);
    }

    protected void sortHelper(int[] array, int[] buf, int a, int b, int bLen, int badAllowed) {
        while (b - a > QUICKSORT_INSERT_THRESHOLD) {
            int pIdx = pseudomo243(array, a, b);
            Highlights.clearMark(2);
            int[] pr = partition(array, buf, a, b, bLen, array[pIdx], 1);
            int m = pr[0];
/* 
            if ((pr[1] & 0x2) != 0) {
                // left sublist only has one unique value, iterate on new sublist immediately
                a = m;
                continue;
            }
 */
            if (m == b) {
                // pivot is highest rank, partition again with inverted bias
                pr = partition(array, buf, a, b, bLen, array[pIdx], 0);
                // due to pivot, the right half only has one unique, so iterate on new sublist immediately
                b = pr[0];
                continue;
            }
            int lLen = m - a, rLen = b - m;
            boolean bad = rLen / 8 > lLen || lLen / 8 > rLen;
            if (bad) {
                badAllowed--;
                if (badAllowed == 0) {
                    blockMergeSort(array, buf, a, b, bLen);
                    return;
                }
            } else if ((pr[1] & 0x1) != 0 && partialInsert(array, a, m) && partialInsert(array, m, b)) return;
            if (lLen > rLen) {
                sortHelper(array, buf, m, b, bLen, badAllowed);
                b = m;
            } else {
                sortHelper(array, buf, a, m, bLen, badAllowed);
                a = m;
            }
        }
        insertSort(array, a, b);
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Peach Sort.
     * 
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     * @param bLen  the block size (automatically set to the minimum block size if
     *              it is lower than that value)
     */
    public void quickSort(int[] array, int a, int b, int bLen) {
        int len = b - a;
        if (len <= QUICKSORT_INSERT_THRESHOLD) {
            insertSort(array, a, b);
            return;
        }
        int balance = 0, eq = 0, streaks = 0, dist, eqdist, loop, cnt = len, pos = a;
        while (cnt > 16) {
            for (eqdist = dist = 0, loop = 0; loop < 16; loop++) {
                int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
                dist += cmp > 0 ? 1 : 0;
                eqdist += cmp == 0 ? 1 : 0;
                pos++;
            }
            streaks += branchlessEqual(dist, 0) | branchlessEqual(dist + eqdist, 16);
            balance += dist;
            eq += eqdist;
            cnt -= 16;
        }
        while (--cnt > 0) {
            int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
            balance += cmp > 0 ? 1 : 0;
            eq += cmp == 0 ? 1 : 0;
            pos++;
        }
        if (balance == 0) return;
        if (balance + eq == len - 1) {
            if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            if (eq > 0) segmentReversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        bLen = Math.max(productLog2(len)[0], Math.min(bLen, len));
        int[] buf;
        int sixth = len / 6;
        if (streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth) {
            buf = Writes.createExternalArray(2 * bLen);
            blockMergeSort(array, buf, a, b, bLen);
        } else {
            buf = Writes.createExternalArray(bLen);
            sortHelper(array, buf, a, b, bLen, log2(len));
        }
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength, bucketCount);
    }
}
