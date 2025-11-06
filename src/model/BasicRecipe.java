package model;
import java.util.List;

/* TEMEL TARİF SINIFI
 * Recipe arayüzünü uygulayan sade model.
 * Tüm alanlar final → değişmezlik (immutability) sağlanır. */

public class BasicRecipe implements Recipe {
    private final String id, title, instructions;
    private final List<String> ingredients;
    private final MealType mealType;

    public BasicRecipe(String id, String title,
                       List<String> ingredients,
                       String instructions,
                       MealType mealType) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.mealType = mealType;
    }
    @Override public String getId()          { return id; }
    @Override public String getTitle()       { return title; }
    @Override public List<String> getIngredients() { return ingredients; }
    @Override public String getInstructions(){ return instructions; }
    @Override public MealType getMealType()  { return mealType; }

    /** Listede okunur görünüm */
    @Override public String toString() {
        return title + "  (" + mealType.name().charAt(0) +
               mealType.name().substring(1).toLowerCase() + ")";
    }
}
