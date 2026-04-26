import javax.swing.SwingUtilities;

public class Main {
    // Main method to launch the application by creating an instance of the
    // LoginFrame
    public static void main(String[] args) {
        // Using SwingUtilities.invokeLater to ensure that the GUI is created and
        // updated on the Event Dispatch Thread (EDT), which is the proper way to handle
        // Swing components for thread safety
        SwingUtilities.invokeLater(LoginFrame::new);
    }

}
