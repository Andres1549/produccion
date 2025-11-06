
package negocio.producto;

public class ConcreteProductoBuilder implements ProductoBuilder {
    private Producto producto;

    public ConcreteProductoBuilder() {
        reset();
    }

    @Override public void reset() { producto = new Producto(); }
    @Override public void setId(int id) { producto.setId(id); }
    @Override public void setNombre(String nombre) { producto.setNombre(nombre); }
    @Override public void setPrecio(double precio) { producto.setPrecio(precio); }
    @Override public void setDescripcion(String descripcion) { producto.setDescripcion(descripcion); }
    @Override public Producto getResult() { return producto; }
}
