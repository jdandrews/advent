package advent.y2019;

public class Day02 {
    public static void main(String[] args) {
        System.out.println("Part 1: SAMPLE 1: " + run(SAMPLE1));
        System.out.println("Part 1: SAMPLE 2: " + run(SAMPLE2));
        System.out.println("Part 1: SAMPLE 3: " + run(SAMPLE3));
        System.out.println("Part 1: SAMPLE 4: " + run(SAMPLE4));
        System.out.println("Part 1: SAMPLE 5: " + run(SAMPLE5));

        int[] program = copyProgram(PROGRAM);
        program[1] = 12;
        program[2] = 2;
        System.out.println("Part 1: PROGRAM: " + run(program));

        // part 2
        for (int i=0; i<100; ++i) {
            for (int j=0; j<100; ++j) {
                program = copyProgram(PROGRAM);
                program[1] = i;
                program[2] = j;
                int result = run(program);
                if (result==19690720) {
                    System.out.println("Part 2: " + ((100*i)+j));
                    break;
                }
            }
        }
    }

    private static int[] copyProgram(int[] program) {
        int[] programCopy = new int[program.length];
        for (int i=0; i<program.length; ++i) {
            programCopy[i] = program[i];
        }
        return programCopy;
    }

    private static int run(int[] program) {
        int pc = 0;
        while (program[pc] != 99) {
            switch (program[pc]) {
            case 1:
                program[program[pc+3]] = program[program[pc+1]] + program[program[pc+2]];
                break;
            case 2:
                program[program[pc+3]] = program[program[pc+1]] * program[program[pc+2]];
                break;
            default:
                throw new RuntimeException("found "+program[pc]);
            }
            pc += 4;
        }
        return program[0];
    }

    private static int SAMPLE1[] = {1,9,10,3,2,3,11,0,99,30,40,50};
    private static int SAMPLE2[] = {1,0,0,0,99};
    private static int SAMPLE3[] = {2,3,0,3,99};
    private static int SAMPLE4[] = {2,4,4,5,99,0};
    private static int SAMPLE5[] = {1,1,1,4,99,5,6,0,99};

    private static int PROGRAM[] = {
            1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,6,1,19,1,5,19,23,2,9,23,27,1,6,27,31,1,31,9,35,2,35,10,39,1,5,39,43,
            2,43,9,47,1,5,47,51,1,51,5,55,1,55,9,59,2,59,13,63,1,63,9,67,1,9,67,71,2,71,10,75,1,75,6,79,2,10,79,
            83,1,5,83,87,2,87,10,91,1,91,5,95,1,6,95,99,2,99,13,103,1,103,6,107,1,107,5,111,2,6,111,115,1,115,
            13,119,1,119,2,123,1,5,123,0,99,2,0,14,0};
}
