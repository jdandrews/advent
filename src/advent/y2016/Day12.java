package advent.y2016;

import java.util.Arrays;
import java.util.List;

import advent.Util;

public class Day12 {

    private enum Opcode {
        CPY, JNZ, INC, DEC;
    }

    private static final List<String> REGISTERS = Arrays.asList("a","b","c","d");

    private static class Op {
        final Opcode opcode;
        final String op1;
        final String op2;

        public Op(Opcode op, String op1, String op2) {
            this.opcode = op;
            this.op1 = op1;
            this.op2 = op2;
        }

        @Override
        public String toString() {
        	String result;

            switch(opcode) {
            case CPY:
            	result = opcode+":"+op1+"<-"+op2;
                break;
            case JNZ:
            	result = opcode+":"+op1 +" ? "+op2;
                break;
            case DEC:
            	result = opcode+":"+op1;
                break;
            case INC:
            	result = opcode+":"+op1;
                break;

            default:
                throw new UnsupportedOperationException("unrecognized: "+opcode);
            }

            return result;
        }
    }

    public static void main(String[] args) {
        String[] program = PROGRAM;

        execute(program);
    }

	private static void execute(String[] program) {
		int pc = 0;
		int step=0;
        int[] registers = new int[4];	// a, b, c, d, and "immediate"

        // problem 2:
        registers[2] = 1;

        while (pc<program.length) {
            Op op = parse(program[pc]);
            // String logMe = op.toString();

            switch(op.opcode) {
            case CPY:
            	if (! REGISTERS.contains(op.op2)) {
            		throw new IllegalArgumentException("Can't copy to a numeric target: "+op.toString());
            	}

            	if (REGISTERS.contains(op.op1)) {
            		registers[getRegisterIndex(op.op2)] = registers[getRegisterIndex(op.op1)];
            	}
            	else {
            		registers[getRegisterIndex(op.op2)] = Integer.valueOf(op.op1);
            	}

                ++pc;
                break;

            case JNZ:
            	int testMe;
            	if (REGISTERS.contains(op.op1)) {
            		testMe = registers[getRegisterIndex(op.op1)];
            	}
            	else {
            		testMe = registers[Integer.valueOf(op.op1)];
            	}

                pc += testMe!=0 ? Integer.valueOf(op.op2) : 1;
                break;

            case DEC:
                --registers[getRegisterIndex(op.op1)];
                ++pc;
                break;

            case INC:
                ++registers[getRegisterIndex(op.op1)];
                ++pc;
                break;

            default:
                throw new UnsupportedOperationException("unrecognized: "+op);
            }

            if (step++%1000000==0) System.out.print(".");
            if (step%100000000==0) System.out.println("");
        }
        Util.log("\n%s\tpc: %d\ta: %d\tb: %d\tc: %d\td: %d", "final state", pc, registers[0], registers[1], registers[2], registers[3]);
	}

	private static int getRegisterIndex(String registerName) {
		return REGISTERS.indexOf(registerName);
	}

    private static Op parse(String string) {
        String[] s = string.split(" ");
        Opcode opcode = Opcode.valueOf(s[0].toUpperCase());

        switch(opcode) {
        case DEC:
        case INC:
        	return new Op(opcode, s[1], "");

        case CPY:
        case JNZ:
        	return new Op(opcode, s[1], s[2]);

        default:
    		throw new UnsupportedOperationException("Unrecognized operation: "+string);
        }
    }

    private static final String[] TEST_PROGRAM = {
    		"cpy 41 a",
    		"inc a",
    		"inc a",
    		"dec a",
    		"jnz a 2",
    		"dec a"
    };

    private static final String[] PROGRAM = {
    		"cpy 1 a",
    		"cpy 1 b",
    		"cpy 26 d",
    		"jnz c 2",
    		"jnz 1 5",
    		"cpy 7 c",
    		"inc d",
    		"dec c",
    		"jnz c -2",
    		"cpy a c",
    		"inc a",
    		"dec b",
    		"jnz b -2",
    		"cpy c b",
    		"dec d",
    		"jnz d -6",
    		"cpy 19 c",
    		"cpy 14 d",
    		"inc a",
    		"dec d",
    		"jnz d -2",
    		"dec c",
    		"jnz c -5",
    };
}
