
package negocio;

import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int id;
    private Usuario usuario;
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {}
    public Venta(int id, Usuario usuario) { this.id = id; this.usuario = usuario; }

    public int getId() { return id; } public Usuario getUsuario() { return usuario; } public List<DetalleVenta> getDetalles() { return detalles; }
    public void setId(int id) { this.id = id; } public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void agregarDetalle(DetalleVenta detalle) { detalles.add(detalle); }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder(); sb.append("Venta #").append(id).append("\nUsuario: ").append(usuario.getNombre()).append("\n"); for(DetalleVenta d: detalles) sb.append("  ").append(d.toString()).append("\n"); return sb.toString();
    }
}
