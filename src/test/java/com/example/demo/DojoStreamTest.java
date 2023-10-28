package com.example.demo;


import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DojoStreamTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35(){
        List<Player> list = CsvUtilFile.getPlayers();
        Set<Player> result = list.stream()
                .filter(jugador -> jugador.getAge() > 35)
                .collect(Collectors.toSet());
        result.forEach(System.out::println);
    }

    @Test
    void jugadoresMayoresA35SegunClub(){
        List<Player> list = CsvUtilFile.getPlayers();
        Map<String, List<Player>> result = list.stream()
                .filter(player -> player.getAge() > 35)
                .distinct()
                .collect(Collectors.groupingBy(Player::getClub));

        result.forEach((key, jugadores) -> {
            System.out.println("\n");
            System.out.println(key + ": ");
            jugadores.forEach(System.out::println);
        });

    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){
        List<Player> list = CsvUtilFile.getPlayers();
        AtomicInteger highestWinners = new AtomicInteger(Integer.MIN_VALUE);
        List<Player> result = list.stream()
                .filter(player -> Objects.equals(player.getNational(), "France"))
                .distinct()
                .filter(frenchPlayer ->
                {
                    int bestPlayer = highestWinners.get();
                    if(frenchPlayer.getWinners() > bestPlayer)
                    {
                        highestWinners.set(frenchPlayer.winners);
                        return true;
                    }
                    if(frenchPlayer.getWinners() == highestWinners.get())
                    {
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());

        result.forEach(System.out::println);

        //More simplified
        /*List<Player> list = CsvUtilFile.getPlayers();
        Optional<Player> result = list.stream()
                .filter(jugador -> "France".equals(jugador.getNational()))
                .max((j1, j2) -> Integer.compare(j1.getWinners(), j2.getWinners()));

        if (result.isPresent()) {
            Player maxWinner = result.get();
            System.out.println("Jugador con más winners de Francia: " + maxWinner.getName() +
                    " con " + maxWinner.getWinners() + " victorias.");
        } else {
            System.out.println("No se encontró ningún jugador de Francia.");
        }*/
    }


    @Test
    void clubsAgrupadosPorNacionalidad(){
        List<Player> list = CsvUtilFile.getPlayers();
        Map<String, List<String>> clubsAgrupadosPorNacionalidad = list.stream()
                .collect(Collectors.groupingBy(Player::getNational,
                        Collectors.mapping(Player::getClub, Collectors.toList())));

        clubsAgrupadosPorNacionalidad.forEach((nacionalidad, club) -> {
            System.out.println("Club: " + club + "\n");
            System.out.println("Nacionalidad: " + nacionalidad);
        });
    }

    @Test
    void clubConElMejorJugador() {
        List<Player> jugadores = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = jugadores.stream()
                .max((jugador1, jugador2) -> Integer.compare(jugador1.getWinners(), jugador2.getWinners()));
        if (mejorJugador.isPresent()) {
            String clubConElMejorJugador = mejorJugador.get().club;
            System.out.println("El club con el mejor jugador es: " + clubConElMejorJugador);
        }
    }

    @Test
    void ElMejorJugador(){
        List<Player> jugadores = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = jugadores.stream()
                .max((jugador1, jugador2) -> Integer.compare(jugador1.getWinners(), jugador2.getWinners()));
        if (mejorJugador.isPresent()) {
            Player mejorJugadorDeTodos = mejorJugador.get();
            System.out.println("El mejor jugador es es: " + mejorJugadorDeTodos.getName());
        }
    }

    @Test
    void mejorJugadorSegunNacionalidad(){
        List<Player> jugadores = CsvUtilFile.getPlayers();
        Map<String, Optional<Player>> mejorJugadorPorNacionalidad = jugadores.stream()
                .collect(Collectors.groupingBy(Player::getNational,
                        Collectors.maxBy(Comparator.comparing(Player::getWinners))));
        mejorJugadorPorNacionalidad.forEach((nacionalidad, jugador) -> {
            System.out.println("Nacionalidad: " + nacionalidad);
            System.out.println("Jugador: " + (jugador.isPresent() ? jugador.get().getName() : "") + "\n");
        });
    }


}
