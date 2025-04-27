/*package interfaces;

import DBO.DBconnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ValidationSeancesResponsable extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton btnValider, btnRefuser;

    public ValidationSeancesResponsable() {
        setTitle("Validation des séances");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("Séances en attente de validation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        model.addColumn("ID");
        model.addColumn("Enseignant");
        model.addColumn("Cours");
        model.addColumn("Date");
        model.addColumn("Contenu");

        JPanel panelBtn = new JPanel();
        btnValider = new JButton("Valider");
        btnRefuser = new JButton("Refuser");
        panelBtn.add(btnValider);
        panelBtn.add(btnRefuser);
        add(panelBtn, BorderLayout.SOUTH);

        chargerSeances();

        btnValider.addActionListener(e -> changerStatut("validée", null));
        btnRefuser.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une séance.");
                return;
            }
            String commentaire = JOptionPane.showInputDialog(this, "Commentaire (raison du refus) :");
            if (commentaire != null && !commentaire.trim().isEmpty()) {
                changerStatut("refusée", commentaire);
            }
        });
    }

    private void chargerSeances() {
        try (Connection conn = DBconnect.getconnection()) {
            String sql = """
                SELECT s.id, u.login AS enseignant, c.nom AS cours, s.date_seance, s.contenu
                FROM seances s
                JOIN utilisateurs u ON s.id_enseignant = u.id
                JOIN cours c ON s.id_cours = c.id
                WHERE s.statut = 'en attente'
                ORDER BY s.date_seance DESC
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("enseignant"),
                        rs.getString("cours"),
                        rs.getDate("date_seance"),
                        rs.getString("contenu")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur chargement séances.");
        }
    }

    private void changerStatut(String statut, String commentaire) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une séance.");
            return;
        }

        int idSeance = (int) model.getValueAt(selectedRow, 0);

        try (Connection conn = DBconnect.getconnection()) {
            String sql = "UPDATE seances SET statut = ?, commentaire = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, statut);
            ps.setString(2, commentaire);
            ps.setInt(3, idSeance);
            ps.executeUpdate();

            model.removeRow(selectedRow); // Retire de la table affichée
            JOptionPane.showMessageDialog(this, "Statut mis à jour !");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
        }
    }
}


package interfaces;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ValidationSeancesResponsable extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ValidationSeancesResponsable() {
        setTitle("Validation des Séances");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("Séances en attente de validation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Cours", "Date", "Contenu", "Commentaire", "Valider", "Refuser"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        chargerSeancesNonValidees();

        // Ajouter écouteur pour les boutons dans le tableau
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int ligne = table.rowAtPoint(e.getPoint());
                int colonne = table.columnAtPoint(e.getPoint());

                if (colonne == 5) { // bouton Valider
                    int idSeance = (int) table.getValueAt(ligne, 0);
                    mettreAJourStatut(idSeance, "validée", null);
                    model.removeRow(ligne);
                }

                if (colonne == 6) { // bouton Refuser
                    int idSeance = (int) table.getValueAt(ligne, 0);
                    String commentaire = (String) table.getValueAt(ligne, 4);
                    if (commentaire == null || commentaire.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Veuillez saisir un commentaire de refus.");
                    } else {
                        mettreAJourStatut(idSeance, "refusée", commentaire);
                        model.removeRow(ligne);
                    }
                }
            }
        });
    }

    private void chargerSeancesNonValidees() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT s.id, c.nom_cours, s.date, s.contenu FROM seance s JOIN cours c ON s.id_cours = c.id WHERE s.statut = 'non validée'")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String cours = rs.getString("nom_cours");
                String date = rs.getString("date");
                String contenu = rs.getString("contenu");

                // Ligne avec bouton texte
                model.addRow(new Object[]{
                        id, cours, date, contenu, "", "Valider", "Refuser"
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des séances.");
        }
    }

    private void mettreAJourStatut(int idSeance, String statut, String commentaire) {
        String sql = "UPDATE seance SET statut = ?, commentaire_refus = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, statut);
            pst.setString(2, commentaire);
            pst.setInt(3, idSeance);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
        }
    }
}


package interfaces;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ValidationSeancesResponsable extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ValidationSeancesResponsable() {
        setTitle("Validation des Séances");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Titre
        JLabel titre = new JLabel("Séances en attente de validation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Cours", "Date", "Contenu", "Commentaire", "Action"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Charger les séances
        chargerSeancesNonValidees();
    }

    private void chargerSeancesNonValidees() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT s.id, c.nom_cours, s.date, s.contenu FROM seance s JOIN cours c ON s.id_cours = c.id WHERE s.statut = 'non validée'")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String cours = rs.getString("nom_cours");
                String date = rs.getString("date");
                String contenu = rs.getString("contenu");

                // Ajouter une ligne avec champ commentaire et bouton de validation
                model.addRow(new Object[]{
                        id, cours, date, contenu,
                        "", // commentaire vide par défaut
                        "Valider / Refuser"
                });
            }

            // Ajouter un bouton personnalisé dans la colonne Action (plus tard si tu veux, on peut le faire avec un renderer/éditeur)

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des séances.");
        }
    }



}

 */
/*
package interfaces;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ValidationSeancesResponsable extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public ValidationSeancesResponsable() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titre = new JLabel("Séances en attente de validation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Cours", "Date", "Contenu", "Commentaire", "Valider", "Refuser"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Ajout écouteur clics sur boutons
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int ligne = table.rowAtPoint(e.getPoint());
                int colonne = table.columnAtPoint(e.getPoint());

                if (colonne == 5) { // Valider
                    int idSeance = (int) table.getValueAt(ligne, 0);
                    mettreAJourStatut(idSeance, "validée", null);
                    model.removeRow(ligne);
                }

                if (colonne == 6) { // Refuser
                    int idSeance = (int) table.getValueAt(ligne, 0);
                    String commentaire = (String) table.getValueAt(ligne, 4);
                    if (commentaire == null || commentaire.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Veuillez saisir un commentaire de refus.");
                    } else {
                        mettreAJourStatut(idSeance, "refusée", commentaire);
                        model.removeRow(ligne);
                    }
                }
            }
        });
    }

    public void chargerSeancesNonValidees() {
        model.setRowCount(0); // Nettoie avant de recharger

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT s.id, c.nom AS nom_cours, s.dateseance AS date, s.contenu" +
                             "FROM seance s" +
                             "JOIN cours c ON s.cours_id = c.id" +
                             "WHERE s.statut = 'non validé';"
             )) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String cours = rs.getString("nom_cours");
                String date = rs.getString("date");
                String contenu = rs.getString("contenu");

                model.addRow(new Object[]{id, cours, date, contenu, "", "Valider", "Refuser"});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des séances.");
        }
    }

    private void mettreAJourStatut(int idSeance, String statut, String commentaire) {
        String sql = "UPDATE seance SET statut = ?, commentaire_refus = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, statut);
            pst.setString(2, commentaire);
            pst.setInt(3, idSeance);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.");
        }
    }
}


 */



/*
package interfaces;

import DBO.SeanceDAO;
import models.Seance;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ValidationSeancesResponsable extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public ValidationSeancesResponsable() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titre = new JLabel("Séances en attente de validation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
                "ID", "Cours", "Date", "Heure", "Contenu", "Commentaire", "Valider", "Refuser"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Seulement la colonne commentaire est éditable
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int ligne = table.rowAtPoint(e.getPoint());
                int colonne = table.columnAtPoint(e.getPoint());

                if (colonne == 6) { // Valider
                    int idSeance = (int) model.getValueAt(ligne, 0);
                    boolean ok = SeanceDAO.mettreAJourStatut(idSeance, "validée", null);
                    if (ok) model.removeRow(ligne);
                }

                if (colonne == 7) { // Refuser
                    int idSeance = (int) model.getValueAt(ligne, 0);
                    String commentaire = (String) model.getValueAt(ligne, 5);
                    if (commentaire == null || commentaire.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Veuillez saisir un commentaire de refus.");
                    } else {
                        boolean ok = SeanceDAO.mettreAJourStatut(idSeance, "refusée", commentaire);
                        if (ok) model.removeRow(ligne);
                    }
                }
            }
        });
    }

    public void chargerSeancesNonValidees() {
        model.setRowCount(0); // Nettoyer l'ancien contenu
        List<Seance> seances = SeanceDAO.getSeancesNonValidees();

        for (Seance s : seances) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getNomCours(),
                    s.getDate().toString(),
                    s.getHeureDebut().toString(),
                    s.getContenu(),
                    "", // Champ commentaire vide par défaut
                    "Valider",
                    "Refuser"
            });
        }
    }
}




 */


package interfaces;

import DBO.SeanceDAO;
import models.Seance;

import javax.swing.*;
        import javax.swing.table.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.time.format.DateTimeFormatter;
import java.util.List;

public class ValidationSeancesResponsable extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public ValidationSeancesResponsable() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Panel principal
        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel titre = new JLabel("Séances en attente de validation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(titre, BorderLayout.CENTER);

        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> filterTable());
        searchPanel.add(new JLabel("Filtrer: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        //  table avec colonnes
        model = new DefaultTableModel(new String[]{
                "ID", "Cours", "Date", "Heure", "Contenu", "Commentaire", "Actions"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Seul le commentaire est éditable
            }
        };

        // Configuration de la table
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                if (column == 6) { // Colonne Actions
                    return createActionButtons(row);
                }
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                return c;
            }
        };

        // Style de la table
        table.setRowHeight(50);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Centrer le texte dans les colonnes
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount() - 2; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Personnalisation de la colonne Actions
        table.getColumnModel().getColumn(6).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return createActionButtons(row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createActionButtons(int row) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(table.getBackground());

        // Bouton Valider
        JButton validerBtn = new JButton("Valider");
        styleButton(validerBtn, new Color(46, 125, 50));
        validerBtn.addActionListener(e -> validerSeance(row));

        // Bouton Refuser
        JButton refuserBtn = new JButton("Refuser");
        styleButton(refuserBtn, new Color(198, 40, 40));
        refuserBtn.addActionListener(e -> refuserSeance(row));

        panel.add(validerBtn);
        panel.add(refuserBtn);
        return panel;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    }

    private void validerSeance(int row) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirmer la validation de cette séance ?",
                "Confirmation de validation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int idSeance = (int) model.getValueAt(row, 0);
            boolean ok = SeanceDAO.mettreAJourStatut(idSeance, "validée", null);
            if (ok) {
                model.removeRow(row);
                JOptionPane.showMessageDialog(this, "Séance validée avec succès !");
            }
        }
    }

    private void refuserSeance(int row) {
        String commentaire = (String) model.getValueAt(row, 5);
        if (commentaire == null || commentaire.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez saisir un commentaire expliquant le refus.",
                    "Commentaire requis",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirmer le refus de cette séance ?",
                "Confirmation de refus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int idSeance = (int) model.getValueAt(row, 0);
            boolean ok = SeanceDAO.mettreAJourStatut(idSeance, "refusée", commentaire);
            if (ok) {
                model.removeRow(row);
                JOptionPane.showMessageDialog(this, "Séance refusée avec succès !");
            }
        }
    }

    public void chargerSeancesNonValidees() {
        model.setRowCount(0); // Effacer les données existantes
        List<Seance> seances = SeanceDAO.getSeancesNonValidees();

        for (Seance s : seances) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getNomCours(),
                    s.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    s.getHeureDebut().format(DateTimeFormatter.ofPattern("HH:mm")),
                    s.getContenu(),
                    "", // Champ commentaire vide initialement
                    ""  // La colonne Actions sera remplie par le renderer
            });
        }
    }

    private void filterTable() {
        String query = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        if (query.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }
}