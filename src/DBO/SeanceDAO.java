
package DBO;

import models.Seance;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO {

    // Récupérer toutes les séances quel que soit le statut
    public static List<Seance> getToutesLesSeances() {
        List<Seance> seances = new ArrayList<>();

        String query = """
        SELECT s.id, s.dateseance, s.heure_debut, s.contenu, s.statut, 
               s.commentaire_refus, c.nom AS nomCours
        FROM seance s
        JOIN cours c ON s.cours_id = c.id
        ORDER BY s.dateseance, s.heure_debut
    """;

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate date = rs.getDate("dateseance").toLocalDate();
                LocalTime heureDebut = rs.getTime("heure_debut").toLocalTime();
                String contenu = rs.getString("contenu");
                String nomCours = rs.getString("nomCours");
                String statut = rs.getString("statut");
                String commentaireRefus = rs.getString("commentaire_refus"); // Nom de colonne correct

                Seance s = new Seance(id, nomCours, date, heureDebut, contenu, statut, commentaireRefus);
                seances.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seances;
    }



    // Mettre à jour le statut d'une séance
    public static boolean mettreAJourStatut(int idSeance, String statut, String commentaire) {
        String query = "UPDATE seance SET statut = ?, commentaire_refus = ? WHERE id = ?";

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, statut);
            if (commentaire == null || commentaire.isEmpty()) {
                pst.setNull(2, Types.VARCHAR);
            } else {
                pst.setString(2, commentaire);
            }
            pst.setInt(3, idSeance);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer les séances par enseignant
    public static List<Seance> getSeancesByEnseignant(int idEnseignant) {
        List<Seance> seances = new ArrayList<>();

        String query = """
            SELECT s.id, s.dateseance AS date, s.heure_debut, s.contenu, s.statut, 
                   s.commentaire_refus AS commentaire, c.nom AS nomCours
            FROM seance s
            JOIN cours c ON s.cours_id = c.id
            WHERE s.Enseignant_id = ?
            ORDER BY c.nom, s.dateseance, s.heure_debut
        """;

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idEnseignant);
            ResultSet results = pstmt.executeQuery();

            while (results.next()) {
                int id = results.getInt("id");
                String nomCours = results.getString("nomCours");
                LocalDate date = results.getDate("date").toLocalDate();
                LocalTime heureDebut = results.getTime("heure_debut").toLocalTime();
                String contenu = results.getString("contenu");
                String statut = results.getString("statut");
                String commentaire = results.getString("commentaire");

                Seance s = new Seance(id, nomCours, date, heureDebut, contenu, statut, commentaire);
                seances.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seances;
    }
}