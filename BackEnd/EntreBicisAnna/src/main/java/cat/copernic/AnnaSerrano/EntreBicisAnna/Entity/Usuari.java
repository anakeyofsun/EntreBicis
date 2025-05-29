package cat.copernic.AnnaSerrano.EntreBicisAnna.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entitat que representa un usuari del sistema.
 *
 * Pot ser administrador o usuari normal. Aquesta classe implementa
 * `UserDetails`
 * per tal d'integrar-se amb Spring Security.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuari")
public class Usuari implements UserDetails {
    /**
     * Correu electrònic de l’usuari (també és l’identificador principal).
     */
    @Id
    private String email;

    /**
     * Telèfon de contacte de l’usuari.
     */
    @Column(unique = true, nullable = false)
    private String telefon;

    /**
     * Nom complet de l’usuari.
     */
    @Column(unique = true, nullable = false)
    private String nomComplet;

    /**
     * Tipus o rol de l’usuari (ADMIN o USER).
     */
    @Column(name = "rol")
    private UsuariType rol;

    /**
     * Data en què es va registrar l’usuari.
     */
    @Column(name = "data_alta")
    private LocalDate dataAlta;

    /**
     * Imatge de perfil de l’usuari, guardada com a BLOB.
     */
    @Lob
    @Column(name = "imatge_perfil", columnDefinition = "LONGBLOB")
    private byte[] imatgePerfil;

    /**
     * Representació en Base64 de la imatge de perfil (no es guarda a la base de
     * dades).
     */
    @Transient // No es guarda a la base de dades
    private String imatgePerfilBase64;

    /**
     * Saldo disponible de l’usuari.
     */
    private Double saldo;

    /**
     * Població de residència de l’usuari.
     */
    private String poblacio;

    /**
     * Observacions internes sobre l’usuari (ús administratiu).
     */
    private String observacions;

    /**
     * Contrasenya codificada de l’usuari.
     */
    private String contrasenya;

    /**
     * Llista de rutes realitzades per aquest usuari.
     */
    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ruta> rutes = new ArrayList<>();

    /**
     * Retorna la contrasenya de l’usuari.
     * 
     * @return contrasenya codificada.
     */
    @Override
    public String getPassword() {
        return getContrasenya();
    }

    /**
     * Retorna el nom d’usuari, que en aquest cas és el seu correu electrònic.
     * 
     * @return correu electrònic de l’usuari.
     */
    @Override
    public String getUsername() {
        return getEmail();
    }

    /**
     * Retorna les autoritats (rols) de l’usuari.
     * 
     * @return llista d'autoritats per a Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String toString() {
        return "Usuari{" +
                ", nom=" + nomComplet +
                ", email=" + email +
                // no incloure rutes!
                '}';
    }
}