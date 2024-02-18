import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel {
    public Board() {
        JButton endGameButton = new JButton("Board implementation");
        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });




        this.add(endGameButton);

        // hexagon_grid = ...;
        // this.add(hexagon_grid)
    }
    private void startGame() {
        SwingUtilities.invokeLater(Hexagon::new);
    }
}
