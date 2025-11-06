package negocio.ui;

import proyecto.Database;
import proyecto.negocio.Venta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VentaPanel extends JPanel {
    private final java.util.function.Supplier<Database> dbSupplier;
    private JTable table;

    public VentaPanel(java.util.function.Supplier<Database> dbSupplier) {
        this.dbSupplier = dbSupplier;
        setLayout(new BorderLayout());
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel south = new JPanel();
        JButton btnLoad = new JButton("Actualizar");
        btnLoad.addActionListener(e -> cargarVentas());
        south.add(btnLoad);
        add(south, BorderLayout.SOUTH);
        cargarVentas();
    }

    private void cargarVentas() {
        Database db = dbSupplier.get();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Usuario","#Items"},0);
        if (db == null) { table.setModel(model); return; }
        try (Connection conn = db.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT v.id, u.nombre, COUNT(d.producto) as items FROM ventas v LEFT JOIN usuarios u ON v.usuario_id = u.id LEFT JOIN detalles d ON d.venta_id = v.id GROUP BY v.id, u.nombre")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(3)});
            }
            table.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar ventas: " + e.getMessage());
            table.setModel(model);
        }
    }
}
