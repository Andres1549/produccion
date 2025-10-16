import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import chucknorris.*;

public class InterfazGraficaChistes extends JFrame {
    // Colores estilo Matrix
    private static final Color MATRIX_BG = new Color(0, 0, 0);
    private static final Color MATRIX_GREEN = new Color(0, 255, 65);
    private static final Color MATRIX_GREEN_DARK = new Color(0, 150, 40);
    private static final Color MATRIX_GREEN_LIGHT = new Color(100, 255, 150);
    private static final Color PANEL_BG = new Color(10, 20, 10);
    private static final Color BUTTON_BG = new Color(0, 100, 30);
    private static final Color BUTTON_HOVER = new Color(0, 180, 50);

    private JComboBox<String> cbBaseDatos;
    private JButton btnConectarMostrar;
    private JButton btnObtenerChiste;
    private JTable tablaChistes;
    private DefaultTableModel modeloTabla;
    private JTextArea txtAreaChiste;
    private JLabel lblEstado;
    private JLabel lblTitulo;

    private Database databaseSeleccionada;
    private String jokeInEnglish = "";
    private String jokeInSpanish = "";

    public InterfazGraficaChistes() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("CHUCK NORRIS JOKES DATABASE SYSTEM");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(MATRIX_BG);
        setLayout(new BorderLayout(15, 15));
    }

    private void inicializarComponentes() {
        // Panel de título con efecto neón
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(MATRIX_BG);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        lblTitulo = new JLabel("CHUCK NORRIS DATABASE MANAGER");
        lblTitulo.setFont(new Font("Consolas", Font.BOLD, 28));
        lblTitulo.setForeground(MATRIX_GREEN);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelTitulo.add(lblTitulo);

        // Panel superior - Selección de base de datos
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelSuperior.setBackground(PANEL_BG);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MATRIX_GREEN, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblBaseDatos = new JLabel("DATABASE:");
        lblBaseDatos.setFont(new Font("Consolas", Font.BOLD, 14));
        lblBaseDatos.setForeground(MATRIX_GREEN_LIGHT);

        cbBaseDatos = new JComboBox<>(new String[]{"SQLite", "MySQL", "PostgreSQL"});
        cbBaseDatos.setPreferredSize(new Dimension(150, 35));
        estilizarComboBox(cbBaseDatos);

        btnConectarMostrar = crearBotonMatrix("CONNECT & SHOW", 180, 35);
        btnConectarMostrar.addActionListener(e -> conectarYMostrar());

        btnObtenerChiste = crearBotonMatrix("GET NEW JOKE", 180, 35);
        btnObtenerChiste.addActionListener(e -> obtenerNuevoChiste());

        panelSuperior.add(lblBaseDatos);
        panelSuperior.add(cbBaseDatos);
        panelSuperior.add(btnConectarMostrar);
        panelSuperior.add(btnObtenerChiste);

        // Panel central - Área de chiste actual
        JPanel panelChiste = new JPanel(new BorderLayout(5, 5));
        panelChiste.setBackground(MATRIX_BG);
        panelChiste.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MATRIX_GREEN, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblChisteTitulo = new JLabel("LATEST JOKE RETRIEVED");
        lblChisteTitulo.setFont(new Font("Consolas", Font.BOLD, 14));
        lblChisteTitulo.setForeground(MATRIX_GREEN);
        lblChisteTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelChiste.add(lblChisteTitulo, BorderLayout.NORTH);

        txtAreaChiste = new JTextArea(4, 50);
        txtAreaChiste.setEditable(false);
        txtAreaChiste.setLineWrap(true);
        txtAreaChiste.setWrapStyleWord(true);
        txtAreaChiste.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtAreaChiste.setBackground(PANEL_BG);
        txtAreaChiste.setForeground(MATRIX_GREEN_LIGHT);
        txtAreaChiste.setCaretColor(MATRIX_GREEN);
        txtAreaChiste.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollChiste = new JScrollPane(txtAreaChiste);
        scrollChiste.setBorder(BorderFactory.createLineBorder(MATRIX_GREEN_DARK, 1));
        scrollChiste.getViewport().setBackground(PANEL_BG);
        panelChiste.add(scrollChiste, BorderLayout.CENTER);

        // Panel de tabla
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBackground(MATRIX_BG);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MATRIX_GREEN, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTablaTitulo = new JLabel("DATABASE RECORDS");
        lblTablaTitulo.setFont(new Font("Consolas", Font.BOLD, 14));
        lblTablaTitulo.setForeground(MATRIX_GREEN);
        lblTablaTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelTabla.add(lblTablaTitulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "ENGLISH JOKE", "SPANISH JOKE"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaChistes = new JTable(modeloTabla);
        estilizarTabla();

        JScrollPane scrollPane = new JScrollPane(tablaChistes);
        scrollPane.setBorder(BorderFactory.createLineBorder(MATRIX_GREEN_DARK, 1));
        scrollPane.getViewport().setBackground(PANEL_BG);
        scrollPane.setBackground(PANEL_BG);

        // Estilizar las barras de desplazamiento
        estilizarScrollBar(scrollPane);

        panelTabla.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior - Estado
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(MATRIX_BG);
        panelInferior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MATRIX_GREEN, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        lblEstado = new JLabel("STATUS: SYSTEM READY");
        lblEstado.setFont(new Font("Consolas", Font.BOLD, 12));
        lblEstado.setForeground(MATRIX_GREEN_LIGHT);
        panelInferior.add(lblEstado, BorderLayout.WEST);

        JLabel lblFooter = new JLabel("POWERED BY CHUCK NORRIS API 2025");
        lblFooter.setFont(new Font("Consolas", Font.ITALIC, 10));
        lblFooter.setForeground(MATRIX_GREEN_DARK);
        panelInferior.add(lblFooter, BorderLayout.EAST);

        // Agregar componentes a la ventana
        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
        panelNorte.setBackground(MATRIX_BG);
        panelNorte.add(panelTitulo, BorderLayout.NORTH);

        JPanel panelControles = new JPanel(new BorderLayout(10, 10));
        panelControles.setBackground(MATRIX_BG);
        panelControles.add(panelSuperior, BorderLayout.NORTH);
        panelControles.add(panelChiste, BorderLayout.CENTER);
        panelNorte.add(panelControles, BorderLayout.CENTER);

        add(panelNorte, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Márgenes generales
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void estilizarScrollBar(JScrollPane scrollPane) {
        // Configurar las barras de scroll
        scrollPane.getVerticalScrollBar().setBackground(PANEL_BG);
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = MATRIX_GREEN_DARK;
                this.trackColor = PANEL_BG;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(BUTTON_BG);
                button.setForeground(MATRIX_GREEN);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(BUTTON_BG);
                button.setForeground(MATRIX_GREEN);
                return button;
            }
        });

        scrollPane.getHorizontalScrollBar().setBackground(PANEL_BG);
        scrollPane.getHorizontalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = MATRIX_GREEN_DARK;
                this.trackColor = PANEL_BG;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(BUTTON_BG);
                button.setForeground(MATRIX_GREEN);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(BUTTON_BG);
                button.setForeground(MATRIX_GREEN);
                return button;
            }
        });
    }

    private JButton crearBotonMatrix(String texto, int ancho, int alto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(ancho, alto));
        boton.setFont(new Font("Consolas", Font.BOLD, 12));
        boton.setBackground(BUTTON_BG);
        boton.setForeground(MATRIX_GREEN);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MATRIX_GREEN, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(BUTTON_HOVER);
                boton.setForeground(MATRIX_BG);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(BUTTON_BG);
                boton.setForeground(MATRIX_GREEN);
            }
        });

        return boton;
    }

    private void estilizarComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Consolas", Font.BOLD, 13));
        combo.setBackground(PANEL_BG);
        combo.setForeground(MATRIX_GREEN);
        combo.setBorder(BorderFactory.createLineBorder(MATRIX_GREEN, 2));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? BUTTON_BG : PANEL_BG);
                setForeground(isSelected ? MATRIX_GREEN_LIGHT : MATRIX_GREEN);
                setFont(new Font("Consolas", Font.BOLD, 13));
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
    }

    private void estilizarTabla() {
        tablaChistes.setFont(new Font("Consolas", Font.PLAIN, 12));
        tablaChistes.setBackground(PANEL_BG);
        tablaChistes.setForeground(MATRIX_GREEN_LIGHT);
        tablaChistes.setGridColor(MATRIX_GREEN_DARK);
        tablaChistes.setSelectionBackground(BUTTON_BG);
        tablaChistes.setSelectionForeground(MATRIX_GREEN_LIGHT);
        tablaChistes.setRowHeight(30);
        tablaChistes.setShowGrid(true);
        tablaChistes.setIntercellSpacing(new Dimension(1, 1));

        // Estilizar el header
        JTableHeader header = tablaChistes.getTableHeader();
        header.setFont(new Font("Consolas", Font.BOLD, 13));
        header.setBackground(BUTTON_BG);
        header.setForeground(MATRIX_GREEN);
        header.setBorder(BorderFactory.createLineBorder(MATRIX_GREEN, 2));

        // Renderizador personalizado para las celdas
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? PANEL_BG : new Color(5, 15, 5));
                    c.setForeground(MATRIX_GREEN_LIGHT);
                } else {
                    c.setBackground(BUTTON_BG);
                    c.setForeground(MATRIX_GREEN_LIGHT);
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        };

        // Aplicar a todas las columnas
        for (int i = 0; i < tablaChistes.getColumnCount(); i++) {
            tablaChistes.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        tablaChistes.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaChistes.getColumnModel().getColumn(1).setPreferredWidth(450);
        tablaChistes.getColumnModel().getColumn(2).setPreferredWidth(450);
    }

    private void obtenerNuevoChiste() {
        lblEstado.setText("STATUS: FETCHING JOKE FROM API...");
        lblEstado.setForeground(new Color(255, 200, 0));
        btnObtenerChiste.setEnabled(false);

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                JokeService jokeService = new ChuckNorrisAdapter();
                return jokeService.getJoke();
            }

            @Override
            protected void done() {
                try {
                    String joke = get();
                    String[] jokes = joke.split("\n");
                    jokeInEnglish = jokes[0].replace("Inglés: ", "").trim();
                    jokeInSpanish = jokes[1].replace("Español: ", "").trim();

                    txtAreaChiste.setText("ENGLISH: " + jokeInEnglish + "\n\nSPANISH: " + jokeInSpanish);

                    // Insertar en la base de datos seleccionada
                    if (databaseSeleccionada != null) {
                        insertarChiste();
                    }

                    lblEstado.setText("STATUS: JOKE SUCCESSFULLY RETRIEVED AND STORED");
                    lblEstado.setForeground(MATRIX_GREEN);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(InterfazGraficaChistes.this,
                            "ERROR FETCHING JOKE: " + e.getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    lblEstado.setText("STATUS: ERROR OCCURRED");
                    lblEstado.setForeground(Color.RED);
                } finally {
                    btnObtenerChiste.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void insertarChiste() {
        String dbType = (String) cbBaseDatos.getSelectedItem();
        String jokeEn = escapeSingleQuotes(jokeInEnglish);
        String jokeEs = escapeSingleQuotes(jokeInSpanish);

        try (Connection conn = databaseSeleccionada.connect();
             Statement stmt = conn.createStatement()) {

            String createTable = "";
            String insertQuery = "";

            switch (dbType) {
                case "SQLite":
                    createTable = "CREATE TABLE IF NOT EXISTS ChistesBasto (" +
                            "AUTOID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "chiste_en_ingles TEXT, " +
                            "chiste_en_espanol TEXT)";
                    insertQuery = "INSERT INTO ChistesBasto (chiste_en_ingles, chiste_en_espanol) " +
                            "VALUES ('" + jokeEn + "', '" + jokeEs + "')";
                    break;

                case "MySQL":
                    createTable = "CREATE TABLE IF NOT EXISTS ChistesBasto (" +
                            "AUTOID INT AUTO_INCREMENT PRIMARY KEY, " +
                            "chiste_en_ingles TEXT, " +
                            "chiste_en_espanol TEXT)";
                    insertQuery = "INSERT INTO ChistesBasto (chiste_en_ingles, chiste_en_espanol) " +
                            "VALUES ('" + jokeEn + "', '" + jokeEs + "')";
                    break;

                case "PostgreSQL":
                    createTable = "CREATE TABLE IF NOT EXISTS ChistesBasto (" +
                            "AUTOID SERIAL PRIMARY KEY, " +
                            "chiste_en_ingles TEXT, " +
                            "chiste_en_espanol TEXT)";
                    insertQuery = "INSERT INTO ChistesBasto (chiste_en_ingles, chiste_en_espanol) " +
                            "VALUES ('" + jokeEn + "', '" + jokeEs + "')";
                    break;
            }

            stmt.execute(createTable);
            stmt.execute(insertQuery);

            // Actualizar la tabla automáticamente
            cargarDatos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "DATABASE ERROR (" + dbType + "): " + e.getMessage(),
                    "DATABASE ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void conectarYMostrar() {
        String seleccion = (String) cbBaseDatos.getSelectedItem();
        lblEstado.setText("STATUS: CONNECTING TO " + seleccion + "...");
        lblEstado.setForeground(new Color(255, 200, 0));

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DatabaseFactory factory = null;
                String url = "";

                switch (seleccion) {
                    case "SQLite":
                        factory = new SQLiteFactory();
                        url = "jdbc:sqlite:baseproduccion.db";
                        break;
                    case "MySQL":
                        factory = new MySQLFactory();
                        url = "jdbc:mysql://172.30.16.45:3306/construccion1";
                        break;
                    case "PostgreSQL":
                        factory = new PostgresFactory();
                        url = "jdbc:postgresql://172.30.16.45:5432/construccion1";
                        break;
                }

                if (factory != null) {
                    databaseSeleccionada = factory.createDatabase(url);
                }

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    cargarDatos();
                    lblEstado.setText("STATUS: CONNECTED TO " + seleccion + " DATABASE");
                    lblEstado.setForeground(MATRIX_GREEN);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(InterfazGraficaChistes.this,
                            "CONNECTION ERROR (" + seleccion + "): " + e.getMessage(),
                            "CONNECTION ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    lblEstado.setText("STATUS: CONNECTION FAILED");
                    lblEstado.setForeground(Color.RED);
                }
            }
        };
        worker.execute();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);

        if (databaseSeleccionada == null) {
            return;
        }

        try (Connection conn = databaseSeleccionada.connect();
             Statement stmt = conn.createStatement()) {

            // Crear tabla si no existe
            String dbType = (String) cbBaseDatos.getSelectedItem();
            String createTable = "";

            switch (dbType) {
                case "SQLite":
                    createTable = "CREATE TABLE IF NOT EXISTS ChistesBasto (" +
                            "AUTOID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "chiste_en_ingles TEXT, " +
                            "chiste_en_espanol TEXT)";
                    break;
                case "MySQL":
                    createTable = "CREATE TABLE IF NOT EXISTS ChistesBasto (" +
                            "AUTOID INT AUTO_INCREMENT PRIMARY KEY, " +
                            "chiste_en_ingles TEXT, " +
                            "chiste_en_espanol TEXT)";
                    break;
                case "PostgreSQL":
                    createTable = "CREATE TABLE IF NOT EXISTS ChistesBasto (" +
                            "AUTOID SERIAL PRIMARY KEY, " +
                            "chiste_en_ingles TEXT, " +
                            "chiste_en_espanol TEXT)";
                    break;
            }

            stmt.execute(createTable);

            ResultSet rs = stmt.executeQuery("SELECT * FROM ChistesBasto");

            while (rs.next()) {
                int id = rs.getInt("AUTOID");
                String ingles = rs.getString("chiste_en_ingles");
                String espanol = rs.getString("chiste_en_espanol");

                modeloTabla.addRow(new Object[]{id, ingles, espanol});
            }

            lblEstado.setText("STATUS: " + modeloTabla.getRowCount() + " RECORDS LOADED SUCCESSFULLY");
            lblEstado.setForeground(MATRIX_GREEN);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "ERROR LOADING DATA: " + e.getMessage(),
                    "DATABASE ERROR",
                    JOptionPane.ERROR_MESSAGE);
            lblEstado.setText("STATUS: ERROR LOADING DATA");
            lblEstado.setForeground(Color.RED);
        }
    }

    private String escapeSingleQuotes(String str) {
        if (str != null) {
            return str.replace("'", "''");
        }
        return str;
    }
    public static void main(String[] args) {
        // Configurar el look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            InterfazGraficaChistes ventana = new InterfazGraficaChistes();
            ventana.setVisible(true);
        });
    }
}