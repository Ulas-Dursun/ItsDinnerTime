package model;
import java.util.List;

public interface Recipe {
    String  getId();          // ← int yerine String
    String  getTitle();
    List<String> getIngredients();
    String  getInstructions();
    MealType getMealType();
}
