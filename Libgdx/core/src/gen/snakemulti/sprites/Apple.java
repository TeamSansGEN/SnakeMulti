package gen.snakemulti.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import gen.snakemulti.SnakeMulti;
import java.util.concurrent.ThreadLocalRandom;

public class Apple extends Consumable {

    //Random
    //https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java

    public Apple(){
    }

    public Apple(float x, float y, Texture texture) {
        super(x, y, texture);
    }

    public int randomX(){

        return ThreadLocalRandom.current().nextInt(Math.abs(SnakeMulti.WIDTH+1-20));
    }

    public int randomY(){

        return ThreadLocalRandom.current().nextInt(Math.abs(SnakeMulti.HEIGHT+1-20));
    }
}
