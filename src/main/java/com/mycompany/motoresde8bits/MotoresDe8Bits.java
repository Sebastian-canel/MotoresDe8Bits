
package motoresde8bits;

public class MotoresDe8Bits {

    public static void main(String[] args) {
        // Lanza la ventana de la carrera en el hilo de la interfaz gráfica
        java.awt.EventQueue.invokeLater(() -> {
            new CarreraGameFrame().setVisible(true);
        });
    }
}
