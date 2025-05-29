package cat.copernic.AnnaSerrano.EntreBicisAnna.WebController;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Recompensa;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.RecompensaLogic;

/**
 * Controlador web per a la gestió de recompenses.
 *
 * Permet afegir, consultar, llistar, assignar i esborrar recompenses des d'una
 * interfície web.
 */
@Controller
@RequestMapping("/recompenses")
public class RecompensaWebController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RecompensaLogic recompensaLogic;

    /**
     * Mostra el formulari per crear una nova recompensa.
     *
     * @param model model per passar l’objecte recompensa a la vista.
     * @return la vista "afegirRecompensa" amb el formulari buit.
     */
    @GetMapping("/afegir")
    public String mostrarFormulariAfegirRecompensa(Model model) {
        log.info("➡ Cridant mostrarFormulariAfegirRecompensa()...");

        Recompensa recompensa = new Recompensa();
        model.addAttribute("recompensa", recompensa);

        return "afegirRecompensa";
    }

    /**
     * Desa una nova recompensa creada per l’usuari.
     *
     * Assigna la data de creació i guarda la imatge si s’ha pujat.
     * En cas d’error, mostra el formulari amb missatge d’error.
     *
     * @param model      model utilitzat per passar dades a la vista.
     * @param recompensa objecte recompensa omplert pel formulari.
     * @param imageFile  arxiu de la imatge adjunta.
     * @return redirecció a la llista de recompenses si té èxit, o
     *         torna a la vista d’afegir recompensa si hi ha errors.
     */
    @PostMapping("/afegir")
    public String afegirRecompensa(Model model, @ModelAttribute("recompensa") Recompensa recompensa,
            @RequestParam("imageFile") MultipartFile imageFile) {
        log.info("➡ Cridant afegirRecompensa()...");

        try {

            if (!imageFile.isEmpty()) {
                recompensa.setImatgeRecompensa(imageFile.getBytes());
            }
            recompensa.setDataCreacio(LocalDate.now());
            recompensaLogic.altaRecompensa(recompensa);
            log.info("Recompensa registrada correctament amb descripcio: {}", recompensa.getDescripcio());

        } catch (Exception e) {
            log.error("Error durant el registre de la recompensa amb descripcio: " + recompensa.getDescripcio() + " "
                    + e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("recompensa", recompensa);

            if (recompensa.getImatgeRecompensa() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(recompensa.getImatgeRecompensa());
                model.addAttribute("imatgeBase64", imatgeBase64);
            }

            return "afegirRecompensa";
        }
        log.info("Procés d'alta finalitzat amb èxit per la recompensa amb descripcio: {}", recompensa.getDescripcio());
        return "redirect:/recompenses/llistar";

    }

    /**
     * Mostra la llista de totes les recompenses disponibles.
     *
     * Afegeix la imatge codificada en Base64 a cada recompensa per mostrar-la a la
     * vista.
     *
     * @param model model per enviar la llista de recompenses a la vista.
     * @return la vista "llistarRecompenses".
     */
    @GetMapping("/llistar")
    public String llistarRecompenses(Model model) {
        log.info("➡ Cridant llistarRecompenses()...");
        log.info("Carregant la pàgina de llistat de recompenses");

        List<Recompensa> recompenses = recompensaLogic.getAllRecompenses();
        // Obtenim la llista de rutes des de la lògica
        for (Recompensa recompensa : recompenses) {
            if (recompensa.getImatgeRecompensa() != null) {
                String base64Image = Base64.getEncoder().encodeToString(recompensa.getImatgeRecompensa());
                recompensa.setImatgeBase64(base64Image);
            }
        }
        model.addAttribute("recompenses", recompenses);

        return "llistarRecompenses";
    }

    /**
     * Mostra la llista de recompenses associades a un usuari concret.
     *
     * @param email correu electrònic de l’usuari.
     * @param model model per passar la llista a la vista.
     * @return la vista "llistarRecompenses" amb les recompenses de l’usuari.
     */
    @GetMapping("/llistar/{email}")
    public String llistarRecompensesPerUsuari(@PathVariable String email, Model model) {
        log.info("➡ Cridant llistarRecompensesPerUsuari() per l'usuari amb email: " + email);

        List<Recompensa> recompenses = recompensaLogic.getAllRecompensesByUsuari(email); // 🔍 Obtenim les rutes per
                                                                                         // email
        log.info("Recompenses trobades: " + recompenses.size());

        for (Recompensa recompensa : recompenses) {
            if (recompensa.getImatgeRecompensa() != null) {
                String base64Image = Base64.getEncoder().encodeToString(recompensa.getImatgeRecompensa());
                recompensa.setImatgeBase64(base64Image);
            }
        }

        model.addAttribute("recompenses", recompenses);
        model.addAttribute("email", email); // Per si vols mostrar-lo a la vista

        return "llistarRecompenses";
    }

    /**
     * Elimina una recompensa segons el seu identificador.
     *
     * Mostra un missatge d’èxit o error després del procés d’esborrat.
     *
     * @param idRecompensa       identificador de la recompensa a esborrar.
     * @param redirectAttributes atributs per mostrar missatges després de
     *                           redirigir.
     * @return redirecció a la llista de recompenses.
     */
    @GetMapping("/esborrar/{idRecompensa}")
    public String esborrarRecompensa(@PathVariable("idRecompensa") Long idRecompensa,
            RedirectAttributes redirectAttributes) {
        try {
            log.info("Iniciant procés d'esborrat per a la recompensa amb id: {}", idRecompensa);
            recompensaLogic.esborrarRecompensa(idRecompensa);
            log.info("Recompensa amb id {} esborrada correctament.", idRecompensa);

            redirectAttributes.addFlashAttribute("missatgeExit", "Recompensa esborrada correctament!");
        } catch (RuntimeException e) {
            log.warn("No s'ha pogut esborrar la recompensa: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("missatgeError", e.getMessage());
        }

        return "redirect:/recompenses/llistar";
    }

    /**
     * Mostra els detalls d’una recompensa concreta.
     *
     * Si no es troba, redirigeix a la llista amb un missatge d’error.
     *
     * @param idRecompensa identificador de la recompensa.
     * @param model        model per passar la recompensa a la vista.
     * @return la vista "consultaRecompensa" o redirecció a "llistarRecompenses".
     */
    @GetMapping("/consulta/{idRecompensa}")
    public String visualitzarRecompensa(@PathVariable Long idRecompensa, Model model) {

        log.info("Iniciant consulta per a la recompensa amb id: {}", idRecompensa);

        Optional<Recompensa> optionalRecompensa = recompensaLogic.findById(idRecompensa);

        if (optionalRecompensa.isEmpty()) {
            log.warn("No s'ha trobat cap recompensa amb id: {}", idRecompensa);
            model.addAttribute("error", "No s'ha trobat cap recompensa amb id: " + idRecompensa);
            return "redirect:/recompenses/llistar";
        }

        Recompensa recompensa = optionalRecompensa.get();

        recompensa.setImatgeBase64(
                recompensa.getImatgeRecompensa() != null ? convertirImatgeABase64(recompensa.getImatgeRecompensa())
                        : "");

        model.addAttribute("recompensa", recompensa);
        log.info("Consulta finalitzada correctament per a la recompensa amb id: {}", idRecompensa);

        return "consultaRecompensa";
    }

    /**
     * Converteix una imatge en format byte array a una cadena Base64.
     *
     * @param imatge imatge en bytes.
     * @return representació Base64 de la imatge.
     */
    public String convertirImatgeABase64(byte[] imatge) {
        if (imatge == null) {
            return ""; // O devuelve una imagen por defecto en Base64 si lo prefieres
        }
        return Base64.getEncoder().encodeToString(imatge);
    }

    /**
     * Assigna una recompensa a l’usuari actiu (o sistema) segons el seu
     * identificador.
     *
     * @param idRecompensa identificador de la recompensa a assignar.
     * @return redirecció a la vista de consulta de la recompensa.
     */
    @PostMapping("/assignar/{idRecompensa}")
    public String assignarRecompensa(@PathVariable Long idRecompensa) {
        recompensaLogic.assignarRecompensa(idRecompensa);
        return "redirect:/recompenses/consulta/" + idRecompensa;
    }

}
