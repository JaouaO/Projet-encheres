package fr.eni.encheres;

import fr.eni.encheres.bll.EnchereService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class ProjetEncheresApplication {

	private static EnchereService enchereService = null;

	public ProjetEncheresApplication(EnchereService enchereService) {
		ProjetEncheresApplication.enchereService = enchereService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ProjetEncheresApplication.class, args);

		Timer timer = new Timer();

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				enchereService.mettreEnVente();
			}
		};

		Date startTime = new Date();
		timer.schedule(task, startTime, 30000);
	}

}
