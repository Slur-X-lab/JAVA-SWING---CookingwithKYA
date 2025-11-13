import java.util.ArrayList;
import java.util.List;

// Recipe Manager
public class RecipeManager {
    private List<Recipe> recipes;

    public RecipeManager() {
        recipes = new ArrayList<>();
        initializeDefaultRecipes();
    }

    private void initializeDefaultRecipes() {
        // 1. Adobo - Put adobo.jpg in the same folder
        MainDishRecipe adobo = new MainDishRecipe("Chicken Adobo", 4, "images/adobo.jpg");
        adobo.addIngredient(new Ingredient("Chicken", "1 kg", 180.00));
        adobo.addIngredient(new Ingredient("Soy Sauce", "1/2 cup", 25.00));
        adobo.addIngredient(new Ingredient("Vinegar", "1/2 cup", 15.00));
        adobo.addIngredient(new Ingredient("Garlic", "8 cloves", 10.00));
        adobo.addIngredient(new Ingredient("Bay Leaves", "3 pieces", 5.00));
        adobo.addIngredient(new Ingredient("Black Pepper", "1 tsp", 8.00));
        adobo.setInstructions("1. Combine chicken, soy sauce, vinegar, garlic, bay leaves, and pepper in a pot.\n" +
                "2. Marinate for at least 30 minutes.\n" +
                "3. Bring to a boil, then reduce heat and simmer for 30-40 minutes.\n" +
                "4. Remove chicken and reduce sauce until thickened.\n" +
                "5. Pour sauce over chicken and serve with rice.");
        adobo.setPersonalNotes("Add a tablespoon of sugar for a sweeter version. Can use pork instead of chicken.");
        recipes.add(adobo);

        // 2. Lumpia - Put lumpia.jpg in the same folder
        AppetizerRecipe lumpia = new AppetizerRecipe("Lumpia Shanghai", "With sweet chili sauce", "images/lumpia.jpg");
        lumpia.addIngredient(new Ingredient("Ground Pork", "500g", 150.00));
        lumpia.addIngredient(new Ingredient("Carrots", "2 pieces", 20.00));
        lumpia.addIngredient(new Ingredient("Onion", "1 piece", 15.00));
        lumpia.addIngredient(new Ingredient("Garlic", "5 cloves", 8.00));
        lumpia.addIngredient(new Ingredient("Spring Roll Wrapper", "1 pack", 35.00));
        lumpia.addIngredient(new Ingredient("Egg", "1 piece", 8.00));
        lumpia.addIngredient(new Ingredient("Cooking Oil", "2 cups", 40.00));
        lumpia.setInstructions("1. Mix ground pork, minced carrots, onion, garlic, and egg.\n" +
                "2. Season with salt, pepper, and soy sauce.\n" +
                "3. Wrap mixture in spring roll wrappers.\n" +
                "4. Deep fry until golden brown.\n" +
                "5. Serve hot with sweet chili sauce.");
        lumpia.setPersonalNotes("Make sure oil is hot enough before frying. Can be frozen for later use.");
        recipes.add(lumpia);

        // 3. Sinigang - Put sinigang.jpg in the same folder
        MainDishRecipe sinigang = new MainDishRecipe("Sinigang na Baboy", 6, "images/sinigang.jpg");
        sinigang.addIngredient(new Ingredient("Pork Ribs", "800g", 280.00));
        sinigang.addIngredient(new Ingredient("Tamarind Mix", "1 pack", 25.00));
        sinigang.addIngredient(new Ingredient("Kangkong", "1 bunch", 20.00));
        sinigang.addIngredient(new Ingredient("Radish", "1 piece", 15.00));
        sinigang.addIngredient(new Ingredient("Tomatoes", "3 pieces", 30.00));
        sinigang.addIngredient(new Ingredient("Onion", "1 piece", 15.00));
        sinigang.addIngredient(new Ingredient("String Beans", "1 bundle", 25.00));
        sinigang.setInstructions("1. Boil pork ribs in water until tender (about 45 minutes).\n" +
                "2. Add tomatoes and onions, simmer for 5 minutes.\n" +
                "3. Add tamarind mix and stir well.\n" +
                "4. Add radish and string beans, cook for 5 minutes.\n" +
                "5. Add kangkong and turn off heat.\n" +
                "6. Serve hot with rice.");
        sinigang.setPersonalNotes("Can use fresh tamarind instead of mix for more authentic taste.");
        recipes.add(sinigang);

        // 4. Pancit Canton - Put pancit.jpg in the same folder
        MainDishRecipe pancit = new MainDishRecipe("Pancit Canton", 5, "images/pancit.jpg");
        pancit.addIngredient(new Ingredient("Canton Noodles", "500g", 45.00));
        pancit.addIngredient(new Ingredient("Chicken Breast", "300g", 120.00));
        pancit.addIngredient(new Ingredient("Cabbage", "1/4 head", 25.00));
        pancit.addIngredient(new Ingredient("Carrots", "2 pieces", 20.00));
        pancit.addIngredient(new Ingredient("Snow Peas", "1 cup", 30.00));
        pancit.addIngredient(new Ingredient("Soy Sauce", "1/4 cup", 15.00));
        pancit.addIngredient(new Ingredient("Garlic", "6 cloves", 8.00));
        pancit.setInstructions("1. Boil noodles according to package instructions, drain.\n" +
                "2. Sauté garlic, add chicken and cook until done.\n" +
                "3. Add vegetables and stir-fry.\n" +
                "4. Add noodles and soy sauce, mix well.\n" +
                "5. Cook for 3-5 minutes, stirring constantly.\n" +
                "6. Serve with calamansi.");
        pancit.setPersonalNotes("Don't overcook the noodles. Add more vegetables as desired.");
        recipes.add(pancit);

        // 5. Halo-Halo - Put halohalo.jpg in the same folder
        DessertRecipe halohalo = new DessertRecipe("Halo-Halo", "Medium Sweet", "images/halohalo.jpg");
        halohalo.addIngredient(new Ingredient("Shaved Ice", "2 cups", 0.00));
        halohalo.addIngredient(new Ingredient("Evaporated Milk", "1/2 cup", 25.00));
        halohalo.addIngredient(new Ingredient("Sugar", "2 tbsp", 5.00));
        halohalo.addIngredient(new Ingredient("Sweet Beans", "1/4 cup", 20.00));
        halohalo.addIngredient(new Ingredient("Nata de Coco", "1/4 cup", 15.00));
        halohalo.addIngredient(new Ingredient("Kaong", "1/4 cup", 20.00));
        halohalo.addIngredient(new Ingredient("Ube Halaya", "2 tbsp", 30.00));
        halohalo.addIngredient(new Ingredient("Leche Flan", "1 slice", 40.00));
        halohalo.addIngredient(new Ingredient("Ube Ice Cream", "1 scoop", 35.00));
        halohalo.setInstructions("1. In a tall glass, layer sweet beans, nata de coco, and kaong.\n" +
                "2. Add shaved ice on top.\n" +
                "3. Pour evaporated milk and sprinkle sugar.\n" +
                "4. Top with ube halaya, leche flan, and ice cream.\n" +
                "5. Mix well before eating (halo means 'mix').");
        halohalo.setPersonalNotes(
                "Chill all ingredients before assembling. Can customize toppings based on preference.");
        recipes.add(halohalo);

        // 6. Lechon Kawali - Put lechon.jpg in the same folder
        MainDishRecipe lechon = new MainDishRecipe("Lechon Kawali", 4, "images/lechon.jpg");
        lechon.addIngredient(new Ingredient("Pork Belly", "1 kg", 320.00));
        lechon.addIngredient(new Ingredient("Bay Leaves", "3 pieces", 5.00));
        lechon.addIngredient(new Ingredient("Peppercorns", "1 tbsp", 10.00));
        lechon.addIngredient(new Ingredient("Salt", "2 tbsp", 3.00));
        lechon.addIngredient(new Ingredient("Cooking Oil", "3 cups", 60.00));
        lechon.addIngredient(new Ingredient("Garlic", "1 head", 12.00));
        lechon.setInstructions("1. Boil pork belly with bay leaves, peppercorns, and salt for 45 minutes.\n" +
                "2. Remove and let cool completely. Pat dry.\n" +
                "3. Rub with salt all over the skin.\n" +
                "4. Deep fry in hot oil until golden and crispy.\n" +
                "5. Chop into serving pieces.\n" +
                "6. Serve with lechon sauce or liver sauce.");
        lechon.setPersonalNotes("Make sure pork is completely dry before frying for extra crispy skin.");
        recipes.add(lechon);
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void deleteRecipe(Recipe recipe) {
        recipes.remove(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    public List<Recipe> searchRecipes(String keyword) {
        List<Recipe> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Recipe recipe : recipes) {
            if (recipe.getTitle().toLowerCase().contains(lowerKeyword) ||
                    recipe.getCategory().toLowerCase().contains(lowerKeyword)) {
                results.add(recipe);
            }
        }
        return results;
    }
}