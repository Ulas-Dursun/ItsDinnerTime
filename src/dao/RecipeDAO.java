package dao;
import model.Recipe;
import java.util.List;

/* TASARIM DESENİ: DATA ACCESS OBJECT (DAO)
 * Sorumluluk: Recipe (tarif) verisi üzerinde CRUD işlemleri yapar. */
public interface RecipeDAO {
    void save(Recipe recipe) throws Exception;
    Recipe findById(String id) throws Exception;
    List<Recipe> findAll() throws Exception;
    void update(Recipe recipe) throws Exception;
    void delete(String id) throws Exception;
}
