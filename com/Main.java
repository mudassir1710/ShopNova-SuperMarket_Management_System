import javax.swing.SwingUtilities;
import com.shopnova.ui.LoginFrame;
public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }

}