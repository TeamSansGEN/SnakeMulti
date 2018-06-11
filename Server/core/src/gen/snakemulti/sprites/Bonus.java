package gen.snakemulti.sprites;

import server.GameConstants;

public class Bonus extends Consumable {

    public Bonus(String textureName, int size) {
        super(textureName, size);
    }

    public Bonus(float x, float y, String textureName, int size) {
        super(x, y, textureName, size);
    }
}
