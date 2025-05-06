
package interfaces;

import DBO.DBconnect;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AjoutCoursPanel extends JPanel {
    private JTextField nomField;
    private JTextArea descriptionArea;
    private JButton ajouterBtn, supprimerBtn;
    private JTable coursTable;
    private DefaultTableModel tableModel;

    // Couleurs personnalisées

    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color HOVER_COLOR = new Color(50, 110, 160);
    private final Color SECONDARY_COLOR = new Color(220, 80, 80);
    private final Color SECONDARY_HOVER_COLOR = new Color(200, 60, 60);
    private final Color BACKGROUND_COLOR = new Color(245, 248, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TABLE_ODD_ROW = new Color(248, 248, 248);

    public AjoutCoursPanel(AssignerCours assignerCoursPanel) {
        initUI();
        chargerCours();
    }
    // Configuration du layout principal

    private void initUI() {

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        //  Panel formulaire d'ajout

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(5, 5, 5, 5);
        formGbc.anchor = GridBagConstraints.WEST;
        formGbc.fill = GridBagConstraints.HORIZONTAL;

        // Composants du formulaire

        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formPanel.add(new JLabel("Nom du cours:"), formGbc);

        formGbc.gridx = 1;
        formGbc.weightx = 1;
        nomField = new JTextField();
        nomField.putClientProperty("JTextField.placeholderText", "Entrez le nom du cours...");
        formPanel.add(nomField, formGbc);

        formGbc.gridx = 0;
        formGbc.gridy = 1;
        formGbc.weightx = 0;
        formPanel.add(new JLabel("Description:"), formGbc);

        formGbc.gridx = 1;
        formGbc.weightx = 1;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.putClientProperty("JTextArea.placeholderText", "Description optionnelle du cours");
        formPanel.add(new JScrollPane(descriptionArea), formGbc);

        // Ajout du formPanel au panel principal

        gbc.gridy = 0;
        add(formPanel, gbc);

        // Panel des boutons

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        ajouterBtn = createStyledButton("Ajouter", PRIMARY_COLOR, HOVER_COLOR);
        supprimerBtn = createStyledButton("Supprimer", SECONDARY_COLOR, SECONDARY_HOVER_COLOR);

        ajouterBtn.addActionListener(e -> ajouterCours());
        supprimerBtn.addActionListener(e -> supprimerCours());

        buttonPanel.add(ajouterBtn);
        buttonPanel.add(supprimerBtn);

        gbc.gridy = 1;
        add(buttonPanel, gbc);

        //Tableau des cours

        String[] columns = {"ID", "Nom", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        coursTable = new JTable(tableModel);
        coursTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        coursTable.getTableHeader().setForeground(Color.black);
        coursTable.setRowHeight(25);
        coursTable.setShowGrid(false);
        coursTable.setIntercellSpacing(new Dimension(0, 0));
        coursTable.setFillsViewportHeight(true);

        // Style alterné pour les lignes

        coursTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ODD_ROW);
                }
                return c;
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(coursTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Liste des cours"));
        tableScrollPane.setBackground(BACKGROUND_COLOR);

        gbc.gridy = 2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(tableScrollPane, gbc);
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet de survol

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void ajouterCours() {
        String nom = nomField.getText().trim();

        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Le nom du cours est obligatoire",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO cours (nom, description) VALUES (?, ?)")) {

            ps.setString(1, nom);
            ps.setString(2, descriptionArea.getText().trim());

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this,
                        "Cours ajouté avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderFormulaire();
                chargerCours();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerCours() {
        int selectedRow = coursTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un cours à supprimer",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nomCours = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer le cours '" + nomCours + "'?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBconnect.getconnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM cours WHERE id = ?")) {

                ps.setInt(1, id);

                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Cours supprimé avec succès",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    chargerCours();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void chargerCours() {
        tableModel.setRowCount(0);

        try (Connection conn = DBconnect.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nom, description FROM cours ORDER BY id")) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des cours",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viderFormulaire() {
        nomField.setText("");
        descriptionArea.setText("");
    }
}

