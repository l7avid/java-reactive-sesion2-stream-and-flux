package com.example.demo;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

        observable.sort(Comparator.comparing(Player::getNational))
                .distinct()
                .subscribe(System.out::println);
    }

    @Test
    void clubConElMejorJugador(){
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);
        AtomicInteger highestWinners = new AtomicInteger(Integer.MIN_VALUE);

        observable.sort(Comparator.comparing(Player::getClub))
                .distinct()
                .filter(player ->
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
                .subscribe(System.out::println)
        ;

    }

    @Test
    void clubConElMejorJugador2() {
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
        .subscribe(System.out::println);
    }

    @Test
    void ElMejorJugador2() {

    }

    @Test
    void mejorJugadorSegunNacionalidad() {

        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);
        AtomicInteger highestWinners = new AtomicInteger(Integer.MIN_VALUE);
        AtomicReference<String> bestPlayerNationality = new AtomicReference<>("");


    }

}
