package motoresde8bits;

import java.util.Random;

public class MotoSuper extends Vehiculo {
    private Random random = new Random();

    public MotoSuper(String nombre) {
        super(nombre, 8);
    }

    @Override
    public void avanzar() {
        // Tiene un 30% de probabilidad de meter un mega turbo
        int turbo = (random.nextInt(10) > 6) ? 18 : 2;
        posicion += velocidadBase + turbo;
    }
}
