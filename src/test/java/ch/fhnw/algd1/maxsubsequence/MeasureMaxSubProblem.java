/*
 * Created on 16.10.2023
 */
package ch.fhnw.algd1.maxsubsequence;

import java.util.Random;

/**
 * @author Wolfgang Weck
 */
public class MeasureMaxSubProblem {
	private static record TestData(int[] data, int result) {}

	private static TestData[] testData = { new TestData(new int[] { 1, 3, -5, 3, 3, 2, -9, -2 }, 8),
			new TestData(new int[] { 31, -41, 59, 26, -53, 58, 97, -93, -23 }, 187),
			new TestData(new int[] { 31, -41, 259, 26, -453, 58, 97, -93, -23 }, 285),
			new TestData(new int[] { 41, -31, 59, -97, -53, -58, 26 }, 69),
			new TestData(new int[] { -97, 41, -31, 59, -97, -53, -58, 26 }, 69),
			new TestData(new int[] { 31, -41, 59, 26, -53, 58, 97 }, 187),
			new TestData(new int[] { 31, -41, 59, 26, -53, 58, 97, -1 }, 187),
			new TestData(new int[] { 2, -10, 8, -10, 2 }, 8), new TestData(new int[] { 41, -31, -59, -26, -13 }, 41),
			new TestData(new int[] { -31, -59, -26, -13, 47 }, 47), new TestData(new int[] { 12 }, 12),
			new TestData(new int[] { 1 }, 1), new TestData(new int[] { 0 }, 0), new TestData(new int[] { -12 }, 0),
			new TestData(new int[] { -1 }, 0), new TestData(new int[] { -31, -59, -26, -13, -47 }, 0),
			new TestData(new int[] {}, 0) };
	private static Random rand = new Random(42);

	private static int[] randData(int size) {
		int[] d = new int[size];
		for (int i = 0; i < d.length; i++)
			d[i] = rand.nextInt(-100, 101);
		return d;
	}

	private static boolean testsFail(MaxSubProblem p) {
		int i = 0;
		while (i < testData.length && p.maxSub(testData[i].data) == testData[i].result)
			i++;
		return i < testData.length;
	}

	private static int microsec(MaxSubProblem p, int[] data) {
		System.gc();
		long t = System.nanoTime();
		p.maxSub(data);
		return (int)((System.nanoTime() - t + 500_000) / 1_000_000);
	}

	public static void main(String[] args) {
		MaxSubProblem p = new MaxSubProblem();
		System.out.print("Running correctness checks ... ");
		if (testsFail(p)) {
			System.out.println("failed!");
			System.out.println("Algorithm is not working correctly yet.");
		} else {
			System.out.print("ok.\nWarming up hotspot engine. Be patient ...");
			p.maxSub(randData(4000));
			int n = 250;
			int t1 = microsec(p, randData(n));
			System.out.printf("ok.%n%,d values: %,d msec%n", n, t1);
			while (t1 < 10_000 && n < 200_000_000) {
				n <<= 1;
				int t0 = t1;
				t1 = microsec(p, randData(n));
				double ratio = (double)t1 / t0;
				if (Double.isFinite(ratio) && ratio > 0) System.out.printf("%,d values: %,d msec, ratio: %f%n", n, t1, ratio);
				else System.out.printf("%,d values: %,d msec%n", n, t1);
			}
		}
	}
}
