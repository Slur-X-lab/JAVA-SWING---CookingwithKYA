// Inheritance - MainDishRecipe
class MainDishRecipe extends Recipe {
    private int servings;

    public MainDishRecipe(String title, int servings, String imagePath) {
        super(title, "Main Dish", imagePath);
        this.servings = servings;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    @Override
    public String displayRecipe() {
        return super.displayRecipe() + "\nServings: " + servings;
    }
}

// Inheritance - AppetizerRecipe
class AppetizerRecipe extends Recipe {
    private String servingStyle;

    public AppetizerRecipe(String title, String servingStyle, String imagePath) {
        super(title, "Appetizer", imagePath);
        this.servingStyle = servingStyle;
    }

    public String getServingStyle() {
        return servingStyle;
    }

    public void setServingStyle(String style) {
        this.servingStyle = style;
    }

    @Override
    public String displayRecipe() {
        return super.displayRecipe() + "\nServing Style: " + servingStyle;
    }
}

// Inheritance - DessertRecipe
class DessertRecipe extends Recipe {
    private String sweetness;

    public DessertRecipe(String title, String sweetness, String imagePath) {
        super(title, "Dessert", imagePath);
        this.sweetness = sweetness;
    }

    public String getSweetness() {
        return sweetness;
    }

    public void setSweetness(String sweetness) {
        this.sweetness = sweetness;
    }

    @Override
    public String displayRecipe() {
        return super.displayRecipe() + "\nSweetness Level: " + sweetness;
    }
}