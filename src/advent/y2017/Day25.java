package advent.y2017;

import java.util.HashMap;
import java.util.Map;

import advent.Util;
import advent.y2017.datastructures.LinkedList;

public class Day25 {
    private static class State {
        public char name;
        public int[] write = new int[2];
        public char[] move = new char[2];
        public char[] nextStateName = new char[2];

        public State(char n, int w0, char m0, char n0, int w1, char m1, char n1) {
            name = n;

            write[0] = w0;
            move[0] = m0;
            nextStateName[0] = n0;

            write[1] = w1;
            move[1] = m1;
            nextStateName[1] = n1;
        }
    }

    private static class Machine {
        int checksum = 0;

        int startCount;

        char startState;
        char state;

        LinkedList tape = new LinkedList();
        LinkedList.Element currentTape;

        public Machine(State[] states, char initialState, int checksumInterval) {
            currentTape = tape.addLast(0);
            startCount = checksumInterval;
            startState = state = initialState;
            for (State s : states) {
                nameToState.put(s.name, s);
            }
        }

        Map<Character, State> nameToState = new HashMap<>();

        public void reset() {
            tape.clear();
            currentTape = tape.addLast(0);
            state = startState;
        }

        public int run() {
            for (int countdown = 0; countdown < startCount; ++countdown) {
                State s = nameToState.get(state);
                int index = currentTape.value;

                // update tape
                checksum += s.write[index] - currentTape.value;
                currentTape.value = s.write[index];

                // move tape
                switch (s.move[index]) {
                case 'L':
                    if (currentTape.previous == null) {
                        currentTape = tape.addFirst(0);
                    } else {
                        currentTape = currentTape.previous;
                    }
                    break;
                case 'R':
                    if (currentTape.next == null) {
                        currentTape = tape.addLast(0);
                    } else {
                        currentTape = currentTape.next;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("can't move the tape " + s.move[index]);
                }

                // state transition
                state = s.nextStateName[index];
            }

            return checksum;
        }
    }

    public static void main(String[] args) {
        State[] states = { 
                // sample data
//                new State('A', 1, 'R', 'B', 0, 'L', 'B'), 
//                new State('B', 1, 'L', 'A', 1, 'R', 'A')
                // real data
                new State('A', 1, 'R', 'B', 0, 'L', 'C'), 
                new State('B', 1, 'L', 'A', 1, 'R', 'C'),
                new State('C', 1, 'R', 'A', 0, 'L', 'D'),
                new State('D', 1, 'L', 'E', 1, 'L', 'C'),
                new State('E', 1, 'R', 'F', 1, 'R', 'A'),
                new State('F', 1, 'R', 'A', 1, 'R', 'E')
        };
        Machine testMachine = new Machine(states, 'A', 12134527);
        int checksum = testMachine.run();

        Util.log("checksum after 12134527 iterations is %d", checksum);
    }
/*
 * test data
Begin in state A.
Perform a diagnostic checksum after 6 steps.
 "A: 0:{ 1, R, B }, 1:{ 0, L, B }",
 "B: 0:{ 1, L, A }, 1:{ 1, R, A }";
*/
/*
Begin in state A.
Perform a diagnostic checksum after 12134527 steps.

"A: 0:{ 1, R, B } 1:{ 0, L, C}",
"B: 0:{ 1, L, A } 1:{ 1, R, C}",
"C: 0:{ 1, R, A } 1:{ 0, L, D}",
"D: 0:{ 1, L, E } 1:{ 1, L, C}",
"E: 0:{ 1, R, F } 1:{ 1, R, A}",
"F: 0:{ 1, R, A } 1:{ 1, R, E}";
*/
}
