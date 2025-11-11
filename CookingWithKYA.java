import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

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

// Image Utilities
class ImageUtils {
    public static ImageIcon loadAndScaleImage(String path, int width, int height) {
        if (path == null || path.isEmpty()) {
            return createPlaceholderImage(width, height);
        }

        try {
            File file = new File(path);
            if (!file.exists()) {
                return createPlaceholderImage(width, height);
            }

            BufferedImage originalImage = ImageIO.read(file);
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            return createPlaceholderImage(width, height);
        }
    }

    private static ImageIcon createPlaceholderImage(int width, int height) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(new Color(180, 180, 180));
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
        String text = "🍽️";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.dispose();
        return new ImageIcon(placeholder);
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
        MainDishRecipe adobo = new MainDishRecipe("Chicken Adobo", 4, null);
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
        AppetizerRecipe lumpia = new AppetizerRecipe("Lumpia Shanghai", "With sweet chili sauce", null);
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
        MainDishRecipe sinigang = new MainDishRecipe("Sinigang na Baboy", 6, null);
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
        MainDishRecipe pancit = new MainDishRecipe("Pancit Canton", 5, null);
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
        DessertRecipe halohalo = new DessertRecipe("Halo-Halo", "Medium Sweet", null);
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
        MainDishRecipe lechon = new MainDishRecipe("Lechon Kawali", 4, null);
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

// Custom Recipe Card Panel
class RecipeCardPanel extends JPanel {
    private JLabel imageLabel;

    public RecipeCardPanel(Recipe recipe, Runnable onSelect, Runnable onDelete) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        setBackground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(245, 245, 245));
        imagePanel.setPreferredSize(new Dimension(180, 180));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        ImageIcon recipeImage = ImageUtils.loadAndScaleImage(recipe.getImagePath(), 180, 180);
        imageLabel.setIcon(recipeImage);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(recipe.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel categoryLabel = new JLabel(recipe.getCategory());
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryLabel.setForeground(new Color(100, 100, 100));
        categoryLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel costLabel = new JLabel("Cost: ₱" + String.format("%.2f", recipe.computeTotalCost()));
        costLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        costLabel.setForeground(new Color(0, 128, 0));
        costLabel.setAlignmentX(LEFT_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(categoryLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(costLabel);

        add(imagePanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);

        // Click to select
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onSelect.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(240, 240, 255));
                infoPanel.setBackground(new Color(240, 240, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.WHITE);
                infoPanel.setBackground(Color.WHITE);
            }
        });
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
        setSize(1100, 750);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(1100, 80));
        headerPanel.setBackground(new Color(255, 250, 240));
        JLabel titleLabel = new JLabel("🍳 CookingwithKYA");
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

        // Recipe cards container
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new GridLayout(0, 2, 15, 15));
        cardsContainer.setBackground(Color.WHITE);

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsArea.setMargin(new Insets(10, 10, 10, 10));

        final Recipe[] selectedRecipe = { null };

        for (Recipe recipe : manager.getAllRecipes()) {
            RecipeCardPanel card = new RecipeCardPanel(recipe,
                    () -> {
                        selectedRecipe[0] = recipe;
                        detailsArea.setText(recipe.displayRecipe() + "\n\nTotal Cost: ₱" +
                                String.format("%.2f", recipe.computeTotalCost()));
                    },
                    () -> {
                    });
            cardsContainer.add(card);
        }

        JScrollPane cardsScroll = new JScrollPane(cardsContainer);
        cardsScroll.setPreferredSize(new Dimension(500, 0));
        cardsScroll.getVerticalScrollBar().setUnitIncrement(16);

        JButton editButton = createStyledButton("Edit Recipe");
        editButton.addActionListener(e -> {
            if (selectedRecipe[0] != null) {
                showEditRecipeDialog(selectedRecipe[0]);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a recipe to edit.");
            }
        });

        JButton deleteButton = createStyledButton("Delete Recipe");
        deleteButton.addActionListener(e -> {
            if (selectedRecipe[0] != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this recipe?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    manager.deleteRecipe(selectedRecipe[0]);
                    refreshAllRecipesPanel();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a recipe to delete.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                cardsScroll, new JScrollPane(detailsArea));
        splitPane.setDividerLocation(500);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private void showEditRecipeDialog(Recipe recipe) {
        JDialog editDialog = new JDialog(this, "Edit Recipe - " + recipe.getTitle(), true);
        editDialog.setSize(800, 700);
        editDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField titleField = createStyledTextField();
        titleField.setText(recipe.getTitle());

        JComboBox<String> categoryBox = new JComboBox<>(new String[] { "Main Dish", "Appetizer", "Dessert" });
        categoryBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryBox.setSelectedItem(recipe.getCategory());

        // Image selection
        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        JTextField imagePathField = createStyledTextField();
        imagePathField.setText(recipe.getImagePath() != null ? recipe.getImagePath() : "");
        imagePathField.setEditable(false);
        JButton browseButton = new JButton("Browse");
        browseButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
            int result = fileChooser.showOpenDialog(editDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        JButton clearImageButton = new JButton("Clear");
        clearImageButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearImageButton.addActionListener(e -> imagePathField.setText(""));

        JPanel imageButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        imageButtonPanel.add(browseButton);
        imageButtonPanel.add(clearImageButton);

        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(imageButtonPanel, BorderLayout.EAST);

        JTextArea instructionsArea = createStyledTextArea(5, 30);
        instructionsArea.setText(recipe.getInstructions());

        JTextArea notesArea = createStyledTextArea(3, 30);
        notesArea.setText(recipe.getPersonalNotes());

        addFormRow(formPanel, gbc, 0, "Recipe Title:", titleField);
        addFormRow(formPanel, gbc, 1, "Category:", categoryBox);
        addFormRow(formPanel, gbc, 2, "Recipe Image:", imagePanel);
        addFormRow(formPanel, gbc, 3, "Instructions:", new JScrollPane(instructionsArea));
        addFormRow(formPanel, gbc, 4, "Personal Notes:", new JScrollPane(notesArea));

        DefaultTableModel ingredientModel = new DefaultTableModel(
                new String[] { "Ingredient", "Quantity", "Price (₱)" }, 0);

        // Load existing ingredients
        for (Ingredient ing : recipe.getIngredients()) {
            ingredientModel.addRow(new Object[] {
                    ing.getName(),
                    ing.getQuantity(),
                    ing.getPrice()
            });
        }

        JTable ingredientTable = new JTable(ingredientModel);
        ingredientTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ingredientTable.setRowHeight(25);

        JPanel ingredientPanel = new JPanel(new BorderLayout(5, 5));
        ingredientPanel.setBorder(BorderFactory.createTitledBorder("Ingredients"));
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

            int result = JOptionPane.showConfirmDialog(editDialog, ingPanel,
                    "Add Ingredient", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    ingredientModel.addRow(new Object[] {
                            nameField.getText(),
                            qtyField.getText(),
                            Double.parseDouble(priceField.getText())
                    });
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(editDialog, "Invalid price format!");
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveButton = createStyledButton("Save Changes");
        JButton cancelButton = createStyledButton("Cancel");

        saveButton.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(editDialog, "Please enter a recipe title!");
                return;
            }

            // Update recipe properties
            recipe.setTitle(titleField.getText());
            recipe.setCategory((String) categoryBox.getSelectedItem());
            String imagePath = imagePathField.getText().trim();
            recipe.setImagePath(imagePath.isEmpty() ? null : imagePath);
            recipe.setInstructions(instructionsArea.getText());
            recipe.setPersonalNotes(notesArea.getText());

            // Clear and reload ingredients
            recipe.getIngredients().clear();
            for (int i = 0; i < ingredientModel.getRowCount(); i++) {
                String name = (String) ingredientModel.getValueAt(i, 0);
                String qty = (String) ingredientModel.getValueAt(i, 1);
                double price = (Double) ingredientModel.getValueAt(i, 2);
                recipe.addIngredient(new Ingredient(name, qty, price));
            }

            JOptionPane.showMessageDialog(editDialog, "Recipe updated successfully!");
            editDialog.dispose();
            refreshAllRecipesPanel();
        });

        cancelButton.addActionListener(e -> editDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(ingredientPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        editDialog.add(mainPanel);
        editDialog.setVisible(true);
    }

    private void refreshAllRecipesPanel() {
        tabbedPane.setComponentAt(0, createAllRecipesPanel());
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

        // Image selection
        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        JTextField imagePathField = createStyledTextField();
        imagePathField.setEditable(false);
        JButton browseButton = new JButton("Browse");
        browseButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.EAST);

        JTextArea instructionsArea = createStyledTextArea(5, 30);
        JTextArea notesArea = createStyledTextArea(3, 30);

        addFormRow(formPanel, gbc, 0, "Recipe Title:", titleField);
        addFormRow(formPanel, gbc, 1, "Category:", categoryBox);
        addFormRow(formPanel, gbc, 2, "Recipe Image:", imagePanel);
        addFormRow(formPanel, gbc, 3, "Instructions:", new JScrollPane(instructionsArea));
        addFormRow(formPanel, gbc, 4, "Personal Notes:", new JScrollPane(notesArea));

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
            String imagePath = imagePathField.getText().trim();
            if (imagePath.isEmpty())
                imagePath = null;

            if (category.equals("Main Dish")) {
                recipe = new MainDishRecipe(titleField.getText(), 4, imagePath);
            } else if (category.equals("Appetizer")) {
                recipe = new AppetizerRecipe(titleField.getText(), "Hot", imagePath);
            } else {
                recipe = new DessertRecipe(titleField.getText(), "Medium", imagePath);
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
            imagePathField.setText("");
            instructionsArea.setText("");
            notesArea.setText("");
            ingredientModel.setRowCount(0);

            refreshAllRecipesPanel();
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

        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new GridLayout(0, 2, 15, 15));
        cardsContainer.setBackground(Color.WHITE);

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsArea.setMargin(new Insets(10, 10, 10, 10));

        searchButton.addActionListener(e -> {
            cardsContainer.removeAll();
            List<Recipe> results = manager.searchRecipes(searchField.getText());

            if (results.isEmpty()) {
                detailsArea.setText("No recipes found matching '" + searchField.getText() + "'");
            } else {
                for (Recipe recipe : results) {
                    RecipeCardPanel card = new RecipeCardPanel(recipe,
                            () -> {
                                detailsArea.setText(recipe.displayRecipe() + "\n\nTotal Cost: ₱" +
                                        String.format("%.2f", recipe.computeTotalCost()));
                            },
                            () -> {
                            });
                    cardsContainer.add(card);
                }
            }
            cardsContainer.revalidate();
            cardsContainer.repaint();
        });

        JScrollPane cardsScroll = new JScrollPane(cardsContainer);
        cardsScroll.setPreferredSize(new Dimension(500, 0));
        cardsScroll.getVerticalScrollBar().setUnitIncrement(16);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                cardsScroll, new JScrollPane(detailsArea));
        splitPane.setDividerLocation(500);

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
        savedValueLabel.setForeground(new Color(0, 128, 0));

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
                    // Extract ingredient name from the display string
                    String ingredientName = item.split(" - ")[0];
                    // Remove quantity part (everything before the last space and ingredient name)
                    int lastSpace = ingredientName.lastIndexOf(' ');
                    if (lastSpace > 0) {
                        ingredientName = ingredientName.substring(lastSpace + 1);
                    }
                    available.add(ingredientName.toLowerCase().trim());
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