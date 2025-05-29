package cat.copernic.AnnaSerrano.EntreBicisAnna.Entity;


/**
 * Classe que representa una sol·licitud d'inici de sessió.
 */

public class LoginRequest {

    /**
     * l'EMAIL per a l'inici de sessió.
     */
    private String email;

    /**
     * La contrasenya per a l'inici de sessió.
     */
    private String contrasenya;

    /**
     * Obté l'email de l'usuari.
     *
     * @return L'email de l'usuari.
     */
    public String getEmail() { return email; }

    /**
     * Obté la contrasenya.
     *
     * @return La contrasenya.
     */
    public String getContrasenya() { return contrasenya; }
}

