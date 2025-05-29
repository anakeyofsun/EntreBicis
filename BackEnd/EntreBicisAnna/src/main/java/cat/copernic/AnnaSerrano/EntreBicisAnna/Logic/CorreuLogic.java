package cat.copernic.AnnaSerrano.EntreBicisAnna.Logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servei que encapsula la lògica per enviar correus electrònics.
 *
 * Utilitza `JavaMailSender` per enviar missatges de text simples.
 */
@Service
public class CorreuLogic {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Envia un correu electrònic amb el destinatari, assumpte i contingut
     * especificats.
     *
     * @param destinatari correu electrònic del receptor.
     * @param assumpte    títol o assumpte del missatge.
     * @param cos         contingut del correu electrònic.
     */
    public void enviarEmail(String destinatari, String assumpte, String cos) {
        SimpleMailMessage missatge = new SimpleMailMessage();
        missatge.setTo(destinatari);
        missatge.setSubject(assumpte);
        missatge.setText(cos);
        javaMailSender.send(missatge);
    }
}
