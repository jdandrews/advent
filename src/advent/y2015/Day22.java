package advent.y2015;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Day22 {
    //
    // goal
    //
    private static int minimumMana = Integer.MAX_VALUE;
    private static boolean RUNNING_PART_2 = false;

    //
    // capabilities
    //
    private record Spell(String name, int manaCost, int damage, int heals, Effect effect) {
    }

    private record Effect(String name, int turns, int armor, int damage, int mana) {
    }

    private static final Map<String, Spell> SPELLS = new HashMap<>();
    static {
        // name, cost, damage, heal, effect
        SPELLS.put("Magic Missile", new Spell("Magic Missile", 53, 4, 0, null));

        SPELLS.put("Drain", new Spell("Drain", 73, 2, 2, null));

        // name, turns, armor, damage, mana
        Effect effect = new Effect("Shield", 6, 7, 0, 0);
        SPELLS.put("Shield", new Spell("Shield", 113, 0, 0, effect));

        effect = new Effect("Poison", 6, 0, 3, 0);
        SPELLS.put("Poison", new Spell("Poison", 173, 0, 0, effect));

        effect = new Effect("Recharge", 5, 0, 0, 101);
        SPELLS.put("Recharge", new Spell("Recharge", 229, 0, 0, effect));
    }

    //
    // initial conditions
    //
    private record BossStats(int hitPoints, int damage) {
    }

    private record MyStats(int hitPoints, int mana) {
    }

    //
    // test cases
    //
    private static final BossStats TEST1_BOSS = new BossStats(13, 8);
    private static final MyStats TEST1_ME = new MyStats(10, 250);
    private static final List<Spell> TEST1_SPELLS = Arrays.asList(SPELLS.get("Poison"), SPELLS.get("Magic Missile"));

    private static final BossStats TEST2_BOSS = new BossStats(14, 8);
    private static final MyStats TEST2_ME = new MyStats(10, 250);
    private static final List<Spell> TEST2_SPELLS = Arrays.asList(
            SPELLS.get("Recharge"), SPELLS.get("Shield"), SPELLS.get("Drain"), SPELLS.get("Poison"), SPELLS.get("Magic Missile"));

    private static final List<Spell> TEST3_SPELLS = Arrays.asList(
            SPELLS.get("Recharge"), SPELLS.get("Shield"), SPELLS.get("Drain"), SPELLS.get("Poison"));

    private static final List<Spell> TEST4_SPELLS = Arrays.asList(SPELLS.get("Recharge"), SPELLS.get("Recharge"));

    private static final BossStats PART1_BOSS = new BossStats(55, 8);
    private static final MyStats PART1_ME = new MyStats(50, 500);

    //
    // battle results
    //
    public enum BattleResult {
        NOT_OVER, PLAYER_WON, BOSS_WON, FAILURE;
    }

    public enum Failure {
        OUT_OF_SPELLS, ILLEGAL_SPELL, INSUFFICIENT_MANA;

    }

    //
    // combatants
    //
    public static class Me {
        private int hitPoints;
        private int mana;
        private int armor = 0;
        private Map<Effect, Integer> activeEffects = new HashMap<>();

        public Me(MyStats stats) {
            hitPoints = stats.hitPoints;
            mana = stats.mana;
        }

        public boolean isAlive() {
            return hitPoints > 0;
        }

        public boolean canCast(Spell spell) {
            return spell.manaCost <= mana;
        }

        public int armor() {
            int boost = 0;
            for (Effect effect : activeEffects.keySet()) {
                boost += effect.armor;
            }
            return armor + boost;
        }

        public void applyEffects(Boss boss) {
            Set<Effect> removeMe = new HashSet<>();
            for (Effect effect : activeEffects.keySet()) {
                int timer = activeEffects.get(effect) - 1;
                applyEffect(effect, boss);
                Day22.log(effect.name + " timer is now " + timer);
                if (timer <= 0) {
                    Day22.log(effect.name + " wears off.");
                    removeMe.add(effect);
                }
                activeEffects.put(effect, timer);
            }

            for (Effect effect : removeMe) {
                activeEffects.remove(effect);
            }
        }

        private void applyEffect(Effect effect, Boss boss) {
            if (effect.mana > 0) {
                mana += effect.mana;
                Day22.log(effect.name + " provides " + effect.mana + " mana; total mana is now " + mana + ".");
            }
            if (effect.damage > 0) {
                boss.hitPoints -= effect.damage;
                if (boss.hitPoints <= 0) {
                    Day22.log(effect.name + " deals " + effect.damage + " damage; boss dies.");
                } else {
                    Day22.log(effect.name + " deals " + effect.damage + " damage.");
                }
            }
        }

        public Failure cast(Spell spell, Boss boss) {
            if (spell.manaCost() > mana) {
                Day22.log("Tried to case " + spell.name + " requiring " + spell.manaCost()
                        + " mana, but I only have " + mana + ".");
                return Failure.INSUFFICIENT_MANA;
            }
            if (spell.effect != null && isActive(spell.name)) {
                Day22.log("Unable to cast " + spell.name + " because it's effect is already active.");
                return Failure.ILLEGAL_SPELL;
            }

            mana -= spell.manaCost();

            if (spell.damage() > 0) {
                boss.hitPoints -= spell.damage;
                Day22.log("Player casts " + spell.name + ", dealing " + spell.damage + " damage.");
                if (boss.hitPoints <= 0) {
                    Day22.log("This kills the boss, and the player wins.");
                }
            }

            if (spell.heals() > 0) {
                hitPoints += spell.heals;
                Day22.log("Player casts " + spell.name + " heals " + spell.heals + "; Play has " + hitPoints + " hit points.");
            }

            if (spell.effect() != null) {
                activeEffects.put(spell.effect(), spell.effect().turns());
                Day22.log("Player casts " + spell.name);
            }
            return null;    // no failure
        }

        private boolean isActive(String name) {
            for (Effect effect : activeEffects.keySet()) {
                if (effect.name().equals(name))
                    return true;
            }
            return false;
        }
    }

    public static class Boss {
        private int hitPoints;
        private int damage;

        public Boss(BossStats stats) {
            hitPoints = stats.hitPoints;
            damage = stats.damage;
        }

        public Boss(Boss boss) {
            hitPoints = boss.hitPoints;
            damage = boss.damage;
        }

        public boolean isAlive() {
            return hitPoints > 0;
        }

        public void runAttack(Me me) {
            int shield = me.armor();
            int hit = Math.max(1, damage - shield);
            me.hitPoints -= hit;
            if (shield > 0) {
                Day22.log("Boss attacks for " + damage + " - " + shield + " = " + hit + " damage.");
            } else {
                Day22.log("Boss attacks for " + damage + " damage.");
            }
            if (me.hitPoints <= 0) {
                Day22.log("This kills the player, and the boss wins.");
            }
        }

    }

    public static class Battle {
        public Me me;
        public Boss boss;
        public Queue<Spell> spellQueue = new ArrayDeque<>();

        public Failure failure;

        public Battle(MyStats myStats, BossStats bossStats, Queue<Spell> spells) {
            me = new Me(myStats);
            boss = new Boss(bossStats);
            spellQueue = spells;
        }

        public boolean isNotOver() {
            return me.isAlive() && boss.isAlive() && failure == null;
        }

        public Failure getFailure() {
            return failure;
        }

        public BattleResult getResult() {
            if (isNotOver()) {
                return BattleResult.NOT_OVER;

            } else if (me.hitPoints <= 0) {
                return BattleResult.BOSS_WON;

            } else if (boss.hitPoints <= 0) {
                return BattleResult.PLAYER_WON;

            } else if (failure != null) {
                return BattleResult.FAILURE;
            }

            throw new IllegalStateException("How did we get here?");
        }

        public void run() {
            while (isNotOver() && failure == null) {
                failure = runPlayerTurn();
                if (isNotOver() && failure == null) {
                    runBossTurn();
                }
            }
        }

        public Failure runPlayerTurn() {
            Day22.log("\n-- Player turn --");
            logState();

            if (RUNNING_PART_2) {
                me.hitPoints--;
                if (me.hitPoints <= 0) {
                    return null;
                }
            }

            me.applyEffects(boss);

            Failure castWorked = Failure.OUT_OF_SPELLS;
            if (!spellQueue.isEmpty())
                castWorked = me.cast(spellQueue.poll(), boss);

            return castWorked;
        }

        public void runBossTurn() {
            Day22.log("\n--Boss turn --");
            logState();
            me.applyEffects(boss);
            if (boss.isAlive()) {
                boss.runAttack(me);
            }
        }

        private void logState() {
            Day22.log("- Player has " + me.hitPoints + " hit points, " + me.armor() + " armor, " + me.mana + " mana");
            Day22.log("- Boss has " + boss.hitPoints + " hit points.");
        }
    }

    private static boolean LOGGING = false;

    private static void log(String s) {
        if (LOGGING)
            System.out.println(s);
    }

    //
    // battles are driven by a series of spells. We are exploring a tree of all possible combinations
    // of spells, searching for a list from root to tip that results in a minimum mana expenditure.
    // This is one node in that tree.
    //
    public static class Node {
        public Node parent;
        public Spell spell;
        public Node[] children = new Node[SPELLS.size()];
        public BattleResult battleResult;
        public Failure failure;

        public Node(Node parent, Spell spell) {
            this.parent = parent;
            this.spell = spell;
        }

        /**
         * Populates the next layer in the tree attached to this node. A new layer comprises one of each of the possible
         * spells, excluding those which don't contribute to a better outcome.
         * 
         * @return true if new children were added, else false.
         */
        public boolean makeNextLevel() {
            boolean result = false;
            int n = 0;
            for (Spell s : SPELLS.values()) {
                if (failure == Failure.OUT_OF_SPELLS || parent == null) {
                    children[n++] = new Node(this, s);
                    result = true;
                }
            }
            return result;
        }

        public List<List<Node>> generateSpellLists() {
            List<List<Node>> result = new ArrayList<>();
            for (int i = 0; i < children.length; ++i) {
                if (children[i] != null) {
                    List<List<Node>> childLists = children[i].generateSpellLists();
                    if (childLists.isEmpty()) {
                        List<Node> list = new ArrayList<>();
                        list.add(children[i]);
                        result.add(list);
                    } else {
                        for (List<Node> childList : childLists) {
                            List<Node> list = new ArrayList<>();
                            list.add(children[i]);
                            list.addAll(childList);
                            result.add(list);
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public String toString() {
            return "Node{spell=" + spell + "," + battleResult + "," + failure + ")";
        }
    }

    public static void main(String[] args) {
        LOGGING = true;
        Battle battle = new Battle(TEST1_ME, TEST1_BOSS, new ArrayDeque<>(TEST1_SPELLS));
        battle.run();
        log("\n" + (battle.getResult()));

        log("\n-----------------------------------------\n");

        battle = new Battle(TEST2_ME, TEST2_BOSS, new ArrayDeque<>(TEST2_SPELLS));
        battle.run();
        log("\n" + (battle.getResult()));

        log("\n-----------------------------------------\n");

        battle = new Battle(TEST2_ME, TEST2_BOSS, new ArrayDeque<>(TEST3_SPELLS));
        battle.run();
        log("\n" + (battle.getResult()) + "; " + battle.failure);

        log("\n-----------------------------------------\n");

        battle = new Battle(new MyStats(10, 500), TEST2_BOSS, new ArrayDeque<>(TEST4_SPELLS));
        battle.run();
        log("\n" + (battle.getResult()) + "; " + battle.failure);

        log("\n--------------------Part 1---------------------\n");
        runSearch();
        
        log("\n--------------------Part 2---------------------\n");
        RUNNING_PART_2 = true;
        minimumMana = Integer.MAX_VALUE;
        runSearch();
    }

    private static void runSearch() {
        Battle battle;
        Node root = new Node(null, null);
        root.makeNextLevel();

        List<List<Node>> spellLists = root.generateSpellLists();
        for (boolean done = false; !done;) {
            done = true;
            log("processing " + spellLists.size() + " spell lists.");
            for (List<Node> nodeList : spellLists) {
                Deque<Spell> spells = new ArrayDeque<>();
                Node lastNode = nodeList.get(0);
                for (Node node : nodeList) {
                    spells.add(node.spell);
                    lastNode = node;
                }
                battle = new Battle(PART1_ME, PART1_BOSS, spells);
                LOGGING = false;
                battle.run();
                LOGGING = true;
                lastNode.battleResult = battle.getResult();
                lastNode.failure = battle.getFailure();

                int totalManaCost = 0;
                for (Node node = lastNode; node.parent != null; node = node.parent) {
                    totalManaCost += node.spell.manaCost();
                }

                if (lastNode.battleResult == BattleResult.PLAYER_WON) {
                    minimumMana = Math.min(totalManaCost, minimumMana);
                }

                if (battle.failure == Failure.OUT_OF_SPELLS && totalManaCost <= minimumMana) {
                    lastNode.makeNextLevel();
                    done = false;
                }

            }
            summarizeToLog(spellLists);
            spellLists = root.generateSpellLists();
        }
        log("\nFinal minimum: " + minimumMana);
    }

    private static void summarizeToLog(List<List<Node>> spellLists) {
        Map<BattleResult, Integer> results = new HashMap<>();
        Map<Failure, Integer> failures = new HashMap<>();

        int minMana = Integer.MAX_VALUE;
        for (List<Node> list : spellLists) {
            Node lastNode = list.get(list.size() - 1);
            BattleResult result = lastNode.battleResult;
            Failure failure = lastNode.failure;
            if (!results.containsKey(result)) {
                results.put(result, 0);
            }
            if (!failures.containsKey(failure)) {
                failures.put(failure, 0);
            }
            results.put(result, results.get(result) + 1);
            failures.put(failure, failures.get(failure) + 1);

            if (result == BattleResult.PLAYER_WON) {
                int mana = 0;
                for (Node node : list) {
                    mana += node.spell.manaCost;
                }
                minMana = Math.min(mana, minMana);
            }
        }

        log("results=" + results + "; failures=" + failures);
        log("minimum mana = " + minMana);
    }
}
/*
 * Little Henry Case decides that defeating bosses with swords and stuff is boring. Now he's playing the game with a
 * wizard. Of course, he gets stuck on another boss and needs your help again.
 * 
 * In this version, combat still proceeds with the player and the boss taking alternating turns. The player still goes
 * first. Now, however, you don't get any equipment; instead, you must choose one of your spells to cast. The first
 * character at or below 0 hit points loses.
 * 
 * Since you're a wizard, you don't get to wear armor, and you can't attack normally. However, since you do magic damage,
 * your opponent's armor is ignored, and so the boss effectively has zero armor as well. As before, if armor (from a
 * spell, in this case) would reduce damage below 1, it becomes 1 instead - that is, the boss' attacks always deal at
 * least 1 damage.
 * 
 * On each of your turns, you must select one of your spells to cast. If you cannot afford to cast any spell, you lose.
 * Spells cost mana; you start with 500 mana, but have no maximum limit. You must have enough mana to cast a spell,
 * and its cost is immediately deducted when you cast it. Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.
 * 
 * - Magic Missile costs 53 mana. It instantly does 4 damage.
 * - Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
 * - Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
 * - Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active,
 * it deals the boss 3 damage.
 * - Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active,
 * it gives you 101 new mana.
 * 
 * Effects all work the same way. Effects apply at the start of both the player's turns and the boss' turns. Effects
 * are created with a timer (the number of turns they last); at the start of each turn, after they apply any effect
 * they have, their timer is decreased by one. If this decreases the timer to zero, the effect ends. You cannot cast
 * a spell that would start an effect which is already active. However, effects can be started on the same turn they end.
 * 
 * For example, suppose the player has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:
 * 
 * -- Player turn --
 * - Player has 10 hit points, 0 armor, 250 mana
 * - Boss has 13 hit points
 * Player casts Poison.
 * 
 * -- Boss turn --
 * - Player has 10 hit points, 0 armor, 77 mana
 * - Boss has 13 hit points
 * Poison deals 3 damage; its timer is now 5.
 * Boss attacks for 8 damage.
 * 
 * -- Player turn --
 * - Player has 2 hit points, 0 armor, 77 mana
 * - Boss has 10 hit points
 * Poison deals 3 damage; its timer is now 4.
 * Player casts Magic Missile, dealing 4 damage.
 * 
 * -- Boss turn --
 * - Player has 2 hit points, 0 armor, 24 mana
 * - Boss has 3 hit points
 * Poison deals 3 damage. This kills the boss, and the player wins.
 * 
 * Now, suppose the same initial conditions, except that the boss has 14 hit points instead:
 * 
 * -- Player turn --
 * - Player has 10 hit points, 0 armor, 250 mana
 * - Boss has 14 hit points
 * Player casts Recharge.
 * 
 * -- Boss turn --
 * - Player has 10 hit points, 0 armor, 21 mana
 * - Boss has 14 hit points
 * Recharge provides 101 mana; its timer is now 4.
 * Boss attacks for 8 damage!
 * 
 * -- Player turn --
 * - Player has 2 hit points, 0 armor, 122 mana
 * - Boss has 14 hit points
 * Recharge provides 101 mana; its timer is now 3.
 * Player casts Shield, increasing armor by 7.
 * 
 * -- Boss turn --
 * - Player has 2 hit points, 7 armor, 110 mana
 * - Boss has 14 hit points
 * Shield's timer is now 5.
 * Recharge provides 101 mana; its timer is now 2.
 * Boss attacks for 8 - 7 = 1 damage!
 * 
 * -- Player turn --
 * - Player has 1 hit point, 7 armor, 211 mana
 * - Boss has 14 hit points
 * Shield's timer is now 4.
 * Recharge provides 101 mana; its timer is now 1.
 * Player casts Drain, dealing 2 damage, and healing 2 hit points.
 * 
 * -- Boss turn --
 * - Player has 3 hit points, 7 armor, 239 mana
 * - Boss has 12 hit points
 * Shield's timer is now 3.
 * Recharge provides 101 mana; its timer is now 0.
 * Recharge wears off.
 * Boss attacks for 8 - 7 = 1 damage!
 * 
 * -- Player turn --
 * - Player has 2 hit points, 7 armor, 340 mana
 * - Boss has 12 hit points
 * Shield's timer is now 2.
 * Player casts Poison.
 * 
 * -- Boss turn --
 * - Player has 2 hit points, 7 armor, 167 mana
 * - Boss has 12 hit points
 * Shield's timer is now 1.
 * Poison deals 3 damage; its timer is now 5.
 * Boss attacks for 8 - 7 = 1 damage!
 * 
 * -- Player turn --
 * - Player has 1 hit point, 7 armor, 167 mana
 * - Boss has 9 hit points
 * Shield's timer is now 0.
 * Shield wears off, decreasing armor by 7.
 * Poison deals 3 damage; its timer is now 4.
 * Player casts Magic Missile, dealing 4 damage.
 * 
 * -- Boss turn --
 * - Player has 1 hit point, 0 armor, 114 mana
 * - Boss has 2 hit points
 * Poison deals 3 damage. This kills the boss, and the player wins.
 * 
 * You start with 50 hit points and 500 mana points. The boss's actual stats are in your puzzle input.
 * What is the least amount of mana you can spend and still win the fight?
 * (Do not include mana recharge effects as "spending" negative mana.)
 */
