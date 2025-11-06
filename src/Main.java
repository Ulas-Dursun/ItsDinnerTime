import dao.*;
import model.*;
import ui.RecipeAppFrame;
import util.DataSourceType;

import java.util.Arrays;

/** Main – Factory + Decorator (calorie badges) demo */
public class Main {

    /** Yardımcı: kalori eşik kontrolü ve dekore etme */
    private static Recipe badge(Recipe r, int kcal) {
        return (kcal <= 350)
                ? new LowCalorieRecipeDecorator(r, kcal)   // 🥗 rozet
                : r;                                       // rozet yok
    }

    public static void main(String[] args) throws Exception {

        RecipeDAO dao = RecipeDAOFactory.create(DataSourceType.MEMORY);

        /* ---------- Breakfast (≈300 kcal → 🥗) ---------- */
        Recipe menemen = badge(new BasicRecipe(null,"Menemen",
                Arrays.asList("3 eggs","2 tomatoes (diced)","1 green pepper",
                              "1 tbsp butter","Salt & pepper"),
                """
                1. Melt butter in a pan over medium heat.<br>
                2. Sauté diced pepper 2&nbsp;min; add tomatoes, cook until softened.<br>
                3. Whisk eggs, pour; stir gently until just set.<br>
                4. Season and serve hot with bread.""",
                MealType.BREAKFAST), 300);
        dao.save(menemen);

        /* ---------- Lunch #1 (≈550 kcal → rozet yok) ----- */
        Recipe caesar = badge(new BasicRecipe(null,"Chicken Caesar Wrap",
                Arrays.asList("Tortilla","Grilled chicken strips","Romaine lettuce",
                              "Parmesan","Caesar dressing"),
                """
                Toss lettuce with dressing and parmesan.<br>
                Layer tortilla with chicken &amp; salad; roll tightly, slice diagonally.""",
                MealType.LUNCH), 550);
        dao.save(caesar);

        /* ---------- Lunch #2 (≈280 kcal → 🥗) ------------ */
        Recipe quinoa = badge(new BasicRecipe(null,"Quinoa Salad",
                Arrays.asList("1 cup cooked quinoa","Cherry tomatoes",
                              "Cucumber","Feta","Olive oil"),
                """
                Mix all ingredients, season with salt &amp; pepper.<br>
                Chill for 15&nbsp;minutes before serving.""",
                MealType.LUNCH), 280);
        dao.save(quinoa);

        /* ---------- Dinner (≈400 kcal → rozet yok) ------- */
        Recipe salmon = badge(new BasicRecipe(null,"Baked Salmon with Lemon",
                Arrays.asList("Salmon fillet 200 g","1 tbsp olive oil",
                              "½ lemon (sliced)","Salt, pepper, dill"),
                """
                • Preheat oven to 200 °C.<br>
                • Place salmon on foil, drizzle oil, season.<br>
                • Top with lemon slices &amp; dill; seal foil.<br>
                • Bake 15–17 min until flakes easily.""",
                MealType.DINNER), 400);
        dao.save(salmon);

        /* ---------- GUI ---------- */
        javax.swing.SwingUtilities.invokeLater(() -> {
            try { new RecipeAppFrame(dao).setVisible(true); }
            catch (Exception e) { e.printStackTrace(); }
        });
    }
}
