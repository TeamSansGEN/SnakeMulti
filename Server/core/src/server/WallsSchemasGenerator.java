package server;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WallsSchemasGenerator {
    private static List<Vector2> schema;

    private static final int BRICK_SIZE = GameConstants.BRICK_SIZE;

    public WallsSchemasGenerator() {
        this(1);
    }

    public WallsSchemasGenerator(int choice) {
        schema = new ArrayList<>();
        switch (choice) {
            case 1:
                generateSchema1();
                break;

            case 2:
                generateSchema2();
                break;

            case 3:
                generateSchema3();
                break;

            default:
        }
    }

    public List<Vector2> getWallsSchema() {
        return new ArrayList<>(schema);
    }

    private void generateSchema1() {
        for(int x = 96; x < 864; x += BRICK_SIZE) {
            if(x % 192 == 0) {
                x += 96;
            }
            else {
                schema.add(new Vector2(x, 512));
                schema.add(new Vector2(x, 112));
            }
        }

        for(int y = 112; y <= 512 ; y += BRICK_SIZE) {
            if(y % 224 == 0) {
                y += 96;
            }
            else {
                schema.add(new Vector2(96, y));
                schema.add(new Vector2(864, y));
            }
        }

        for(int x = 304; x < 640; x += BRICK_SIZE) {
            schema.add(new Vector2(x, 304));
        }

    }

    private void generateSchema2() {

    }

    private void generateSchema3() {

    }
}
