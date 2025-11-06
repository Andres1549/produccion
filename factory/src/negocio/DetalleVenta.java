
package negocio;

import negocio.producto.Producto;

public class DetalleVenta {
    private Producto producto;
    private int cantidad;

    public DetalleVenta() {}
    public DetalleVenta(Producto producto, int cantidad) { this.producto = producto; this.cantidad = cantidad; }

    public Producto getProducto() { return producto; } public int getCantidad() { return cantidad; }
    public void setProducto(Producto producto) { this.producto = producto; } public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override public String toString() { return producto.getNombre() + " x" + cantidad + " ($" + String.format("%,.0f", producto.getPrecio()) + ")"; }
}
