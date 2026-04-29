package models;

public class Etudiant {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private int niveau;
    private int points;
    private boolean estMentor;

    public Etudiant() {}

    public Etudiant(String nom, String prenom, String email,
                    String motDePasse, int niveau, int points, boolean estMentor) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.niveau = niveau;
        this.points = points;
        this.estMentor = estMentor;
    }

    public Etudiant(int id, String nom, String prenom, String email,
                    String motDePasse, int niveau, int points, boolean estMentor) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.niveau = niveau;
        this.points = points;
        this.estMentor = estMentor;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public int getNiveau() { return niveau; }
    public void setNiveau(int niveau) { this.niveau = niveau; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public boolean isEstMentor() { return estMentor; }
    public void setEstMentor(boolean estMentor) { this.estMentor = estMentor; }

    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", niveau=" + niveau +
                ", points=" + points +
                ", estMentor=" + estMentor +
                "}\n";
    }
}