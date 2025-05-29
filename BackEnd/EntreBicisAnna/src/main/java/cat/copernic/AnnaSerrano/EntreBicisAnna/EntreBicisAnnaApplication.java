package cat.copernic.AnnaSerrano.EntreBicisAnna;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Parametres;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.UsuariType;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.ParametresLogic;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.UsuariLogic;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.UsuariRepository;

/**
 * Classe principal de l'aplicació EntreBicis.
 * 
 * Inicia el context de Spring Boot, crea un usuari administrador per defecte
 * i afegeix els paràmetres inicials del sistema si encara no existeixen.
 */
@SpringBootApplication
public class EntreBicisAnnaApplication {

    private static final Logger log = LoggerFactory.getLogger(EntreBicisAnnaApplication.class);

    /**
     * Punt d’entrada de l’aplicació.
     *
     * Inicialitza el context de Spring Boot, afegeix un administrador
     * i crea els paràmetres per defecte si no s’han definit prèviament.
     *
     */
    public static void main(String[] args) {
        var context = SpringApplication.run(EntreBicisAnnaApplication.class, args);

        log.info("Iniciant aplicació EntreBicis...");

        UsuariRepository usuariRepository = context.getBean(UsuariRepository.class);
        UsuariLogic usuariLogic = context.getBean(UsuariLogic.class);
        ParametresLogic parametresLogic = context.getBean(ParametresLogic.class);

        log.info("Afegint administrador");

        Usuari adminUser = new Usuari();
        adminUser.setEmail("admin@gmail.com");
        adminUser.setTelefon("123456789");
        adminUser.setNomComplet("Pepita López Rodríguez");
        adminUser.setRol(UsuariType.ADMIN);
        adminUser.setDataAlta(LocalDate.now());
        adminUser.setSaldo(100.00);
        adminUser.setPoblacio("Barcelona");
        adminUser.setObservacions("Administrador principal");
        adminUser.setContrasenya("123456");

        try {
            usuariLogic.altaUsuari(adminUser);
            log.info("Usuari afegit: {}", adminUser);
        } catch (Exception e) {
            log.error("Error afegint usuari: {}", e.getMessage(), e);
        }

        Optional<Parametres> parametres = parametresLogic.findById();
        if (!parametres.isPresent()) {
            log.info("Afegint paràmetres inicials");

            Parametres parametresNou = new Parametres();
            parametresNou.setIdParametres(1);
            parametresNou.setVelocitatMaxValida(60);
            parametresNou.setTempsMaxAturada(5);
            parametresNou.setConversioSaldoKm(1);
            parametresNou.setTempsMaxRecompensa(72);

            try {
                parametresLogic.guardarParametres(parametresNou);
                log.info("Paràmetres afegits: {}", parametresNou);
            } catch (Exception e) {
                log.error("Error afegint paràmetres: {}", e.getMessage(), e);
            }
        } else {
            log.info("Els paràmetres ja existeixen: {}", parametres.get());
        }
    }
}
