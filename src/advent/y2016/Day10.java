package advent.y2016;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.Util;

public class Day10 {
    enum Action {
        VALUE, GIVE
    }
    private static class Command {
        Action command;
        int bot;
        int value;
        int loBot;
        int hiBot;
        public Command(int b, int v) {
            command = Action.VALUE;
            bot = b;
            value = v;
        }
        public Command(int b, int lo, int hi) {
            command = Action.GIVE;
            bot = b;
            loBot = lo;
            hiBot = hi;
        }
        @Override
        public String toString() {
            return command+" {"+bot+":"+loBot+","+hiBot+"}";
        }
    }

    private static class Bot {
        int id;
        int value1=Integer.MAX_VALUE;
        int value2=Integer.MAX_VALUE;
        public Bot(int botId) {
            this.id = botId;
        }
        public void give(int value) {
            if (value1==Integer.MAX_VALUE) {
                value1 = value;
            } else if (value > value1) {
                value2 = value;
            } else {
                value2 = value1;
                value1 = value;
            }
        }
        @Override
        public String toString() {
            return "bot "+id+ " {"+(value1<Integer.MAX_VALUE ? value1+" " : " " )+
                    (value2<Integer.MAX_VALUE ? value2+" " : "")+"}";
        }
        public boolean has2chips() {
            return value2<Integer.MAX_VALUE;
        }
        public void clearChips() {
            value1 = Integer.MAX_VALUE;
            value2 = Integer.MAX_VALUE;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> commandStrings = Arrays.asList(
        		"value 5 goes to bot 2", "bot 2 gives low to bot 1 and high to bot 0",
        	    "value 3 goes to bot 1", "bot 1 gives low to output 1 and high to bot 0",
        	    "bot 0 gives low to output 2 and high to output 0", "value 2 goes to bot 2");
        processCommands(commandStrings);
        Util.log("---------- end test ---------");

        commandStrings = Util.readInput("2016", "Day10.txt");
        processCommands(commandStrings);
    }

	private static void processCommands(List<String> commandStrings) {
		List<Command> commands = new ArrayList<>();
        Map<Integer, Bot> bots = new HashMap<>();
        int[] slots1to3 = {Integer.MIN_VALUE, -1, -2};
        int[] values1to3 = new int[3];

        for (String commandString : commandStrings) {
            Command command = parseCommand(commandString);
            switch(command.command) {
            case GIVE:
                commands.add(command);
                break;
            case VALUE:
                if (! bots.containsKey(command.bot)) {
                    bots.put(command.bot, new Bot(command.bot));
                }
                bots.get(command.bot).give(command.value);
                break;
            default:
                throw new IllegalArgumentException("I'm confused");
            }
        }

        Util.log("%s", bots.values());

        for (Bot activeBot = findActiveBot(bots); activeBot!=null; activeBot = findActiveBot(bots)) {
            Command command = findActiveCommand(activeBot.id, commands);
            Util.log("%s %s", activeBot, command);
            if (command.loBot>=0) {
                Bot bot = bots.get(command.loBot);
                if(bot==null) {
                    bot = new Bot(command.loBot);
                    bots.put(bot.id, bot);
                }
                bot.give(activeBot.value1);
            }
            if (command.hiBot>=0) {
                Bot bot = bots.get(command.hiBot);
                if(bot==null) {
                    bot = new Bot(command.hiBot);
                    bots.put(bot.id, bot);
                }
                bot.give(activeBot.value2);
            }
            if (activeBot.value1==17 && activeBot.value2==61) {
                Util.log("-----> result bot is %d", activeBot.id);
            }

            for (int i=0; i<3; ++i) {
	            if (command.loBot==slots1to3[i]) values1to3[i] = activeBot.value1;
	            if (command.hiBot==slots1to3[i]) values1to3[i] = activeBot.value2;
            }

            activeBot.clearChips();
        }
        Util.log("-----> result product is %d * %d * %d = %d", values1to3[0], values1to3[1], values1to3[2],
        		values1to3[0] * values1to3[1] * values1to3[2]);
	}

    private static Command findActiveCommand(int botId, List<Command> commands) {
        Command candidate = null;
        for (Command command : commands) {
            if (command.bot==botId) {
                if (candidate!=null) {
                    throw new IllegalStateException("2 commands for bot "+botId);
                }
                candidate = command;
            }
        }
        return candidate;
    }

    private static Bot findActiveBot(Map<Integer, Bot> bots) {
        for (Bot bot : bots.values()) {
            if (bot.has2chips()) {
                return bot;
            }
        }
        return null;
    }

    private static Command parseCommand(String commandString) {
        Command result;
        String[] commandTokens = commandString.split(" ");
        if (commandTokens[0].equals("value")) {
            int bot = Integer.parseInt(commandTokens[5]);
            int value = Integer.parseInt(commandTokens[1]);
            result = new Command(bot, value);
        } else if (commandString.startsWith("bot ")) {
            int bot = Integer.parseInt(commandTokens[1]);
            int loBot = getBot(commandTokens[5], commandTokens[6]);
            int hiBot = getBot(commandTokens[10], commandTokens[11]);
            result = new Command(bot, loBot, hiBot);
        } else {
            throw new IllegalArgumentException("Unrecognized command: "+commandString);
        }
        return result;
    }

    private static int getBot(String botOrOutput, String chipId) {
        int sign = botOrOutput.equals("bot") ? 1 : -1;
        int id = Integer.parseInt(chipId);

        if (id==0 && sign < 0)
        	return Integer.MIN_VALUE;
        return sign * id ;
    }
}
