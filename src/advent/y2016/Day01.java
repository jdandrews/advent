package advent.y2016;

import java.util.HashSet;
import java.util.Set;

public class Day01 {
	static enum Heading {
		N(0), E(1), S(2), W(3);
		private int index;

		private Heading(int i) {
			index = i;
		}

		public Heading turn(String direction) {
			int newIndex;
			switch (direction) {
			case "R":
				newIndex = (index + 1) % 4;
				break;
			case "L":
				newIndex = (index + 3) % 4;
				break;
			default:
				throw new IllegalArgumentException("don't know how to turn " + direction);
			}
			for (Heading result : Heading.values()) {
				if (newIndex == result.index) {
					return result;
				}
			}
			throw new IllegalStateException("don't know how to turn to " + newIndex);
		}
	}

	private static class Location {
		int row;
		int col;

		public Location(int r, int c) {
			row = r;
			col = c;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Location)) {
				return false;
			}
			Location loc = (Location) obj;
			return row == loc.row && col == loc.col;
		}

		@Override
		public int hashCode() {
			return Integer.hashCode(row) * 100 + Integer.hashCode(col);
		}

		@Override
		public String toString() {
			return "(" + row + "," + col + ")";
		}
	}

	public static void main(String[] args) {
		Set<Location> locations = new HashSet<>();

		String[] instructions = DATA.split(",");
		Location location = new Location(0, 0);
		Location firstRevisited = null;
		Heading heading = Heading.N;

		for (String instruction : instructions) {
			String turn = instruction.trim().substring(0, 1);
			int move = Integer.parseInt(instruction.trim().substring(1));

			heading = heading.turn(turn);
			// move
			for (int step = 0; step < move; ++step) {
				if (firstRevisited==null && !locations.add(location)) {
					firstRevisited = location;
				}
				switch (heading) {
				case E:
					location = new Location(location.row + 1, location.col);
					break;
				case N:
					location = new Location(location.row, location.col + 1);
					break;
				case W:
					location = new Location(location.row - 1, location.col);
					break;
				case S:
					location = new Location(location.row, location.col - 1);
					break;
				default:
					throw new IllegalArgumentException("no heading " + heading.name());
				}
			}
		}
		System.out.println("Following all instructions (part 1) leads to:\n" +
				location.row + "," + location.col + " --> " + (Math.abs(location.row) + Math.abs(location.col)) + " blocks");
		System.out.println("...but revisited location (part 2) is "+
				firstRevisited.row + "," + firstRevisited.col + " --> " + (Math.abs(firstRevisited.row) + Math.abs(firstRevisited.col)) + " blocks");

	}

	private static final String DATA = "L1, L5, R1, R3, L4, L5, R5, R1, L2, L2, L3, R4, L2, R3, R1, L2, R5, R3, L4, R4, L3, R3, R3, L2, R1, L3, "
			+ "R2, L1, R4, L2, R4, L4, R5, L3, R1, R1, L1, L3, L2, R1, R3, R2, L1, R4, L4, R2, L189, L4, R5, R3, L1, R47, R4, R1, R3, L3, L3, L2, "
			+ "R70, L1, R4, R185, R5, L4, L5, R4, L1, L4, R5, L3, R2, R3, L5, L3, R5, L1, R5, L4, R1, R2, L2, L5, L2, R4, L3, R5, R1, L5, L4, L3, "
			+ "R4, L3, L4, L1, L5, L5, R5, L5, L2, L1, L2, L4, L1, L2, R3, R1, R1, L2, L5, R2, L3, L5, L4, L2, L1, L2, R3, L1, L4, R3, R3, L2, R5, "
			+ "L1, L3, L3, L3, L5, R5, R1, R2, L3, L2, R4, R1, R1, R3, R4, R3, L3, R3, L5, R2, L2, R4, R5, L4, L3, L1, L5, L1, R1, R2, L1, R3, R4, "
			+ "R5, R2, R3, L2, L1, L5";
}
