
package motoresde8bits;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class SimuladorCarrera extends Thread {
    private Vehiculo auto;
    private JProgressBar barraMeta;
    private static boolean hayGanador = false;

    public SimuladorCarrera(Vehiculo auto, JProgressBar barraMeta) {
        this.auto = auto;
        this.barraMeta = barraMeta;
    }

    @Override
    public void run() {
        while (!hayGanador && auto.getPosicion() < 100) {
            auto.avanzar();
            barraMeta.setValue(auto.getPosicion());

            // APLICACIÓN DE MANEJO DE EXCEPCIONES (TRY-CATCH)
            try {
                // Pausa aleatoria para simular la aceleración y ritmo en la pista
                Thread.sleep((long) (Math.random() * 250 + 150));
            } catch (InterruptedException e) {
                System.out.println("Error durante la simulación de " + auto.getNombre() + ": " + e.getMessage());
                Thread.currentThread().interrupt();
            }

            // Validar si el vehículo cruzó la meta
            if (auto.getPosicion() >= 100 && !hayGanador) {
                hayGanador = true;
                JOptionPane.showMessageDialog(null, 
                    "🏆 ¡" + auto.getNombre() + " HA GANADO LA CARRERA! 🏁",
                    "¡Victoria!", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void reiniciarSimulacion() {
        hayGanador = false;
    }
}
