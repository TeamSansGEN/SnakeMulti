package gen.snakemulti.sprites;

import com.badlogic.gdx.graphics.Texture;
import server.GameConstants;

import java.util.concurrent.ThreadLocalRandom;

public class Apple extends Consumable {

    //Random
    //https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java

    public Apple(){

    }

    public Apple(String textureName) {
        setNewPosition();
    }

    public Apple(float x, float y, String textureName) {
        super(x, y, textureName);
    }

    public void setNewPosition() {
        setX(randomX());
        setY(randomY());
    }

    private int randomX(){
        return ThreadLocalRandom.current().nextInt(Math.abs(GameConstants.WIDTH+1-20));
    }

    private int randomY(){
        return ThreadLocalRandom.current().nextInt(Math.abs(GameConstants.HEIGHT+1-20));
    }
}
