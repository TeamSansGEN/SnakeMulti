package gen.snakemulti.sprites;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Wall {

    List<Vector2> bricks;

    public Wall(Vector2 brick) {
        bricks = new ArrayList<>();
        bricks.add(brick);
    }

    public Wall(List<Vector2> bricks) {
        this.bricks = new ArrayList<>(bricks);
    }
}
