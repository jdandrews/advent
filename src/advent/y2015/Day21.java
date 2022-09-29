package advent.y2015;

import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day21 {
    private static final int BOSS_HP = 104;
    private static final int BOSS_DAMAGE = 8;
    private static final int BOSS_ARMOR = 1;

    private static final int MY_HP = 100;
    
    private static class Gear {
        public String name;
        public int cost;
        public int damage;
        public int armor;
        public Gear(String n, int c, int d, int a) {
            name = n;
            cost = c;
            damage = d;
            armor = a;
        }
    }

    public static List<Gear> weapons = new ArrayList<Gear>() {{
        add(new Gear("Dagger",      8, 4, 0));
        add(new Gear("Shortsword", 10, 5, 0));
        add(new Gear("Warhammer",  25, 6, 0));
        add(new Gear("Longsword",  40, 7, 0));
        add(new Gear("Greataxe",   74, 8, 0));
    }};

    public static List<Gear> armors = new ArrayList<Gear>() {{
        add(new Gear("none",          0,     0,       0));
        add(new Gear("Leather",      13,     0,       1));
        add(new Gear("Chainmail",    31,     0,       2));
        add(new Gear("Splintmail",   53,     0,       3));
        add(new Gear("Bandedmail",   75,     0,       4));
        add(new Gear("Platemail",   102,     0,       5));
    }};

    public static List<Gear> rings = new ArrayList<Gear>() {{
        add(new Gear("none a",        0,     0,       0));
        add(new Gear("none b",        0,     0,       0));
        add(new Gear("Damage +1",    25,     1,       0));
        add(new Gear("Damage +2",    50,     2,       0));
        add(new Gear("Damage +3",   100,     3,       0));
        add(new Gear("Defense +1",   20,     0,       1));
        add(new Gear("Defense +2",   40,     0,       2));
        add(new Gear("Defense +3",   80,     0,       3));
    }};

    private static final boolean I_WIN = true;

    public static void main(String args[]) 
    {
        int cost = Integer.MAX_VALUE;
        int costOfFailure = 0;
        for (Gear weapon : weapons) {
            for (Gear armor : armors) {
                for (Gear ring1 : rings) {
                    for (Gear ring2 : rings) {
                        if (ring1==ring2) continue;

                        if (I_WIN == battleOutcome(weapon, armor, ring1, ring2)) {
                            cost = Math.min(cost, weapon.cost + armor.cost + ring1.cost + ring2.cost);
                        }
                        else {
                            costOfFailure = Math.max(costOfFailure, weapon.cost + armor.cost + ring1.cost + ring2.cost);
                        }
                    }
                }
            }
        }
        Util.log("cost of success is %d", cost);
        Util.log("cost of failure is %d", costOfFailure);
    }

    private static boolean battleOutcome(Gear weapon, Gear armor, Gear ring1, Gear ring2) {
        int bossHp = BOSS_HP;
        int hp = MY_HP;

        while (bossHp>0 && hp>0) {
            bossHp -= Math.max(1, weapon.damage + armor.damage + ring1.damage + ring2.damage - BOSS_ARMOR);
            if (bossHp <= 0)
                break;
            
            hp -= Math.max(1, BOSS_DAMAGE - weapon.armor - armor.armor - ring1.armor - ring2.armor);
        }

        return hp>0;
    } 

}
