
package motoresde8bits;

import java.util.Random;

public class AutoDeportivo extends Vehiculo {
    private Random random = new Random();

    public AutoDeportivo(String nombre) {
        super(nombre, 12);
    }

    @Override
    public void avanzar() {
        int avance = velocidadBase + random.nextInt(10);
        posicion += avance;
    }
}
