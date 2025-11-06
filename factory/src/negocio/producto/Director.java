
package negocio.producto;

public class Director {
    public void hacerPcBasica(ProductoBuilder builder) {
        builder.reset();
        builder.setId(1);
        builder.setNombre("PC Rendimiento Básico");
        builder.setPrecio(1_200_000);
        builder.setDescripcion("Intel i3 | 8GB RAM | SSD 256GB | Gráficos integrados");
    }
    public void hacerPcMedia(ProductoBuilder builder) {
        builder.reset();
        builder.setId(2);
        builder.setNombre("PC Rendimiento Medio");
        builder.setPrecio(2_500_000);
        builder.setDescripcion("Intel i5 | 16GB RAM | SSD 512GB | GTX 1660");
    }
    public void hacerPcAlto(ProductoBuilder builder) {
        builder.reset();
        builder.setId(3);
        builder.setNombre("PC Rendimiento Alto");
        builder.setPrecio(4_000_000);
        builder.setDescripcion("Intel i7 | 32GB RAM | SSD 1TB | RTX 3070");
    }
    public void hacerPcSuperior(ProductoBuilder builder) {
        builder.reset();
        builder.setId(4);
        builder.setNombre("PC Rendimiento Superior");
        builder.setPrecio(7_000_000);
        builder.setDescripcion("Intel i9 | 64GB RAM | SSD 2TB | RTX 4090");
    }
}
