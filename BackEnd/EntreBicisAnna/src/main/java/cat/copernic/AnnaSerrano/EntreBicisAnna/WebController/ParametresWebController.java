package cat.copernic.AnnaSerrano.EntreBicisAnna.WebController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Parametres;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.ParametresLogic;

/**
 * Controlador web per a la gestió dels paràmetres del sistema.
 * 
 * Permet visualitzar i modificar els paràmetres generals a través d'una
 * interfície web.
 */
@Controller
@RequestMapping("/parametres")
public class ParametresWebController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ParametresLogic parametresLogic;

    /**
     * Mostra el formulari per modificar els paràmetres del sistema.
     *
     * Recupera els paràmetres existents (si n'hi ha) i els passa a la vista per a
     * la seva edició.
     *
     * @param model model utilitzat per enviar dades a la vista.
     * @return la vista "modificarParametres" amb el formulari.
     */
    @GetMapping("/modificar")
    public String mostrarFormulariModificarParametres(Model model) {
        log.info("➡ Cridant mostrarFormulariModificarParametres...");

        Parametres parametres = parametresLogic.findById().orElse(new Parametres());
        model.addAttribute("parametres", parametres);

        return "modificarParametres";
    }

    /**
     * Desa els canvis realitzats als paràmetres del sistema.
     *
     * Si es produeix un error durant el procés de desat, es mostra un missatge a la
     * vista.
     *
     * @param parametres objecte amb els nous valors dels paràmetres.
     * @param model      model per mostrar informació i errors a la vista.
     * @return redirecció a la pàgina principal si l'operació té èxit, o
     *         la mateixa vista amb error si falla.
     */
    @PostMapping("/modificar")
    public String guardarParametresModificatS(@ModelAttribute("parametres") Parametres parametres, Model model) {

        try {

            parametresLogic.guardarParametres(parametres);
            log.info("Paràmetres modificats amb èxit: {}", parametres);

        } catch (Exception e) {
            log.error("Error al desar els paràmetres: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("parametres", parametres);
            return "modificarParametres";
        }

        return "redirect:/";
    }

}
