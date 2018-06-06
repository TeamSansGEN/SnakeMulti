package server;

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

    public Game(Map<String, Snake> players) {
        if(players == null) {
            throw new IllegalArgumentException("null argument");
        }

        this.players = new HashMap<>(players);
        apples       = new ArrayList<>();
        bonuses      = new ArrayList<>();
        penalties    = new ArrayList<>();
    }

    public Game(Map<String, Snake> players, List<Apple> apples, List<Bonus> bonuses, List<Penalty> penalties) {
        if(players == null || apples == null || bonuses == null || penalties == null) {
            throw new IllegalArgumentException("null arguments.");
        }

        this.players   = new HashMap<>(players);
        this.apples    = new ArrayList<>(apples);
        this.bonuses   = new ArrayList<>(bonuses);
        this.penalties = new ArrayList<>(penalties);
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

    public void setPlayers(Map<String, Snake> players) {
        this.players = players;
    }

    public String checkCollides(Map<String, Snake> players) {
        for(String k1 : players.keySet()) {
            for(String k2 : players.keySet()) {
                if(k1.equals(k2)) continue;

                //if(players.get(k1).collides(players.get(k2))) {
                if(collides(players.get(k1), players.get(k2))) {
                    return k1;
                }
            }
        }
        return "";
    }

    private static boolean collides(Snake s1, Snake s2) {
        for(int i = 0; i < s2.getBodyParts().size(); i++) {
            if(s1.getHeadPosition().x == s2.getBodyParts().get(i).x && s1.getHeadPosition().y == s2.getBodyParts().get(i).y) {
                return true;
            }
        }

        return false;
    }

}
