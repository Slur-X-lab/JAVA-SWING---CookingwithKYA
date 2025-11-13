import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;

public class AddRecipePanel extends JPanel {
    private RecipeManager manager;
    private JTextField titleField;
    private JComboBox<String> categoryBox;
    private JTextField imagePathField;
    private JTextArea instructionsArea;
    private JTextArea notesArea;
    private DefaultTableModel ingredientModel;
    private JTable ingredientTable;
    private Runnable refreshCallback;
    private Runnable switchToAllRecipesCallback;

    public AddRecipePanel(RecipeManager manager, Runnable refreshCallback, Runnable switchToAllRecipesCallback) {
        this.manager = manager;
        this.refreshCallback = refreshCallback;
        this.switchToAllRecipesCallback = switchToAllRecipesCallback;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        titleField = createStyledTextField();
        categoryBox = new JComboBox<>(new String[] { "Main Dish", "Appetizer", "Dessert" });
        categoryBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Image selection
        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        imagePathField = createStyledTextField();
        imagePathField.setEditable(false);
        JButton browseButton = new JButton("Browse");
        browseButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        browseButton.addActionListener(e -> browseForImage());
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.EAST);

        instructionsArea = createStyledTextArea(5, 30);
        notesArea = createStyledTextArea(3, 30);

        addFormRow(formPanel, gbc, 0, "Recipe Title:", titleField);
        addFormRow(formPanel, gbc, 1, "Category:", categoryBox);
        addFormRow(formPanel, gbc, 2, "Recipe Image:", imagePanel);
        addFormRow(formPanel, gbc, 3, "Instructions:", new JScrollPane(instructionsArea));
        addFormRow(formPanel, gbc, 4, "Personal Notes:", new JScrollPane(notesArea));

        ingredientModel = new DefaultTableModel(
                new String[] { "Ingredient", "Quantity", "Price (₱)" }, 0);
        ingredientTable = new JTable(ingredientModel);
        ingredientTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ingredientTable.setRowHeight(25);

        JPanel ingredientPanel = new JPanel(new BorderLayout(5, 5));
        ingredientPanel.add(new JScrollPane(ingredientTable), BorderLayout.CENTER);

        JPanel ingredientButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addIngButton = createStyledButton("Add Ingredient");
        JButton removeIngButton = createStyledButton("Remove Selected");

        addIngButton.addActionListener(e -> addIngredient());
        removeIngButton.addActionListener(e -> removeIngredient());

        ingredientButtonPanel.add(addIngButton);
        ingredientButtonPanel.add(removeIngButton);
        ingredientPanel.add(ingredientButtonPanel, BorderLayout.SOUTH);

        JButton saveButton = createStyledButton("Save Recipe");
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.addActionListener(e -> saveRecipe());

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        savePanel.add(saveButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(ingredientPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(savePanel, BorderLayout.SOUTH);
    }

    private void browseForImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void addIngredient() {
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
    }

    private void removeIngredient() {
        int row = ingredientTable.getSelectedRow();
        if (row >= 0) {
            ingredientModel.removeRow(row);
        }
    }

    private void saveRecipe() {
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

        clearForm();
        refreshCallback.run();
        switchToAllRecipesCallback.run();
    }

    private void clearForm() {
        titleField.setText("");
        imagePathField.setText("");
        instructionsArea.setText("");
        notesArea.setText("");
        ingredientModel.setRowCount(0);
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
}