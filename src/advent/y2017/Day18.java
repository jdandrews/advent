package advent.y2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import advent.Util;

public class Day18 {

    public static void main(String[] args) {
        if (args.length==0) {
            throw new UnsupportedOperationException("the deadlock detection is broken");
        }

        Util.log("\nexecuting sample run");
        List<Instruction> instructions = new ArrayList<>();
        for (String i : sample) {
            instructions.add(new Instruction(i));
        }
        Machine machine = new Machine(0L);
        machine.setTarget(new MessagePump(machine));
        machine.load(instructions);
        machine.run();

        Util.log("\nexecuting part 1");
        instructions = new ArrayList<>();
        for (String i : input) {
            instructions.add(new Instruction(i));
        }
        machine = new Machine(0L);
        machine.setTarget(new MessagePump(machine));
        machine.load(instructions);
        machine.run();

        Util.log("\nexecuting part 2");
        instructions = new ArrayList<>();
        for (String i : input) {
            instructions.add(new Instruction(i));
        }
        Machine machine0 = new Machine(0L);
        Machine machine1 = new Machine(1L);
        machine0.setTarget(machine1);
        machine1.setTarget(machine0);

        machine0.load(instructions);
        machine1.load(instructions);

        new Thread(machine0).start();
        new Thread(machine1).start();
    }

    private static interface MessageSink {
        boolean isBlocked();
        void send(long message);
    }

    private static class MessagePump implements MessageSink {
        private MessageSink target;

        @Override
        public boolean isBlocked() {
            return true;
        }

        @Override
        public void send(long message) {
            target.send(message);
        }

        public MessagePump (MessageSink sink) {
            this.target = sink;
        }

    }

    private static class Machine implements MessageSink, Runnable {
        List<Instruction> program;
        Map<String, Long> registers = new HashMap<>();
        int pc=0;
        long startingP;
        int messageCount = 0;
        BlockingQueue<Long> messages = new LinkedBlockingQueue<>();
        private MessageSink target;
        boolean blocked = false;

        public Machine(long id) {
            setRegister("p", id);
            startingP = id;
        }

        public void setTarget(MessageSink messageTarget) {
            target = messageTarget;
        }

        @Override
        public void send(long message) {
            messages.offer(message);
        }

        @Override
        public boolean isBlocked() {
            return blocked;
        }

        public void load(List<Instruction> programToLoad) {
            this.program = programToLoad;
        }

        @Override
        public void run() {
            while(pc < program.size() && pc >= 0) {
                exec(program.get(pc));
            }
        }

        private void exec(Instruction i) {
            switch (i.getOpCode()) {
            case SND:
                if (target!=null) {
                    target.send(getV1(i));
                    Util.log("machine %d send %d messages", startingP, ++messageCount);
                } else {
                    throw new IllegalStateException("no message sink");
                }
                ++pc;
                break;

            case RCV:
                if (messages.isEmpty() && target != null && target.isBlocked()) {
                    Util.log("deadlock; exiting.");
                    pc = program.size();

                } else if (target != null) {
                    blocked = true;
                    long message;
                    try {
                        message = messages.poll(5, TimeUnit.SECONDS);
                    } catch (@SuppressWarnings("unused") InterruptedException | NullPointerException e) {
                        pc = -1;        // terminate the machine
                        break;
                    }
                    blocked = false;
                    // Util.log("machine %d got message %d", startingP, message);
                    setRegister(i.r1, message);

                } else {
                    throw new IllegalStateException("no message sink");
                }

                ++pc;
                break;

            case SET:
                setRegister(i.r1, getV2(i));
                ++pc;
                break;

            case ADD:
                setRegister(i.r1, getRegister(i.r1) + getV2(i));
                ++pc;
                break;

            case MUL:
                setRegister(i.r1, getRegister(i.r1) * getV2(i));
                ++pc;
                break;

            case MOD:
                setRegister(i.r1, getRegister(i.r1) % getV2(i));
                ++pc;
                break;

            case JGZ:
                if (getV1(i) > 0) {
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

        private static List<String> registers = Arrays.asList("a","b","f","i","p");

        public Instruction(String code) {
            String[] tokens = code.split(" ");
            opcode = OpCode.valueOf(tokens[0].toUpperCase());

            if (registers.contains(tokens[1])) {
                r1 = tokens[1];
            } else {
                v1 = Integer.parseInt(tokens[1]);
            }

            switch (opcode) {
            case SND:
            case RCV:
                break;
            case SET:
            case ADD:
            case MUL:
            case MOD:
            case JGZ:
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
    private static enum OpCode {
        SND, SET, ADD, MUL, MOD, RCV, JGZ;
    }

    static String[] sample = {
            "set a 1",
            "add a 2",
            "mul a a",
            "mod a 5",
            "snd a",
            "set a 0",
            "rcv a",
            "jgz a -1",
            "set a 1",
            "jgz a -2"
    };

    static String[] input = {
            "set i 31",
            "set a 1",
            "mul p 17",
            "jgz p p",
            "mul a 2",
            "add i -1",
            "jgz i -2",
            "add a -1",
            "set i 127",
            "set p 316",
            "mul p 8505",
            "mod p a",
            "mul p 129749",
            "add p 12345",
            "mod p a",
            "set b p",
            "mod b 10000",
            "snd b",
            "add i -1",
            "jgz i -9",
            "jgz a 3",
            "rcv b",
            "jgz b -1",
            "set f 0",
            "set i 126",
            "rcv a",
            "rcv b",
            "set p a",
            "mul p -1",
            "add p b",
            "jgz p 4",
            "snd a",
            "set a b",
            "jgz 1 3",
            "snd b",
            "set f 1",
            "add i -1",
            "jgz i -11",
            "snd a",
            "jgz f -16",
            "jgz a -19"
    };
}
