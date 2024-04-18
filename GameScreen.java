import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameScreen extends JPanel {
    private boolean isGameActive = false;
    private Board board;
    private JButton newGameButton, endGameButton;

    public GameScreen(CardLayout cardLayout, JPanel cardPanel) {
        this.setBackground(Color.BLACK);

        board = new Board();
        endGameButton = new JButton("End Game");
        endGameButton.setBackground(Color.red);
        endGameButton.setVisible(false);

        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGameActive();
                Board.showCircles = true;
                EndScreen.setLastPoints(board.calculateScore());
                //board.exit();
                cardLayout.show(cardPanel, "EndScreen");
            }
        });

        this.add(endGameButton);

        newGameButton = new JButton("Board implementation");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGameActive();
                board.initGame();
                board.createAndShowGUI();
            }
        });
        this.add(newGameButton);

        if (Board.showCircles) {
            JButton revealAtomsButton = new JButton("reveal atoms");
            revealAtomsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.checkAtoms();
                    // showRayPaths
                }
            });
            this.add(revealAtomsButton);
        }
    }

    private void updateGameActive() {
        this.isGameActive = !this.isGameActive;
        this.endGameButton.setVisible(this.isGameActive);
        this.newGameButton.setVisible(!this.isGameActive);
    }

}
