import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EndScreen extends JPanel {
    private JButton menuButton;
    private static JTextArea scoreText;
    public static int lastPoints;

    public EndScreen(CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Adds space above and below the component

        scoreText = new JTextArea("You scored " + lastPoints + " points!");
        scoreText.setEditable(false);
        scoreText.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(scoreText, gbc);

        gbc.insets = new Insets(10, 0, 0, 0); // Resets insets for the menu button
        menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 16));
        menuButton.setPreferredSize(new Dimension(150, 50));
        menuButton.setBackground(Color.PINK);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "MenuScreen");
            }
        });
        add(menuButton, gbc);

        setBackground(Color.BLACK);
    }

    public static void setLastPoints(int last) {
        EndScreen.lastPoints = last;
        scoreText.setText("You scored " + lastPoints + " points!");
    }
}
