import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameScreen extends JPanel {
    private boolean isGameActive = false;
    private Board board;
    private JButton newGameButton, endGameButton;
    private JLabel title, instructions; // Add JLabel for instructions

    public GameScreen(CardLayout cardLayout, JPanel cardPanel) {
        this.setBackground(Color.BLACK);

        board = new Board();
        endGameButton = new JButton("End Game");
        newGameButton = new JButton("Start");
        endGameButton.setBackground(Color.RED);
        newGameButton.setBackground(Color.PINK);
        endGameButton.setVisible(false);

        title = new JLabel("Press Start To Play", JLabel.CENTER);
        title.setForeground(Color.WHITE);

        // Create instructions label
        instructions = new JLabel("<html><b>How to play:</b><br/>" +
                "1) Press hexagons on the <b>perimeter</b> to send out rays i.e pressing the upper half sends them from the upper halves direction, and vice versa for lower half<br/>" +
                "2) See where the rays go using the ray markers<br/>"+
                "3) <b>Red</b> ray marker means ray got absorbed, <b>blue</b> otherwise<br/>" +
                "4) Press on hexagons in the <b>center</b> to guess where the atoms are<br/>" +
                "5) Minimize the screen and press <b>end game</b> on this screen to get a score</html>");
        instructions.setForeground(Color.WHITE);
        instructions.setHorizontalAlignment(JLabel.CENTER);
        instructions.setPreferredSize(new Dimension(400, instructions.getPreferredSize().height+50));
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

        // Add instructions label to the panel
        this.add(instructions, gbc);
        // Add title and buttons to the panel
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
        this.title.setText(this.isGameActive ? "Game in Progress." : "Press Start To Play");
    }
}
