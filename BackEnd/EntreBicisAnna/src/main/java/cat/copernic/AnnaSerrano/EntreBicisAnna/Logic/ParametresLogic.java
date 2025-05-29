package cat.copernic.AnnaSerrano.EntreBicisAnna.Logic;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Parametres;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.ParametresRepository;

/**
 * Servei per gestionar els paràmetres generals del sistema.
 *
 * Permet accedir, consultar i actualitzar els paràmetres globals com velocitat,
 * temps màxim, o la conversió de saldo.
 */
@Service
public class ParametresLogic {

    Logger log = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private ParametresRepository parametresRepository;

    /**
     * Retorna els paràmetres de sistema amb identificador fix (id = 1).
     *
     * @return paràmetres encapsulats en un Optional.
     */
    public Optional<Parametres> findById() {
        log.info("Cridant findById amb id 1");
        return parametresRepository.findById(1L);
    }

    /**
     * Desa els paràmetres del sistema a la base de dades.
     *
     * @param parametres objecte amb els valors de configuració.
     * @return objecte `Parametres` desat.
     */
    public Parametres guardarParametres(Parametres parametres) {

        log.info("Cridant guardarParametres");
        parametres.setIdParametres(1);
        return parametresRepository.save(parametres);
    }

    /**
     * Retorna els paràmetres de sistema si existeixen.
     *
     * @return objecte `Parametres` o `null` si no es troba.
     */
    public Parametres getParametres() {
        log.info("Cridant getParametres()");
        return parametresRepository.findById(1L).orElse(null);
    }
}
