package server;

import gen.snakemulti.sprites.Apple;
import gen.snakemulti.sprites.Bonus;
import gen.snakemulti.sprites.Penalty;
import gen.snakemulti.sprites.Snake;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void checkCollidesTest() {

        WallsSchemasGenerator wall = new WallsSchemasGenerator();
        Snake s1 = new Snake(97, 512, Snake.RIGHT, "Jee", "127.0.0.1");
        Snake s2 = new Snake(800, 600, Snake.DOWN, "Lio", "127.0.0.1");
        Map<String, Snake> players = new HashMap<>();
        players.put("Jee", s1);
        players.put("Lio", s2);
        Game game1 = new Game(players);
        List<Vector2> walls = wall.getWallsSchema();

        //check if there is a collison with a wall
        String s = game1.checkCollides(players, walls);
        assertEquals("Jee", s);
        s = game1.checkCollides(players, walls);
        assertNotEquals("Lio", s);


        Snake s3 = new Snake(800, 600, Snake.LEFT, "Gui", "127.0.0.1");
        Snake s4 = new Snake(820, 600, Snake.UP, "Neto", "127.0.0.1");
        players.remove("Jee");
        players.remove("Lio");
        players.put("Gui" , s3);
        players.put("Neto", s4);
        Game game2 = new Game(players);

        //Check if there is a collison with another snake
        s = game2.checkCollides(players, walls);
        assertEquals("Neto", s);
    }

    @Test
    public void checkCollidesWithPoopTest() {

        WallsSchemasGenerator wall = new WallsSchemasGenerator();
        Snake s2 = new Snake(800, 600, Snake.DOWN, "Lio", "127.0.0.1");
        Map<String, Snake> players = new HashMap<>();
        players.put("Lio", s2);

        List<Vector2> poops = new ArrayList<>();
        poops.add(new Vector2(800,600));
        List<Apple> apples = new ArrayList<>();
        List<Bonus> bonus = new ArrayList<>();
        List<Penalty> penalties = new ArrayList<>();

        Game game1 = new Game(players, apples, bonus, penalties, poops);

        List<Vector2> walls = wall.getWallsSchema();

        //Check if there is a collison with a poop
        String s = game1.checkCollides(players, walls);
        assertEquals("Lio", s);
    }

    @Test
    public void eatApple() {

        WallsSchemasGenerator wall = new WallsSchemasGenerator();
        Snake s1 = new Snake(800, 600, Snake.DOWN, "Lio", "127.0.0.1");
        Map<String, Snake> players = new HashMap<>();
        players.put("Lio", s1);

        int oldSize = s1.getSize();

        List<Vector2> poops = new ArrayList<>();
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple(800, 600, "Texture", 20));
        List<Bonus> bonus = new ArrayList<>();
        List<Penalty> penalties = new ArrayList<>();

        Game game1 = new Game(players, apples, bonus, penalties, poops);

        List<Vector2> walls = wall.getWallsSchema();

        game1 = game1.update(walls);

        //Check if the snake eat an apple and if the body grows
        assertTrue(oldSize < game1.getPlayers().get("Lio").getSize());
    }

    @Test
    public void eatPenalty() {

        WallsSchemasGenerator wall = new WallsSchemasGenerator();
        Snake s1 = new Snake(800, 600, Snake.LEFT, "Lio", "127.0.0.1");
        Map<String, Snake> players = new HashMap<>();
        players.put("Lio", s1);

        List<Vector2> poops = new ArrayList<>();
        List<Apple> apples = new ArrayList<>();
        List<Bonus> bonus = new ArrayList<>();
        List<Penalty> penalties = new ArrayList<>();
        penalties.add(new Penalty(800, 600, "Texture", 20));

        Game game1 = new Game(players, apples, bonus, penalties, poops);

        int oldSpeed = game1.getPlayerSpeed().get("Lio");

        List<Vector2> walls = wall.getWallsSchema();

        game1 = game1.update(walls);

        //It should be equal to zero if there is a penalty
        assertTrue(oldSpeed > game1.getPlayerSpeed().get("Lio"));
    }


    @Test
    public void consumableValidPosition() {

        WallsSchemasGenerator wall = new WallsSchemasGenerator();
        Snake s1 = new Snake(800, 600, Snake.LEFT, "Lio", "127.0.0.1");
        List<Snake> snakes = new ArrayList<>();
        snakes.add(s1);
        Map<String, Snake> players = new HashMap<>();
        players.put("Lio", s1);

        List<Vector2> poops = new ArrayList<>();
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple(780, 600, "Texture", 20));
        apples.add(new Apple(820, 600, "Texture", 20));
        apples.add(new Apple(97 , 512, "Texture", 20));
        List<Bonus> bonus = new ArrayList<>();
        List<Penalty> penalties = new ArrayList<>();

        Game game1 = new Game(players, apples, bonus, penalties, poops);

        List<Vector2> walls = wall.getWallsSchema();

        //ok
        assertTrue(game1.consumableValidPosition(apples.get(0), walls, snakes));
        //snakes body
        assertFalse(game1.consumableValidPosition(apples.get(1), walls, snakes));
        //walls
        assertFalse(game1.consumableValidPosition(apples.get(2), walls, snakes));
    }
}