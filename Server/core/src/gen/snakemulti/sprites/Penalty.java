package gen.snakemulti.sprites;

import com.badlogic.gdx.graphics.Texture;

public class Penalty extends Consumable {

    public Penalty(String textureName) {
        super(textureName);
    }

    public Penalty(float x, float y, String textureName) {
        super(x, y, textureName);
    }
}
