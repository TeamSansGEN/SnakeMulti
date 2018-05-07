package gen.snakemulti.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import gen.snakemulti.SnakeMulti;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    //TODO classe avec toutes les constantes

    public static final String UP    =    "up";
    public static final String LEFT  =  "left";
    public static final String RIGHT = "right";
    public static final String DOWN  =  "down";

    private static final int NUMBER_BODYPART_INIT = 50;

    // bodyParts of the snake. Each couple (x,y) represents a bodyPart.
    // At the start of the game, 'bodyParts' is filled with the default number of bodyParts
    private List<Vector2> bodyParts;

    private Vector2 headPosition;
    private Vector2 tailPosition;

    private float speed;
    private String direction;

    private int size;

    private List<Texture> snake;

    public Snake(float x, float y, String directionInit) {
        headPosition = new Vector2(x, y);
        tailPosition = new Vector2(headPosition.x, headPosition.y);
        speed = 300f;

        direction = directionInit;
        size = NUMBER_BODYPART_INIT;
        bodyParts = new ArrayList<Vector2>();
        snake = new ArrayList<Texture>();
        snake.add(new Texture("snake.png"));
        bodyParts.add(headPosition);

        for(int i = 1; i < size; i++) {
            addBodyPart();
        }
    }

    public void addBodyPart() {
        snake.add(new Texture("snake.png"));

        float newX = 0;
        float newY = 0;

        if(direction.equals(UP)) {
            newX = tailPosition.x;
            newY = tailPosition.y - snake.get(0).getHeight() ;
        }
        else if(direction.equals(LEFT)) {
            newX = tailPosition.x + snake.get(0).getWidth();
            newY = tailPosition.y;
        }
        else if(direction.equals(DOWN)) {
            newX = tailPosition.x;
            newY = tailPosition.y + snake.get(0).getHeight();
        }
        else if(direction.equals(RIGHT)) {
            newX = tailPosition.x - snake.get(0).getWidth();
            newY = tailPosition.y;
        }

        Vector2 newBodyPart = new Vector2(newX, newY);
        bodyParts.add(newBodyPart);
        tailPosition = new Vector2(newBodyPart.x, newBodyPart.y);
    }

    public void moveSnake() {
        for(int i = bodyParts.size()-1; i > 0; i--) {
            bodyParts.set(i, new Vector2(bodyParts.get(i-1).x, bodyParts.get(i-1).y));
        }

    }

    public int getSize() {
        return size;
    }

    private void moveSnakeHead(float dt) {

        if(direction.equals(UP)) {
            //headPosition.y += Gdx.graphics.getDeltaTime() * speed;
            headPosition.y += 4;
        }
        else if(direction.equals(LEFT)) {
            headPosition.x -= 4;
            //headPosition.x -= Gdx.graphics.getDeltaTime() * speed;
        }
        else if(direction.equals(DOWN)) {
            headPosition.y -= 4;
            //headPosition.y -= Gdx.graphics.getDeltaTime() * speed;
        }
        else if(direction.equals(RIGHT)) {
            headPosition.x += 4;
            //headPosition.x += Gdx.graphics.getDeltaTime() * speed;
        }
    }

    public void update(float dt) {

        moveSnake();
        moveSnakeHead(dt);
        if(headPosition.x >= SnakeMulti.WIDTH) {
            headPosition.x = 0;
        }

        if(headPosition.x < 0) {
            headPosition.x = SnakeMulti.WIDTH;
        }

        if(headPosition.y >= SnakeMulti.HEIGHT) {
            headPosition.y = 0;
        }

        if(headPosition.y < 0) {
            headPosition.y = SnakeMulti.HEIGHT;
        }
    }

    public List<Vector2> getBodyParts() {
        return new ArrayList<Vector2>(bodyParts);
    }

    public Vector2 getHeadPosition() {
        return headPosition;
    }

    public List<Texture> getTexture() {
        return new ArrayList<Texture>(snake);
    }

    public void moveX(float amount) {
        headPosition.x += amount;
    }

    public void moveY(float amount) {
        headPosition.y += amount;
    }

    public float getSpeed() {
        return speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

