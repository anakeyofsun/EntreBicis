package cat.copernic.AnnaSerrano.EntreBicisAnna.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;

/**
 * Repositori per accedir a les dades dels usuaris.
 *
 * Ofereix operacions per buscar usuaris per correu i obtenir tots els
 * registrats.
 */
@Repository
public interface UsuariRepository extends JpaRepository<Usuari, Long> {

    /**
     * Cerca un usuari pel seu correu electrònic.
     *
     * @param email correu electrònic.
     * @return Optional amb l’usuari si existeix.
     */
    Optional<Usuari> findByEmail(String email);

    /**
     * Retorna tots els usuaris registrats al sistema.
     *
     * @return llista d'usuaris.
     */
    List<Usuari> findAll();

}
