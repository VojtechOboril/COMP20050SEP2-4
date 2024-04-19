import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameScreen extends JPanel {
    private boolean isGameActive = false;
    private Board board;
    private JButton newGameButton, endGameButton;
    private JLabel title;

    public GameScreen(CardLayout cardLayout, JPanel cardPanel) {
        this.setBackground(Color.BLACK);

        board = new Board();
        endGameButton = new JButton("End Game");
        newGameButton = new JButton("New Game");
        endGameButton.setBackground(Color.RED);
        newGameButton.setBackground(Color.PINK);
        endGameButton.setVisible(false);

        title = new JLabel("Press to begin...", JLabel.CENTER);
        title.setForeground(Color.WHITE);

        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGameActive();
                Board.showCircles = true;
                EndScreen.setLastPoints(board.calculateScore());
                cardLayout.show(cardPanel, "EndScreen");
            }
        });
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGameActive();
                board.initGame();
                title.setText("Game in Progress.");
                board.createAndShowGUI();
            }
        });

        // Set layout to center the buttons
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Make buttons bigger
        Dimension buttonSize = new Dimension(200, 50);
        newGameButton.setPreferredSize(buttonSize);
        endGameButton.setPreferredSize(buttonSize);

        // Add buttons and title to the panel
        this.add(title, gbc);
        this.add(newGameButton, gbc);
        this.add(endGameButton, gbc);

        if (Board.showCircles) {
            JButton revealAtomsButton = new JButton("Reveal Atoms");
            revealAtomsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.checkAtoms();
                    // showRayPaths
                }
            });
            revealAtomsButton.setPreferredSize(buttonSize);
            this.add(revealAtomsButton, gbc);
        }
    }

    private void updateGameActive() {
        this.isGameActive = !this.isGameActive;
        this.endGameButton.setVisible(this.isGameActive);
        this.newGameButton.setVisible(!this.isGameActive);
    }
}
