package server;

import com.badlogic.gdx.math.Vector2;
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

    public String checkCollides(Map<String, Snake> players, List<Vector2> walls) {
        for(String k1 : players.keySet()) {
            // no need to check if the snake is dead
            if(!players.get(k1).isAlive()) continue;

            if(collidesWalls(players.get(k1), walls)) {
                return k1;
            }

            for(String k2 : players.keySet()) {
                // TODO: self collision on client or server ????
                if(k1.equals(k2)) continue;

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

    private static boolean collidesWalls(Snake s, List<Vector2> walls) {
        for(Vector2 wall : walls) {
            if(s.getHeadPosition().x - wall.x <= GameConstants.BRICK_SIZE &&
               s.getHeadPosition().y - wall.y <= GameConstants.BRICK_SIZE &&
               s.getHeadPosition().x - wall.x >= 0 && s.getHeadPosition().y - wall.y >= 0) {

                return true;
            }
        }
        return false;
    }

    public String eatApple() {
        for(Snake snake : players.values()) {
            // no need to check if the snake is dead
            if(!snake.isAlive()) continue;

            for(Apple apple : apples) {
                if(snake.getHeadPosition().x - apple.getX() <= 16 && snake.getHeadPosition().y - apple.getY() <= 16 &&
                   snake.getHeadPosition().x - apple.getX() >= 0  && snake.getHeadPosition().y - apple.getY() >= 0){

                    apple.setNewPosition();
                    return snake.getName();
                }
            }
        }

        return "";
    }

    public Game update(List<Vector2> walls) {
        // check for collisions
        String deadPlayer = checkCollides(players, walls);
        if(!deadPlayer.equals("")) {
            // kill the player
            players.get(deadPlayer).kill();
        }

        // check for eaten apples
        String playerEatsApple = eatApple();

        if(!playerEatsApple.equals("")) {
            // add a body part to the snake
            for(int i = 0; i < GameConstants.GROWTH; i++) {
                players.get(playerEatsApple).addBodyPart();
            }
        }

        return this;
    }

}
