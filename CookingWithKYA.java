import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// Abstraction - Interface
interface RecipeInterface {
    void addIngredient(Ingredient ingredient);

    void removeIngredient(String ingredientName);

    double computeTotalCost();

    double computeRemainingCost(List<String> availableIngredients);

    String displayRecipe();
}

// Encapsulation - Ingredient Class
class Ingredient {
    private String name;
    private String quantity;
    private double price;

    public Ingredient(String name, String quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return quantity + " " + name + " - ₱" + String.format("%.2f", price);
    }
}

// Base Recipe Class with Encapsulation
abstract class Recipe implements RecipeInterface {
    private String title;
    private String category;
    private String instructions;
    private String personalNotes;
    private List<Ingredient> ingredients;

    public Recipe(String title, String category) {
        this.title = title;
        this.category = category;
        this.ingredients = new ArrayList<>();
        this.instructions = "";
        this.personalNotes = "";
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

// Inheritance - MainDishRecipe
class MainDishRecipe extends Recipe {
    private int servings;

    public MainDishRecipe(String title, int servings) {
        super(title, "Main Dish");
        this.servings = servings;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    // Polymorphism - Method Overriding
    @Override
    public String displayRecipe() {
        return super.displayRecipe() + "\nServings: " + servings;
    }
}

// Inheritance - AppetizerRecipe
class AppetizerRecipe extends Recipe {
    private String servingStyle;

    public AppetizerRecipe(String title, String servingStyle) {
        super(title, "Appetizer");
        this.servingStyle = servingStyle;
    }

    public String getServingStyle() {
        return servingStyle;
    }

    public void setServingStyle(String style) {
        this.servingStyle = style;
    }

    // Polymorphism - Method Overriding
    @Override
    public String displayRecipe() {
        return super.displayRecipe() + "\nServing Style: " + servingStyle;
    }
}

// Inheritance - DessertRecipe
class DessertRecipe extends Recipe {
    private String sweetness;

    public DessertRecipe(String title, String sweetness) {
        super(title, "Dessert");
        this.sweetness = sweetness;
    }

    public String getSweetness() {
        return sweetness;
    }

    public void setSweetness(String sweetness) {
        this.sweetness = sweetness;
    }

    // Polymorphism - Method Overriding
    @Override
    public String displayRecipe() {
        return super.displayRecipe() + "\nSweetness Level: " + sweetness;
    }
}

// Recipe Manager
class RecipeManager {
    private List<Recipe> recipes;

    public RecipeManager() {
        recipes = new ArrayList<>();
        initializeDefaultRecipes();
    }

    private void initializeDefaultRecipes() {
        // 1. Adobo
        MainDishRecipe adobo = new MainDishRecipe("Chicken Adobo", 4);
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

        // 2. Lumpia
        AppetizerRecipe lumpia = new AppetizerRecipe("Lumpia Shanghai", "With sweet chili sauce");
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

        // 3. Sinigang
        MainDishRecipe sinigang = new MainDishRecipe("Sinigang na Baboy", 6);
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

        // 4. Pancit Canton
        MainDishRecipe pancit = new MainDishRecipe("Pancit Canton", 5);
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

        // 5. Halo-Halo
        DessertRecipe halohalo = new DessertRecipe("Halo-Halo", "Medium Sweet");
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

        // 6. Lechon Kawali
        MainDishRecipe lechon = new MainDishRecipe("Lechon Kawali", 4);
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

// Main GUI Application
public class CookingWithKYA extends JFrame {
    private RecipeManager manager;
    private JTabbedPane tabbedPane;

    public CookingWithKYA() {
        manager = new RecipeManager();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("CookingwithKYA - Recipe Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        JLabel titleLabel = new JLabel("CookingwithKYA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerPanel.add(titleLabel);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("All Recipes", createAllRecipesPanel());
        tabbedPane.addTab("Add Recipe", createAddRecipePanel());
        tabbedPane.addTab("Search", createSearchPanel());
        tabbedPane.addTab("Cost Calculator", createCostCalculatorPanel());

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createAllRecipesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headerLabel = new JLabel("Available Recipes");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> recipeList = new JList<>(listModel);
        recipeList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        for (Recipe recipe : manager.getAllRecipes()) {
            listModel.addElement(recipe.getTitle() + " (" + recipe.getCategory() + ")");
        }

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsArea.setMargin(new Insets(10, 10, 10, 10));

        recipeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = recipeList.getSelectedIndex();
                if (index >= 0) {
                    Recipe recipe = manager.getAllRecipes().get(index);
                    detailsArea.setText(recipe.displayRecipe() + "\n\nTotal Cost: ₱" +
                            String.format("%.2f", recipe.computeTotalCost()));
                }
            }
        });

        JButton deleteButton = createStyledButton("Delete Recipe");
        deleteButton.addActionListener(e -> {
            int index = recipeList.getSelectedIndex();
            if (index >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this recipe?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    manager.deleteRecipe(manager.getAllRecipes().get(index));
                    listModel.remove(index);
                    detailsArea.setText("");
                }
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerLabel, BorderLayout.WEST);
        topPanel.add(deleteButton, BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(recipeList), new JScrollPane(detailsArea));
        splitPane.setDividerLocation(300);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAddRecipePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField titleField = createStyledTextField();
        JComboBox<String> categoryBox = new JComboBox<>(new String[] { "Main Dish", "Appetizer", "Dessert" });
        categoryBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextArea instructionsArea = createStyledTextArea(5, 30);
        JTextArea notesArea = createStyledTextArea(3, 30);

        addFormRow(formPanel, gbc, 0, "Recipe Title:", titleField);
        addFormRow(formPanel, gbc, 1, "Category:", categoryBox);
        addFormRow(formPanel, gbc, 2, "Instructions:", new JScrollPane(instructionsArea));
        addFormRow(formPanel, gbc, 3, "Personal Notes:", new JScrollPane(notesArea));

        DefaultTableModel ingredientModel = new DefaultTableModel(
                new String[] { "Ingredient", "Quantity", "Price (₱)" }, 0);
        JTable ingredientTable = new JTable(ingredientModel);
        ingredientTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ingredientTable.setRowHeight(25);

        JPanel ingredientPanel = new JPanel(new BorderLayout(5, 5));
        ingredientPanel.add(new JScrollPane(ingredientTable), BorderLayout.CENTER);

        JPanel ingredientButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addIngButton = createStyledButton("Add Ingredient");
        JButton removeIngButton = createStyledButton("Remove Selected");

        addIngButton.addActionListener(e -> {
            JTextField nameField = new JTextField(15);
            JTextField qtyField = new JTextField(10);
            JTextField priceField = new JTextField(10);

            JPanel ingPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            ingPanel.add(new JLabel("Ingredient Name:"));
            ingPanel.add(nameField);
            ingPanel.add(new JLabel("Quantity:"));
            ingPanel.add(qtyField);
            ingPanel.add(new JLabel("Price (₱):"));
            ingPanel.add(priceField);

            int result = JOptionPane.showConfirmDialog(this, ingPanel,
                    "Add Ingredient", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    ingredientModel.addRow(new Object[] {
                            nameField.getText(),
                            qtyField.getText(),
                            Double.parseDouble(priceField.getText())
                    });
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price format!");
                }
            }
        });

        removeIngButton.addActionListener(e -> {
            int row = ingredientTable.getSelectedRow();
            if (row >= 0) {
                ingredientModel.removeRow(row);
            }
        });

        ingredientButtonPanel.add(addIngButton);
        ingredientButtonPanel.add(removeIngButton);
        ingredientPanel.add(ingredientButtonPanel, BorderLayout.SOUTH);

        JButton saveButton = createStyledButton("Save Recipe");
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a recipe title!");
                return;
            }

            Recipe recipe;
            String category = (String) categoryBox.getSelectedItem();
            if (category.equals("Main Dish")) {
                recipe = new MainDishRecipe(titleField.getText(), 4);
            } else if (category.equals("Appetizer")) {
                recipe = new AppetizerRecipe(titleField.getText(), "Hot");
            } else {
                recipe = new DessertRecipe(titleField.getText(), "Medium");
            }

            recipe.setInstructions(instructionsArea.getText());
            recipe.setPersonalNotes(notesArea.getText());

            for (int i = 0; i < ingredientModel.getRowCount(); i++) {
                String name = (String) ingredientModel.getValueAt(i, 0);
                String qty = (String) ingredientModel.getValueAt(i, 1);
                double price = (Double) ingredientModel.getValueAt(i, 2);
                recipe.addIngredient(new Ingredient(name, qty, price));
            }

            manager.addRecipe(recipe);
            JOptionPane.showMessageDialog(this, "Recipe saved successfully!");

            titleField.setText("");
            instructionsArea.setText("");
            notesArea.setText("");
            ingredientModel.setRowCount(0);

            tabbedPane.setSelectedIndex(0);
        });

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        savePanel.add(saveButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(ingredientPanel, BorderLayout.CENTER);

        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(savePanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField searchField = createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 35));
        JButton searchButton = createStyledButton("Search");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        DefaultListModel<String> resultModel = new DefaultListModel<>();
        JList<String> resultList = new JList<>(resultModel);
        resultList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsArea.setMargin(new Insets(10, 10, 10, 10));

        searchButton.addActionListener(e -> {
            resultModel.clear();
            List<Recipe> results = manager.searchRecipes(searchField.getText());
            for (Recipe recipe : results) {
                resultModel.addElement(recipe.getTitle() + " (" + recipe.getCategory() + ")");
            }
            if (results.isEmpty()) {
                detailsArea.setText("No recipes found matching '" + searchField.getText() + "'");
            }
        });

        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = resultList.getSelectedIndex();
                if (index >= 0) {
                    List<Recipe> results = manager.searchRecipes(searchField.getText());
                    if (index < results.size()) {
                        Recipe recipe = results.get(index);
                        detailsArea.setText(recipe.displayRecipe() + "\n\nTotal Cost: ₱" +
                                String.format("%.2f", recipe.computeTotalCost()));
                    }
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(resultList), new JScrollPane(detailsArea));
        splitPane.setDividerLocation(300);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCostCalculatorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel selectLabel = new JLabel("Select Recipe:");
        selectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JComboBox<String> recipeBox = new JComboBox<>();
        for (Recipe recipe : manager.getAllRecipes()) {
            recipeBox.addItem(recipe.getTitle());
        }
        recipeBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        topPanel.add(selectLabel);
        topPanel.add(recipeBox);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel ingredientsPanel = new JPanel(new BorderLayout(5, 5));
        ingredientsPanel.setBorder(BorderFactory.createTitledBorder("All Ingredients"));

        DefaultListModel<String> allIngredientsModel = new DefaultListModel<>();
        JList<String> allIngredientsList = new JList<>(allIngredientsModel);
        allIngredientsList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ingredientsPanel.add(new JScrollPane(allIngredientsList), BorderLayout.CENTER);

        JPanel availablePanel = new JPanel(new BorderLayout(5, 5));
        availablePanel.setBorder(BorderFactory.createTitledBorder("Available Ingredients"));

        DefaultListModel<String> availableModel = new DefaultListModel<>();
        JList<String> availableList = new JList<>(availableModel);
        availableList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        availablePanel.add(new JScrollPane(availableList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = createStyledButton("Mark as Available >>");
        JButton removeButton = createStyledButton("<< Remove");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        availablePanel.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.add(ingredientsPanel);
        centerPanel.add(availablePanel);

        JPanel costPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        costPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel totalLabel = new JLabel("Total Cost:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel totalValueLabel = new JLabel("₱0.00");
        totalValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel remainingLabel = new JLabel("Remaining Cost:");
        remainingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel remainingValueLabel = new JLabel("₱0.00");
        remainingValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel savedLabel = new JLabel("You Save:");
        savedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel savedValueLabel = new JLabel("₱0.00");
        savedValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        costPanel.add(totalLabel);
        costPanel.add(totalValueLabel);
        costPanel.add(remainingLabel);
        costPanel.add(remainingValueLabel);
        costPanel.add(savedLabel);
        costPanel.add(savedValueLabel);

        Runnable updateCosts = () -> {
            int index = recipeBox.getSelectedIndex();
            if (index >= 0) {
                Recipe recipe = manager.getAllRecipes().get(index);
                double total = recipe.computeTotalCost();

                List<String> available = new ArrayList<>();
                for (int i = 0; i < availableModel.size(); i++) {
                    String item = availableModel.get(i);
                    available.add(item.substring(0, item.indexOf(" -")).toLowerCase());
                }

                double remaining = recipe.computeRemainingCost(available);
                double saved = total - remaining;

                totalValueLabel.setText("₱" + String.format("%.2f", total));
                remainingValueLabel.setText("₱" + String.format("%.2f", remaining));
                savedValueLabel.setText("₱" + String.format("%.2f", saved));
            }
        };

        recipeBox.addActionListener(e -> {
            int index = recipeBox.getSelectedIndex();
            if (index >= 0) {
                Recipe recipe = manager.getAllRecipes().get(index);
                allIngredientsModel.clear();
                for (Ingredient ing : recipe.getIngredients()) {
                    allIngredientsModel.addElement(ing.toString());
                }
                availableModel.clear();
                updateCosts.run();
            }
        });

        addButton.addActionListener(e -> {
            int index = allIngredientsList.getSelectedIndex();
            if (index >= 0) {
                String item = allIngredientsModel.get(index);
                if (!availableModel.contains(item)) {
                    availableModel.addElement(item);
                    updateCosts.run();
                }
            }
        });

        removeButton.addActionListener(e -> {
            int index = availableList.getSelectedIndex();
            if (index >= 0) {
                availableModel.remove(index);
                updateCosts.run();
            }
        });

        if (recipeBox.getItemCount() > 0) {
            recipeBox.setSelectedIndex(0);
        }

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(costPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
        return button;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new CookingWithKYA();
        });
    }
}