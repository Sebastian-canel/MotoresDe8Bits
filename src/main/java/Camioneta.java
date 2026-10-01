
package motoresde8bits;

import java.util.Random;

public class Camioneta extends Vehiculo {
    private Random random = new Random();

    public Camioneta(String nombre) {
        super(nombre, 10);
    }

    @Override
    public void avanzar() {
        int avance = velocidadBase + random.nextInt(6);
        posicion += avance;
    }
}
