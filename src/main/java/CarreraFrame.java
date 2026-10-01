
package motoresde8bits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CarreraFrame extends JFrame {

    private JComboBox<String> comboJ1, comboJ2;
    private JProgressBar barraJ1, barraJ2, barraCPU;
    private JButton btnArrancar, btnManual;
    private JCheckBox checkModoCPU;

    private Vehiculo v1, v2, vCPU;
    private boolean carreraEnCurso = false;
    private static boolean hayGanador = false;

    public CarreraFrame() {
        // Configuración básica de la ventana
        setTitle("🎮 Motores De 8 Bits - Carrera Extrema 🏁");
        setSize(750, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        String[] listaVehiculos = {
            "Auto Deportivo 🏎️", 
            "Camioneta 🛻", 
            "Moto Súper 🏍️", 
            "Tuk-Tuk Mortal 🛺"
        };

        // --- PANEL SUPERIOR: Configuración ---
        JPanel panelConfig = new JPanel(new GridLayout(2, 3, 10, 8));
        panelConfig.setBorder(BorderFactory.createTitledBorder(" Configuración de Competidores "));

        comboJ1 = new JComboBox<>(listaVehiculos);
        comboJ2 = new JComboBox<>(listaVehiculos);
        checkModoCPU = new JCheckBox("Jugar contra CPU 🤖");

        panelConfig.add(new JLabel(" Jugador 1 (Tecla 'A'):"));
        panelConfig.add(comboJ1);
        panelConfig.add(checkModoCPU);

        panelConfig.add(new JLabel(" Jugador 2 (Flecha Derecha ➡️):"));
        panelConfig.add(comboJ2);

        btnManual = new JButton("📖 Manual de Usuario");
        panelConfig.add(btnManual);

        add(panelConfig, BorderLayout.NORTH);

        // --- PANEL CENTRAL: Pistas de Carrera ---
        JPanel panelPistas = new JPanel(new GridLayout(3, 1, 10, 10));
        panelPistas.setBorder(BorderFactory.createTitledBorder(" Pista de Carreras - ¡Presiona tu tecla para acelerar! "));

        barraJ1 = new JProgressBar(0, 100);
        barraJ1.setStringPainted(true);
        barraJ1.setString("JUGADOR 1 [Tecla A]");

        barraJ2 = new JProgressBar(0, 100);
        barraJ2.setStringPainted(true);
        barraJ2.setString("JUGADOR 2 [Flecha Derecha]");

        barraCPU = new JProgressBar(0, 100);
        barraCPU.setStringPainted(true);
        barraCPU.setString("CPU (Automático)");

        panelPistas.add(barraJ1);
        panelPistas.add(barraJ2);
        panelPistas.add(barraCPU);

        add(panelPistas, BorderLayout.CENTER);

        // --- PANEL INFERIOR: Botón de Salida ---
        btnArrancar = new JButton("🚦 ¡INICIAR CARRERA!");
        btnArrancar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(btnArrancar, BorderLayout.SOUTH);

        // EVITAR QUE LOS BOTONES LE QUITEN EL FOCO AL TECLADO
        btnArrancar.setFocusable(false);
        btnManual.setFocusable(false);
        comboJ1.setFocusable(false);
        comboJ2.setFocusable(false);
        checkModoCPU.setFocusable(false);

        // --- EVENTOS DE BOTONES ---
        btnManual.addActionListener((ActionEvent e) -> abrirManualDivertido());
        btnArrancar.addActionListener((ActionEvent e) -> comenzarCarrera());

        checkModoCPU.addActionListener((ActionEvent e) -> {
            comboJ2.setEnabled(!checkModoCPU.isSelected());
        });

        // --- CONTROLES POR TECLADO (KeyEventListener) ---
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!carreraEnCurso || hayGanador) return;

                // Tecla para Jugador 1: Letra 'A' o 'a'
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    avanzarJugador(v1, barraJ1, "JUGADOR 1");
                }

                // Tecla para Jugador 2: Flecha Derecha (VK_RIGHT)
                if (!checkModoCPU.isSelected() && e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    avanzarJugador(v2, barraJ2, "JUGADOR 2");
                }
            }
        });
    }

    private void avanzarJugador(Vehiculo vehiculo, JProgressBar barra, String nombreJugador) {
        if (vehiculo == null || hayGanador) return;

        vehiculo.avanzar();
        barra.setValue(vehiculo.getPosicion());

        // Verificar si ganó
        if (vehiculo.getPosicion() >= 100 && !hayGanador) {
            hayGanador = true;
            carreraEnCurso = false;
            JOptionPane.showMessageDialog(this,
                    "🏆 ¡" + nombreJugador + " (" + vehiculo.getNombre() + ") HA GANADO LA CARRERA! 🏁",
                    "¡Victoria!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void abrirManualDivertido() {
        String textoManual = "🏎️ MANUAL DE COMPETENCIA - MOTORES DE 8 BITS 🏎️\n\n"
                + "🎮 CONTROLES DEL TECLADO:\n"
                + "   • Jugador 1: Presiona la tecla 'A' repetidamente para acelerar.\n"
                + "   • Jugador 2: Presiona la 'Flecha Derecha (➡️)' para acelerar.\n\n"
                + "🚗 VEHÍCULOS Y SUS CARACTERÍSTICAS:\n"
                + "   • Auto Deportivo: Picos de turbo aleatorios, muy veloz. 🏎️️\n"
                + "   • Camioneta: Robusta y constante, avanza firme. 🛻\n"
                + "   • Moto Súper: Rápida con impulsos inestables. 🏍️️\n"
                + "   • Tuk-Tuk Mortal: ¡Diversión e impredecibilidad total! 🛺\n\n"
                + "🏁 OBJETIVO:\n"
                + "   ¡Presiona tu tecla lo más rápido posible para llegar al 100% antes que tu oponente!";
        JOptionPane.showMessageDialog(this, textoManual, "Manual del Piloto", JOptionPane.INFORMATION_MESSAGE);
    }

    private Vehiculo fabricarVehiculo(int opcion, String piloto) {
        switch (opcion) {
            case 0: return new AutoDeportivo(piloto);
            case 1: return new Camioneta(piloto);
            case 2: return new MotoSuper(piloto);
            case 3: return new TukTukMortal(piloto);
            default: return new AutoDeportivo(piloto);
        }
    }

    private void comenzarCarrera() {
        // Reiniciar estado
        barraJ1.setValue(0);
        barraJ2.setValue(0);
        barraCPU.setValue(0);
        hayGanador = false;
        carreraEnCurso = true;

        // Instanciar vehículos
        v1 = fabricarVehiculo(comboJ1.getSelectedIndex(), "Jugador 1");
        v2 = fabricarVehiculo(comboJ2.getSelectedIndex(), "Jugador 2");

        // Devolver el foco a la ventana para capturar los eventos del teclado
        this.requestFocusInWindow();

        // Si se juega contra CPU, se activa su hilo de avance automático (con try-catch)
        if (checkModoCPU.isSelected()) {
            vCPU = fabricarVehiculo((int) (Math.random() * 4), "CPU Bot");
            new Thread(() -> {
                while (carreraEnCurso && !hayGanador && vCPU.getPosicion() < 100) {
                    vCPU.avanzar();
                    barraCPU.setValue(vCPU.getPosicion());

                    // TRY-CATCH para el ritmo de avance de la CPU
                    try {
                        Thread.sleep((long) (Math.random() * 250 + 150));
                    } catch (InterruptedException ex) {
                        System.err.println("Error en hilo CPU: " + ex.getMessage());
                        Thread.currentThread().interrupt();
                    }

                    if (vCPU.getPosicion() >= 100 && !hayGanador) {
                        hayGanador = true;
                        carreraEnCurso = false;
                        JOptionPane.showMessageDialog(CarreraFrame.this,
                                "🤖 ¡LA CPU (" + vCPU.getNombre() + ") HA GANADO LA CARRERA! 🏁",
                                "Derrota",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }).start();
        }
    }
}
