package channelfinder;


import java.util.ArrayList;


/**
 * Separate tool to find best frequencies. Outputs frequencies from 0 - this may mean arbitrary frequency
 * of your choice. For example 5650Mhz - start of legal band.
 *
 * Works recursively by choosing first channel and marking all of the prohibited frequencies in the array.
 *
 * @author Sergey Kulakov (@klsrg)
 */
public class FindBestFrequencies {

    private static int HZ_TARGET_COUNT = 8;
    private static int REQS_1 = 25;
    private static int REQS_2 = 12;
    private static int REQS_3 = 12;
    private static int IN_ONE_ROW = 30;

    public static void main(String... args) {
        int[] hzs = new int[10];
        int[] base = addNextHz(new int[259], hzs, 0);
        System.out.print("result: [");
        for (int i = 0; i < hzs.length; i++) {
            System.out.printf("%4d", hzs[i]);
            if (i < hzs.length - 1) {
                System.out.format(", ");
            }
        }
        System.out.println("]\n");
        if (base != null) {
            printBase(base);
        } else {
            System.out.println("Failed to find the solution :(");
        }
    }

    // hzs - сортированный по возрастанию
    public static int[] addNextHz(int[] oldBase, int[] hzs, int addingHzIdx) {
        int[] base = oldBase.clone();

        int addingHzBaseIdx = 0;
        while (addingHzBaseIdx < base.length) {
            if (base[addingHzBaseIdx] != 0) {
                addingHzBaseIdx++;
                continue;
            }

            base[addingHzBaseIdx] = -1;
            hzs[addingHzIdx] = addingHzBaseIdx;

            if (addingHzIdx == HZ_TARGET_COUNT - 1) {
                return base;
            }

            ArrayList<Integer> returnToZeros = new ArrayList<Integer>(REQS_1 * 2 + REQS_2 * 2 + REQS_3 * 2);

            //delete1
            {
                int d1 = addingHzBaseIdx;
                while (d1 < addingHzBaseIdx + REQS_1 && d1 < base.length) {
                    if (base[d1] == 0) {
                        base[d1] = 1;
                        returnToZeros.add(d1);
                    }
                    d1++;
                }
            }

            //delete2
            {
                int d2_1 = addingHzBaseIdx + 190 - REQS_2;
                while (d2_1 < addingHzBaseIdx + 190 + REQS_2) {
                    if (0 <= d2_1 && d2_1 < base.length && base[d2_1] == 0) {
                        base[d2_1] = 2;
                        returnToZeros.add(d2_1);
                    }
                    d2_1++;
                }
            }
            {
                int d2_2 = addingHzBaseIdx + 240 - REQS_2;
                while (d2_2 < addingHzBaseIdx + 240 + REQS_2) {
                    if (0 <= d2_2 && d2_2 < base.length && base[d2_2] == 0) {
                        base[d2_2] = 2;
                        returnToZeros.add(d2_2);
                    }
                    d2_2++;
                }
            }

            //delete3
            for (int k = 0; k < addingHzIdx; k++) {
                {
                    int d3_1 = (hzs[k] * 2 - addingHzBaseIdx) - REQS_3;
                    while (d3_1 < (hzs[k] * 2 - addingHzBaseIdx) + REQS_3) {
                        if (0 <= d3_1 && d3_1 < base.length && base[d3_1] == 0) {
                            base[d3_1] = 3;
                            returnToZeros.add(d3_1);
                        }
                        d3_1++;
                    }
                }
                {
                    int d3_2 = (addingHzBaseIdx * 2 - hzs[k]) - REQS_3;
                    while (d3_2 < (addingHzBaseIdx * 2 - hzs[k]) + REQS_3) {
                        if (0 <= d3_2 && d3_2 < base.length && base[d3_2] == 0) {
                            base[d3_2] = 3;
                            returnToZeros.add(d3_2);
                        }
                        d3_2++;
                    }
                }
            }

            int[] result = addNextHz(base, hzs, addingHzIdx + 1);
            if (result != null) {
                return result;
            }

            hzs[addingHzIdx] = 0;
            base[addingHzBaseIdx] = 4; // не подходят
            for (Integer returnToZero : returnToZeros) {
                base[returnToZero] = 0;
            }
            addingHzBaseIdx++;
        }

        return null;
    }

    private static void printBase(int[] base) {
        for (int i = 0; i <= base.length - IN_ONE_ROW; i += IN_ONE_ROW) {
            System.out.printf("%4d - %4d: [", i, (i + IN_ONE_ROW));
            for (int j = 0; j < IN_ONE_ROW; j++) {
                System.out.format("%2d", base[i + j]);
                if (j < IN_ONE_ROW - 1) {
                    System.out.format(", ");
                }
            }
            System.out.println("]");
        }
    }

}