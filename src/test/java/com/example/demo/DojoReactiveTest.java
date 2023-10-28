package com.example.demo;


import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Boolean.parseBoolean;

public class DojoReactiveTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35() {
        List<Player> list = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(list);

        observable.filter(jugador -> jugador.getAge() > 35)
                .subscribe(System.out::println);
    }


    @Test
    void jugadoresMayoresA35SegunClub(){
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable.filter(player -> player.getAge() > 35)
                .distinct()
                .groupBy(Player::getClub)
                .flatMap(groupedFlux -> groupedFlux
                        .collectList()
                        .map(list -> {
                            Map<String, List<Player>> map = new HashMap<>();
                            map.put(groupedFlux.key(), list);
                            return map;
                        }))
                .subscribe(map -> {
                    map.forEach((key, value) -> {
                        System.out.println("\n");
                        System.out.println(key + ": ");
                        value.forEach(System.out::println);
                    });
                });

    }


    @Test
    void mejorJugadorConNacionalidadFrancia(){
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);
        AtomicInteger highestWinners = new AtomicInteger(Integer.MIN_VALUE);

        observable.filter(player -> player.national.equals("France"))
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
                .subscribe(System.out::println)
        ;
    }

    @Test
    void clubsAgrupadosPorNacionalidad(){
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable.collectMultimap(Player::getNational, Player::getClub)
                .subscribe(clubsAgrupadosPorNacionalidad -> {
                    clubsAgrupadosPorNacionalidad.forEach((nacionalidad, club) -> {
                        System.out.println("Nacionalidad: " + nacionalidad);
                        System.out.println("Club: " + club + "\n");

                    });
                });
    }

    @Test
    void clubConElMejorJugador(){
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);
        AtomicInteger highestWinners = new AtomicInteger(Integer.MIN_VALUE);

        observable.filter(player -> {
            int bestPlayer = highestWinners.get();
            if(player.getWinners() > highestWinners.get())
            {
                highestWinners.set(player.winners);
                return true;
            }
            return false;
        })
            .subscribe(player -> System.out.println("El club con el mejor jugador es: " + player.getClub()));
    }

    @Test
    void clubConElMejorJugador2() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable.reduce((player1, player2) -> {
            if(player1.getWinners() > player2.getWinners())
            {
                return player1;
            }
            return player2;
        })
                .subscribe(player -> System.out.println("El club con el mejor jugador es: " + player.getClub()));
    }

    @Test
    void ElMejorJugador() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);
        AtomicInteger highestWinners = new AtomicInteger(Integer.MIN_VALUE);

        observable.filter(player ->
        {
            int bestPlayer = highestWinners.get();
            if(player.getWinners() > bestPlayer)
            {
                highestWinners.set(player.winners);
                return true;
            }
            if(player.getWinners() == highestWinners.get())
            {
                return true;
            }
            return false;
        })
        .subscribe(player -> {
            System.out.println(player.getName());
        });
    }

    @Test
    void ElMejorJugador2() {
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable.reduce((player1, player2) -> {
                    if(player1.getWinners() > player2.getWinners())
                    {
                        return player1;
                    }
                    return player2;
                })
                .subscribe(player -> System.out.println("El mejor jugador es " + player.getName()));
    }

    @Test
    void mejorJugadorSegunNacionalidad() {

        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable.collect(Collectors.groupingBy(Player::getNational, Collectors.maxBy(Comparator.comparing(Player::getWinners))))
                .subscribe(player -> {
                    player.forEach((nationality, bestPlayer) -> {
                        System.out.println("Nacionalidad: " + nationality);
                        System.out.println("Mejor jugador: " + (bestPlayer.isPresent() ? bestPlayer.get().getName() : "") + "\n");
                    });
                });
    }

}
