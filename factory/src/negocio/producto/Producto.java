
package negocio.producto;

public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private String descripcion;

    public Producto() {}

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        return id + " - " + nombre + " ($" + String.format("%,.0f", precio) + " COP) - " + descripcion;
    }
}
