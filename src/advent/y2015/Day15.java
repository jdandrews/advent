package advent.y2015;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import advent.Util;

public class Day15 {

    public static class Ingredient {
        public String name;
        public int capacity;
        public int durability;
        public int flavor;
        public int texture;
        public int calories;
        public Ingredient(String n, int c, int d, int f, int t, int cal) {
            name = n;
            capacity = c;
            durability = d;
            flavor = f;
            texture = t;
            calories = cal;
        }
    }

    public static void main(String[] args) {
        List<Ingredient> ingredients = new ArrayList<Ingredient>() {{
            add(new Ingredient("Frosting",      4, -2,  0, 0, 5));
            add(new Ingredient("Candy",         0,  5, -1, 0, 8));
            add(new Ingredient("Butterscotch", -1,  0,  5, 0, 6));
            add(new Ingredient("Sugar",         0,  0, -2, 2, 1));
        }};
        List<Ingredient> test = new ArrayList<Ingredient>() {{
            add(new Ingredient("Butterscotch", -1, -2,  6,  3, 8));
            add(new Ingredient("Cinnamon",      2,  3, -2, -1, 3));
        }};

        int[] sampleRecipe1 = { 44, 56 };
        Util.log("sample recipe 1 scores %d", score(sampleRecipe1, test));

        int[] sampleRecipe2 = { 40, 60 };
        Util.log("sample recipe 2 scores %d", score(sampleRecipe2, test));

        int[] recipe = new int[ingredients.size()];
        long score = 0;
        long scoreFor500Calories = 0;
        int[] maxRecipe = null;
        int[] maxWith500Calories = null;
        for (int i=1; i<98; ++i) {
            recipe[0] = i;
            for (int j=1; j<100-i-2; ++j) {
                recipe[1] = j;
                for (int k=1; k<100-i-j-1; ++k) {
                    recipe[2] = k;
                    recipe[3] = 100-i-j-k;
                    
                    long recipeScore = score(recipe, ingredients);
                    if (recipeScore > score) {
                        score = Math.max(score, recipeScore);
                        maxRecipe = Arrays.copyOf(recipe, recipe.length);
                    }
                    if (calories(recipe, ingredients)==500 && recipeScore > scoreFor500Calories) {
                        scoreFor500Calories = Math.max(scoreFor500Calories, recipeScore);
                        maxWith500Calories = Arrays.copyOf(recipe, recipe.length);
                    }
                }
            }
        }
        Util.log("max recipe score %d for [%d, %d, %d, %d]", score, 
                maxRecipe[0], maxRecipe[1], maxRecipe[2], maxRecipe[3]);
        Util.log("max 500-calorie recipe score %d for [%d, %d, %d, %d]", scoreFor500Calories, 
                maxWith500Calories[0], maxWith500Calories[1], maxWith500Calories[2], maxWith500Calories[3]);
    }

    private static long calories(int[] tsps, List<Ingredient> ingredients) {
        long calories=0;
        for (int i=0; i<tsps.length; ++i) {
            calories    += 1L * tsps[i] * ingredients.get(i).calories;
        }
        calories =   Math.max(0L, calories);
        return calories;
    }

    private static long score(int[] tsps, List<Ingredient> ingredients) {
        long capacity=0;
        long durability=0;
        long flavor=0;
        long texture=0;

        for (int i=0; i<tsps.length; ++i) {
            capacity    += 1L * tsps[i] * ingredients.get(i).capacity;
            durability  += 1L * tsps[i] * ingredients.get(i).durability;
            flavor      += 1L * tsps[i] * ingredients.get(i).flavor;
            texture     += 1L * tsps[i] * ingredients.get(i).texture;
        }
        
        capacity =   Math.max(0L, capacity);
        durability = Math.max(0L, durability);
        flavor =     Math.max(0L,  flavor);
        texture =    Math.max(0L,  texture);

        return capacity*durability*flavor*texture;
    }
}
