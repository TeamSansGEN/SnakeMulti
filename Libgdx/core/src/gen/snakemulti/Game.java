package gen.snakemulti;

import gen.snakemulti.sprites.Apple;
import gen.snakemulti.sprites.Bonus;
import gen.snakemulti.sprites.Penalty;
import gen.snakemulti.sprites.Snake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private Map<String, Snake> players;
    private List<Apple> apples;
    private List<Bonus> bonuses;
    private List<Penalty> penalties;
    private Map<String, Integer> playerSpeed;

    public Game(Map<String, Snake> players) {
        if(players == null) {
            throw new IllegalArgumentException("null argument");
        }

        this.players = new HashMap<>(players);
        apples       = new ArrayList<>();
        bonuses      = new ArrayList<>();
        penalties    = new ArrayList<>();
        playerSpeed  = new HashMap<>();

        for(String player : players.keySet()) {
            playerSpeed.put(player, players.get(player).getSpeed());
        }
    }

    public Game(Map<String, Snake> players, List<Apple> apples, List<Bonus> bonuses, List<Penalty> penalties) {
        if(players == null || apples == null || bonuses == null || penalties == null) {
            throw new IllegalArgumentException("null arguments.");
        }

        this.players   = new HashMap<>(players);
        this.apples    = new ArrayList<>(apples);
        this.bonuses   = new ArrayList<>(bonuses);
        this.penalties = new ArrayList<>(penalties);
        playerSpeed = new HashMap<>();

        for(String player : players.keySet()) {
            playerSpeed.put(player, players.get(player).getSpeed());
        }
    }

    public Map<String, Snake> getPlayers() {
        return players;
    }

    public List<Apple> getApples() {
        return apples;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public List<Penalty> getPenalties() {
        return penalties;
    }

    public Map<String, Integer> getPlayerSpeed() {
        return playerSpeed;
    }
}
