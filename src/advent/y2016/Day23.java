package advent.y2016;

import java.util.Arrays;
import java.util.List;

import advent.Util;

public class Day23 {

    private enum Opcode {
        CPY, JNZ, INC, DEC, TGL,
        ADD, MUL, NOP;
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
                break;
                // new opcodes added to speed things up
            case ADD:
                result = opcode+":"+op1 +" + "+op2 + "->" + op1;
                break;
            case MUL:
                result = opcode+":"+op1 +" * "+op2 + "->" + op1;
                break;
            case NOP:
                result = opcode.toString();
                break;

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
        execute(Arrays.copyOf(PROGRAM, PROGRAM.length), 7, false);

        Util.log("\n\n-----\npart 2 PROGRAM\n");
        execute(PROGRAM, 12, false);
    }

    private static void execute(String[] program) {
        execute(program, 0, true);
    }

    private static void execute(String[] program, int a, boolean logProgress) {
        int pc = 0;
        int step=0;
        long[] registers = new long[4];         // a, b, c, d, and "immediate"

        registers[0] = a;

        if (logProgress) {
            Util.log("\n%s \tpc: %7d a: %7d b: %7d c: %7d d: %7d op[%d]=%s", "state",
                    pc, registers[0], registers[1], registers[2], registers[3], pc, pc < program.length ? program[pc] : "");
        }

        while (pc<program.length) {
            Op op = parse(program[pc]);

            switch(op.opcode) {
            case CPY:
                if (REGISTERS.contains(op.op2)) {
                    registers[getRegisterIndex(op.op2)] = decode(op.op1, registers);
                } else {
                    Util.log("CPY target %s not found at %d; %s", op.op2, pc, op);
                }

                ++pc;
                break;

            case JNZ:
                pc += decode(op.op1, registers) != 0 ? decode(op.op2, registers) : 1;
                if (pc < program.length && program[pc].equals("nop")){
                    Util.log("ERROR--jump into replaced code at %d", pc);
                }
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
                int offset = (int)decode(op.op1, registers);

                if (pc + offset < program.length) {
                    Op toggleMe = parse(program[pc + offset]);
                    switch (toggleMe.opcode) {
                    // one-argument instructions
                    case DEC:
                        program[pc + offset] = "inc" + program[pc + offset].substring(3);
                        break;
                    case INC:
                        program[pc + offset] = "dec" + program[pc + offset].substring(3);
                        break;
                    case TGL:
                        program[pc + offset] = "inc" + program[pc + offset].substring(3);
                        break;
                        // 2-argument instructions
                    case CPY:
                        program[pc + offset] = "jnz" + program[pc + offset].substring(3);
                        break;
                    case JNZ:
                        program[pc + offset] = "cpy" + program[pc + offset].substring(3);
                        break;
                    default:
                        throw new UnsupportedOperationException("unrecognized: " + toggleMe.opcode);
                    }
                } else {
                    Util.log("--- no-op: tgl %d is past the end of the program of length %d.", offset, program.length);
                    logState(program, pc, registers) ;
                    Util.log("---");
                }
                ++pc;
                break;

            case NOP:
                ++pc;
                break;

            case ADD:
                registers[getRegisterIndex(op.op1)] += registers[getRegisterIndex(op.op2)];
                registers[getRegisterIndex(op.op2)] = 0;
                ++pc;
                break;

            case MUL:
                registers[getRegisterIndex(op.op1)] *= registers[getRegisterIndex(op.op2)];
                registers[getRegisterIndex(op.op2)] = 0;
                ++pc;
                break;

            default:
                throw new UnsupportedOperationException("unrecognized: "+op);
            }

            if (logProgress) {
                logState(program, pc, registers);
            }
            if (++step%10000000==0) System.out.print(".");
            if (step%1000000000==0) System.out.println("");
        }
        Util.log("\n%s \tpc: %7d a: %7d b: %7d c: %7d d: %7d", "final state", pc, registers[0], registers[1], registers[2], registers[3]);
    }

    private static void logState(String[] program, int pc, long[] registers) {
        Util.log("%s \tpc: %7d a: %7d b: %7d c: %7d d: %7d op[%d]=%s", "state",
                pc, registers[0], registers[1], registers[2], registers[3], pc, pc < program.length ? program[pc] : "");
    }

    private static long decode(String op, long[] registers) {
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
        case ADD:
        case MUL:
            return new Op(opcode, s[1], s[2]);

        case NOP:
            return new Op(opcode, "", "");

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
            "cpy a b",  //      0
            "dec b",    //      1
            "cpy a d",  //      2
            "cpy 0 a",  //      3
            "cpy b c",  //      4
            "add a c",  // inc a        5
            "nop",      // dec c        6
            "nop",      // jnz c -2     7
            "dec d",    //      8
            "jnz d -5", //      9
            "dec b",
            "cpy b c",
            "cpy c d",
            "add c d",  // "dec d",
            "nop",      // "inc c",
            "nop",      // "jnz d -2",
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
