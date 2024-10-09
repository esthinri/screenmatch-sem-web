package com.esther.screenmatch.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Getter
@Setter
public class Episodio {

    private Integer season;
    private String title;
    private Integer episode;
    private Double imdbRating;
    private LocalDate released;

    public Episodio(Integer season, DadosEpisodio dadosEpisodio) {
        this.season = season;
        this.title = dadosEpisodio.title();
        this.episode = dadosEpisodio.episode();

        try {
            this.imdbRating = Double.valueOf(dadosEpisodio.imdbRating());
        } catch (NumberFormatException e) {
            this.imdbRating = 0.0;
        }

        try{
            this.released = LocalDate.parse(dadosEpisodio.released());
        } catch (DateTimeParseException e) {
            this.released = null;
        }
    }



    @Override
    public String toString() {
        return "temporada=" + season +
                ", titulo='" + title + '\'' +
                ", numeroEpisodio=" + episode +
                ", avaliacao=" + imdbRating +
                ", dataLancamento=" + released;
    }

}
