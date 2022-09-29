package advent.y2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;
/*
 * If you analyze the assembly, setting a=1 initializes b to a certain value (105700 in /u/ghlmtz's case) 
 * and c to b+17000. I'm thinking that only the initial value of b varies between different people's inputs, 
 * so you should have the same loops and line numbers I do:
 * 
 * for d in range 2 to b {         // Init line 10, inc line 21, check condition lines 22-24
 *     for e in range 2 to b {     // Init line 11, inc line 17, check condition lines 18-20
 *         if d * e == b, let f=0  // Evaluated on lines 12-16
 *     }
 * }
 * where register g is just used to evaluate expressions. This means for a given b, the register f is cleared 
 * if b is a composite number (i.e. non-prime).
 * 
 * Then lines 25-26 are if f==0: h += 1 and the remaining lines are if (b==c) { exit } else { GOTO 9 }
 * 
 * So basically, all the program does is set a value for b, increase it by 17 1000 times, and count how many of 
 * those numbers are composite.
 */
public class Day23 {

    public static void main(String[] args) {
        Util.log("\nexecuting part 1");
        List<Instruction> instructions = new ArrayList<>();
        for (String i : input) {
            instructions.add(new Instruction(i));
        }
        Machine machine = new Machine();
        machine.load(instructions);
        machine.run();
        Util.log("executed MUL %d times", machine.mulCount);

        Util.log("\nexecuting part 2");
        instructions.clear();
        for (String i : bSolution) {
            instructions.add(new Instruction(i));
        }
        machine = new Machine();
        machine.load(instructions);
        machine.registers.put("a", 1L);
        machine.run();
        Util.log("h = %d", machine.registers.get("h"));
        Util.log("executed MUL %d times", machine.mulCount);
    }

    private static class Machine {
        List<Instruction> program;
        Map<String, Long> registers = new HashMap<>();
        int pc=0;
        int mulCount = 0;

        public void load(List<Instruction> programToLoad) {
            this.program = programToLoad;
        }

        public void run(int startingPc) {
            pc = startingPc;
            run();
        }

        public void run() {
            long h = getRegister("h");
            while(pc < program.size() && pc >= 0) {
                int preExecPc = pc;

                exec(program.get(pc));

	            if (h != getRegister("h")) {
	                Util.log("%s\t -> %s", program.get(preExecPc), registers);
	                h = getRegister("h");
	            }
                // Util.log("%s\t -> %s", program.get(preExecPc), registers);
            }
        }

        private void exec(Instruction i) {
            switch (i.getOpCode()) {
            case SET:
                setRegister(i.r1, getV2(i));
                ++pc;
                break;

            case SUB:
                setRegister(i.r1, getV1(i) - getV2(i));
                ++pc;
                break;

            case MUL:
                setRegister(i.r1, getV1(i) * getV2(i));
                ++pc;
                ++mulCount;
                break;

            case JNZ:
                if (getV1(i) != 0) {
                    pc += getV2(i);
                } else {
                    ++pc;
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown opcode: "+i.opcode);
            }
        }

        private long getV1(Instruction i) {
            if (i.r1==null) {
                return i.v1;
            }
            return getRegister(i.r1);
        }

        private long getV2(Instruction i) {
            if (i.r2==null) {
                return i.v2;
            }
            return getRegister(i.r2);
        }

        private long getRegister(String r) {
            if (registers.containsKey(r)) {
                return registers.get(r);
            }
            return 0;
        }

        private void setRegister(String r, long value) {
            registers.put(r, value);
        }
    }

    private static class Instruction {
        private OpCode opcode;
        private String r1=null;
        private String r2=null;
        private int v1;
        private int v2;

        private static List<String> registers = Arrays.asList("a","b","c","d","e","f","g","h");

        public Instruction(String code) {
            String[] tokens = code.split(" ");
            opcode = OpCode.valueOf(tokens[0].toUpperCase());

            if (registers.contains(tokens[1])) {
                r1 = tokens[1];
            } else {
                v1 = Integer.parseInt(tokens[1]);
            }

            switch (opcode) {
            case SET:
            case MUL:
            case JNZ:
            case SUB:
                if (registers.contains(tokens[2])) {
                    r2 = tokens[2];
                } else {
                    v2 = Integer.parseInt(tokens[2]);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown opcode: "+opcode);
            }
        }

        public OpCode getOpCode() {
            return opcode;
        }

        @Override
        public String toString() {
            return String.format("{%s %s %s}", opcode, r1==null ? Integer.toString(v1) : r1, r2==null ? Integer.toString(v2) : r2);
        }
    }

    private enum OpCode {
        SET, SUB, MUL, JNZ;
    }

    // It was just counting non prime numbers. from b to c going by +17.

    static String[] bSolution = {
            "set b 57",		// 65",
            "set c b",
            "jnz a 2",
            "jnz 1 5",
            "mul b 100",
            "sub b -100000",
            "set c b",
            "sub c -17000",
            "set a 1",
            "set d c",
            "set g 1",
            "set e 1",
            "sub d 1",
            "sub g 1",
            "jnz g 6",
            "sub e 1",
            "set g a",
            "jnz e 3",
            "sub a -1",
            "set e 2",
            "jnz d -8",
            "sub a 1",
            "set d b",
            "set g a",
            "set f 0",
            "jnz g 3",
            "set g a",
            "sub f -1",
            "sub d 1",
            "sub g 1",
            "jnz d -5",
            "set e f",
            "sub e 1",
            "mul e f",
            "set g b",
            "sub g e",
            "set e 2",
            "jnz e 2",
            "set e 2",
            "sub g 1",
            "sub e 1",
            "jnz g -4",
            "jnz e 3",
            "sub b -17",
            "sub h -1",
            "set d 3",
            "set e a",
            "set g d",
            "jnz g 2",
            "set g d",
            "sub e 1",
            "sub g 1",
            "jnz e -4",
            "set e a",
            "mul g -1",
            "sub e g",
            "sub e d",
            "mul e f",
            "set g b",
            "sub g e",
            "set e d",
            "jnz e 2",
            "set e d",
            "sub g 1",
            "sub e 1",
            "jnz g -4",
            "jnz e 3",
            "sub h -1",
            "jnz 1 9",
            "sub d -1",
            "set g d",
            "sub g a",
            "jnz g 2",
            "jnz 1 4",
            "sub d -1",
            "sub g -1",
            "jnz g -30",
            "set g b",
            "sub g c",
            "jnz g 2",
            "jnz 1 9",
            "sub b -17",
            "sub h -1",
            "set g b",
            "sub g c",
            "jnz g 2",
            "jnz 1 3",
            "sub b -17",
            "jnz 1 -43"
    };

    static String[] input = {
            "set b 57",
            "set c b",
            "jnz a 2",
            "jnz 1 5",
            "mul b 100",
            "sub b -100000",
            "set c b",
            "sub c -17000",
            "set f 1",
            "set d 2",
            "set e 2",
            "set g d",
            "mul g e",
            "sub g b",
            "jnz g 2",
            "set f 0",
            "sub e -1",
            "set g e",
            "sub g b",
            "jnz g -8",
            "sub d -1",
            "set g d",
            "sub g b",
            "jnz g -13",
            "jnz f 2",
            "sub h -1",
            "set g b",
            "sub g c",
            "jnz g 2",
            "jnz 1 3",
            "sub b -17",
            "jnz 1 -23"
        };
}
