package cat.copernic.AnnaSerrano.EntreBicisAnna.WebController;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Parametres;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.PuntsGPS;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Ruta;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.ParametresLogic;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.RutesLogic;

/**
 * Controlador web per a la gestió de rutes.
 * 
 * Permet llistar, veure detalls i canviar l'estat de validació de les rutes
 * a través d'una interfície web.
 */
@Controller
@Scope("session")
public class RutaWebController {
    private static final Logger log = LoggerFactory.getLogger(RutaWebController.class);

    @Autowired
    private RutesLogic rutesLogic;

    @Autowired
    private ParametresLogic parametresLogic;

    /**
     * Mostra la pàgina amb el llistat de totes les rutes del sistema.
     *
     * @param model model utilitzat per passar la llista de rutes a la vista.
     * @return la vista "index" amb totes les rutes.
     */
    @GetMapping("/")
    public String llistarRutes(Model model) {
        log.info("➡ Cridant llistarRutes()...");
        log.info("Carregant la pàgina de llistat de rutes");

        List<Ruta> rutes = rutesLogic.getAllRutes();
        // Obtenim la llista de rutes des de la lògica
        model.addAttribute("rutes", rutes);

        return "index";
    }

    /**
     * Mostra les rutes associades a un usuari concret segons el seu correu
     * electrònic.
     *
     * @param email correu electrònic de l’usuari.
     * @param model model utilitzat per passar dades a la vista.
     * @return la vista "index" amb les rutes filtrades.
     */
    @GetMapping("/llistar/{email}")
    public String llistarRutesPerUsuari(@PathVariable String email, Model model) {
        log.info("➡ Cridant llistarRutesPerUsuari() per l'usuari amb email: " + email);

        List<Ruta> rutes = rutesLogic.getAllRutesByUsuari(email); // 🔍 Obtenim les rutes per email
        log.info("Rutes trobades: " + rutes.size());

        model.addAttribute("rutes", rutes);
        model.addAttribute("email", email); // Per si vols mostrar-lo a la vista

        return "index";
    }

    /**
     * Mostra els detalls d'una ruta concreta, incloent els punts GPS i els
     * paràmetres del sistema.
     *
     * @param idRuta identificador de la ruta.
     * @param model  model utilitzat per passar les dades a la vista.
     * @return la vista "consultaRuta" amb la informació detallada.
     */
    @GetMapping("/detall/{idRuta}")
    public String veureDetallRuta(@PathVariable Long idRuta, Model model) {
        Ruta ruta = rutesLogic.findById(idRuta);
        // Recuperem punts GPS associats
        List<PuntsGPS> punts = ruta.getPuntsGPS(); // O també podries fer-ho via servei/repo

        // Recuperem els paràmetres del sistema
        Parametres parametres = parametresLogic.getParametres();

        model.addAttribute("ruta", ruta);
        model.addAttribute("puntsGPS", punts);
        model.addAttribute("parametres", parametres);

        return "consultaRuta"; // nom de la plantilla HTML
    }

    /**
     * Modifica l’estat de validació d’una ruta.
     *
     * @param idRuta             identificador de la ruta.
     * @param validada           nou estat de validació (true per validar, false per
     *                           invalidar).
     * @param redirectAttributes atributs per mostrar missatges després de la
     *                           redirecció.
     * @return redirecció a la vista de detall de la ruta.
     */
    @PostMapping("/rutes/validacio")
    public String canviarEstatRuta(
            @RequestParam Long idRuta,
            @RequestParam Boolean validada,
            RedirectAttributes redirectAttributes) {

        try {
            rutesLogic.canviarEstatValidacio(idRuta, validada);
            redirectAttributes.addFlashAttribute("missatgeExit",
                    validada ? "Ruta validada correctament." : "Ruta invalidada correctament.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("missatgeError", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("missatgeError",
                    "No s'ha pogut modificar l'estat de la ruta.");
        }

        return "redirect:/detall/" + idRuta;
    }
}