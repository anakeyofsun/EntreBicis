package cat.copernic.AnnaSerrano.EntreBicisAnna.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Parametres;

/**
 * Repositori per accedir i gestionar els paràmetres del sistema.
 * 
 * Proporciona operacions de persistència sobre l'entitat {@link Parametres}.
 */
@Repository
public interface ParametresRepository extends JpaRepository<Parametres, Long> {
   
     /**
     * Cerca els paràmetres pel seu identificador.
     *
     * @param id identificador únic dels paràmetres.
     * @return un Optional amb els paràmetres si existeixen.
     */
    public Optional<Parametres> findById(long id); // Mètode per trobar els paràmetres per id
        
}
