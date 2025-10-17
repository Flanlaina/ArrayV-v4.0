package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class SegmentTreeSort extends Sort {
    public SegmentTreeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Segment Tree");
        this.setRunAllSortsName("Segment Tree Sort");
        this.setRunSortName("Segment Treesort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /**
     * A segment tree that numbers the nodes of the tree in the order of an Euler
     * tour traversal.
     */
    class SegmentTree {
        int[] arr, tree;
        int ofs, len;

        public SegmentTree(int[] array, int start, int end) {
            this.len = end - start;
            this.ofs = start;
            this.arr = array;
            this.tree = Writes.createExternalArray(2 * this.len);
            this.build(0, 0, this.len - 1);
        }

        public void free() {
            Writes.deleteExternalArray(tree);
        }

        int combine(int a, int b) {
            if (a == -1) return b;
            if (b == -1) return a;
            return Reads.compareIndices(arr, ofs + a, ofs + b, 0.5, true) <= 0 ? a : b;
        }

        void build(int id, int l, int r) {
            if (l == r) {
                tree[id] = l;
                return;
            }
            int mid = (l + r) >>> 1;
            int idL = id + 1, idR = id + (mid - l + 1) * 2;
            build(idL, l, mid);
            build(idR, mid + 1, r);
            tree[id] = combine(tree[idL], tree[idR]);
        }

        void modify(int id, int l, int r, int p, int value) {
            if (l == r) {
                tree[id] = value;
                return;
            }
            int mid = (l + r) >>> 1;
            int idL = id + 1, idR = id + (mid - l + 1) * 2;
            if (p <= mid) modify(idL, l, mid, p, value);
            else modify(idR, mid + 1, r, p, value);
            tree[id] = combine(tree[idL], tree[idR]);
        }

        public int peek() {
            return this.arr[ofs + this.tree[0]];
        }

        public int findNext() {
            int idx = this.tree[0];
            modify(0, 0, len - 1, idx, -1);
            return this.peek();
        }
    }

    public void sort(int[] array, int a, int b) {
        int len = b - a;
        SegmentTree tree = new SegmentTree(array, a, b);
        int[] buf = Writes.createExternalArray(len);
        Highlights.markArray(3, 0);
        Writes.write(buf, 0, tree.peek(), 1, false, true);
        for (int i = 1; i < len; i++) {
            int val = tree.findNext();
            Highlights.markArray(3, i);
            Writes.write(buf, i, val, 1, false, true);
        }
        Highlights.clearAllMarks();
        tree.free();
        Writes.arraycopy(buf, 0, array, a, len, 1, true, false);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        sort(array, 0, sortLength);
    }
}
