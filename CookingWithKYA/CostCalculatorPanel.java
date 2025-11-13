import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CostCalculatorPanel extends JPanel {
    private RecipeManager manager;
    private JComboBox<String> recipeBox;
    private DefaultListModel<String> allIngredientsModel;
    private DefaultListModel<String> availableModel;
    private JLabel totalValueLabel;
    private JLabel remainingValueLabel;
    private JLabel savedValueLabel;

    public CostCalculatorPanel(RecipeManager manager) {
        this.manager = manager;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel selectLabel = new JLabel("Select Recipe:");
        selectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        recipeBox = new JComboBox<>();
        for (Recipe recipe : manager.getAllRecipes()) {
            recipeBox.addItem(recipe.getTitle());
        }
        recipeBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        topPanel.add(selectLabel);
        topPanel.add(recipeBox);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // All Ingredients Panel
        JPanel ingredientsPanel = new JPanel(new BorderLayout(5, 5));
        ingredientsPanel.setBorder(BorderFactory.createTitledBorder("All Ingredients"));

        allIngredientsModel = new DefaultListModel<>();
        JList<String> allIngredientsList = new JList<>(allIngredientsModel);
        allIngredientsList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ingredientsPanel.add(new JScrollPane(allIngredientsList), BorderLayout.CENTER);

        // Available Ingredients Panel
        JPanel availablePanel = new JPanel(new BorderLayout(5, 5));
        availablePanel.setBorder(BorderFactory.createTitledBorder("Available Ingredients"));

        availableModel = new DefaultListModel<>();
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

        // Cost Display Panel
        JPanel costPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        costPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel totalLabel = new JLabel("Total Cost:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalValueLabel = new JLabel("₱0.00");
        totalValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel remainingLabel = new JLabel("Remaining Cost:");
        remainingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        remainingValueLabel = new JLabel("₱0.00");
        remainingValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel savedLabel = new JLabel("You Save:");
        savedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        savedValueLabel = new JLabel("₱0.00");
        savedValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        savedValueLabel.setForeground(new Color(0, 128, 0));

        costPanel.add(totalLabel);
        costPanel.add(totalValueLabel);
        costPanel.add(remainingLabel);
        costPanel.add(remainingValueLabel);
        costPanel.add(savedLabel);
        costPanel.add(savedValueLabel);

        // Event Listeners
        recipeBox.addActionListener(e -> loadRecipeIngredients());

        addButton.addActionListener(e -> {
            int index = allIngredientsList.getSelectedIndex();
            if (index >= 0) {
                String item = allIngredientsModel.get(index);
                if (!availableModel.contains(item)) {
                    availableModel.addElement(item);
                    updateCosts();
                }
            }
        });

        removeButton.addActionListener(e -> {
            int index = availableList.getSelectedIndex();
            if (index >= 0) {
                availableModel.remove(index);
                updateCosts();
            }
        });

        if (recipeBox.getItemCount() > 0) {
            recipeBox.setSelectedIndex(0);
        }

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(costPanel, BorderLayout.SOUTH);
    }

    private void loadRecipeIngredients() {
        int index = recipeBox.getSelectedIndex();
        if (index >= 0) {
            Recipe recipe = manager.getAllRecipes().get(index);
            allIngredientsModel.clear();
            for (Ingredient ing : recipe.getIngredients()) {
                allIngredientsModel.addElement(ing.toString());
            }
            availableModel.clear();
            updateCosts();
        }
    }

    private void updateCosts() {
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
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 35));
        return button;
    }
}