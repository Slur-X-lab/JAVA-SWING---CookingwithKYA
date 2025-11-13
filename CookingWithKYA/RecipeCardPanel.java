import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Custom Recipe Card Panel
public class RecipeCardPanel extends JPanel {
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