package advent.y2016;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class Day02 {
    private static abstract class Keypad {
        protected int rows;
        protected int cols;
        protected int currentKey = 5;
        private Table<Integer, Character, Integer> transitions = HashBasedTable.create();

        public int getCurrentKey() {
            return currentKey;
        }

        protected void init() {
            Integer[][] keypad = buildKeypad();
            transitions = getTransitions(keypad);
        }

        protected abstract Integer[][] buildKeypad();

        protected Table<Integer, Character, Integer> getTransitions(Integer[][] keypad) {
            Table<Integer, Character, Integer> result = HashBasedTable.create();
            for (int i=0; i<rows; ++i) {
                for (int j=0; j<cols; ++j) {
                    int key = keypad[i][j];
                    result.put(key, 'U', i==0      || keypad[i-1][j]<0 ? key : keypad[i-1][j]);
                    result.put(key, 'R', j==cols-1 || keypad[i][j+1]<0 ? key : keypad[i][j+1]);
                    result.put(key, 'D', i==rows-1 || keypad[i+1][j]<0 ? key : keypad[i+1][j]);
                    result.put(key, 'L', j==0      || keypad[i][j-1]<0 ? key : keypad[i][j-1]);
                }
            }
            return result;
        }

        public int move(char direction) {
            currentKey = transitions.get(currentKey, direction);
            return currentKey;
        }
    }
    // keypad B:
    //      1
    //    2 3 4         1 2
    //  5 6 7 8 9     3 4 5 6
    //    A B C         7 8
    //      D
    private static class KeypadB extends Keypad {
        public KeypadB() {
            rows = 5;
            cols = 5;
            init();
        }

        @Override
        protected Integer[][] buildKeypad() {
            Integer[][] keypad = {
                    {-1, -1,  1, -1, -1},
                    {-1,  2,  3,  4, -1},
                    { 5,  6,  7,  8,  9},
                    {-1, 10, 11, 12, -1},
                    {-1, -1, 13, -1, -1},
            };
            for (int i=0; i<rows; ++i) {
                for (int j=0; j<cols; ++j) {
                    System.out.print((keypad[i][j]<0 ? " " : Integer.toHexString(keypad[i][j]))+" ");
                }
                System.out.println();
            }

            return keypad;
        }
    }

    // keypad A:
    //   1 2 3
    //   4 5 6
    //   7 8 9
    private static class KeypadA extends Keypad {
        public KeypadA(int r, int c) {
            rows = r;
            cols = c;
            init();
        }

        @Override
        protected Integer[][] buildKeypad() {
            Integer[][] keypad = new Integer[rows][];
            for (int i=0; i<rows; ++i) {
                keypad[i] = new Integer[cols];
                for (int j=0; j<cols; ++j) {
                    keypad[i][j] = i*cols+j+1;
                    System.out.print(keypad[i][j]+" ");
                }
                System.out.println();
            }
            return keypad;
        }
    }

    public static void main(String[] args) {
        System.out.println("keypad a:");
        Keypad keypadA = new KeypadA(3,3);
        System.out.println("keypad b:");
        Keypad keypadB = new KeypadB();

        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();

        System.out.println("\ncombinations: ");
        for (String row : DATA) {
            for (int i=0; i<row.length(); ++i) {
                keypadA.move(row.charAt(i));
                keypadB.move(row.charAt(i) );
            }
            a.append(Integer.toHexString(keypadA.getCurrentKey()));
            b.append(Integer.toHexString(keypadB.getCurrentKey()));
        }
        System.out.println("\tkeypad a: "+a.toString());
        System.out.println("\tkeypad b: "+b.toString());
    }

    private static final String[] DATA = {
    			"RRLLRLLRULLRUUUDRDLDDLLLDDDDDUUURRRRUUDLRULURRRDRUDRUUDDRUDLLLRLDDDUDRDDRRLLLLRLRLULUURDRURRUULDRRDUDURRUURURDLURULLDUDRDLUUUUDDURRLL"
    			+ "LUDLDLRDRRRDULLDLDULLDRLDLDURDLRRULLDDLDRLLLUDDLLRDURULLDDDDDUURURLRLRRDUURUULRLLLULLRLULLUUDRRLLDURLDDDDULUUDLUDDDULRLDURDDRUUDRRU"
    			+ "UURLLLULURUDRULDRDUDUDRRDDULRURLLRRLRRLLDLULURDRDRULDRDRURUDLLRRDUUULDDDUURDLULDLRLLURRURLLUDURDDRUDRDLLLLDLRLDLDDRDRRDUUULLUULRRDL"
    			+ "URLDULLDLDUUUULLLDRURLRULLULRLULUURLLRDDRULDULRLDRRURLURUDLRRRLUDLDUULULLURLDDUDDLLUDRUDRLDUDURRRRLRUUURLUDDUDURDUDDDLLRLRDDURDRUUD"
    			+ "UDRULURLRLDRULDRRLRLDDDRDDDRLDUDRLULDLUDLRLRRRLRDULDDLRRDDLDDULDLLDU",
    			"RULLUDDUDLULRRDLLDRUDLLLDURLLLURDURLRDRRDLRDRDLLURRULUULUDUDDLLRRULLURDRLDURDLDDUURLUURLDLDLRLDRLRUULDRLRLDRLRLUDULURDULLLDRUDULDURUR"
    			+ "RRUDURDUDLRDRRURULRRLRLRRRRRRDRUDLDRULDRUDLRDLRRUDULDLRLURRRLLDRULULRUDULRLULLRLULDRUDUULLRUULDULDUDDUUULLLDRDDRRDLURUUDRRLRRRDLRRL"
    			+ "ULLLLDLRUULDLLULURUURURDRURLLDUDRRURRURRUUDDRRDDRRRRUDULULRLUULRRDDRDDLLUDLDLULLRLDRLLUULDURLDRULDDUDRUUUURRLDDUDRUURUDLLDLDLURDLUL"
    			+ "DRLLLULLLUDLLDLD",
    			"RDLDULURDLULRRDLRLLLULRUULURULLLDLLDDRLLURUUUURDRLURLLRLRLLLULRDLURDURULULDDUDDUDRLRLDLULLURRRUULUDRDURRRUDDDLUDLDLRLRRLLLRUULLLLURRD"
    			+ "DDRRRUURULRLDRRRLRLUDDRRULDDDRUUDDRLLDULRLUDUDLDLDDDUDDLLDDRDRDUDULDRRUDRDRRDRLUURDLRDDDULLDRRRRRUDRLURDUURRDDRLUDLURRRLRDDDLRRLUUL"
    			+ "RLURDUUURRDLDDULLLRURRRUDRLUDLLDDDDDUDDRDULLUUDDURRLULLUDULUUDRLDRRRLLURLRRLLDLLLLUDRUUUDDULLRDLLDUDUDUURRUUUDRUURDRDLLDLDDULLDDRRU"
    			+ "LDLDDUUURLDLULLLRRLLRDDULLDLDLDDLDLDULURRDURURDRDRRDLR",
    			"RDRLRRUUDRLDUDLLDLUDLUUDUDLRRUUDRDDDLDDLLLRRRUDULLRRRRRURRRLUDDDLRRRRUUULDURDRULLDLRURRUULUDRURRRRLRURLRDUUDUDUDRDDURRURUDLLLLLRURUUL"
    			+ "RUURLLURDRUURLUDDDRLDDURDLDUDRURDRLRRRRUURDDRRRRURDLUUDRLDRDUULURUDDULLURRDUDLUULLDURRURLUDUUDRDDDUUDDUUUULDLDUDDLUDUUDRURLLULRUUUL"
    			+ "LRRDDUDDLULDDUUUDLUDDLDDLLRUUDRULLRRDRLLDLLRRLULLRRDDRLRDUULLLUULLDLLUDUDDLRDULUDLDLUDDRRRRDUDLUULLULDLRRDLULRLRRRULRURRDRLULDDUDLD"
    			+ "LDULLURLLRDLURRULURDLURLUDRDRRUUDRLLUDDRLRDDUURLRRDUDLDRURDUUUDRRLLRDLDLLDRRURLUDURUULDUDLDDDDRUULLDDRLRURRDURLURRLDDRRRRLRLRDRURUD"
    			+ "DRDLDRURLULDDL",
    			"RULRDLDDLRURDDDDDDRURLLLDDDUUULLRRDLDLURUURLUDLURRLUDUURDULDRUULDDURULDUULDDULLLUDLRULDRLDLRDDRRDLDDLLDRRUDDUDRDUULUDLLLDDLUUULDDUUUL"
    			+ "RRDULLURLULDLRLLLRLURLLRLRLDRDURRDUUDDURRULDDURRULRDRDUDLRRDRLDULULDRDURDURLLLDRDRLULRDUURRUUDURRDRLUDDRRLDLDLULRLLRRUUUDDULURRDRLL"
    			+ "DLRRLDRLLLLRRDRRDDLDUULRLRRULURLDRLRDULUDRDLRUUDDDURUDLRLDRRUDURDDLLLUDLRLURDUDUDULRURRDLLURLLRRRUDLRRRLUDURDDDDRRDLDDLLDLRDRDDRLLL"
    			+ "URDDRDRLRULDDRRLUURDURDLLDRRRDDURUDLDRRDRUUDDDLUDULRUUUUDRLDDD"};
}
