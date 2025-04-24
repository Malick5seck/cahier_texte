/*package interfaces;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SeancesValidees extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public SeancesValidees() {
        setTitle("Séances Validées");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("Liste des Séances Validées", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Cours", "Enseignant", "Date", "Contenu"},
                0
        );

        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        chargerSeancesValidees();
    }

    private void chargerSeancesValidees() {
        String query = """
            SELECT s.id, c.nom_cours, u.prenom AS enseignant, s.date, s.contenu
            FROM seance s
            JOIN cours c ON s.id_cours = c.id
            JOIN utilisateur u ON c.id_enseignant = u.id
            WHERE s.statut = 'validée'
        """;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "");
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String cours = rs.getString("nom_cours");
                String enseignant = rs.getString("enseignant");
                String date = rs.getString("date");
                String contenu = rs.getString("contenu");

                model.addRow(new Object[]{id, cours, enseignant, date, contenu});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des séances validées.");
        }
    }
}

 */
/*
package interfaces;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SeancesValidees extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public SeancesValidees() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titre = new JLabel("Liste des Séances Validées", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Cours", "Enseignant", "Date", "Contenu"},
                0
        );

        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        chargerSeancesValidees();
    }

    public void chargerSeancesValidees() {
        model.setRowCount(0); // Réinitialise le tableau avant chargement

        String query = "SELECT s.id, c.nom, u.nom AS enseignant, s.dateseance, s.contenu " +
                "FROM seance s " +
                "JOIN cours c ON s.cours_id = c.id " +
                "JOIN utilisateur u ON c.Enseignant_id = u.id " +
                "WHERE s.statut = 'validée'";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "");
             PreparedStatement pst = conn.prepareStatement(query);
            ) {
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String cours = rs.getString("nom_cours");
                String enseignant = rs.getString("enseignant");
                String date = rs.getString("date");
                String contenu = rs.getString("contenu");

                model.addRow(new Object[]{id, cours, enseignant, date, contenu});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des séances validées.");
        }
    }
}


 */

/*
package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Map;

public class SeancesValidees extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private Map<String, Object> appState;

    public SeancesValidees() {
        this.appState = appState;
        setLayout(new BorderLayout());
        initUI();
        chargerSeancesValidees();
    }

    private void initUI() {
        // Création du modèle de table avec colonnes
        model = new DefaultTableModel(
                new Object[]{"ID", "Date", "Heure", "Matière", "Enseignant", "Salle"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table non modifiable
            }
        };

        // Configuration de la JTable
        table = new JTable(model);
        table.setRowHeight(25); // Hauteur des lignes
        table.setAutoCreateRowSorter(true); // Tri possible sur les colonnes

        // Ajout d'un scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ajout des composants au panel
        add(scrollPane, BorderLayout.CENTER);
    }

    public void chargerSeancesValidees() {
        model.setRowCount(0); // Réinitialisation des données

        try (Connection conn = DBconnect.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT s.id, s.dateSeance, s.heureDebut, m.nom AS matiere, " +
                             "CONCAT(u.nom, ' ', u.prenom) AS enseignant, s.salle " +
                             "FROM seance s " +
                             "JOIN matiere m ON s.matiere_id = m.id " +
                             "JOIN utilisateur u ON s.enseignant_id = u.id " +
                             "WHERE s.statut = 'validee' " +
                             "ORDER BY s.dateSeance DESC")) {

            // Remplissage du modèle avec les données
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getDate("dateSeance"),
                        rs.getTime("heureDebut"),
                        rs.getString("matiere"),
                        rs.getString("enseignant"),
                        rs.getString("salle")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des séances:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour rafraîchir les données
    public void refresh() {
        chargerSeancesValidees();
    }
}

 */




package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SeancesValidees extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;

    public SeancesValidees() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initUI();
        chargerSeancesValidees();
    }

    private void initUI() {
        // Panel d'en-tête avec titre et outils de filtrage
        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel titre = new JLabel("Séances validées", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(titre, BorderLayout.NORTH);

        // Panel de recherche et filtres
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        filterPanel.setBackground(Color.WHITE);

        // Filtre par matière
        filterComboBox = new JComboBox<>();
        filterComboBox.addItem("Toutes les matières");
        filterComboBox.addActionListener(e -> filterTable());

        // Barre de recherche
        searchField = new JTextField(20);
        searchField.addActionListener(e -> filterTable());

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> filterTable());

        filterPanel.add(new JLabel("Matière: "));
        filterPanel.add(filterComboBox);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Rechercher: "));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);

        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Modèle de table (sans la colonne Salle)
        model = new DefaultTableModel(
                new Object[]{"ID", "Date", "Heure", "Matière", "Enseignant", "Contenu"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Configuration de la table
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                return c;
            }
        };

        // Style de la table
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);

        // Centrer le texte dans les cellules (sauf enseignant et contenu)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 4 && i != 5) { // Colonnes Enseignant et Contenu non centrées
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Personnalisation de la colonne contenu
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                label.setToolTipText(value != null ? value.toString() : "");
                label.setText("Voir contenu");
                label.setForeground(Color.BLUE);
                label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return label;
            }
        });

        // Listener pour voir le contenu
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col == 5) { // Colonne contenu
                    String contenu = (String) model.getValueAt(row, 5);
                    JOptionPane.showMessageDialog(SeancesValidees.this,
                            contenu, "Contenu de la séance", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void chargerSeancesValidees() {
        model.setRowCount(0);
        filterComboBox.removeAllItems();
        filterComboBox.addItem("Toutes les matières");

        try (Connection conn = DBconnect.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT s.id, s.dateSeance, s.heureDebut, m.nom AS matiere, " +
                             "CONCAT(u.nom, ' ', u.prenom) AS enseignant, s.contenu " + // Retrait de salle
                             "FROM seance s " +
                             "JOIN matiere m ON s.matiere_id = m.id " +
                             "JOIN utilisateur u ON s.enseignant_id = u.id " +
                             "WHERE s.statut = 'validee' " +
                             "ORDER BY s.dateSeance DESC")) {

            while (rs.next()) {
                String matiere = rs.getString("matiere");
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getDate("dateSeance").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getTime("heureDebut").toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        matiere,
                        rs.getString("enseignant"),
                        rs.getString("contenu")
                });

                if (!matiereExistsInFilter(matiere)) {
                    filterComboBox.addItem(matiere);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des séances:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean matiereExistsInFilter(String matiere) {
        for (int i = 0; i < filterComboBox.getItemCount(); i++) {
            if (filterComboBox.getItemAt(i).equals(matiere)) {
                return true;
            }
        }
        return false;
    }

    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        String selectedMatiere = (String) filterComboBox.getSelectedItem();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                if (!"Toutes les matières".equals(selectedMatiere) &&
                        !selectedMatiere.equals(entry.getStringValue(3))) {
                    return false;
                }

                if (searchText.isEmpty()) return true;

                for (int i = 1; i < entry.getValueCount(); i++) {
                    if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                        return true;
                    }
                }
                return false;
            }
        };

        sorter.setRowFilter(filter);
    }

    public void refresh() {
        chargerSeancesValidees();
    }
}