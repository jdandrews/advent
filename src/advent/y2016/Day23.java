package advent.y2016;

import java.util.Arrays;
import java.util.List;

import advent.Util;

public class Day23 {

    private enum Opcode {
        CPY, JNZ, INC, DEC, TGL;
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
            case TGL:
                result = opcode+":"+op1;

            default:
                throw new UnsupportedOperationException("unrecognized: "+opcode);
            }

            return result;
        }
    }

    public static void main(String[] args) {
        Util.log("part 1 TEST_PROGRAM\n");
        String[] program = TEST_PROGRAM;
        execute(program);

        Util.log("\n\n-----\npart 1 PROGRAM\n");
        execute(PROGRAM, 7);

        Util.log("\n\n-----\npart 2 PROGRAM\n");
        execute(PROGRAM, 12);
    }

    private static void execute(String[] program) {
        execute(program, 0);
    }

    private static void execute(String[] program, int a) {
        int pc = 0;
        int step=0;
        int[] registers = new int[4];	// a, b, c, d, and "immediate"

        registers[0] = a;

        while (pc<program.length) {
            Op op = parse(program[pc]);

            switch(op.opcode) {
            case CPY:
                if (REGISTERS.contains(op.op2)) {
                    registers[getRegisterIndex(op.op2)] = decode(op.op1, registers);
                }

                ++pc;
                break;

            case JNZ:
                pc += decode(op.op1, registers) != 0 ? decode(op.op2, registers) : 1;
                break;

            case DEC:
                --registers[getRegisterIndex(op.op1)];
                ++pc;
                break;

            case INC:
                ++registers[getRegisterIndex(op.op1)];
                ++pc;
                break;

            case TGL:
                int offset = decode(op.op1, registers);

                if (pc + offset < program.length) {
                    Op toggleMe = parse(program[pc + offset]);
                    switch (toggleMe.opcode) {
                    case DEC:
                        program[pc + offset] = "inc" + program[pc + offset].substring(3);
                        break;
                    case INC:
                        program[pc + offset] = "dec" + program[pc + offset].substring(3);
                        break;
                    case TGL:
                        program[pc + offset] = "inc" + program[pc + offset].substring(3);
                        break;
                    case CPY:
                        program[pc + offset] = "jnz" + program[pc + offset].substring(3);
                        break;
                    case JNZ:
                        program[pc + offset] = "cpy" + program[pc + offset].substring(3);
                        break;
                    default:
                        throw new UnsupportedOperationException("unrecognized: " + toggleMe.opcode);
                    }
                }
                ++pc;
                break;

            default:
                throw new UnsupportedOperationException("unrecognized: "+op);
            }

            // Util.log("\n%s \tpc: %7d a: %7d b: %7d c: %7d d: %7d", "state", pc, registers[0], registers[1], registers[2], registers[3]);
            if (step++%1000000==0) System.out.print(".");
            if (step%100000000==0) System.out.println("");
        }
        Util.log("\n%s \tpc: %7d a: %7d b: %7d c: %7d d: %7d", "final state", pc, registers[0], registers[1], registers[2], registers[3]);
    }

    private static int decode(String op, int[] registers) {
        if (REGISTERS.contains(op)) {
            return registers[getRegisterIndex(op)];
        }
        return Integer.parseInt(op);
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
        case TGL:
            return new Op(opcode, s[1], "");

        case CPY:
        case JNZ:
            return new Op(opcode, s[1], s[2]);

        default:
            throw new UnsupportedOperationException("Unrecognized operation: "+string);
        }
    }

    private static final String[] TEST_PROGRAM = {
            "cpy 2 a",
            "tgl a",
            "tgl a",
            "tgl a",
            "cpy 1 a",
            "dec a",
            "dec a"
    };

    private static final String[] PROGRAM = {
            "cpy a b",
            "dec b",
            "cpy a d",
            "cpy 0 a",
            "cpy b c",
            "inc a",
            "dec c",
            "jnz c -2",
            "dec d",
            "jnz d -5",
            "dec b",
            "cpy b c",
            "cpy c d",
            "dec d",
            "inc c",
            "jnz d -2",
            "tgl c",
            "cpy -16 c",
            "jnz 1 c",
            "cpy 84 c",
            "jnz 71 d",
            "inc a",
            "inc d",
            "jnz d -2",
            "inc c",
            "jnz c -5"
    };
}
