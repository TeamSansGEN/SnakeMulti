package server;

import gen.snakemulti.sprites.Snake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    Map<String, Snake> players;

    public Game(Map<String, Snake> players) {
        this.players = new HashMap<String, Snake>(players);
    }

    public String checkCollides(Map<String, Snake> players) {
        for(String k1 : players.keySet()) {
            for(String k2 : players.keySet()) {
                if(k1.equals(k2)) continue;
                System.out.println("Check collisions: "+k1+"-"+k2);

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
