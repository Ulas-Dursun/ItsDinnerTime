package model;
import java.util.List;

/* TASARIM DESENİ: DECORATOR (soyut sınıf)
 * Amaç: Recipe arayüzünü süsleyerek (decorate ederek) davranış genişletmeye olanak tanır.
 * Not: Tüm çağrıları içteki (inner) Recipe nesnesine iletir. */
public abstract class RecipeDecorator implements Recipe {
    protected final Recipe inner;
    protected RecipeDecorator(Recipe inner) { this.inner = inner; }

    @Override public String  getId()          { return inner.getId(); }
    @Override public String  getTitle()       { return inner.getTitle(); }
    @Override public List<String> getIngredients(){ return inner.getIngredients(); }
    @Override public String  getInstructions(){ return inner.getInstructions(); }
    @Override public MealType getMealType()   { return inner.getMealType(); }
}
