package advent.y2016;

import java.util.ArrayDeque;
import java.util.Queue;

import advent.Util;

public class Day13 {
	private static final int SALT = 1352;
	private static final int HEIGHT = 55;
	private static final int WIDTH =  55;

	public static void main(String[] args) {
		Day13 it = new Day13();
		int[][] maze = it.render(WIDTH, HEIGHT);

		Point start = new Point(1,1);
		Point end = new Point(31,39);

		Util.log("\nFrom %s to %s requires %d steps", start, end, it.solve(maze, start, end));

		int nTrips = 0;
		for (int i=0; i<51; ++i) {
			for (int j=0; j<51; ++j) {
				try {
					if (it.solve(maze, start, new Point(i,j)) <= 50)
						++nTrips;
				} catch (RuntimeException e) {
					// continue; no solution
				}
			}
		}

		Util.log("there are %d trips of <=50 steps available", nTrips);
	}

	private static class Point {
		public final int x;
		public final int y;
		public Point(int zeroBasedX, int zeroBasedY) {
			this.x = zeroBasedX;
			this.y = zeroBasedY;
		}
		@Override
		public boolean equals(Object obj) {
			if ( ! (obj instanceof Point))
				return false;

			Point p = (Point) obj;
			if (p==this)
				return true;

			return p.x==x && p.y==y;
		}

		@Override
		public int hashCode() {
			return x + 13*y;
		}

		@Override
		public String toString() {
			return "("+x+","+y+")";
		}
	}

	/**
	 * Implements the Lee algorithm for breadth-first search of the shortest path through a maze.
	 * @param maze the maze to solve
	 *
	 * @param start the starting node
	 * @param end the ending node
	 * @return the length of the shortest rectilinear path through the maze
	 */
	private int solve(int[][] maze, Point start, Point end) {

		//	setup

		boolean[][] visited = new boolean[WIDTH][];
		int[][] score = new int[WIDTH][];

		for (int i=0; i<WIDTH; ++i) {
			visited[i] = new boolean[HEIGHT];
			score[i] = new int[HEIGHT];
			for (int j=0; j<HEIGHT; ++j) {
				visited[i][j] = false;
			}
		}

		if (start.equals(end))
			return 0;

		if (maze[start.x][start.y]==0)
			throw new RuntimeException("start position is a wall.");

		visited[start.x][start.y] = true;

		Queue<Point> pointQueue = new ArrayDeque<>();
		pointQueue.offer(start);

		while ( ! pointQueue.isEmpty()) {
			Point p = pointQueue.poll();
			if (p.equals(end)) {
				return score[p.x][p.y];
			}

			enqueueCandidate(new Point(p.x, p.y+1), pointQueue, score[p.x][p.y]+1, visited, score, maze);			// up
			enqueueCandidate(new Point(p.x, p.y-1), pointQueue, score[p.x][p.y]+1, visited, score, maze);			// down
			enqueueCandidate(new Point(p.x-1, p.y), pointQueue, score[p.x][p.y]+1, visited, score, maze);			// left
			enqueueCandidate(new Point(p.x+1, p.y), pointQueue, score[p.x][p.y]+1, visited, score, maze);			// right
		}
		throw new RuntimeException("no path to success");
	}


	private void enqueueCandidate(Point candidate, Queue<Point> pointQueue, int score, boolean[][] visited, int[][] allScores, int[][] maze) {
		if (isValid(candidate) && ! visited[candidate.x][candidate.y] && maze[candidate.x][candidate.y]==1) {
			visited[candidate.x][candidate.y] = true;
			allScores[candidate.x][candidate.y] = score;
			pointQueue.offer(candidate);
		}
	}


	private boolean isValid(Point up) {
		return up.x >= 0 && up.x < WIDTH && up.y >=0 && up.y < HEIGHT;
	}


	private int[][] render(int width, int height) {
		int[][] maze = new int[WIDTH][];
		for (int i=0; i<maze.length; ++i) {
			maze[i] = new int[HEIGHT];
		}

		System.out.print("   ");
		for (int x=0; x<width; ++x) {
			System.out.print(x/10);
		}
		System.out.println("");

		System.out.print("   ");
		for (int x=0; x<width; ++x) {
			System.out.print(x%10);
		}
		System.out.println("");

		for (int y=0; y<height; ++y) {
			System.out.format("%2d",y);
			System.out.print(" ");
			for (int x=0; x<width; ++x) {
				System.out.print(isWall(x,y) ? "#" : ".");
				if (! isWall(x,y) )
					maze[x][y] = 1;
			}
			System.out.println("");
		}

		return maze;
	}

	private boolean isWall(int x, int y) {
		int n = SALT + x*x + 3*x + 2*x*y + y + y*y;
		String bits = Integer.toBinaryString(n);
		boolean isWall = false;
		for (int i=0; i<bits.length(); ++i) {
			if (bits.charAt(i)=='1')
				isWall = ! isWall;
		}
		return isWall;
	}
}
