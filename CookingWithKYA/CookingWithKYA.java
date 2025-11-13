import javax.swing.*;
import java.awt.*;

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

        // Create panels
        AllRecipesPanel allRecipesPanel = new AllRecipesPanel(manager, this::refreshAllRecipesPanel);
        AddRecipePanel addRecipePanel = new AddRecipePanel(manager,
                this::refreshAllRecipesPanel,
                () -> tabbedPane.setSelectedIndex(0));
        SearchPanel searchPanel = new SearchPanel(manager);
        CostCalculatorPanel costCalculatorPanel = new CostCalculatorPanel(manager);

        tabbedPane.addTab("All Recipes", allRecipesPanel);
        tabbedPane.addTab("Add Recipe", addRecipePanel);
        tabbedPane.addTab("Search", searchPanel);
        tabbedPane.addTab("Cost Calculator", costCalculatorPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void refreshAllRecipesPanel() {
        // Refresh the All Recipes panel
        AllRecipesPanel newPanel = new AllRecipesPanel(manager, this::refreshAllRecipesPanel);
        tabbedPane.setComponentAt(0, newPanel);

        // Refresh Cost Calculator panel
        CostCalculatorPanel newCostPanel = new CostCalculatorPanel(manager);
        tabbedPane.setComponentAt(3, newCostPanel);
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