package cat.copernic.AnnaSerrano.EntreBicisAnna.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Ruta;

/**
 * Repositori per accedir i gestionar les rutes dels usuaris.
 *
 * Permet recuperar totes les rutes associades a un usuari concret.
 */

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    /**
     * Retorna totes les rutes associades a l’usuari amb el correu indicat.
     *
     * @param email correu electrònic de l'usuari.
     * @return llista de rutes.
     */
    List<Ruta> findByUsuariEmail(String email);
}
