
package motoresde8bits;

// Clase base abstracta (Aplica ABSTRACCIÓN y POO)
public abstract class Vehiculo {
    protected String nombre;
    protected int posicion;
    protected int velocidadBase;

    public Vehiculo(String nombre, int velocidadBase) {
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.posicion = 0;
    }

    // Método abstracto para aplicar POLIMORFISMO en las subclases
    public abstract void avanzar();

    public int getPosicion() {
        return posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void reiniciar() {
        this.posicion = 0;
    }
}