package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.templates.Sort;
import utils.Distributions;
import utils.Shuffles;
import utils.StopSort;

/*
 *
MIT License

Copyright (c) 2021 Josiah (Gaming32) Glosson

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

final public class RunSummerSort extends MultipleSortThread {
    static final String               SORT_AUTHOR = "Edsger Dijkstra's";
    static boolean                stabilityProper = true;
    static boolean        alternate_distributions = false;
    static boolean                          seeds = false;

    public RunSummerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 80;
        this.categoryCount = this.sortCount;
    }

    protected synchronized void runIndividualSort(Sort sort, int bucketCount, int[] array, int defaultLength,
            double defaultSpeed, int uniques, boolean slowSort, String shuffleName, String sortName, boolean alt) throws Exception {
        Delays.setSleepRatio(2.5);

        int sortLength;
        if (slowSort) sortLength = this.calculateLengthSlow(defaultLength, sort.getUnreasonableLimit());
        else sortLength = this.calculateLength(defaultLength);

        if(sortLength != arrayVisualizer.getCurrentLength()) arrayFrame.setLengthSlider(sortLength);

        if ("Many Similar".equals(shuffleName) || "More Similar".equals(shuffleName) || "Stability Test".equals(shuffleName))
            arrayFrame.setUniqueSlider(uniques);
        else if (alt && alternate_distributions) arrayFrame.setUniqueSlider(sortLength / 8);
        else arrayFrame.setUniqueSlider(sortLength);

        arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), this.arrayVisualizer);

        arrayVisualizer.setHeading(sortName + " (" + shuffleName + ": " + this.sortNumber + " / " + this.sortCount + ")");

        double sortSpeed = this.calculateSpeed(defaultSpeed, arrayVisualizer.getCurrentLength());
        Delays.setSleepRatio(sortSpeed);

        Timer.enableRealTimer();

        // arrayVisualizer.toggleVisualUpdates(true);
        try {
            sort.runSort(array, arrayVisualizer.getCurrentLength(), bucketCount);
        }
        catch (StopSort e) { }
        catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }
        // arrayVisualizer.toggleVisualUpdates(false);

        arrayVisualizer.endSort();
        for (int i = 0; i < 100; i++) {
            Thread.sleep(10);
            arrayVisualizer.updateNow();
        }
        this.sortNumber++;
    }

    protected synchronized void runSort(int[] array, String shuffleName, boolean alt) throws Exception {
        Sort sort = new sorts.select.SmoothSort(arrayVisualizer);
        RunSummerSort.this.runIndividualSort(sort, 0, array, 4096, 4,
            16, false, shuffleName, "Smoothsort", alt);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        arrayManager.setDistribution(Distributions.LINEAR); // 1
        arrayManager.setShuffleSingle(Shuffles.RANDOM);
        RunSummerSort.this.runSort(array, "Random", true);

        arrayManager.setShuffleSingle(Shuffles.REVERSE); // 2
        RunSummerSort.this.runSort(array, "Reversed", true);

        arrayManager.setShuffleSingle(Shuffles.ALMOST); // 3
        RunSummerSort.this.runSort(array, "Almost Sorted", true);

        arrayManager.setShuffleSingle(Shuffles.RANDOM); // 4
        RunSummerSort.this.runSort(array, alternate_distributions ? "More Similar" : "Many Similar", false);

        if (stabilityProper) {
            this.sortNumber--;
            arrayVisualizer.setComparator(2);
            RunSummerSort.this.runSort(array, "Stability Test", false);
            arrayVisualizer.setComparator(0);
        } else {
            arrayVisualizer.setHeading("Stability Test inconsistent with algorithm. Skipping...");
            arrayVisualizer.updateNow();
            Thread.sleep(3000);
        }

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_TAIL_INDEXSORT); // 5
        RunSummerSort.this.runSort(array, "Scrambled Tail", true);

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_HEAD_INDEXSORT); // 6
        RunSummerSort.this.runSort(array, "Scrambled Head", true);

        arrayManager.setShuffleSingle(Shuffles.FINAL_MERGE); // 7
        RunSummerSort.this.runSort(array, "Final Merge", true);

        arrayManager.setShuffleSingle(Shuffles.SAWTOOTH); // 8
        RunSummerSort.this.runSort(array, "Sawtooth Input", true);

        arrayManager.setShuffleSingle(Shuffles.HALF_ROTATION); // 9
        RunSummerSort.this.runSort(array, "Final Merge of Reversed Array", true);

        arrayManager.setShuffleSingle(Shuffles.FINAL_MERGE).addSingle(Shuffles.REVERSE); // 10
        RunSummerSort.this.runSort(array, "Reversed Final Merge", true);

        arrayManager.setShuffleSingle(Shuffles.ORGAN); // 11
        RunSummerSort.this.runSort(array, "Pipe Organ", true);

        arrayManager.setShuffleSingle(Shuffles.FINAL_RADIX); // 12
        RunSummerSort.this.runSort(array, "Final Radix Pass", true);

        arrayManager.setShuffleSingle(Shuffles.PAIRWISE); // 13
        RunSummerSort.this.runSort(array, "Final Pairwise Pass", true);

        arrayManager.setShuffleSingle(Shuffles.BST_TRAVERSAL); // 14
        RunSummerSort.this.runSort(array, "Binary Search Tree", true);

        arrayManager.setShuffleSingle(Shuffles.HEAPIFIED); // 15
        RunSummerSort.this.runSort(array, "Heap", true);

        arrayManager.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.SMOOTH); // 16
        RunSummerSort.this.runSort(array, "Smooth Heap", true);

        arrayManager.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.POPLAR); // 17
        RunSummerSort.this.runSort(array, "Poplar Heap", true);

        arrayManager.setShuffleSingle(Shuffles.PARTIAL_REVERSE); // 18
        RunSummerSort.this.runSort(array, "Half-Reversed Input", true);

        arrayManager.setShuffleSingle(Shuffles.DOUBLE_LAYERED); // 19
        RunSummerSort.this.runSort(array, "Evens Reversed, Odds In-Order", true);

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_ODDS); // 20
        RunSummerSort.this.runSort(array, "Evens In-Order, Scrambled Odds", true);

        arrayManager.setShuffleSingle(Shuffles.INTERLACED); // 21
        RunSummerSort.this.runSort(array, "Evens Ascending, Odds Descending", true);

        arrayManager.setShuffleSingle(Distributions.BELL_CURVE); // 22
        RunSummerSort.this.runSort(array, "Bell Curve", false);

        arrayManager.setShuffleSingle(Distributions.PERLIN_NOISE_CURVE); // 23
        RunSummerSort.this.runSort(array, "Perlin Noise Curve", false);

        arrayManager.setShuffleSingle(Distributions.PERLIN_NOISE); // 24
        RunSummerSort.this.runSort(array, "Perlin Noise", false);

        arrayManager.setShuffleSingle(Shuffles.TRIANGULAR); // 25
        RunSummerSort.this.runSort(array, "Triangular Input", true);

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_HALF_BACK); // 26
        RunSummerSort.this.runSort(array, "Scrambled Second Half", true);

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_HALF_FRONT); // 27
        RunSummerSort.this.runSort(array, "Scrambled First Half", true);

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_ENDS); // 28
        RunSummerSort.this.runSort(array, "Both Sides Scrambled", true);

        arrayManager.setShuffleSingle(Shuffles.NOISY); // 29
        RunSummerSort.this.runSort(array, "Noisy Input", true);

        arrayManager.setShuffleSingle(Shuffles.PARTITIONED); // 30
        RunSummerSort.this.runSort(array, "Partitioned", true);

        arrayManager.setShuffleSingle(Shuffles.SAWTOOTH).addSingle(Shuffles.REVERSE); // 31
        RunSummerSort.this.runSort(array, "Reversed Sawtooth", true);

        arrayManager.setShuffleSingle(Shuffles.FINAL_BITONIC); // 32
        RunSummerSort.this.runSort(array, "Final Bitonic Pass", true);

        arrayManager.setShuffleSingle(Shuffles.DOUBLE_LAYERED).addSingle(Shuffles.HALF_ROTATION); // 33
        RunSummerSort.this.runSort(array, "Diamond", true);

        arrayManager.setShuffleSingle(Shuffles.INV_BST); // 34
        RunSummerSort.this.runSort(array, "Inverted Binary Search Tree", true);

        arrayManager.setShuffleSingle(Shuffles.REC_RADIX); // 35
        RunSummerSort.this.runSort(array, "Recursive Final Radix", true);

        arrayManager.setShuffleSingle(Shuffles.REC_REV); // 36
        RunSummerSort.this.runSort(array, "Recursive Reversal", true);

        arrayManager.setShuffleSingle(Shuffles.TRI_HEAP).setSleepRatio(1.5); // 37
        RunSummerSort.this.runSort(array, "Triangular Heap", true);

        arrayManager.setShuffleSingle(Shuffles.BIT_REVERSE); // 38
        RunSummerSort.this.runSort(array, "Bit Reversed", true);

        arrayManager.setShuffleSingle(Shuffles.BLOCK_RANDOMLY); // 39
        RunSummerSort.this.runSort(array, "Block Shuffled", true);

        arrayManager.setShuffleSingle(Shuffles.BLOCK_REVERSE); // 40
        RunSummerSort.this.runSort(array, "Block Reversed", true);

        arrayManager.setShuffleSingle(Shuffles.QSORT_BAD); // 41
        RunSummerSort.this.runSort(array, "Quicksort Killer", true);

        arrayManager.setShuffleSingle(Shuffles.PDQ_BAD); // 42
        RunSummerSort.this.runSort(array, "PDQ Killer", true);

        arrayManager.setShuffleSingle(Shuffles.GRAIL_BAD); // 43
        RunSummerSort.this.runSort(array, "Grailsort Killer", true);

        arrayManager.setShuffleSingle(Shuffles.SHUF_MERGE_BAD); // 44
        RunSummerSort.this.runSort(array, "Shuffle Merge Killer", true);

        arrayManager.setShuffleSingle(Shuffles.CIRCLE); // 45
        RunSummerSort.this.runSort(array, "Circle Pass", true);

        arrayManager.setShuffleSingle(Shuffles.SORTED); // 46
        RunSummerSort.this.runSort(array, "Already Sorted", true);

        arrayManager.setShuffleSingle(Shuffles.WEAVE); // 47
        RunSummerSort.this.runSort(array, "Final Weave Pass", true);

        arrayManager.setShuffleSingle(Shuffles.RANDOM_ROTATION); // 48
        RunSummerSort.this.runSort(array, "Random Rotation", true);

        arrayManager.setShuffleSingle(Shuffles.MODULO); // 49
        RunSummerSort.this.runSort(array, "Modulo", true);

        arrayManager.setShuffleSingle(Shuffles.XORSWAP); // 50
        RunSummerSort.this.runSort(array, "XOR Swap", true);

        arrayManager.setShuffleSingle(Shuffles.FINAL_MERGE).addSingle(Shuffles.PARTIAL_REVERSE_ALT); // 51
        RunSummerSort.this.runSort(array, "Penultimate Bitonic Pass", true);

        arrayManager.setShuffleSingle(Shuffles.GRAY_CODE); // 52
        RunSummerSort.this.runSort(array, "Gray Code Fractal", true);

        arrayManager.setShuffleSingle(Shuffles.PARTITIONED).addSingle(Shuffles.FINAL_RADIX); // 53
        RunSummerSort.this.runSort(array, "Weaved Partition", true);

        arrayManager.setShuffleSingle(Shuffles.PRIMES_REVERSED); // 54
        RunSummerSort.this.runSort(array, "Primes Reversed", true);

        arrayManager.setShuffleSingle(Shuffles.BST_PREORDER); // 55
        RunSummerSort.this.runSort(array, "Pre-order BST Traversal", true);

        arrayManager.setShuffleSingle(Shuffles.BST_POSTORDER); // 56
        RunSummerSort.this.runSort(array, "Post-order BST Traversal", true);

        arrayManager.setShuffleSingle(Shuffles.RBST_BREADTH); // 57
        RunSummerSort.this.runSort(array, "Breadth RBST Traversal", true);

        arrayManager.setShuffleSingle(Shuffles.ONLY_RUNS); // 58
        RunSummerSort.this.runSort(array, "Random Runs", true);

        arrayManager.setShuffleSingle(Shuffles.LOG_SLOPES); // 59
        RunSummerSort.this.runSort(array, "Logpile", false);

        arrayManager.setShuffleSingle(Distributions.RANDOM); // 60
        RunSummerSort.this.runSort(array, "White Noise", false);

        arrayManager.setShuffleSingle(Distributions.MODULO); // 61
        RunSummerSort.this.runSort(array, "Modulo Function", false);

        arrayManager.setShuffleSingle(Distributions.CUBIC).addSingle(Shuffles.RANDOM).setSleepRatio(2); // 62
        RunSummerSort.this.runSort(array, "Shuffled Cubic", false);

        arrayManager.setShuffleSingle(Distributions.QUINTIC).addSingle(Shuffles.RANDOM).setSleepRatio(2); // 63
        RunSummerSort.this.runSort(array, "Shuffled Quintic", false);

        arrayManager.setShuffleSingle(Distributions.SINE); // 64
        RunSummerSort.this.runSort(array, "Sine Wawe", false);

        arrayManager.setShuffleSingle(Distributions.COSINE); // 65
        RunSummerSort.this.runSort(array, "Cosine Wawe", false);

        arrayManager.setShuffleSingle(Distributions.SIMILAR).addSingle(Shuffles.RANDOM).setSleepRatio(2); // 66
        RunSummerSort.this.runSort(array, "Few Unique", false);

        arrayManager.setShuffleSingle(Distributions.EQUAL); // 67
        RunSummerSort.this.runSort(array, "No Unique", false);

        arrayManager.setShuffleSingle(Distributions.RULER); // 68
        RunSummerSort.this.runSort(array, "Ruler", false);

        arrayManager.setShuffleSingle(Distributions.BLANCMANGE); // 69
        RunSummerSort.this.runSort(array, "Blancmange Curve", false);

        arrayManager.setShuffleSingle(Distributions.CANTOR).addSingle(Shuffles.RANDOM).setSleepRatio(2); // 70
        RunSummerSort.this.runSort(array, "Shuffled Cantor Function", false);

        arrayManager.setShuffleSingle(Distributions.DIVISORS); // 71
        RunSummerSort.this.runSort(array, "Sum of Divisors", false);

        arrayManager.setShuffleSingle(Distributions.DIGITS_PROD); // 72
        RunSummerSort.this.runSort(array, "Product of Digits", false);

        arrayManager.setShuffleSingle(Distributions.DIGITS_SUM); // 73
        RunSummerSort.this.runSort(array, "Sum of Digits", false);

        arrayManager.setShuffleSingle(Distributions.TOTIENT); // 74
        RunSummerSort.this.runSort(array, "Euler Totient Function", false);

        arrayManager.setShuffleSingle(Distributions.FSD); // 75
        RunSummerSort.this.runSort(array, "Fly Straight, Dammit!", false);

        arrayManager.setShuffleSingle(Distributions.NOISY_UNIQUES); // 76
        RunSummerSort.this.runSort(array, "Noisy Uniques", false);

        arrayManager.setShuffleSingle(Distributions.RAMP); // 77
        RunSummerSort.this.runSort(array, "Ramps", false);

        arrayManager.setShuffleSingle(Distributions.REVLOG); // 78
        RunSummerSort.this.runSort(array, "Decreasing Random", false);

        arrayManager.setShuffleSingle(Distributions.INCREASING_RANDOM); // 79
        RunSummerSort.this.runSort(array, "Increasing Random", false);

        arrayManager.setShuffleSingle(Distributions.WEIERSTRASS); // 80
        RunSummerSort.this.runSort(array, "Weierstrass Function", false);
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(arrayVisualizer.isActive()) return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("RSS") {
            @Override
            public void run() {
                boolean prevSeededShuffles = arrayManager.isSeededShufflesEnabled();
                try{
                    if(runAllActive) {
                        RunSummerSort.this.sortNumber = current;
                        RunSummerSort.this.sortCount = total;
                    } else {
                        RunSummerSort.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);
                    arrayManager.toggleSeededShuffles(seeds);
                    arrayVisualizer.setCategory("Seeded Shuffles");
                    arrayVisualizer.setHeading(seeds ? "ON" : "OFF");
                    arrayVisualizer.updateNow();
                    Thread.sleep(3000);
                    arrayVisualizer.setCategory(SORT_AUTHOR);
                    arrayVisualizer.toggleInShowcase(true);
                    RunSummerSort.this.executeSortList(array);

                    if(!runAllActive) {
                        arrayVisualizer.setCategory("RunSummerSort");
                        arrayVisualizer.setHeading("Done");
                    }
                }
                catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }

                arrayVisualizer.toggleInShowcase(false);
                arrayManager.toggleMutableLength(true);
                arrayManager.toggleSeededShuffles(prevSeededShuffles);
                Sounds.toggleSound(false);
                arrayVisualizer.setSortingThread(null);
            }
        });

        arrayVisualizer.runSortingThread();
    }
}