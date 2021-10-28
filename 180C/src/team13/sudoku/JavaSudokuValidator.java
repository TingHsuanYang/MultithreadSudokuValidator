package team13.sudoku;


import java.util.*;

public class JavaSudokuValidator {
	
	private static final int[][] sudoku = 
	   {{6, 2, 4, 5, 3, 9, 1, 8, 7},
		{5, 1, 9, 7, 2, 8, 6, 3, 4},
		{8, 3, 7, 6, 1, 4, 2, 9, 5},
		{1, 4, 3, 8, 6, 5, 7, 2, 9},
		{9, 5, 8, 2, 4, 7, 3, 6, 1},
		{7, 6, 2, 3, 9, 1, 4, 5, 8},
		{3, 7, 1, 9, 5, 6, 8, 4, 2},
		{4, 9, 6, 1, 8, 2, 5, 7, 3},
		{2, 8, 5, 4, 7, 3, 9, 1, 6}};
	
	public static boolean isValid = true;
	
	
	public static class RowValidator extends Thread {
		int r;
		Set<Integer> used;

		public RowValidator(int r, int c) {
			this.r = r;
		}

		public void run() {
			if (!isValid)
				return;
			used = new HashSet<Integer>();
			for (int col = 0; col < 9; col++) {
				int n = sudoku[r][col];
				if (used.contains(n)) {
					isValid = false;
				} else {
					used.add(n);
				}
			}
		}
	}

	public static class ColumnValidator extends Thread {
		int c;
		Set<Integer> used;

		public ColumnValidator(int r, int c) {
			this.c = c;
		}

		public void run() {
			if (!isValid)
				return;
			used = new HashSet<Integer>();
			for (int row = 0; row < 9; row++) {
				int n = sudoku[row][c];
				if (used.contains(n)) {
					isValid = false;
				} else {
					used.add(n);
				}
			}
		}
	}

	

	public static class BoxValidator extends Thread {
		int r, c;
		Set<Integer> used;

		public BoxValidator(int r, int c) {
			this.r = r;
			this.c = c;
		}

		public void run() {
			if (!isValid)
				return;
			used = new HashSet<Integer>();
			for (int row = r; row < r + 3; row++) {
				for (int col = c; col < c + 3; col++) {
					int n = sudoku[row][col];
					if (used.contains(n)) {
						isValid = false;
					} else {
						used.add(n);
					}
				}
			}
		}
	}
	 
	
	public static void main(String[] args) {
		Thread[] threads = new Thread[27];
		int index = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (i == 0) {
					threads[index++] = new RowValidator(i, j);
				}
				if (j == 0) {
					threads[index++] = new ColumnValidator(i, j);
				}
				if (i % 3 == 0 && j % 3 == 0) {
					threads[index++] = new BoxValidator(i, j);
				}
			}
		}

		long startTime = System.nanoTime();

		// Start all threads
		for (int i = 0; i < 27; i++) {
			threads[i].start();
		}
		
		// Wait for all threads to finish
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		System.out.printf("Time used: %d ns%n", timeElapsed);

		if (isValid) {
			System.out.println("sudoku is valid!");
		} else {
			System.out.println("sudoku is invalid!");
		}
		
		
	}
}