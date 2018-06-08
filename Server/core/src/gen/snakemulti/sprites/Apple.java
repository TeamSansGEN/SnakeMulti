package gen.snakemulti.sprites;

public class Apple extends Consumable {

    //Random
    //https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java

    public Apple(){

    }

    public Apple(String textureName) {
        super(textureName);
    }

    public Apple(float x, float y, String textureName) {
        super(x, y, textureName);
    }

}
