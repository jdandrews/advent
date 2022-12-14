package advent.y2019;

import java.io.PrintStream;

/*
 * --- Day 5: Sunny with a Chance of Asteroids ---
 *
 * You're starting to sweat as the ship makes its way toward Mercury. The Elves suggest that you get the air conditioner
 * working by upgrading your ship computer to support the Thermal Environment Supervision Terminal.
 *
 * The Thermal Environment Supervision Terminal (TEST) starts by running a diagnostic program (your puzzle input).
 * The TEST diagnostic program will run on your existing Intcode computer after a few modifications:
 *
 * First, you'll need to add two new instructions:
 *
 *     Opcode 3 takes a single integer as input and saves it to the position given by its only parameter.
 *     For example, the instruction 3,50 would take an input value and store it at address 50.
 *
 *     Opcode 4 outputs the value of its only parameter. For example, the instruction 4,50 would output
 *     the value at address 50.
 *
 * Programs that use these instructions will come with documentation that explains what should be connected to
 * the input and output. The program 3,0,4,0,99 outputs whatever it gets as input, then halts.
 *
 * Second, you'll need to add support for parameter modes:
 *
 * Each parameter of an instruction is handled based on its parameter mode. Right now, your ship computer
 * already understands parameter mode 0, position mode, which causes the parameter to be interpreted as a
 * position - if the parameter is 50, its value is the value stored at address 50 in memory. Until now,
 * all parameters have been in position mode.
 *
 * Now, your ship computer will also need to handle parameters in mode 1, immediate mode. In immediate mode,
 * a parameter is interpreted as a value - if the parameter is 50, its value is simply 50.
 *
 * Parameter modes are stored in the same value as the instruction's opcode. The opcode is a two-digit number
 * based only on the ones and tens digit of the value, that is, the opcode is the rightmost two digits of the
 * first value in an instruction. Parameter modes are single digits, one per parameter, read right-to-left from
 * the opcode: the first parameter's mode is in the hundreds digit, the second parameter's mode is in the
 * thousands digit, the third parameter's mode is in the ten-thousands digit, and so on. Any missing modes are 0.
 *
 * For example, consider the program 1002,4,3,4,33.
 *
 * The first instruction, 1002,4,3,4, is a multiply instruction - the rightmost two digits of the first
 * value, 02, indicate opcode 2, multiplication. Then, going right to left, the parameter modes are 0 (hundreds
 * digit), 1 (thousands digit), and 0 (ten-thousands digit, not present and therefore zero):
 *
 * ABCDE
 *  1002
 *
 * DE - two-digit opcode,      02 == opcode 2
 *  C - mode of 1st parameter,  0 == position mode
 *  B - mode of 2nd parameter,  1 == immediate mode
 *  A - mode of 3rd parameter,  0 == position mode, omitted due to being a leading zero
 *
 * This instruction multiplies its first two parameters. The first parameter, 4 in position mode, works like
 * it did before - its value is the value stored at address 4 (33). The second parameter, 3 in immediate mode,
 * simply has value 3. The result of this operation, 33 * 3 = 99, is written according to the third parameter, 4 in
 * position mode, which also works like it did before - 99 is written to address 4.
 *
 * Parameters that an instruction writes to will never be in immediate mode.
 *
 * Finally, some notes:
 *
 *     It is important to remember that the instruction pointer should increase by the number of values in the
 *     instruction after the instruction finishes. Because of the new instructions, this amount is no longer always 4.
 *
 *     Integers can be negative: 1101,100,-1,4,0 is a valid program (find 100 + -1, store the result in position 4).
 *
 * The TEST diagnostic program will start by requesting from the user the ID of the system to test by running an
 * input instruction - provide it 1, the ID for the ship's air conditioner unit.
 *
 * It will then perform a series of diagnostic tests confirming that various parts of the Intcode computer, like
 * parameter modes, function correctly. For each test, it will run an output instruction indicating how far the
 * result of the test was from the expected value, where 0 means the test was successful. Non-zero outputs mean
 * that a function is not working correctly; check the instructions that were run before the output instruction
 * to see which one failed.
 *
 * Finally, the program will output a diagnostic code and immediately halt. This final output isn't an error; an
 * output followed immediately by a halt means the program finished. If all outputs were zero except the diagnostic
 * code, the diagnostic program ran successfully.
 *
 * After providing 1 to the only input instruction and passing all the tests, what diagnostic code does the
 * program produce?
 *
 */
public class Day05 {
  static int in = 1;
  static PrintStream out = System.out;
  static boolean[] mode = new boolean[3];

  static boolean printMode = true;

  public static void main(String[] args) {
    updateModeArray(91);
    updateModeArray(101);
    updateModeArray(1101);
    updateModeArray(1001);
    updateModeArray(11101);

    printMode = false;

    System.out.println("Part 1: SAMPLE 1 (   3500): " + run(SAMPLE1));
    mode = new boolean[3];
    System.out.println("      : SAMPLE 2 (      2): " + run(SAMPLE2));
    mode = new boolean[3];
    System.out.println("      : SAMPLE 3 (      2): " + run(SAMPLE3));
    mode = new boolean[3];
    System.out.println("      : SAMPLE 4 (      2): " + run(SAMPLE4));
    mode = new boolean[3];
    System.out.println("      : SAMPLE 5 (     30): " + run(SAMPLE5));
    mode = new boolean[3];
    System.out.println("      : SAMPLE 6 (3101844): " + run(SAMPLE6));
    mode = new boolean[3];
    System.out.println("      : SAMPLE 7 (      2): " + run(SAMPLE7));
    mode = new boolean[3];
    System.out.println("      : RESULT:   " + run(PROGRAM));
  }

  private static int run(int[] program) {
    int pc = 0;
    while (program[pc] != 99) {
      updateModeArray(program[pc]);
      int instruction = program[pc] % 100;
      switch (instruction) {
      case 1:
        int a = mode[0] ? program[pc + 1] : program[program[pc + 1]];
        int b = mode[1] ? program[pc + 2] : program[program[pc + 2]];
        program[program[pc + 3]] = a + b;
        pc += 4;
        break;
      case 2:
        a = mode[0] ? program[pc + 1] : program[program[pc + 1]];
        b = mode[1] ? program[pc + 2] : program[program[pc + 2]];
        program[program[pc + 3]] = a * b;
        pc += 4;
        break;
      case 3:
        // memory updates are always in indirect mode
        if (program[pc]>100) System.out.println("\nunexpected program[pc]: "+program[pc]);
        program[program[pc+1]] = in;
        pc += 2;
        break;
      case 4:
        int r0 = mode[0] ? pc + 1 : program[pc + 1];
        out.print(r0);
        pc += 2;
        break;
      default:
        throw new RuntimeException("found " + program[pc]);
      }
    }
    return program[0];
  }

  private static void updateModeArray(int opcode) {
    int maskMe = opcode / 100;
    int c = maskMe / 100;
    int b = (maskMe - c * 100) / 10;
    int a = maskMe - c * 100 - b * 10;

    mode[0] = a == 1;
    mode[1] = b == 1;
    mode[2] = c == 1;
    if (printMode)
      System.out.println("opcode/mode:" + opcode + "/[" + mode[2] + "," + mode[1] + "," + mode[0] + "]");
  }



  private static int SAMPLE1[] = { 1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50 };  // 3500
  private static int SAMPLE2[] = {1,0,0,0,99};                                  // 2
  private static int SAMPLE3[] = {2,3,0,3,99};                                  // 2
  private static int SAMPLE4[] = {2,4,4,5,99,0};                                // 2
  private static int SAMPLE5[] = {1,1,1,4,99,5,6,0,99};                         // 30
  private static int SAMPLE6[] = {
          1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,6,1,19,1,5,19,23,2,9,23,27,1,6,27,31,1,31,9,35,2,35,10,39,1,5,39,43,
          2,43,9,47,1,5,47,51,1,51,5,55,1,55,9,59,2,59,13,63,1,63,9,67,1,9,67,71,2,71,10,75,1,75,6,79,2,10,79,
          83,1,5,83,87,2,87,10,91,1,91,5,95,1,6,95,99,2,99,13,103,1,103,6,107,1,107,5,111,2,6,111,115,1,115,
          13,119,1,119,2,123,1,5,123,0,99,2,0,14,0};    // 3101844
  private static int SAMPLE7[] = { 1002, 4, 3, 4, 33 };

  private static int PROGRAM[] = { 3, 225, 1, 225, 6, 6, 1100, 1, 238, 225, 104, 0, 1102, 57, 23, 224, 101, -1311, 224,
      224, 4, 224, 1002, 223, 8, 223, 101, 6, 224, 224, 1, 223, 224, 223, 1102, 57, 67, 225, 102, 67, 150, 224, 1001,
      224, -2613, 224, 4, 224, 1002, 223, 8, 223, 101, 5, 224, 224, 1, 224, 223, 223, 2, 179, 213, 224, 1001, 224, -469,
      224, 4, 224, 102, 8, 223, 223, 101, 7, 224, 224, 1, 223, 224, 223, 1001, 188, 27, 224, 101, -119, 224, 224, 4,
      224, 1002, 223, 8, 223, 1001, 224, 7, 224, 1, 223, 224, 223, 1, 184, 218, 224, 1001, 224, -155, 224, 4, 224, 1002,
      223, 8, 223, 1001, 224, 7, 224, 1, 224, 223, 223, 1101, 21, 80, 224, 1001, 224, -101, 224, 4, 224, 102, 8, 223,
      223, 1001, 224, 1, 224, 1, 224, 223, 223, 1101, 67, 39, 225, 1101, 89, 68, 225, 101, 69, 35, 224, 1001, 224, -126,
      224, 4, 224, 1002, 223, 8, 223, 1001, 224, 1, 224, 1, 224, 223, 223, 1102, 7, 52, 225, 1102, 18, 90, 225, 1101,
      65, 92, 225, 1002, 153, 78, 224, 101, -6942, 224, 224, 4, 224, 102, 8, 223, 223, 101, 6, 224, 224, 1, 223, 224,
      223, 1101, 67, 83, 225, 1102, 31, 65, 225, 4, 223, 99, 0, 0, 0, 677, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1105, 0,
      99999, 1105, 227, 247, 1105, 1, 99999, 1005, 227, 99999, 1005, 0, 256, 1105, 1, 99999, 1106, 227, 99999, 1106, 0,
      265, 1105, 1, 99999, 1006, 0, 99999, 1006, 227, 274, 1105, 1, 99999, 1105, 1, 280, 1105, 1, 99999, 1, 225, 225,
      225, 1101, 294, 0, 0, 105, 1, 0, 1105, 1, 99999, 1106, 0, 300, 1105, 1, 99999, 1, 225, 225, 225, 1101, 314, 0, 0,
      106, 0, 0, 1105, 1, 99999, 1007, 226, 226, 224, 102, 2, 223, 223, 1005, 224, 329, 1001, 223, 1, 223, 108, 677,
      226, 224, 1002, 223, 2, 223, 1005, 224, 344, 1001, 223, 1, 223, 1007, 677, 677, 224, 1002, 223, 2, 223, 1005, 224,
      359, 1001, 223, 1, 223, 1107, 677, 226, 224, 102, 2, 223, 223, 1006, 224, 374, 1001, 223, 1, 223, 8, 226, 677,
      224, 1002, 223, 2, 223, 1006, 224, 389, 101, 1, 223, 223, 8, 677, 677, 224, 102, 2, 223, 223, 1006, 224, 404,
      1001, 223, 1, 223, 1008, 226, 226, 224, 102, 2, 223, 223, 1006, 224, 419, 1001, 223, 1, 223, 107, 677, 226, 224,
      102, 2, 223, 223, 1006, 224, 434, 101, 1, 223, 223, 7, 226, 226, 224, 1002, 223, 2, 223, 1005, 224, 449, 1001,
      223, 1, 223, 1107, 226, 226, 224, 1002, 223, 2, 223, 1006, 224, 464, 1001, 223, 1, 223, 1107, 226, 677, 224, 1002,
      223, 2, 223, 1005, 224, 479, 1001, 223, 1, 223, 8, 677, 226, 224, 1002, 223, 2, 223, 1006, 224, 494, 1001, 223, 1,
      223, 1108, 226, 677, 224, 1002, 223, 2, 223, 1006, 224, 509, 101, 1, 223, 223, 1008, 677, 677, 224, 1002, 223, 2,
      223, 1006, 224, 524, 1001, 223, 1, 223, 1008, 677, 226, 224, 102, 2, 223, 223, 1006, 224, 539, 1001, 223, 1, 223,
      1108, 677, 677, 224, 102, 2, 223, 223, 1005, 224, 554, 101, 1, 223, 223, 108, 677, 677, 224, 102, 2, 223, 223,
      1006, 224, 569, 101, 1, 223, 223, 1108, 677, 226, 224, 102, 2, 223, 223, 1005, 224, 584, 1001, 223, 1, 223, 108,
      226, 226, 224, 1002, 223, 2, 223, 1005, 224, 599, 1001, 223, 1, 223, 1007, 226, 677, 224, 102, 2, 223, 223, 1005,
      224, 614, 1001, 223, 1, 223, 7, 226, 677, 224, 102, 2, 223, 223, 1006, 224, 629, 1001, 223, 1, 223, 107, 226, 226,
      224, 102, 2, 223, 223, 1005, 224, 644, 101, 1, 223, 223, 7, 677, 226, 224, 102, 2, 223, 223, 1005, 224, 659, 101,
      1, 223, 223, 107, 677, 677, 224, 1002, 223, 2, 223, 1005, 224, 674, 1001, 223, 1, 223, 4, 223, 99, 226 };
}
