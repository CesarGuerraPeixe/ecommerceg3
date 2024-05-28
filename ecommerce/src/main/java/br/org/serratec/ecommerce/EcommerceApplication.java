package br.org.serratec.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.org.serratec.ecommerce.services.EmailService;

@SpringBootApplication
public class EcommerceApplication {

	private EmailService emailservice;

	public EcommerceApplication(EmailService emailservice) {
		super();
		this.emailservice = emailservice;
	}

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	private void Run() {
		emailservice.enviarEmail("enzo.front17@gmail.com", "Email para teste do ecommerce",
				"Parabéns pela sua compra!");
	}

}
