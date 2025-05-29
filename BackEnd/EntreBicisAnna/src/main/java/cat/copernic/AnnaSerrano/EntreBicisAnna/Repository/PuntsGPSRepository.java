package cat.copernic.AnnaSerrano.EntreBicisAnna.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.PuntsGPS;

/**
 * Repositori per gestionar els punts GPS associats a una ruta.
 *
 * Permet fer operacions CRUD sobre l'entitat {@link PuntsGPS}.
 */
@Repository
public interface PuntsGPSRepository extends JpaRepository<PuntsGPS, Long> {

    
} 