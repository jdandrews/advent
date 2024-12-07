package advent.y2015;

import java.util.List;

import advent.FileIO;

public class Day23 {
    private static class Computer {
        List<String> program;
        int pc = 0;
        int a = 0;
        int b = 0;

        public Computer(List<String> program) {
            this.program = program;
        }

        public void runProgram() {
            do {
                String opCode = program.get(pc).substring(0, 3);
                String args = program.get(pc).substring(3).trim();

                switch(opCode) {
                case "hlf": handleHlf(args); break;
                case "tpl": handleTpl(args); break;
                case "inc": handleInc(args); break;
                case "jmp": handleJmp(args); break;
                case "jie": handleJie(args); break;
                case "jio": handleJio(args); break;
                default: throw new UnsupportedOperationException(program.get(pc));
                }
            } while (pc >= 0 && pc < program.size());

        }

        private void handleHlf(String args) {
            switch(args) {
            case "a": a /= 2; break;
            case "b": b /= 2; break;
            default: throw new UnsupportedOperationException(args);
            }
            ++pc;
        }

        private void handleTpl(String args) {
            switch(args) {
            case "a": a *= 3; break;
            case "b": b *= 3; break;
            default: throw new UnsupportedOperationException(args);
            }
            ++pc;
        }

        private void handleInc(String args) {
            switch(args) {
            case "a": ++a; break;
            case "b": ++b; break;
            default: throw new UnsupportedOperationException(args);
            } 
            ++pc;
        }

        private void handleJmp(String args) {
            pc += Integer.valueOf(args);
        }

        private void handleJie(String argStr) {
            String[] args = argStr.split(",");
            switch(args[0].trim()) {
            case "a":
                if (a%2 == 0) {
                    handleJmp(args[1].trim());
                } else {
                    ++pc;
                }
                break;
            case "b":
                if (b%2 == 0) {
                    handleJmp(args[1].trim());
                } else {
                    ++pc;
                }
                break;
            default: throw new UnsupportedOperationException(argStr);
            }
        }

        private void handleJio(String argStr) {
            String[] args = argStr.split(",");
            switch(args[0].trim()) {
            case "a":
                if (a == 1) {
                    handleJmp(args[1].trim());
                } else {
                    ++pc;
                }
                break;
            case "b":
                if (b == 1) {
                    handleJmp(args[1].trim());
                } else {
                    ++pc;
                }
                break;
            default: throw new UnsupportedOperationException(argStr);
            }
        }

        public String getState() {
            StringBuilder result = new StringBuilder();
            result.append("pc = ").append(pc)
                    .append(" (program size = ").append(program.size()).append(")")
                    .append("\n a = ").append(a)
                    .append("\n b = ").append(b);

            return result.toString();
        }
    }

    public static void main(String[] args) {
        List<String> program = FileIO.getFileAsList("src/advent/y2015/Day23.txt");
        log("---------- Part 1-------------------");
        Computer computer = new Computer(program);
        computer.runProgram();
        log(computer.getState());
        log("---------- Part 2-------------------");
        computer = new Computer(program);
        computer.a = 1;
        computer.runProgram();
        log(computer.getState());
        log("------------------------------------");
    }
    
    private static void log(String s) {
            System.out.println(s);
    }
}

