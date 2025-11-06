package dao;
import util.DataSourceType;

/**  DESIGN PATTERN: FACTORY METHOD
 *   İstenilen veri kaynağına uygun RecipeDAO somut sınıfını üretir.  */
public final class RecipeDAOFactory {
    private RecipeDAOFactory() {}
    public static RecipeDAO create(DataSourceType type) throws Exception {
        return switch (type) {
            case MEMORY -> new InMemoryRecipeDAO();
        };
    }
}
