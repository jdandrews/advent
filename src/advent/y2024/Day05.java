package advent.y2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import advent.FileIO;

public class Day05 {
    record Rule(int before, int after) {
    }

    record PageList(List<Integer> pages) {
        public int middleValue() {
            if (pages.size() % 2 == 0) {
                throw new IllegalStateException("no middle values; there are " + pages.size() + " pages in the list.");
            }
            return pages.get(pages.size() / 2);
        }
    }

    public static void main(String[] args) {
        Day05 f = new Day05();
        List<PageList> pageLists = f.parsePages(Arrays.asList(SAMPLE));
        List<Rule> rules = f.parseRules(Arrays.asList(SAMPLE));

        int sumOfMiddlePages = getValidListsSum(f, pageLists, rules);
        f.log("Sum of middle pages in the sample is " + sumOfMiddlePages);
        
        // part 1
        List<String> part1list = FileIO.getFileAsList("src/advent/y2024/Day05.txt");
        pageLists = f.parsePages(part1list);
        rules = f.parseRules(part1list);
        
        sumOfMiddlePages = getValidListsSum(f, pageLists, rules);
        f.log("Sum of middle pages for part 1 is " + sumOfMiddlePages);
        
        // part 2
        sumOfMiddlePages = 0;
        for (PageList pageList : pageLists) {
            if (! f.isCorrectlyOrdered(pageList, rules)) {
                PageList reorderedList = f.correctlyOrder(pageList, rules);
                sumOfMiddlePages += reorderedList.middleValue();
            }
        }
        f.log("Sum of middle pages for part 2 is " + sumOfMiddlePages);
    }

    private PageList correctlyOrder(final PageList pageList, final List<Rule> rules) {
        List<Integer> pages = new ArrayList<>(pageList.pages());
        pages.sort(new Comparator<>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                for (Rule rule : rules) {
                    if (rule.before() == o1 && rule.after() == o2) {
                        return -1;
                    }
                    if (rule.before() == o2 && rule.after() == o1) {
                        return 1;
                    }
                }
                return 0;
            }
            
        });

        return new PageList(pages);
    }

    private static int getValidListsSum(Day05 f, List<PageList> pageLists, List<Rule> rules) {
        int sumOfMiddlePages = 0;
        for (PageList list : pageLists) {
            if (f.isCorrectlyOrdered(list, rules)) {
                sumOfMiddlePages += list.middleValue();
            }
        }
        return sumOfMiddlePages;
    }

    private void log(String s) {
        System.out.println(s);
    }

    private boolean isCorrectlyOrdered(PageList list, List<Rule> rules) {
        for (int i = 0; i < list.pages().size(); ++i) {
            int page = list.pages().get(i);
            for (int j = i + 1; j < list.pages().size(); ++j) {
                int followingPage = list.pages().get(j);
                for (int k = 0; k < rules.size(); ++k) {
                    Rule rule = rules.get(k);

                    if (rule.before() == followingPage && rule.after() == page) {
                        return false;
                    }
                }
            }
            for (int j = 0; j < i; ++j) {
                int precedingPage = list.pages().get(j);
                for (int k = 0; k < rules.size(); ++k) {
                    Rule rule = rules.get(k);

                    if (rule.before() == page && rule.after() == precedingPage) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private List<Rule> parseRules(List<String> stringList) {
        List<Rule> result = new ArrayList<>();
        for (String s : stringList) {
            if (!s.contains("|"))
                break;

            String[] parts = s.split("\\|");
            result.add(new Rule(Integer.valueOf(parts[0]), Integer.valueOf(parts[1])));
        }

        return result;
    }

    private List<PageList> parsePages(List<String> stringList) {
        List<PageList> result = new ArrayList<>();
        for (String s : stringList) {
            if (!s.contains(","))
                continue;

            String[] pages = s.split(",");
            List<Integer> pageList = new ArrayList<>();
            for (String page : pages) {
                pageList.add(Integer.valueOf(page));
            }
            result.add(new PageList(pageList));
        }

        return result;
    }

    private static final String[] SAMPLE = {
            "47|53",
            "97|13",
            "97|61",
            "97|47",
            "75|29",
            "61|13",
            "75|53",
            "29|13",
            "97|29",
            "53|29",
            "61|53",
            "97|53",
            "61|29",
            "47|13",
            "75|47",
            "97|75",
            "47|61",
            "75|61",
            "47|29",
            "75|13",
            "53|13",
            "",
            "75,47,61,53,29",
            "97,61,53,29,13",
            "75,29,13",
            "75,97,47,61,53",
            "61,13,29",
            "97,13,75,29,47" };
}