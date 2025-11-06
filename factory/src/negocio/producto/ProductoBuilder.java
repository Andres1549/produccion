
package negocio.producto;

public interface ProductoBuilder {
    void reset();
    void setId(int id);
    void setNombre(String nombre);
    void setPrecio(double precio);
    void setDescripcion(String descripcion);
    Producto getResult();
}
