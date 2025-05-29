package cat.copernic.AnnaSerrano.EntreBicisAnna.Entity;

/**
 * Classe que encapsula la resposta retornada després d’un inici de sessió
 * correcte.
 *
 * Conté informació bàsica com el correu electrònic i el rol de l’usuari.
 */
public class LoginResponse {

    /**
     * L'email de la resposta d'inici de sessió.
     */
    private String email;

    /**
     * El tipus d'usuari de la resposta d'inici de sessió.
     */
    private UsuariType rol;

    /**
     * Constructor per inicialitzar la resposta d’inici de sessió.
     *
     * @param email correu electrònic de l’usuari.
     * @param rol   rol o tipus d’usuari.
     */
    public LoginResponse(String email, UsuariType rol) {
        this.email = email;
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UsuariType getRol() {
        return rol;
    }

    public void setRol(UsuariType rol) {
        this.rol = rol;
    }
}
