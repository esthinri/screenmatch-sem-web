package com.esther.screenmatch.principal;


import com.esther.screenmatch.model.DadosEpisodio;
import com.esther.screenmatch.model.DadosSerie;
import com.esther.screenmatch.model.DadosTemporada;
import com.esther.screenmatch.model.Episodio;
import com.esther.screenmatch.service.ConsumoAPI;
import com.esther.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Principal {
    private Scanner read = new Scanner(System.in);
    private final String URI="http://www.omdbapi.com/?apikey=";
    private final String API_KEY="30a5ac37";
    private ConverteDados converteDados = new ConverteDados();

    public void exibeMenu(){
        System.out.println("\nDigite o nome da serie:");
        var nomeSerie = read.nextLine();
        var consumoAPI = new ConsumoAPI();
        var json = consumoAPI.obterDados(URI+API_KEY+"&t="+nomeSerie.replace(" ","+"));

        DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
      //  System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for(int i = 1; i<=dadosSerie.totalSeasons(); i++){
			json = consumoAPI.obterDados(URI+API_KEY+"&t="+nomeSerie.replace(" ","+")+"&season="+i);
			DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
	//	temporadas.forEach(System.out::println);


     //   temporadas.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

        /*List<String> nomes = Arrays.asList("Esther","Angelica", "Hinrichsen");
        nomes.stream()
                .sorted()
                .filter(n -> n.startsWith("E"))
                .map(String::toUpperCase)
                .forEach(System.out::println);
        */
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodes().stream())
                .toList();

        /*System.out.println("\nTop 10 episodios da serie "+nomeSerie);
        dadosEpisodios.stream()
                .filter(e -> !e.imdbRating().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("\nPrimeiro Filtro(N/A): " + e))
                .sorted(Comparator.comparing(DadosEpisodio::imdbRating).reversed())
                .peek(e -> System.out.println("\nOrdenação: "+e))
                .limit(10)
                .peek(e -> System.out.println("\nLimite: "+e))
                .map(e -> e.title().toUpperCase())
                .peek(e -> System.out.println("\nMapeamento: "+e))
                .forEach(System.out::println);*/


        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episodio(t.season(), d))
                ).toList();



         episodios.forEach(System.out::println);
        System.out.println("\nDigite parte do titulo do episodio:");
        var partTitle = read.nextLine();
        Optional<Episodio> episodioEncontrado = episodios.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(partTitle.toUpperCase()))
                .findFirst();

        String mensagemRetorno = episodioEncontrado.isPresent()?"Foi encontrado o episodio "+episodioEncontrado.get().getTitle()+" da "+episodioEncontrado.get().getSeason()+" temporada":"Não foi encontrado um episodio que parte do titulo contrenha: "+partTitle;

        System.out.println(mensagemRetorno);


        /*System.out.println("\nA partir de que anos você quer ver os episodios?");
        var ano = read.nextInt();
        read.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter( e -> e.getReleased() != null && e.getReleased().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: "+e.getSeason()+
                                "\nEpisodio: "+e.getTitle()+
                                "\nData Lançamento: "+e.getReleased().format(formatter)
                ));*/

        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getImdbRating() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getSeason,
                        Collectors.averagingDouble(Episodio::getImdbRating)));
        System.out.println(avaliacaoPorTemporada);

        DoubleSummaryStatistics statistics = episodios.stream()
                .filter(e -> e.getImdbRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getImdbRating));

        System.out.println("Media: "+statistics.getAverage());
        System.out.println("Melhor: "+statistics.getMax());
        System.out.println("Pior: "+statistics.getMin());
        System.out.println("Quantidade: "+statistics.getCount());

    }
}
