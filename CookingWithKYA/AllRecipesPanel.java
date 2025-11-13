import javax.swing.*;
import java.awt.*;

public class AllRecipesPanel extends JPanel {
    private RecipeManager manager;
    private JPanel cardsContainer;
    private JTextArea detailsArea;
    private Recipe selectedRecipe;
    private Runnable refreshCallback;

    public AllRecipesPanel(RecipeManager manager, Runnable refreshCallback) {
        this.manager = manager;
        this.refreshCallback = refreshCallback;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headerLabel = new JLabel("Available Recipes");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        // Recipe cards container
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new GridLayout(0, 2, 15, 15));
        cardsContainer.setBackground(Color.WHITE);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsArea.setMargin(new Insets(10, 10, 10, 10));

        loadRecipes();

        JScrollPane cardsScroll = new JScrollPane(cardsContainer);
        cardsScroll.setPreferredSize(new Dimension(500, 0));
        cardsScroll.getVerticalScrollBar().setUnitIncrement(16);

        JButton editButton = createStyledButton("Edit Recipe");
        editButton.addActionListener(e -> editSelectedRecipe());

        JButton deleteButton = createStyledButton("Delete Recipe");
        deleteButton.addActionListener(e -> deleteSelectedRecipe());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                cardsScroll, new JScrollPane(detailsArea));
        splitPane.setDividerLocation(500);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void loadRecipes() {
        cardsContainer.removeAll();
        for (Recipe recipe : manager.getAllRecipes()) {
            RecipeCardPanel card = new RecipeCardPanel(recipe,
                    () -> {
                        selectedRecipe = recipe;
                        detailsArea.setText(recipe.displayRecipe() + "\n\nTotal Cost: ₱" +
                                String.format("%.2f", recipe.computeTotalCost()));
                    },
                    () -> {
                    });
            cardsContainer.add(card);
        }
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private void editSelectedRecipe() {
        if (selectedRecipe != null) {
            new EditRecipeDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    selectedRecipe, manager, refreshCallback).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a recipe to edit.");
        }
    }

    private void deleteSelectedRecipe() {
        if (selectedRecipe != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this recipe?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteRecipe(selectedRecipe);
                selectedRecipe = null;
                detailsArea.setText("");
                refreshCallback.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a recipe to delete.");
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
        return button;
    }
}