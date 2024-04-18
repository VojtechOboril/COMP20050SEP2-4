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
        menuButton = new JButton("Menu");

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "MenuScreen");
            }
        });
        this.setBackground(Color.BLACK);

        scoreText = new JTextArea("You scored " + lastPoints + " points!");
        this.add(scoreText);
        this.add(menuButton);
    }

    public static void setLastPoints(int last) {
        EndScreen.lastPoints = last;
        scoreText.setText("You scored " + lastPoints + " points!");
    }
    
}
