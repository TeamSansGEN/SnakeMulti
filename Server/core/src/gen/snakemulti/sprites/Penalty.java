package gen.snakemulti.sprites;

import com.badlogic.gdx.graphics.Texture;

public class Penalty extends Consumable {

    public Penalty(String textureName, int size) {
        super(textureName, size);
    }

    public Penalty(float x, float y, String textureName, int size) {
        super(x, y, textureName, size);
    }
}
