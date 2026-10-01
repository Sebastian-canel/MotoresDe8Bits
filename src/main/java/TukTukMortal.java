
package motoresde8bits;

import java.util.Random;

public class TukTukMortal extends Vehiculo {
    private Random random = new Random();

    public TukTukMortal(String nombre) {
        super(nombre, 9);
    }

    @Override
    public void avanzar() {
        int avance = velocidadBase + random.nextInt(10);
        posicion += avance;
    }
}