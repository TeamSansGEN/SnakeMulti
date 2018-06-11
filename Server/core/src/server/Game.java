package server;

import com.badlogic.gdx.math.Vector2;
import gen.snakemulti.sprites.*;

import java.util.*;

public class Game {

    private Map<String, Snake> players;
    private List<Apple> apples;
    private List<Bonus> bonuses;
    private List<Penalty> penalties;
    private List<Vector2> poops;
    private Map<String, Integer> playerScore;
    private Map<String, Integer> playerSpeed;

    private static int numberOfDead;
    private static int round = 1;

    public Game(Map<String, Snake> players) {
        if (players == null) {
            throw new IllegalArgumentException("null argument");
        }

        this.players = new HashMap<>(players);
        apples = new ArrayList<>();
        bonuses = new ArrayList<>();
        penalties = new ArrayList<>();
        poops = new ArrayList<>();
        playerSpeed = new HashMap<>();
        playerScore = new HashMap<>();
        numberOfDead = 0;

        for (String player : players.keySet()) {
            playerSpeed.put(player, players.get(player).getSpeed());
            playerScore.put(player, 0);
        }
    }

    public Game(Map<String, Snake> players, List<Apple> apples, List<Bonus> bonuses, List<Penalty> penalties, List<Vector2> poops) {
        if (players == null || apples == null || bonuses == null || penalties == null) {
            throw new IllegalArgumentException("null arguments.");
        }

        this.players = new HashMap<>(players);
        this.apples = new ArrayList<>(apples);
        this.bonuses = new ArrayList<>(bonuses);
        this.penalties = new ArrayList<>(penalties);
        this.poops = new ArrayList<>(poops);

        playerSpeed = new HashMap<>();
        playerScore = new HashMap<>();

        numberOfDead = 0;

        for (String player : players.keySet()) {
            playerSpeed.put(player, players.get(player).getSpeed());
            playerScore.put(player, 0);
        }
    }

    public Game(Map<String, Snake> players, List<Apple> apples, List<Bonus> bonuses, List<Penalty> penalties, List<Vector2> poops, Map<String, Integer> playerScore) {
        this(players, apples, bonuses, penalties, poops);
        this.playerScore = new HashMap<>(playerScore);
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

    public static int getNumberOfDead() {
        return numberOfDead;
    }

    public Map<String, Integer> getPlayerScore() {
        return playerScore;
    }

    public List<Vector2> getPoops() {
        return poops;
    }

    public void setPlayers(Map<String, Snake> players) {
        this.players = players;
    }

    public String checkCollides(Map<String, Snake> players, List<Vector2> walls) {
        for (String k1 : players.keySet()) {
            // no need to check if the snake is dead
            if (!players.get(k1).isAlive()) continue;

            if (collidesWalls(players.get(k1), walls)) {
                return k1;
            }

            if (collidesPoops(players.get(k1))) {
                return k1;
            }

            generatePoop(players.get(k1));

            for (String k2 : players.keySet()) {
                // TODO: self collision on client or server ????
                if (k1.equals(k2)) continue;

                if (collides(players.get(k1), players.get(k2))) {
                    return k1;
                }
            }
        }
        return "";
    }

    private boolean collidesPoops(Snake s) {
        for (Vector2 poop : poops) {
            if (s.getHeadPosition().x == poop.x && s.getHeadPosition().y == poop.y) {
                return true;
            }
        }
        return false;
    }

    private static boolean collides(Snake s1, Snake s2) {
        for (int i = 0; i < s2.getBodyParts().size(); i++) {
            if (s1.getHeadPosition().x == s2.getBodyParts().get(i).x && s1.getHeadPosition().y == s2.getBodyParts().get(i).y) {
                return true;
            }
        }

        return false;
    }

    private static boolean collidesWalls(Snake s, List<Vector2> walls) {
        for (Vector2 wall : walls) {
            if (s.getHeadPosition().x - wall.x <= GameConstants.BRICK_SIZE &&
                    s.getHeadPosition().y - wall.y <= GameConstants.BRICK_SIZE &&
                    s.getHeadPosition().x - wall.x >= 0 && s.getHeadPosition().y - wall.y >= 0) {

                return true;
            }
        }
        return false;
    }

    public String eatApple(List<Vector2> walls) {
        for (Snake snake : players.values()) {
            // no need to check if the snake is dead
            if (!snake.isAlive()) continue;

            for (Apple apple : apples) {
                if (snake.getHeadPosition().x - apple.getX() <= 16 && snake.getHeadPosition().y - apple.getY() <= 16 &&
                        snake.getHeadPosition().x - apple.getX() >= 0 && snake.getHeadPosition().y - apple.getY() >= 0) {

                    apple.setNewPosition();

                    while (!Game.consumableValidPosition(apple, walls, new ArrayList<Snake>(players.values()))) {
                        apple.setNewPosition();
                    }

                    return snake.getName();
                }
            }
        }

        return "";
    }

    public String eatBonus(List<Vector2> walls) {
        for (Snake snake : players.values()) {
            // no need to check if the snake is dead
            if (!snake.isAlive()) continue;

            for (Bonus bonus : bonuses) {
                if (snake.getHeadPosition().x - bonus.getX() <= 16 && snake.getHeadPosition().y - bonus.getY() <= 16 &&
                        snake.getHeadPosition().x - bonus.getX() >= 0 && snake.getHeadPosition().y - bonus.getY() >= 0) {

                    bonus.setNewPosition();

                    while (!Game.consumableValidPosition(bonus, walls, new ArrayList<Snake>(players.values()))) {
                        bonus.setNewPosition();
                    }

                    return snake.getName();
                }
            }
        }

        return "";
    }

    public String eatPenalty(List<Vector2> walls) {
        for (Snake snake : players.values()) {
            // no need to check if the snake is dead
            if (!snake.isAlive()) continue;

            for(Penalty penalty : penalties) {
                if(snake.getHeadPosition().x - penalty.getX() <= 32 && snake.getHeadPosition().y - penalty.getY() <= 32 &&
                        snake.getHeadPosition().x - penalty.getX() >= 0 && snake.getHeadPosition().y - penalty.getY() >= 0) {
                    penalty.setNewPosition();

                    while (!Game.consumableValidPosition(penalty, walls, new ArrayList<Snake>(players.values()))) {
                        penalty.setNewPosition();
                    }

                    return snake.getName();
                }
            }
        }

        return "";
    }

    private boolean roundFinished() {
        if (numberOfDead >= players.size() - 1) {
            for (Snake s : players.values()) {
                if (s.isAlive()) {
                    // give score to winner
                    playerScore.put(s.getName(), playerScore.get(s.getName()) + GameConstants.SCORES[numberOfDead]);
                }
            }

            System.out.println("\nEnd of round " + round);
            for (String player : playerScore.keySet()) {
                System.out.println(player + ": " + playerScore.get(player) + " point(s).");
            }

            round++;
            return true;
        }
        return false;
    }

    private void generatePoop(Snake s) {
        double randDouble = Math.random(); // [0, 1[

        if (randDouble < 0.0025) { //&& s.getName().equals("Jee")) {
            poops.add(new Vector2(s.getTailPosition().x, s.getTailPosition().y));
        }
    }

    public Game update(final List<Vector2> walls) {

        // check for collisions
        String deadPlayer = checkCollides(players, walls);
        if (!deadPlayer.equals("")) {
            // give score to dead player
            playerScore.put(deadPlayer, playerScore.get(deadPlayer) + GameConstants.SCORES[numberOfDead]);

            // kill the player
            players.get(deadPlayer).kill();
            numberOfDead++;
        }

        // check for eaten apples
        final String playerEatsApple = eatApple(walls);
        if (!playerEatsApple.equals("")) {
            // add a body part to the snake
            for (int i = 0; i < GameConstants.GROWTH; i++) {
                players.get(playerEatsApple).addBodyPart();
            }
        }

        // check for eaten bonuses
        final String playerEatsBonus = eatBonus(walls);
        if (!playerEatsBonus.equals("")) {
            // add effect of the bonus
            playerSpeed.put(playerEatsBonus, playerSpeed.get(playerEatsBonus) + 1);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    playerSpeed.put(playerEatsBonus, playerSpeed.get(playerEatsBonus) - 1);
                }
            }, 3000);
        }

        // check for eaten penalties
        final String playerEatsPenalty = eatPenalty(walls);
        if(!playerEatsPenalty.equals("")) {
            // add effect of the penalty
            playerSpeed.put(playerEatsPenalty, 0);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    playerSpeed.put(playerEatsPenalty, 1);
                }
            }, 3000);
        }


        // end of the round => beginning a new round
        if(roundFinished()) {
            return this;
        }

        return this;
    }

    public static boolean consumableValidPosition(Consumable cons, List<Vector2> walls, List<Snake> snakes) {

        for (Vector2 wall : walls) {
            //if(cons.getX() == wall.x && cons.getY() == wall.y) {
            if (cons.getX() - wall.x <= 16 && cons.getX() - wall.x >= 0 &&
                    cons.getY() - wall.y <= 16 && cons.getY() - wall.y >= 0) {
                return false;
            }
        }

        for (Snake snake : snakes) {
            for (int i = 0; i < snake.getSize(); i++) {
                if (cons.getX() == snake.getBodyParts().get(i).x &&
                        cons.getY() == snake.getBodyParts().get(i).y) {
                    return false;
                }
            }
        }

        return true;
    }
}
