package threads;

import java.lang.reflect.Constructor;

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
    final Class<? extends Sort> SORT_CLASS = sorts.hybrid.NewAdaptiveGrailSort.class;
    final int                  SORT_LENGTH = 4096;
    final double                SORT_SPEED = 4;
    final int                 BUCKET_COUNT = 0;
    final int                 UNIQUE_COUNT = 32;
    static boolean         stabilityProper = true;


    public RunSummerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 80;
        this.categoryCount = this.sortCount;
    }

    protected synchronized void runIndividualSort(Sort sort, int bucketCount, int[] array, int defaultLength, double defaultSpeed, boolean slowSort, String shuffleName) throws Exception {
        Delays.setSleepRatio(2.5);

        int sortLength;
        if (slowSort) sortLength = this.calculateLengthSlow(defaultLength, sort.getUnreasonableLimit());
        else sortLength = this.calculateLength(defaultLength);

        if(sortLength != arrayVisualizer.getCurrentLength())
            arrayFrame.setLengthSlider(sortLength);

        arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), this.arrayVisualizer);

        arrayVisualizer.setHeading(shuffleName + " (" + this.sortNumber + " / " + this.sortCount + ")");

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

    protected synchronized Sort createSortInstance() {
        Constructor<? extends Sort> constructor;
        try {
            constructor = SORT_CLASS.getDeclaredConstructor(ArrayVisualizer.class);
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            throw new RuntimeException("Class " + SORT_CLASS.getName() + " is not an accessible sort class.");
        }
        try {
            return constructor.newInstance(arrayVisualizer);
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            throw new RuntimeException("Unable to initialize class " + SORT_CLASS.getName() + ".");
        }
    }

    protected synchronized void runSort(int[] array, String shuffleName) throws Exception {
        Sort sort = createSortInstance();
        RunSummerSort.this.runIndividualSort(sort, BUCKET_COUNT, array, SORT_LENGTH, SORT_SPEED, false, shuffleName);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        arrayManager.setDistribution(Distributions.LINEAR); // 1
        arrayManager.setShuffleSingle(Shuffles.RANDOM);
        RunSummerSort.this.runSort(array, "Random");

        arrayManager.setShuffleSingle(Shuffles.REVERSE); // 2
        RunSummerSort.this.runSort(array, "Reversed");

        arrayManager.setShuffleSingle(Shuffles.ALMOST); // 3
        RunSummerSort.this.runSort(array, "Almost Sorted");

        arrayVisualizer.getArrayFrame().setUniqueSlider(UNIQUE_COUNT); // 4
        arrayManager.setShuffleSingle(Shuffles.RANDOM);
        RunSummerSort.this.runSort(array, "Many Similar");

        if (stabilityProper) {
            this.sortNumber--;
            arrayVisualizer.setComparator(2);
            RunSummerSort.this.runSort(array, "Stability Test");
            arrayVisualizer.setComparator(0);
        } else {
            arrayVisualizer.setHeading("Stability Test inconsistent with algorithm. Skipping...");
            arrayVisualizer.updateNow();
            Thread.sleep(3000);
        }

        arrayVisualizer.getArrayFrame().setUniqueSlider(arrayVisualizer.getCurrentLength()); // 5
        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_TAIL_ALT);
        RunSummerSort.this.runSort(array, "Scrambled Tail");

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_HEAD_ALT); // 6
        RunSummerSort.this.runSort(array, "Scrambled Head");

        arrayManager.setShuffleSingle(Shuffles.FINAL_MERGE); // 7
        RunSummerSort.this.runSort(array, "Final Merge");

        arrayManager.setShuffleSingle(Shuffles.SAWTOOTH); // 8
        RunSummerSort.this.runSort(array, "Sawtooth Input");

        arrayManager.setShuffleSingle(Shuffles.HALF_ROTATION); // 9
        RunSummerSort.this.runSort(array, "Final Merge of Reversed Array");

        arrayManager.setShuffleSingle(Shuffles.FINAL_MERGE).addSingle(Shuffles.REVERSE); // 10
        RunSummerSort.this.runSort(array, "Reversed Final Merge");

        //arrayVisualizer.getArrayFrame().setUniqueSlider(arrayVisualizer.getCurrentLength() / 2);
        arrayManager.setShuffleSingle(Shuffles.ORGAN); // 11
        RunSummerSort.this.runSort(array, "Pipe Organ");

        //arrayVisualizer.getArrayFrame().setUniqueSlider(arrayVisualizer.getCurrentLength());
        arrayManager.setShuffleSingle(Shuffles.FINAL_RADIX); // 12
        RunSummerSort.this.runSort(array, "Final Radix Pass");

        arrayManager.setShuffleSingle(Shuffles.PAIRWISE); // 13
        RunSummerSort.this.runSort(array, "Final Pairwise Pass");

        arrayManager.setShuffleSingle(Shuffles.BST_TRAVERSAL); // 14
        RunSummerSort.this.runSort(array, "Binary Search Tree");

        arrayManager.setShuffleSingle(Shuffles.HEAPIFIED); // 15
        RunSummerSort.this.runSort(array, "Heap");

        arrayManager.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.SMOOTH); // 16
        RunSummerSort.this.runSort(array, "Smooth Heap");

        arrayManager.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.POPLAR); // 17
        RunSummerSort.this.runSort(array, "Poplar Heap");

        arrayManager.setShuffleSingle(Shuffles.PARTIAL_REVERSE); // 18
        RunSummerSort.this.runSort(array, "Half-Reversed Input");

        arrayManager.setShuffleSingle(Shuffles.DOUBLE_LAYERED); // 19
        RunSummerSort.this.runSort(array, "Evens Reversed, Odds In-Order");

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_ODDS); // 20
        RunSummerSort.this.runSort(array, "Evens In-Order, Scrambled Odds");

        arrayManager.setShuffleSingle(Shuffles.INTERLACED); // 21
        RunSummerSort.this.runSort(array, "Evens Ascending, Odds Descending");

        arrayManager.setShuffleSingle(Distributions.BELL_CURVE); // 22
        RunSummerSort.this.runSort(array, "Bell Curve");

        arrayManager.setShuffleSingle(Distributions.PERLIN_NOISE_CURVE); // 23
        RunSummerSort.this.runSort(array, "Perlin Noise Curve");

        arrayManager.setShuffleSingle(Distributions.PERLIN_NOISE); // 24
        RunSummerSort.this.runSort(array, "Perlin Noise");

        arrayManager.setShuffleSingle(Shuffles.TRIANGULAR); // 25
        RunSummerSort.this.runSort(array, "Triangular Input");

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_HALF_BACK); // 26
        RunSummerSort.this.runSort(array, "Scrambled Second Half");

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_HALF_FRONT); // 27
        RunSummerSort.this.runSort(array, "Scrambled First Half");

        arrayManager.setShuffleSingle(Shuffles.SHUFFLED_ENDS); // 28
        RunSummerSort.this.runSort(array, "Both Sides Scrambled");

        arrayManager.setShuffleSingle(Shuffles.NOISY); // 29
        RunSummerSort.this.runSort(array, "Noisy Input");

        arrayManager.setShuffleSingle(Shuffles.PARTITIONED); // 30
        RunSummerSort.this.runSort(array, "Partitioned");

        arrayManager.setShuffleSingle(Shuffles.SAWTOOTH).addSingle(Shuffles.REVERSE); // 31
        RunSummerSort.this.runSort(array, "Reversed Sawtooth");

        arrayManager.setShuffleSingle(Shuffles.FINAL_BITONIC); // 32
        RunSummerSort.this.runSort(array, "Final Bitonic Pass");

        arrayManager.setShuffleSingle(Shuffles.DOUBLE_LAYERED).addSingle(Shuffles.HALF_ROTATION); // 33
        RunSummerSort.this.runSort(array, "Diamond");

        arrayManager.setShuffleSingle(Shuffles.INV_BST); // 34
        RunSummerSort.this.runSort(array, "Inverted Binary Search Tree");

        arrayManager.setShuffleSingle(Shuffles.REC_RADIX); // 35
        RunSummerSort.this.runSort(array, "Recursive Final Radix");

        arrayManager.setShuffleSingle(Shuffles.REC_REV); // 36
        RunSummerSort.this.runSort(array, "Recursive Reversal");

        arrayManager.setShuffleSingle(Shuffles.TRI_HEAP).setSleepRatio(3); // 37
        RunSummerSort.this.runSort(array, "Triangular Heap");

        arrayManager.setShuffleSingle(Shuffles.BIT_REVERSE); // 38
        RunSummerSort.this.runSort(array, "Bit Reversed");

        arrayManager.setShuffleSingle(Shuffles.BLOCK_RANDOMLY); // 39
        RunSummerSort.this.runSort(array, "Block Shuffled");

        arrayManager.setShuffleSingle(Shuffles.BLOCK_REVERSE); // 40
        RunSummerSort.this.runSort(array, "Block Reversed");

        arrayManager.setShuffleSingle(Shuffles.QSORT_BAD); // 41
        RunSummerSort.this.runSort(array, "Quicksort Killer");

        arrayManager.setShuffleSingle(Shuffles.PDQ_BAD); // 42
        RunSummerSort.this.runSort(array, "PDQ Killer");

        arrayManager.setShuffleSingle(Shuffles.GRAIL_BAD); // 43
        RunSummerSort.this.runSort(array, "Grailsort Killer");

        arrayManager.setShuffleSingle(Shuffles.SHUF_MERGE_BAD); // 44
        RunSummerSort.this.runSort(array, "Shuffle Merge Killer");

        arrayManager.setShuffleSingle(Shuffles.CIRCLE); // 45
        RunSummerSort.this.runSort(array, "Circle Pass");

        arrayManager.setShuffleSingle(Shuffles.SORTED); // 46
        RunSummerSort.this.runSort(array, "Already Sorted");

        arrayManager.setShuffleSingle(Shuffles.WEAVE); // 47
        RunSummerSort.this.runSort(array, "Final Weave Pass");

        arrayManager.setShuffleSingle(Shuffles.RANDOM_ROTATION); // 48
        RunSummerSort.this.runSort(array, "Random Rotation");

        arrayManager.setShuffleSingle(Shuffles.MODULO); // 49
        RunSummerSort.this.runSort(array, "Modulo");

        arrayManager.setShuffleSingle(Shuffles.XORSWAP); // 50
        RunSummerSort.this.runSort(array, "XOR Swap");

        arrayManager.setShuffleSingle(Shuffles.FINAL_MERGE)
             .addSingle(Shuffles.PARTIAL_REVERSE_ALT); // 51
        RunSummerSort.this.runSort(array, "Penultimate Bitonic Pass");

        arrayManager.setShuffleSingle(Shuffles.GRAY_CODE); // 52
        RunSummerSort.this.runSort(array, "Gray Code Fractal");

        arrayManager.setShuffleSingle(Shuffles.PARTITIONED).addSingle(Shuffles.FINAL_RADIX); // 53
        RunSummerSort.this.runSort(array, "Weaved Partition");

        arrayManager.setShuffleSingle(Shuffles.PRIMES_REVERSED); // 54
        RunSummerSort.this.runSort(array, "Primes Reversed");

        arrayManager.setShuffleSingle(Shuffles.RBST_BREADTH); // 55
        RunSummerSort.this.runSort(array, "Breadth RBST Traversal");

        arrayManager.setShuffleSingle(Shuffles.ONLY_RUNS); // 56
        RunSummerSort.this.runSort(array, "Random Runs");

        arrayManager.setShuffleSingle(Shuffles.LOG_SLOPES); // 57
        RunSummerSort.this.runSort(array, "Logpile");

        arrayManager.setShuffleSingle(Distributions.RANDOM); // 58
        RunSummerSort.this.runSort(array, "White Noise");

        arrayManager.setShuffleSingle(Distributions.MODULO); // 59
        RunSummerSort.this.runSort(array, "Modulo Function");

        arrayManager.setShuffleSingle(Distributions.CUBIC).addSingle(Shuffles.RANDOM).setSleepRatio(2); // 60
        RunSummerSort.this.runSort(array, "Shuffled Cubic");

        arrayManager.setShuffleSingle(Distributions.QUINTIC).addSingle(Shuffles.RANDOM).setSleepRatio(2); // 61
        RunSummerSort.this.runSort(array, "Shuffled Quintic");

        arrayManager.setShuffleSingle(Distributions.SINE); // 62
        RunSummerSort.this.runSort(array, "Sine Wawe");

        arrayManager.setShuffleSingle(Distributions.COSINE); // 63
        RunSummerSort.this.runSort(array, "Cosine Wawe");

        arrayManager.setShuffleSingle(Distributions.SIMILAR).addSingle(Shuffles.RANDOM).setSleepRatio(2); // 64
        RunSummerSort.this.runSort(array, "Few Unique");

        arrayManager.setShuffleSingle(Distributions.EQUAL); // 65
        RunSummerSort.this.runSort(array, "No Unique");

        arrayManager.setShuffleSingle(Distributions.RULER); // 66
        RunSummerSort.this.runSort(array, "Ruler");

        arrayManager.setShuffleSingle(Distributions.BLANCMANGE); // 67
        RunSummerSort.this.runSort(array, "Blancmange Curve");

        arrayManager.setShuffleSingle(Distributions.CANTOR).addSingle(Shuffles.RANDOM).setSleepRatio(2); // 68
        RunSummerSort.this.runSort(array, "Shuffled Cantor Function");

        arrayManager.setShuffleSingle(Distributions.DIVISORS); // 69
        RunSummerSort.this.runSort(array, "Sum of Divisors");

        arrayManager.setShuffleSingle(Distributions.DIGITS_PROD); // 70
        RunSummerSort.this.runSort(array, "Product of Digits");

        arrayManager.setShuffleSingle(Distributions.DIGITS_SUM); // 71
        RunSummerSort.this.runSort(array, "Sum of Digits");

        arrayManager.setShuffleSingle(Distributions.BITS_SUM); // 72
        RunSummerSort.this.runSort(array, "Sum of Bits");

        arrayManager.setShuffleSingle(Distributions.HEX_DIGITS_SUM); // 73
        RunSummerSort.this.runSort(array, "Sum of Hex Digits");

        arrayManager.setShuffleSingle(Distributions.TOTIENT); // 74
        RunSummerSort.this.runSort(array, "Euler Totient Function");

        arrayManager.setShuffleSingle(Distributions.FSD); // 75
        RunSummerSort.this.runSort(array, "Fly Straight, Dammit!");

        arrayManager.setShuffleSingle(Distributions.NOISY_UNIQUES); // 76
        RunSummerSort.this.runSort(array, "Noisy Uniques");

        arrayManager.setShuffleSingle(Distributions.RAMP); // 77
        RunSummerSort.this.runSort(array, "Ramps");

        arrayManager.setShuffleSingle(Distributions.REVLOG); // 78
        RunSummerSort.this.runSort(array, "Decreasing Random");

        arrayManager.setShuffleSingle(Distributions.INCREASING_RANDOM); // 79
        RunSummerSort.this.runSort(array, "Increasing Random");

        arrayManager.setShuffleSingle(Distributions.WEIERSTRASS); // 80
        RunSummerSort.this.runSort(array, "Weierstrass Function");
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("RSS") {
            @Override
            public void run() {
                try{
                    if(runAllActive) {
                        RunSummerSort.this.sortNumber = current;
                        RunSummerSort.this.sortCount = total;
                    } else {
                        RunSummerSort.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    Sort tempSort = createSortInstance();

                    arrayVisualizer.setCategory(tempSort.getRunAllSortsName());
                    arrayVisualizer.toggleInShowcase(true);
                    RunSummerSort.this.executeSortList(array);
                    arrayVisualizer.toggleInShowcase(false);
                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Run " + tempSort.getRunAllSortsName());
                        arrayVisualizer.setHeading("Done");
                    }

                    arrayManager.toggleMutableLength(true);
                }
                catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                Sounds.toggleSound(false);
                arrayVisualizer.setSortingThread(null);
            }
        });

        arrayVisualizer.runSortingThread();
    }
}