package sorts.bogo;

import java.util.Random;

import sorts.templates.Sort;
import main.ArrayVisualizer;

/*
 * 
MIT License

Copyright (c) 2021 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class SafeBogoSort extends Sort {
    public SafeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Safe Bogo");
        this.setRunAllSortsName("Safe Bogo Sort");
        this.setRunSortName("Safe Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }
	
	private int findLastSorted(int[] array, int length) {
		int i = 1;
		for(; i < length && Reads.compareValues(array[i-1], array[i]) <= 0; i++);
		return i-1;
	}
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		Random r = new Random();
		int p = this.findLastSorted(array, length);
		
		while(p < length-1) {
			Writes.swap(array, p, p + r.nextInt(length-p), 0, true, false);
			p = this.findLastSorted(array, length);
		}
    }
}