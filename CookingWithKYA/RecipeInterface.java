import java.util.List;

// Abstraction - Interface
public interface RecipeInterface {
    void addIngredient(Ingredient ingredient);

    void removeIngredient(String ingredientName);

    double computeTotalCost();

    double computeRemainingCost(List<String> availableIngredients);

    String displayRecipe();
}