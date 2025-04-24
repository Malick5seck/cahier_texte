/*package models ;
import java.time.LocalDate ;
import java.time.LocalTime;

public class Seance {
    private String nomCours ;
    private LocalDate date ;
    private LocalTime heureDebut ;
    private String contenu ;
    private String statut ;
    private  String commentaire ;

    public Seance(String nomCours,LocalDate date,LocalTime heureDebut ,String contenu,String statut,String commentaire) {

        this.nomCours = nomCours;
        this.date = date ;
        this.heureDebut = heureDebut ;
        this.contenu = contenu ;
        this.statut = statut ;
        this.commentaire = commentaire;
    }

    public Seance(int id, String nomCours, LocalDate date, String contenu) {

    }

    public String getnomCours() {
        return nomCours;
    }

    public LocalDate getdate() {
        return date;
    }

    public LocalTime getheureDebut() {
        return heureDebut;
    }

    public String getcontenu() {
        return contenu;
    }

    public String getstatut() {
        return statut;
    }

    public String getcommentaire() {
        return commentaire;
    }

    public void setnomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setheureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public void setcontenu(String contenu) {
        this.contenu = contenu;
    }

    public void setstatut(String statut) {
        this.statut = statut;
    }

    public void setcommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}


 */
//package models;
//
//import java.time.LocalDate;
//
//public class Seance {
//    private int id;
//    private String nomCours;
//    private LocalDate date;
//    private String contenu;
//    private String statut;
//    private String commentaire;
//    private String heureDebut;
//
//    // Constructeur pour affichage dans validation
//    public Seance(int id, String nomCours, LocalDate date, String contenu) {
//        this.id = id;
//        this.nomCours = nomCours;
//        this.date = date;
//        this.contenu = contenu;
//    }
//
//    // Constructeur complet pour affichage général
//    public Seance(String nomCours, LocalDate date, java.time.LocalTime heureDebut, String contenu, String statut, String commentaire) {
//        this.nomCours = nomCours;
//        this.date = date;
//        this.contenu = contenu;
//        this.statut = statut;
//        this.commentaire = commentaire;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getNomCours() {
//        return nomCours;
//    }
//
//    public LocalDate getDate() {
//        return date;
//    }
//
//    public String getContenu() {
//        return contenu;
//    }
//
//    public String getStatut() {
//        return statut;
//    }
//
//    public String getCommentaire() {
//        return commentaire;
//    }
//
//    public String getHeureDebut() {
//
//        return heureDebut;
//    }
//}

package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Seance {

    private int id;
    private String nomCours;
    private LocalDate date;
    private LocalTime heureDebut;
    private String contenu;
    private String statut;
    private String commentaire;

    // ✅ Constructeur complet avec ID (pour validation/responsable)
    public Seance(int id, String nomCours, LocalDate date, LocalTime heureDebut, String contenu, String statut, String commentaire) {
        this.id = id;
        this.nomCours = nomCours;
        this.date = date;
        this.heureDebut = heureDebut;
        this.contenu = contenu;
        this.statut = statut;
        this.commentaire = commentaire;
    }

    // ✅ Constructeur sans ID (utile pour saisie côté enseignant)
    public Seance(String nomCours, LocalDate date, LocalTime heureDebut, String contenu, String statut, String commentaire) {
        this.nomCours = nomCours;
        this.date = date;
        this.heureDebut = heureDebut;
        this.contenu = contenu;
        this.statut = statut;
        this.commentaire = commentaire;
    }

    // ✅ Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCours() {
        return nomCours;
    }

    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    // ✅ Pour affichage console ou debug
    @Override
    public String toString() {
        return "Seance{" +
                "id=" + id +
                ", nomCours='" + nomCours + '\'' +
                ", date=" + date +
                ", heureDebut=" + heureDebut +
                ", contenu='" + contenu + '\'' +
                ", statut='" + statut + '\'' +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }
}
