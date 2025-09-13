package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
The MIT License (MIT)

Copyright (c) 2021 Scandum, Control, implemented by aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

final public class ParityMergeSort extends Sort {
	public ParityMergeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Parity Merge");
		this.setRunAllSortsName("Parity Merge Sort");
		this.setRunSortName("Parity Mergesort");
		this.setCategory("Merge Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private void parityMerge(int[] array, int[] tmp, int a, int m, int b) {
		int n = b-a, len = n/2;
		boolean odd = n%2 == 1;
		
		int i1 = a, j1 = m, p1 = a,
		    i2 = m-1, j2 = b-1, p2 = b-1;
		
		for(int c = 0; c < len; c++) {
			Highlights.markArray(1, i1);
			Highlights.markArray(2, j1);
			
			int val = Reads.compareValues(array[i1], array[j1]) <= 0 ? array[i1++] : array[j1++];
			Writes.write(tmp, p1++, val, 0.5, false, true);
			
			Highlights.markArray(3, i2);
			Highlights.markArray(4, j2);
			
			val = Reads.compareValues(array[i2], array[j2]) > 0 ? array[i2--] : array[j2--];
			Writes.write(tmp, p2--, val, 0.5, false, true);
		}
		Highlights.clearAllMarks();
		
		if(odd) {
			if(i1 < m) Highlights.markArray(1, i1);
			Highlights.markArray(2, j1);
			
			int val = (i1 < m && Reads.compareValues(array[i1], array[j1]) <= 0) ? array[i1] : array[j1];
			Writes.write(tmp, p1++, val, 0.5, false, true);
		}
		Highlights.clearMark(2);
		
		Writes.arraycopy(tmp, a, array, a, n, 0.5, true, false);
	}
	
	private void mergeSort(int[] array, int[] tmp, int a, int b) {
		if(b-a < 2) return;
		
		int m = (a+b)/2;
		
		this.mergeSort(array, tmp, a, m);
		this.mergeSort(array, tmp, m, b);
		this.parityMerge(array, tmp, a, m, b);
	}
	
	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int[] tmp = Writes.createExternalArray(length);
		this.mergeSort(array, tmp, 0, length);
		Writes.deleteExternalArray(tmp);
	}
}