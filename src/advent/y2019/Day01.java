package advent.y2019;

public class Day01 {
    public static void main(String[] args) {
        int sum = 0;
        for (int mass : masses) {
            int fuel = getFuel(mass);

            sum += fuel;
        }
        System.out.println(String.format("Part 1: fuel required: %d", sum));

        sum = 0;
        for (int mass : TEST) {
            int fuel = getFuel(mass);
            System.out.println(fuel);
            for (int moreFuel = getFuel(fuel); moreFuel > 0; moreFuel = getFuel(moreFuel)) {
                System.out.println(moreFuel);
                fuel += moreFuel;
            }

            sum += fuel;
        }
        System.out.println(String.format("Part 2 test: fuel required: %d", sum));

        sum = 0;
        for (int mass : masses) {
            int fuel = getFuel(mass);
            for (int moreFuel = getFuel(fuel); moreFuel > 0; moreFuel = getFuel(moreFuel)) {
                fuel += moreFuel;
            }

            sum += fuel;
        }
        System.out.println(String.format("Part 2: fuel required: %d", sum));
    }

    private static int getFuel(int mass) {
        return mass/3 - 2;
    }

    private static int TEST[] = {1969};
    private static int masses[] = {
            50962, 126857, 127476, 136169, 62054, 116866, 123235, 147126, 146767, 140795, 54110, 106452, 98413,
            114925, 141914, 54864, 120093, 118166, 81996, 143924, 145941, 96950, 126488, 85764, 111438, 63278,
            147558, 128982, 123857, 58646, 80757, 98260, 97143, 136609, 99349, 63167, 142129, 144645, 97212,
            70162, 98044, 125931, 103859, 67890, 67797, 79263, 134255, 130303, 73367, 103091, 97315, 74892,
            82311, 51157, 79802, 138401, 108423, 63111, 61441, 102862, 53184, 125543, 147413, 117762, 106771,
            115805, 66424, 85851, 53101, 82736, 136768, 130745, 140135, 101770, 55349, 143419, 60108, 84990,
            91544, 75240, 92709, 134369, 140901, 59910, 63641, 54966, 104671, 71950, 60358, 127289, 147362,
            70799, 82870, 108630, 53450, 106888, 129843, 53227, 58758, 137751
    };
}
