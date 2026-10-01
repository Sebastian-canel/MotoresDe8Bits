
package motoresde8bits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class CarreraGameFrame extends JFrame {

    private PanelCarril carrilJ1, carrilJ2, carrilCPU;
    private JButton btnIniciar, btnManual;
    private JComboBox<String> comboJ1, comboJ2;
    private JCheckBox checkCPU;

    private boolean carreraEnCurso = false;
    private final Random random = new Random();

    public CarreraGameFrame() {
        setTitle("🎮 Motores De 8 Bits - Carrera Vertical 🏎️");
        setSize(900, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(20, 20, 20));

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.setBackground(new Color(35, 35, 35));

        String[] opciones = {"Auto Deportivo 🏎️", "Camioneta 🛻", "Moto Súper 🏍️", "Tuk-Tuk 🛺"};
        comboJ1 = new JComboBox<>(opciones);
        comboJ2 = new JComboBox<>(opciones);
        checkCPU = new JCheckBox("Modo CPU 🤖", true);
        checkCPU.setForeground(Color.WHITE);
        checkCPU.setOpaque(false);

        btnIniciar = new JButton("🚦 Iniciar Carrera");
        btnManual = new JButton("📖 Manual");

        panelSuperior.add(new JLabel("<html><font color='white'>J1 (W/A/S/D):</font></html>"));
        panelSuperior.add(comboJ1);
        panelSuperior.add(new JLabel("<html><font color='white'>J2 (Flechas):</font></html>"));
        panelSuperior.add(comboJ2);
        panelSuperior.add(checkCPU);
        panelSuperior.add(btnIniciar);
        panelSuperior.add(btnManual);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL: CARRILES VERTICALES ---
        JPanel panelPista = new JPanel(new GridLayout(1, 3, 15, 0));
        panelPista.setBackground(new Color(20, 20, 20));
        panelPista.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        carrilJ1 = new PanelCarril("Jugador 1", Color.RED);
        carrilJ2 = new PanelCarril("Jugador 2", Color.CYAN);
        carrilCPU = new PanelCarril("CPU Bot", Color.YELLOW);

        panelPista.add(carrilJ1);
        panelPista.add(carrilJ2);
        panelPista.add(carrilCPU);

        add(panelPista, BorderLayout.CENTER);

        // Evitar que los botones roben el foco del teclado
        comboJ1.setFocusable(false);
        comboJ2.setFocusable(false);
        checkCPU.setFocusable(false);
        btnIniciar.setFocusable(false);
        btnManual.setFocusable(false);

        btnIniciar.addActionListener(e -> comenzarCarrera());
        btnManual.addActionListener(e -> mostrarManual());

        // CONTROLES DE TECLADO (AVANZAR Y MOVER A LOS LADOS)
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!carreraEnCurso) return;

                int key = e.getKeyCode();

                // --- CONTROLES JUGADOR 1 ---
                if (key == KeyEvent.VK_W || key == KeyEvent.VK_Z) {
                    avanzarVehiculo(carrilJ1, "JUGADOR 1");
                }
                if (key == KeyEvent.VK_A) {
                    carrilJ1.moverIzquierda();
                }
                if (key == KeyEvent.VK_D) {
                    carrilJ1.moverDerecha();
                }

                // --- CONTROLES JUGADOR 2 ---
                if (!checkCPU.isSelected()) {
                    if (key == KeyEvent.VK_UP) {
                        avanzarVehiculo(carrilJ2, "JUGADOR 2");
                    }
                    if (key == KeyEvent.VK_LEFT) {
                        carrilJ2.moverIzquierda();
                    }
                    if (key == KeyEvent.VK_RIGHT) {
                        carrilJ2.moverDerecha();
                    }
                }
            }
        });
    }

    private void comenzarCarrera() {
        carrilJ1.resetear();
        carrilJ2.resetear();
        carrilCPU.resetear();
        carreraEnCurso = true;

        this.requestFocusInWindow();

        // 1. HILO PARA EFECTO VISUAL DE PISTA EN MOVIMIENTO
        new Thread(() -> {
            while (carreraEnCurso) {
                try {
                    Thread.sleep(40); // Velocidad de animación de la carretera
                    carrilJ1.animarPista();
                    carrilJ2.animarPista();
                    carrilCPU.animarPista();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        // 2. HILO DE LA CPU (Avanza y se mueve de lado automáticamente)
        if (checkCPU.isSelected()) {
            new Thread(() -> {
                while (carreraEnCurso) {
                    try {
                        Thread.sleep(180 + random.nextInt(200));
                        if (!carreraEnCurso) break;

                        // Avance hacia adelante
                        carrilCPU.avanzar(random.nextInt(12) + 6);

                        // Movimiento aleatorio izquierda/derecha de la CPU
                        int direccion = random.nextInt(3);
                        if (direccion == 0) carrilCPU.moverIzquierda();
                        else if (direccion == 1) carrilCPU.moverDerecha();

                        if (carrilCPU.getProgreso() >= 100 && carreraEnCurso) {
                            carreraEnCurso = false;
                            JOptionPane.showMessageDialog(this, "🤖 ¡LA CPU HA CRUZADO LA META PRIMERO! 🏁");
                        }
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }
    }

    private void avanzarVehiculo(PanelCarril carril, String nombre) {
        carril.avanzar(random.nextInt(12) + 8);
        if (carril.getProgreso() >= 100 && carreraEnCurso) {
            carreraEnCurso = false;
            JOptionPane.showMessageDialog(this, "🏆 ¡HA GANADO " + nombre + "! CRUZÓ LA META 🏁");
        }
    }

    private void mostrarManual() {
        String manual = "📖 MANUAL DE CONDUCCIÓN COMPLETO:\n\n"
                + "🎮 Controles Jugador 1:\n"
                + "  • W / Z ➔ Acelerar hacia adelante.\n"
                + "  • A ➔ Mover a la Izquierda.\n"
                + "  • D ➔ Mover a la Derecha.\n\n"
                + "🎮 Controles Jugador 2:\n"
                + "  • Flecha Arriba ➔ Acelerar hacia adelante.\n"
                + "  • Flecha Izquierda ➔ Mover a la Izquierda.\n"
                + "  • Flecha Derecha ➔ Mover a la Derecha.\n\n"
                + "🏁 Llega al 100% para cruzar la META a cuadros.";
        JOptionPane.showMessageDialog(this, manual, "Manual", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- PANEL INTERNO QUE DIBUJA EL CARRIL, EL MOVIMIENTO Y LA META ---
    private class PanelCarril extends JPanel {

        private final String titulo;
        private final Color colorAuto;
        private int progreso = 0; // 0 a 100%
        private int offsetY = 0;   // Para animar la línea punteada
        private int offsetX = 0;   // Desplazamiento lateral (-40 a +40 px)

        public PanelCarril(String titulo, Color colorAuto) {
            this.titulo = titulo;
            this.colorAuto = colorAuto;
            setBackground(new Color(50, 50, 50)); // Asfalto
        }

        public void resetear() {
            this.progreso = 0;
            this.offsetX = 0;
            this.offsetY = 0;
            repaint();
        }

        public void animarPista() {
            offsetY = (offsetY + 6) % 30; // Desplaza el patrón de líneas
            repaint();
        }

        public void avanzar(int cantidad) {
            this.progreso = Math.min(this.progreso + cantidad, 100);
            repaint();
        }

        public void moverIzquierda() {
            if (offsetX > -50) {
                offsetX -= 12;
                repaint();
            }
        }

        public void moverDerecha() {
            if (offsetX < 50) {
                offsetX += 12;
                repaint();
            }
        }

        public int getProgreso() {
            return progreso;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // 1. DIBUJAR BORDES DE CARRETERA
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, 6, h);
            g2.fillRect(w - 6, 0, 6, h);

            // 2. DIBUJAR LÍNEAS PUNTEADAS EN MOVIMIENTO
            g2.setColor(Color.YELLOW);
            for (int y = -30 + offsetY; y < h; y += 35) {
                g2.fillRect((w / 2) - 3, y, 6, 18);
            }

            // 3. DIBUJAR LÍNEA DE META (EN LA PARTE SUPERIOR CUANDO PROGRESE O LLEGUE)
            int yMeta = 50;
            if (progreso >= 85) {
                // Cuadros estilo Bandera de Meta
                int tamCuadro = 12;
                for (int x = 6; x < w - 6; x += tamCuadro) {
                    for (int ym = yMeta; ym < yMeta + 24; ym += tamCuadro) {
                        boolean esBlanco = ((x / tamCuadro) + (ym / tamCuadro)) % 2 == 0;
                        g2.setColor(esBlanco ? Color.WHITE : Color.BLACK);
                        g2.fillRect(x, ym, tamCuadro, tamCuadro);
                    }
                }
                g2.setColor(Color.RED);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.drawString("🏁 M E T A 🏁", (w / 2) - 45, yMeta - 8);
            }

            // 4. ETIQUETA SUPERIOR
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.drawString(titulo + " (" + progreso + "%)", 12, 25);

            // 5. CALCULAR POSICIÓN DEL AUTO Y DIBUJARLO
            int yMin = 75; // Posición de la Meta arriba
            int yMax = h - 70; // Salida abajo
            int yAuto = yMax - (int) ((yMax - yMin) * (progreso / 100.0));
            int xAuto = (w / 2) - 18 + offsetX; // Incluye el movimiento A/D o Flechas

            // Chasis
            g2.setColor(colorAuto);
            g2.fillRoundRect(xAuto, yAuto, 36, 50, 10, 10);

            // Llantas
            g2.setColor(Color.BLACK);
            g2.fillRect(xAuto - 4, yAuto + 5, 5, 12);
            g2.fillRect(xAuto + 35, yAuto + 5, 5, 12);
            g2.fillRect(xAuto - 4, yAuto + 32, 5, 12);
            g2.fillRect(xAuto + 35, yAuto + 32, 5, 12);

            // Parabrisas
            g2.setColor(Color.CYAN);
            g2.fillRect(xAuto + 6, yAuto + 10, 24, 10);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CarreraGameFrame().setVisible(true);
        });
    }
}
