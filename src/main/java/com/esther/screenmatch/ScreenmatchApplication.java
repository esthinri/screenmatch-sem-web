package com.esther.screenmatch;

import com.esther.screenmatch.model.DadosSerie;
import com.esther.screenmatch.service.ConsumoAPI;
import com.esther.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run (String... args) throws Exception{
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("http://www.omdbapi.com/?t=Friends&apikey=30a5ac37");
		System.out.println(json);
		ConverteDados converteDados = new ConverteDados();
		DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
		System.out.println(dadosSerie);
		/*var json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);*/
	}


}
