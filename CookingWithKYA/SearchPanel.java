import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchPanel extends JPanel {
    private RecipeManager manager;
    private JTextField searchField;
    private JPanel cardsContainer;
    private JTextArea detailsArea;

    public SearchPanel(RecipeManager manager) {
        this.manager = manager;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchField = createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 35));
        JButton searchButton = createStyledButton("Search");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        cardsContainer = new JPanel();
        cardsContainer.setLayout(new GridLayout(0, 2, 15, 15));
        cardsContainer.setBackground(Color.WHITE);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsArea.setMargin(new Insets(10, 10, 10, 10));

        searchButton.addActionListener(e -> performSearch());

        // Allow Enter key to search
        searchField.addActionListener(e -> performSearch());

        JScrollPane cardsScroll = new JScrollPane(cardsContainer);
        cardsScroll.setPreferredSize(new Dimension(500, 0));
        cardsScroll.getVerticalScrollBar().setUnitIncrement(16);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                cardsScroll, new JScrollPane(detailsArea));
        splitPane.setDividerLocation(500);

        add(searchPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void performSearch() {
        cardsContainer.removeAll();
        List<Recipe> results = manager.searchRecipes(searchField.getText());

        if (results.isEmpty()) {
            detailsArea.setText("No recipes found matching '" + searchField.getText() + "'");
        } else {
            detailsArea.setText("Found " + results.size() + " recipe(s). Click a recipe card to view details.");
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
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
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