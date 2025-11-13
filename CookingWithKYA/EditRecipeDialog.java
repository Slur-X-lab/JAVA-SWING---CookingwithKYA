import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;

public class EditRecipeDialog extends JDialog {
    private Recipe recipe;
    private RecipeManager manager;
    private Runnable refreshCallback;

    public EditRecipeDialog(Frame parent, Recipe recipe, RecipeManager manager, Runnable refreshCallback) {
        super(parent, "Edit Recipe - " + recipe.getTitle(), true);
        this.recipe = recipe;
        this.manager = manager;
        this.refreshCallback = refreshCallback;

        setSize(800, 700);
        setLocationRelativeTo(parent);
        initializeUI();
    }

    private void initializeUI() {
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
            int result = fileChooser.showOpenDialog(this);
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveButton = createStyledButton("Save Changes");
        JButton cancelButton = createStyledButton("Cancel");

        saveButton.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a recipe title!");
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

            JOptionPane.showMessageDialog(this, "Recipe updated successfully!");
            dispose();
            refreshCallback.run();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(ingredientPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
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