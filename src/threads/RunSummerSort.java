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
    final Class<? extends Sort> SORT_CLASS = sorts.hybrid.RemiSort.class;
    final int                  SORT_LENGTH = 4096;
    final double                SORT_SPEED = 4;
    final int                 BUCKET_COUNT = 0;
    final int                 UNIQUE_COUNT = 16;

    public RunSummerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 80;
        this.categoryCount = this.sortCount;
    }

    protected synchronized void runIndividualSort(Sort sort, int bucketCount, int[] array, int defaultLength, double defaultSpeed, boolean slowSort, String shuffleName) throws Exception {
        Delays.setSleepRatio(2.5);
        
        int sortLength;
        if(slowSort) {
            sortLength = this.calculateLengthSlow(defaultLength, sort.getUnreasonableLimit());
        }
        else {
            sortLength = this.calculateLength(defaultLength);
        }
        if(sortLength != arrayVisualizer.getCurrentLength()) {
            arrayFrame.setLengthSlider(sortLength);
        }
        
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
        Thread.sleep(1000);
        
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
        arrayVisualizer.getArrayManager().setDistribution(Distributions.LINEAR); // 1
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.RANDOM);
        RunSummerSort.this.runSort(array, "Random");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REVERSE); // 2
        RunSummerSort.this.runSort(array, "Reversed");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ALMOST); // 3
        RunSummerSort.this.runSort(array, "Almost Sorted");

        arrayVisualizer.getArrayFrame().setUniqueSlider(UNIQUE_COUNT); // 4
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.RANDOM);
        RunSummerSort.this.runSort(array, "Many Similar");

        arrayVisualizer.getArrayFrame().setUniqueSlider(arrayVisualizer.getCurrentLength()); // 5
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_TAIL_ALT);
        RunSummerSort.this.runSort(array, "Scrambled Tail");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_HEAD_ALT); // 6
        RunSummerSort.this.runSort(array, "Scrambled Head");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_MERGE); // 7
        RunSummerSort.this.runSort(array, "Final Merge");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SAWTOOTH); // 8
        RunSummerSort.this.runSort(array, "Sawtooth Input");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.HALF_ROTATION); // 9
        RunSummerSort.this.runSort(array, "Final Merge of Reversed Array");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_MERGE) // 10
                                         .addSingle(Shuffles.REVERSE);
        RunSummerSort.this.runSort(array, "Reversed Final Merge");

        //arrayVisualizer.getArrayFrame().setUniqueSlider(arrayVisualizer.getCurrentLength() / 2);
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ORGAN); // 11
        RunSummerSort.this.runSort(array, "Pipe Organ");

        //arrayVisualizer.getArrayFrame().setUniqueSlider(arrayVisualizer.getCurrentLength());
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_RADIX); // 12
        RunSummerSort.this.runSort(array, "Final Radix Pass");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PAIRWISE); // 13
        RunSummerSort.this.runSort(array, "Final Pairwise Pass");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.BST_TRAVERSAL); // 14
        RunSummerSort.this.runSort(array, "Binary Search Tree");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.HEAPIFIED); // 15
        RunSummerSort.this.runSort(array, "Heap");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REVERSE) // 16
                                         .addSingle(Shuffles.SMOOTH);
        RunSummerSort.this.runSort(array, "Smooth Heap");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REVERSE) // 17
                                         .addSingle(Shuffles.POPLAR);
        RunSummerSort.this.runSort(array, "Poplar Heap");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PARTIAL_REVERSE); // 18
        RunSummerSort.this.runSort(array, "Half-Reversed Input");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.DOUBLE_LAYERED); // 19
        RunSummerSort.this.runSort(array, "Evens Reversed, Odds In-Order");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_ODDS); // 20
        RunSummerSort.this.runSort(array, "Evens In-Order, Scrambled Odds");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.INTERLACED); // 21
        RunSummerSort.this.runSort(array, "Evens Ascending, Odds Descending");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.BELL_CURVE); // 22
        RunSummerSort.this.runSort(array, "Bell Curve");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.PERLIN_NOISE_CURVE); // 23
        RunSummerSort.this.runSort(array, "Perlin Noise Curve");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.PERLIN_NOISE); // 24
        RunSummerSort.this.runSort(array, "Perlin Noise");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.TRIANGULAR); // 25
        RunSummerSort.this.runSort(array, "Triangular Input");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_HALF_BACK); // 26
        RunSummerSort.this.runSort(array, "Scrambled Second Half");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_HALF_FRONT); // 27
        RunSummerSort.this.runSort(array, "Scrambled First Half");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_ENDS); // 28
        RunSummerSort.this.runSort(array, "Both Sides Scrambled");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.NOISY); // 29
        RunSummerSort.this.runSort(array, "Noisy Input");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PARTITIONED); // 30
        RunSummerSort.this.runSort(array, "Partitioned");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SAWTOOTH) // 31
                                         .addSingle(Shuffles.REVERSE);
        RunSummerSort.this.runSort(array, "Reversed Sawtooth");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_BITONIC); // 32
        RunSummerSort.this.runSort(array, "Final Bitonic Pass");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.DOUBLE_LAYERED) // 33
                                         .addSingle(Shuffles.HALF_ROTATION);
        RunSummerSort.this.runSort(array, "Diamond");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.INV_BST); // 34
        RunSummerSort.this.runSort(array, "Inverted Binary Search Tree");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REC_RADIX); // 35
        RunSummerSort.this.runSort(array, "Recursive Final Radix");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REC_REV); // 36
        RunSummerSort.this.runSort(array, "Recursive Reversal");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.TRI_HEAP) // 37
                                         .setSleepRatio(3);
        RunSummerSort.this.runSort(array, "Triangular Heap");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.BIT_REVERSE); // 38
        RunSummerSort.this.runSort(array, "Bit Reversed");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.BLOCK_RANDOMLY); // 39
        RunSummerSort.this.runSort(array, "Block Shuffled");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.BLOCK_REVERSE); // 40
        RunSummerSort.this.runSort(array, "Block Reversed");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.QSORT_BAD); // 41
        RunSummerSort.this.runSort(array, "Quicksort Killer");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PDQ_BAD); // 42
        RunSummerSort.this.runSort(array, "PDQ Killer");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.GRAIL_BAD); // 43
        RunSummerSort.this.runSort(array, "Grailsort Killer");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUF_MERGE_BAD); // 44
        RunSummerSort.this.runSort(array, "Shuffle Merge Killer");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.CIRCLE); // 45
        RunSummerSort.this.runSort(array, "Circle Pass");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SORTED); // 46
        RunSummerSort.this.runSort(array, "Already Sorted");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.WEAVE); // 47
        RunSummerSort.this.runSort(array, "Final Weave Pass");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.RANDOM_ROTATION); // 48
        RunSummerSort.this.runSort(array, "Random Rotation");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.MODULO); // 49
        RunSummerSort.this.runSort(array, "Modulo");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.XORSWAP); // 50
        RunSummerSort.this.runSort(array, "XOR Swap");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.BST_PREORDER); // 51
        RunSummerSort.this.runSort(array, "Pre-order BST Traversal");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.BST_POSTORDER); // 52
        RunSummerSort.this.runSort(array, "Post-order BST Traversal");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.RBST_PREORDER); // 53
        RunSummerSort.this.runSort(array, "Pre-order RBST Traversal");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.RBST_POSTORDER); // 54
        RunSummerSort.this.runSort(array, "Post-order RBST Traversal");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.RBST_BREADTH); // 55
        RunSummerSort.this.runSort(array, "Breadth RBST Traversal");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.CRAZY_BLOCK_RANDOMLY); // 56
        RunSummerSort.this.runSort(array, "Randomly w/ Crazy Blocks");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.LOG_SLOPES); // 57
        RunSummerSort.this.runSort(array, "Logpile");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.RANDOM); // 58
        RunSummerSort.this.runSort(array, "White Noise");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.MODULO); // 59
        RunSummerSort.this.runSort(array, "Modulo Function");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.CUBIC) // 60
                                         .addSingle(Shuffles.RANDOM)
                                         .setSleepRatio(2);
        RunSummerSort.this.runSort(array, "Shuffled Cubic");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.QUINTIC) // 61
                                         .addSingle(Shuffles.RANDOM)
                                         .setSleepRatio(2);
        RunSummerSort.this.runSort(array, "Shuffled Quintic");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.SINE); // 62
        RunSummerSort.this.runSort(array, "Sine Wawe");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.COSINE); // 63
        RunSummerSort.this.runSort(array, "Cosine Wawe");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.SIMILAR) // 64
                                         .addSingle(Shuffles.RANDOM)
                                         .setSleepRatio(2);
        RunSummerSort.this.runSort(array, "Few Unique");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.EQUAL); // 65
        RunSummerSort.this.runSort(array, "No Unique");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.RULER); // 66
        RunSummerSort.this.runSort(array, "Ruler");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.BLANCMANGE); // 67
        RunSummerSort.this.runSort(array, "Blancmange Curve");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.CANTOR) // 68
                                         .addSingle(Shuffles.RANDOM)
                                         .setSleepRatio(2);
        RunSummerSort.this.runSort(array, "Shuffled Cantor Function");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.DIVISORS); // 69
        RunSummerSort.this.runSort(array, "Sum of Divisors");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.DIGITS_PROD); // 70
        RunSummerSort.this.runSort(array, "Product of Digits");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.DIGITS_SUM); // 71
        RunSummerSort.this.runSort(array, "Sum of Digits");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.BITS_SUM); // 72
        RunSummerSort.this.runSort(array, "Sum of Bits");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.HEX_DIGITS_SUM); // 73
        RunSummerSort.this.runSort(array, "Sum of Hex Digits");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.EXP) // 74
                                         .addSingle(Shuffles.RANDOM)
                                         .setSleepRatio(2);
        RunSummerSort.this.runSort(array, "Shuffled Exponential Function");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.NOISY_UNIQUES); // 75
        RunSummerSort.this.runSort(array, "Noisy Uniques");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.RAMP); // 76
        RunSummerSort.this.runSort(array, "Ramps (OEIS A002262)");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.REVLOG); // 77
        RunSummerSort.this.runSort(array, "Decreasing Random");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.INCREASING_RANDOM); // 78
        RunSummerSort.this.runSort(array, "Increasing Random");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.WEIERSTRASS); // 79
        RunSummerSort.this.runSort(array, "Weierstrass Function");

        arrayVisualizer.getArrayManager().setShuffleSingle(Distributions.DIVISORS_COUNT); // 80
        RunSummerSort.this.runSort(array, "Number of Divisors");
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
                    }
                    else {
                        RunSummerSort.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    Sort tempSort = createSortInstance();

                    arrayVisualizer.setCategory(tempSort.getRunAllSortsName());

                    RunSummerSort.this.executeSortList(array);
                    
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