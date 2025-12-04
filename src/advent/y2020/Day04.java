package advent.y2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day04 {

    private static final List<String> SAMPLE = Arrays.asList(
            "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd",
            "byr:1937 iyr:2017 cid:147 hgt:183cm",
            "",
            "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884",
            "hcl:#cfa07d byr:1929",
            "",
            "hcl:#ae17e1 iyr:2013",
            "eyr:2024",
            "ecl:brn pid:760753108 byr:1931",
            "hgt:179cm",
            "",
            "hcl:#cfa07d eyr:2025 pid:166559648",
            "iyr:2011 ecl:brn hgt:59in");

    private static final boolean FULLY_VALIDATE = true;

    public static void main(String[] args) {
        List<String> puzzle = FileIO.getFileAsList("src/advent/y2020/Day04.txt");

        Util.log("part 1 SAMPLE found %d valid passports", findValid(SAMPLE, ! FULLY_VALIDATE));
        Util.log("part 1 puzzle found %d valid passports", findValid(puzzle, ! FULLY_VALIDATE));

        Util.log("----------------");

        Util.log("part 2 puzzle found %d valid passports", findValid(puzzle, FULLY_VALIDATE));
    }

    private static final List<String> REQUIRED_FIELDS =
            Arrays.asList("byr:", "iyr:", "eyr:", "hgt:", "hcl:", "ecl:", "pid:");

    private static int findValid(List<String> batch, boolean fullyValidate) {
        int validPassports = 0;
        List<String> passports = parse(batch);

        for (String passport : passports) {
            boolean valid = true;
            for (String fieldname : REQUIRED_FIELDS) {
                if (! passport.contains(fieldname)) {
                    valid = false;
                    break;
                }
            }
            if (valid && fullyValidate) {
                valid = validateFields(passport);
            }
            if (valid) {
                ++validPassports;
            }
        }

        return validPassports;
    }

    private static boolean validateFields(String passport) {
        boolean valid = true;
        String[] fields = passport.split(" ");
        for (String field : fields) {
            valid = switch(field.substring(0, 3)) {
            case "byr" -> validateByr(field.substring(4));
            case "iyr" -> validateIyr(field.substring(4));
            case "eyr" -> validateEyr(field.substring(4));
            case "hgt" -> validateHgt(field.substring(4));
            case "hcl" -> validateHcl(field.substring(4));
            case "ecl" -> validateEcl(field.substring(4));
            case "pid" -> validatePid(field.substring(4));
            case "cid" -> valid;
            default -> throw new UnsupportedOperationException("Unrecoginized: " + field);
            };

            if (! valid) {
                break;
            }
        }
        return valid;
    }

    private static boolean validatePid(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return value.length() == 9;
    }

    static List<String> validEyeColors = Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    private static boolean validateEcl(String value) {
        return validEyeColors.contains(value);
    }

    private static boolean validateHcl(String value) {
        try {
            Integer.parseInt(value.substring(1), 16);
        } catch (NumberFormatException e) {
            return false;
        }
        return value.startsWith("#") && value.length() == 7;
    }

    private static boolean validateHgt(String value) {
        if (value.endsWith("cm")) {
            int h = Integer.parseInt(value.substring(0, value.indexOf("cm")));
            return h >= 150 && h <= 193;
        } else if (value.endsWith("in")) {
            int h = Integer.parseInt(value.substring(0, value.indexOf("in")));
            return h >= 59 && h <= 76;
        }
        return false;
    }

    private static boolean validateEyr(String value) {
        return validateYear(value, 2020, 2030);
    }

    private static boolean validateIyr(String value) {
        return validateYear(value, 2010, 2020);
    }

    private static boolean validateByr(String value) {
        return validateYear(value, 1920, 2002);
    }

    private static boolean validateYear(String value, int min, int max) {
        try {
            int year = Integer.parseInt(value);
            return year >= min && year <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static List<String> parse(List<String> batch) {
        List<String> passports = new ArrayList<>();
        String passport = "";
        for (String line : batch) {
            if ("".equals(line)) {
                passports.add(passport.trim());
                passport = "";
            }
            else {
                passport = passport + " " + line;
            }
        }
        if (! "".equals(passport.trim())) {
            passports.add(passport.trim());
        }

        return passports;
    }

}
