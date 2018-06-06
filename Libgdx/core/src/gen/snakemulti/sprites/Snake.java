package gen.snakemulti.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import gen.snakemulti.SnakeMulti;

import javax.xml.crypto.Data;
import java.awt.geom.RectangularShape;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Snake implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    //TODO classe avec toutes les constantes

    public static final transient String UP    =    "up";
    public static final transient String LEFT  =  "left";
    public static final transient String RIGHT = "right";
    public static final transient String DOWN  =  "down";

    public static final String[] DIRECTIONS = {RIGHT, DOWN, LEFT, UP};

    public static final int BODY_SIZE = 4;

    private static final transient int NUMBER_BODYPART_INIT = 50;

    // bodyParts of the snake. Each couple (x,y) represents a bodyPart.
    // At the start of the game, 'bodyParts' is filled with the default number of bodyParts
    private List<Vector2> bodyParts;

    private Vector2  headPosition;
    private Vector2 tailPosition;

    private float speed;
    private String direction;
    private boolean alive;
    private int size;

    private String name;
    private String ipAdress;


    public Snake(float x, float y, String directionInit, String name, String ipAdress) {
        headPosition = new Vector2(x, y);
        tailPosition = new Vector2(headPosition.x, headPosition.y);
        speed = 300f;
        alive = true;
        direction = directionInit;
        size = NUMBER_BODYPART_INIT;
        bodyParts = new ArrayList<Vector2>();
        bodyParts.add(headPosition);

        this.name = name;
        this.ipAdress = ipAdress;

        for(int i = 1; i < size; i++) {
            addBodyPart();
        }
    }

    public String getName(){

        return name;
    }

    public void kill() {
        alive = false;
    }

    public void addNewHead() {
        float newX = 0;
        float newY = 0;

        if(direction.equals(UP)) {
            newX = headPosition.x;
            newY = headPosition.y + BODY_SIZE;
        }
        else if(direction.equals(LEFT)) {
            newX = headPosition.x - BODY_SIZE;
            newY = headPosition.y;
        }
        else if(direction.equals(DOWN)) {
            newX = headPosition.x;
            newY = headPosition.y - BODY_SIZE;
        }
        else if(direction.equals(RIGHT)) {
            newX = headPosition.x + BODY_SIZE;
            newY = headPosition.y;
        }


        if(newY >= SnakeMulti.HEIGHT) {
            newY = 0;
        }
        if(newX >= SnakeMulti.WIDTH) {
            newX = 0;
        }
        if(newX < 0) {
            newX = SnakeMulti.WIDTH;
        }
        if(newY < 0) {
            newY = SnakeMulti.HEIGHT;
        }

        Vector2 newBodyPart = new Vector2(newX, newY);
        bodyParts.add(0, newBodyPart);
        bodyParts.remove(bodyParts.size() - 1);

        headPosition = newBodyPart;
        tailPosition = bodyParts.get(bodyParts.size() - 1);
    }

    public void addTail(){

        for (int i = 0; i < 10 ; i++) {

            addBodyPart();
        }
        size += 10;
    }

    public void addBodyPart() {

        float newX = 0;
        float newY = 0;

        if(direction.equals(UP)) {
            newX = tailPosition.x;
            newY = tailPosition.y - BODY_SIZE;
        }
        else if(direction.equals(LEFT)) {
            newX = tailPosition.x + BODY_SIZE;
            newY = tailPosition.y;
        }
        else if(direction.equals(DOWN)) {
            newX = tailPosition.x;
            newY = tailPosition.y + BODY_SIZE;
        }
        else if(direction.equals(RIGHT)) {
            newX = tailPosition.x - BODY_SIZE;
            newY = tailPosition.y;
        }

        Vector2 newBodyPart = new Vector2(newX, newY);
        bodyParts.add(newBodyPart);
        tailPosition = new Vector2(newBodyPart.x, newBodyPart.y);
    }

    public void moveSnake() {
        /*for(int i = bodyParts.size()-1; i > 0; i--) {
            bodyParts.set(i, new Vector2(bodyParts.get(i-1).x, bodyParts.get(i-1).y));
        }*/
        if(isAlive()) {
            addNewHead();
        }
    }

    public boolean collides() {
        for(int i = 1; i < bodyParts.size(); i++) {
            //System.out.println("Collision: ");
            //System.out.println("head("+headPosition.x+","+headPosition.y+")  body("+bodyParts.get(i).x+","+bodyParts.get(i).y+")");
            if (headPosition.x == bodyParts.get(i).x && headPosition.y == bodyParts.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public boolean collides(Snake otherSnake) {
        for(int i = 0; i < otherSnake.getBodyParts().size(); i++) {
            //System.out.println("head("+headPosition.x+","+headPosition.y+")  body("+otherSnake.getBodyParts().get(i).x+","+otherSnake.getBodyParts().get(i).y+")");
            if(headPosition.x == otherSnake.getBodyParts().get(i).x && headPosition.y == otherSnake.getBodyParts().get(i).y) {
                return true;
            }
        }
        return false;
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


    public void setHeadPosition(Vector2 pos) {
        headPosition.x = pos.x;
        headPosition.y = pos.y;
    }

    public void update(float dt) {
        if(alive) {

            if (headPosition.x >= SnakeMulti.WIDTH) {
                headPosition.x = 0;
            }

            if (headPosition.x < 0) {
                headPosition.x = SnakeMulti.WIDTH;
            }

            if (headPosition.y >= SnakeMulti.HEIGHT) {
                headPosition.y = 0;
            }

            if (headPosition.y < 0) {
                headPosition.y = SnakeMulti.HEIGHT;
            }
            alive = !collides();
        }
        else {

        }
    }

    public boolean isAlive(){
        return alive;
    }

    public List<Vector2> getBodyParts() {
        return new ArrayList<Vector2>(bodyParts);
    }

    public Vector2 getHeadPosition() {
        return headPosition;
    }

    /*public List<Texture> getTexture() {
        return new ArrayList<Texture>(snake);
    }*/

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

    public String getIpAdress(){

        return ipAdress;
    }

    public String toString(){

        StringBuilder s = new StringBuilder();
        s.append("IP :" + ipAdress + " ");
        s.append("Name :" + name + " ");
        s.append("Body : " + getBodyParts().get(0));
        for (int i = 0; i< getBodyParts().size(); ++i) {
            s.append(" ," + getBodyParts().get(i));
        }

        return s.toString();
    }
}

