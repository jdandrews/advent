package advent.y2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent.FileIO;
import advent.Util;

public class Day04 {
    private enum Action {
        SLEEP,
        WAKE,
        BEGIN;
    }

    private static class LogEntry {
        public String guardId;
        public int month;
        public int day;
        public int minute;
        public Action action;
        
        public LogEntry(String id, String mo, String dy, String min, String act) {
            guardId = id;
            month = Integer.valueOf(mo);
            day = Integer.valueOf(dy);
            minute = Integer.valueOf(min);
            switch(act) {
            case ("wakes"):
                action = Action.WAKE;
                break;
            case ("falls"):
                action = Action.SLEEP;
                break;
            case ("begins"):
                action = Action.BEGIN;
                break;
            default:
                throw new UnsupportedOperationException("Unknown action: "+act);
            }
        }
    }

    private static class MinuteLog {
        public int[] log = new int[60];
    }

    public static void main(String[] args) {
        List<String> stringData = FileIO.getFileAsList("src/advent/y2018/Day04.txt");
        List<LogEntry> data = parse(stringData);

        sortData(data);

        Map<String, Integer> guardToMinutes = mapGuardToMinutesSlept(data);

        String sleepyGuard = null;
        int maxMinutes = 0;
        for (Map.Entry<String, Integer> entry : guardToMinutes.entrySet()) {
            if (maxMinutes < entry.getValue()) {
                sleepyGuard = entry.getKey();
                maxMinutes = entry.getValue();
            }
        }

        Map<String, MinuteLog> guardToMinuteLog = mapGuardsToMinuteLogs(data);

        int maxMinute = 0;
        for (int i=0; i<60; ++i) {
            maxMinute = Math.max(maxMinute, guardToMinuteLog.get(sleepyGuard).log[i]);
        }

        for (int i=0; i<60; ++i) {
            if (guardToMinuteLog.get(sleepyGuard).log[i] == maxMinute) {
                Util.log("Sleepy guard %s slept %d minutes at %d; product is %d", sleepyGuard, maxMinute, i, 
                        i * Integer.valueOf(sleepyGuard.substring(1)));
            }
        }

        // strategy 2
        int minuteMax = 0;
        maxMinute = 0;
        sleepyGuard = null;
        for (Map.Entry<String, MinuteLog> entry : guardToMinuteLog.entrySet()) {
            for (int i=0; i<60; ++i) {
                if (minuteMax < entry.getValue().log[i]) {
                    minuteMax = entry.getValue().log[i];
                    maxMinute = i;
                    sleepyGuard = entry.getKey();
                }
            }
        }
        Util.log("Guard %s slept %d minutes at %d; product is %d", sleepyGuard, minuteMax, maxMinute, 
                maxMinute*Integer.valueOf(sleepyGuard.substring(1)));
    }

    private static Map<String, MinuteLog> mapGuardsToMinuteLogs(List<LogEntry> data) {
        Map<String, MinuteLog> result = new HashMap<>();
        int startSleep = 60;
        for (LogEntry entry : data) {
            switch(entry.action) {
            case BEGIN:
                if (!result.containsKey(entry.guardId)) {
                    result.put(entry.guardId, new MinuteLog());
                }
                break;
            case SLEEP:
                startSleep = entry.minute;
                break;
            case WAKE:
                MinuteLog log = result.get(entry.guardId);
                for (int i=startSleep; i<entry.minute; ++i) {
                    ++log.log[i];
                }
                break;
            default:
                throw new UnsupportedOperationException("Action "+entry.action+" unknown");
            }
        }
        return result;
    }

    private static Map<String, Integer> mapGuardToMinutesSlept(List<LogEntry> data) {
        Map<String, Integer> guardToMinutes = new HashMap<>();
        String currentGuard = null;
        int startSleep = 60;
        for (LogEntry entry : data) {
            switch(entry.action) {
            case BEGIN:
                currentGuard = entry.guardId;
                break;
            case SLEEP:
                startSleep = entry.minute;
                break;
            case WAKE:
                if (! guardToMinutes.containsKey(currentGuard)) {
                    guardToMinutes.put(currentGuard, 0);
                }
                guardToMinutes.put(currentGuard, guardToMinutes.get(currentGuard) + entry.minute - startSleep);
                break;
            default:
                throw new UnsupportedOperationException("Action "+entry.action+" unknown");
            }
            if (entry.guardId==null) {
                entry.guardId = currentGuard;
            }
        }
        return guardToMinutes;
    }

    private static void sortData(List<LogEntry> data) {
        Collections.sort(data, new Comparator<LogEntry>() {
            @Override
            public int compare(LogEntry o1, LogEntry o2) {
                if (o1.month==o2.month) {
                    if (o1.day==o2.day) {
                        return (o1.minute > o2.minute ? 1 : (o1.minute < o2.minute) ? -1 : 0);
                    }
                    return (o1.day > o2.day ? 1 : (o1.day < o2.day) ? -1 : 0);
                }
                return (o1.month > o2.month ? 1 : (o1.month < o2.month) ? -1 : 0);
            }
        });
    }

    private static List<LogEntry> parse(List<String> stringData) {
        List<LogEntry> result = new ArrayList<>();
        for (String s : stringData) {
            String[] s1 = s.split(" ");
            String[] s2 = s1[0].split("-");
            String id = (s.contains("#") ? s1[3] : null);
            result.add(new LogEntry(id, s2[1], s2[2], s1[1].substring(3, 5), id==null ? s1[2] : s1[4]));
        }
        return result;
    }

    private static final List<String> SAMPLE = new ArrayList<String>() {{
        add("[1518-11-01 00:00] Guard #10 begins shift");
        add("[1518-11-01 00:05] falls asleep");
        add("[1518-11-01 00:25] wakes up");
        add("[1518-11-01 00:30] falls asleep");
        add("[1518-11-01 00:55] wakes up");
        add("[1518-11-01 23:58] Guard #99 begins shift");
        add("[1518-11-02 00:40] falls asleep");
        add("[1518-11-02 00:50] wakes up");
        add("[1518-11-03 00:05] Guard #10 begins shift");
        add("[1518-11-03 00:24] falls asleep");
        add("[1518-11-03 00:29] wakes up");
        add("[1518-11-04 00:02] Guard #99 begins shift");
        add("[1518-11-04 00:36] falls asleep");
        add("[1518-11-04 00:46] wakes up");
        add("[1518-11-05 00:03] Guard #99 begins shift");
        add("[1518-11-05 00:45] falls asleep");
        add("[1518-11-05 00:55] wakes up");
    }};
}
