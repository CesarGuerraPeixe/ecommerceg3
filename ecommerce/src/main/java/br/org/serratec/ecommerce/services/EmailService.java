package br.org.serratec.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	public JavaMailSender emailSender;

	public EmailService(JavaMailSender javaMailSender) {
		this.emailSender = javaMailSender;
	}

	public void enviarEmail(String destinatario, String assunto, String mensagem) {
		var mailMessage = new SimpleMailMessage();

		mailMessage.setTo(destinatario);
		mailMessage.setSubject(assunto);
		mailMessage.setText(mensagem);
		mailMessage.setFrom("teste@gmail.com");

		try {
			emailSender.send(mailMessage);
			System.out.println("email enviado");
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar enviar o e-mail: " + ex.getMessage());
		}

	}
}