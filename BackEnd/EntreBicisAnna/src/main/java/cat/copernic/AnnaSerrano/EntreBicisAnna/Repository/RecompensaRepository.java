package cat.copernic.AnnaSerrano.EntreBicisAnna.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.EstatRecompensaType;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Recompensa;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;

/**
 * Repositori per accedir a les recompenses.
 *
 * Permet recuperar recompenses per usuari, estat o combinacions d'ambdós.
 */
@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {

        /**
         * Retorna totes les recompenses associades a un usuari.
         *
         * @param usuari objecte Usuari.
         * @return llista de recompenses.
         */
        List<Recompensa> findByUsuari(Usuari usuari);

        /**
         * Retorna totes les recompenses associades a un usuari a partir del seu email.
         *
         * @param email correu electrònic de l'usuari.
         * @return llista de recompenses.
         */
        List<Recompensa> findByUsuariEmail(String email);

        /**
         * Retorna totes les recompenses amb un estat concret.
         *
         * @param estatRecompensa estat de la recompensa.
         * @return llista de recompenses.
         */
        List<Recompensa> findAllByEstatRecompensa(EstatRecompensaType estatRecompensa);

        /**
         * Retorna una recompensa reservada per un usuari concret.
         *
         * @param email           correu electrònic de l'usuari.
         * @param estatRecompensa estat de la recompensa.
         * @return Optional amb la recompensa si existeix.
         */
        Optional<Recompensa> findByUsuariEmailAndEstatRecompensa(String email, EstatRecompensaType estatRecompensa);
}
