package model;

/* TASARIM DESENİ: DECORATOR (somut sınıf)
 * Amaç: Var olan BasicRecipe sınıfına dokunmadan,
 *       "Düşük Kalori" etiketi ve kalori bilgisi ekler. */

public class LowCalorieRecipeDecorator extends RecipeDecorator {

    private final int calories;   // explicitly provided

    public LowCalorieRecipeDecorator(Recipe inner, int calories) {
        super(inner);
        this.calories = calories;
    }
    public int getCalories() { return calories; }

    @Override
    public String getTitle() {       // badge in list
        return "🥗 " + super.getTitle();
    }
}
