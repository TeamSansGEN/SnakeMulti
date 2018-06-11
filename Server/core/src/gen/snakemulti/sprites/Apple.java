package gen.snakemulti.sprites;

import server.GameConstants;

public class Apple extends Consumable {

    //Random
    //https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java

    public Apple(){

    }

    public Apple(String textureName, int size) {
        super(textureName, size);
    }

    public Apple(float x, float y, String textureName, int size) {
        super(x, y, textureName, size);
    }

}
