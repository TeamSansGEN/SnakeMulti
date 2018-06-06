package gen.snakemulti.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

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

    private static final transient int NUMBER_BODYPART_INIT = 10;

    // bodyParts of the snake. Each couple (x,y) represents a bodyPart.
    // At the start of the game, 'bodyParts' is filled with the default number of bodyParts
    private List<Vector2> bodyParts;

    private Vector2  headPosition;
    private Vector2 tailPosition;

    private float speed;
    private String direction;
    private boolean alive;

    private String name;
    private String ipAdress;

    //private transient List<Texture> snake;

    public Snake(float x, float y, String directionInit, String name, String ipAdress) {
        headPosition = new Vector2(x, y);
        tailPosition = new Vector2(headPosition.x, headPosition.y);
        speed = 300f;
        alive = true;
        direction = directionInit;
        bodyParts = new ArrayList<Vector2>();
        bodyParts.add(headPosition);

        this.name = name;
        this.ipAdress = ipAdress;

        for(int i = 1; i < NUMBER_BODYPART_INIT; i++) {
            addBodyPart();
        }
    }

    public String getName(){
        return name;
    }

    public void kill() {
        alive = false;
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
        for(int i = bodyParts.size()-1; i > 0; i--) {
            bodyParts.set(i, new Vector2(bodyParts.get(i-1).x, bodyParts.get(i-1).y));
        }

    }

    public boolean isAlive() {
        return alive;
    }

    public boolean collides() {
        for(int i = 1; i < bodyParts.size(); i++) {
            if (headPosition.x == bodyParts.get(i).x && headPosition.y == bodyParts.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public boolean collides(Snake s1, Snake s2) {
        // TODO: check head
        for(int i = 0; i < s2.getBodyParts().size(); i++) {
            //System.out.println("head("+s1.getHeadPosition().x+","+s1.getHeadPosition().y+")  otherBody("+s2.getBodyParts().get(i).x+","+s2.getBodyParts().get(i).y+")");
            if(s1.getHeadPosition().x == s2.getBodyParts().get(i).x && s1.getHeadPosition().y == s2.getBodyParts().get(i).y) {
                System.out.println("\n\n\nCOLLISION!!!\n\n\n");
                return true;
            }
        }

        return false;
    }

    public boolean collides(Snake otherSnake) {
        for(int i = 0; i < otherSnake.getBodyParts().size(); i++) {
            System.out.println("head("+headPosition.x+","+headPosition.y+")  otherBody("+otherSnake.getBodyParts().get(i).x+","+otherSnake.getBodyParts().get(i).y+")");
            if(headPosition.x == otherSnake.getBodyParts().get(i).x && headPosition.y == otherSnake.getBodyParts().get(i).y) {
                System.out.println("\n\n\nCOLLISION!!!\n\n\n");
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return bodyParts.size();
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

    /*
    public void update(float dt) {
        if(alive) {
            moveSnake();
            moveSnakeHead(dt);

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
    */

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

