import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EndScreen extends JPanel {
    public EndScreen(CardLayout cardLayout, JPanel cardPanel) {
        JButton menuButton = new JButton("Menu");

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "MenuScreen");
            }
        });

        this.add(menuButton); 
    }
    
}
