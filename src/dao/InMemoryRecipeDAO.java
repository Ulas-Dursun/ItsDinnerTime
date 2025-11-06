package dao;

import model.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/** RECIPE DAO – IN-MEMORY IMPLEMENTATION
 *  Tarif verilerini bellekte saklar. Kalıcı veri kaynağı kullanılmaz. */
public class InMemoryRecipeDAO implements RecipeDAO {

    private final Map<String, Recipe> map = new HashMap<>();// Tarif verileri burada tutulur
    private final AtomicInteger seq = new AtomicInteger(1);// Otomatik ID üretimi için sayaç

    private String nextId() { return String.valueOf(seq.getAndIncrement()); }

    @Override
    public void save(Recipe r) {
        String id = (r.getId() == null || r.getId().isBlank())
                    ? nextId() : r.getId();
        Recipe stored = new BasicRecipe(id, r.getTitle(),
                r.getIngredients(), r.getInstructions(), r.getMealType());
        map.put(id, stored);
    }

    @Override public Recipe findById(String id) { return map.get(id); }

    @Override public List<Recipe> findAll() { return new ArrayList<>(map.values()); }

    @Override public void update(Recipe r) { map.put(r.getId(), r); }

    @Override public void delete(String id) { map.remove(id); }
}
