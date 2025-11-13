import java.util.ArrayList;
import java.util.List;

// Base Recipe Class with Encapsulation
public abstract class Recipe implements RecipeInterface {
    private String title;
    private String category;
    private String instructions;
    private String personalNotes;
    private List<Ingredient> ingredients;
    private String imagePath;

    public Recipe(String title, String category, String imagePath) {
        this.title = title;
        this.category = category;
        this.ingredients = new ArrayList<>();
        this.instructions = "";
        this.personalNotes = "";
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPersonalNotes() {
        return personalNotes;
    }

    public void setPersonalNotes(String notes) {
        this.personalNotes = notes;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    @Override
    public void removeIngredient(String ingredientName) {
        ingredients.removeIf(i -> i.getName().equalsIgnoreCase(ingredientName));
    }

    @Override
    public double computeTotalCost() {
        return ingredients.stream().mapToDouble(Ingredient::getPrice).sum();
    }

    @Override
    public double computeRemainingCost(List<String> availableIngredients) {
        return ingredients.stream()
                .filter(i -> !availableIngredients.contains(i.getName().toLowerCase()))
                .mapToDouble(Ingredient::getPrice)
                .sum();
    }

    @Override
    public String displayRecipe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recipe: ").append(title).append("\n");
        sb.append("Category: ").append(category).append("\n\n");
        sb.append("Ingredients:\n");
        for (Ingredient ing : ingredients) {
            sb.append("  - ").append(ing.toString()).append("\n");
        }
        sb.append("\nInstructions:\n").append(instructions);
        if (!personalNotes.isEmpty()) {
            sb.append("\n\nPersonal Notes:\n").append(personalNotes);
        }
        return sb.toString();
    }
}