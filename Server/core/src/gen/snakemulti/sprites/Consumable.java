package gen.snakemulti.sprites;

import com.badlogic.gdx.graphics.Texture;
import server.GameConstants;

import java.util.concurrent.ThreadLocalRandom;


public class Consumable {

    private float x;
    private float y;

    //private Texture texture;
    private String textureName;

    public Consumable() {

    }

    public Consumable(String textureName) {
        if(textureName == null) {
            throw new IllegalArgumentException("null texture.");
        }
        this.textureName = textureName;
        setNewPosition();
    }

    public Consumable(float x, float y, String textureName) {
        if(x < 0 || x > GameConstants.WIDTH) {
            throw new IllegalArgumentException("invalid coordinate (x axis).");
        }
        if(y < 0 || y > GameConstants.HEIGHT) {
            throw new IllegalArgumentException("invalid coordinate (y axis).");
        }
        if(textureName == null) {
            throw new IllegalArgumentException("null texture.");
        }

        this.x = x;
        this.y = y;
        this.textureName = textureName;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        if(x >= 0 && x <= GameConstants.WIDTH) {
            this.x = x;
        }
    }

    public void setY(float y) {
        if(y >= 0 && y <= GameConstants.HEIGHT) {
            this.y = y;
        }
    }

    public String getTexture() {
        return textureName;
    }

    public void setTexture(String textureName) {
        this.textureName = textureName;
    }

    public void setNewPosition() {
        x = randomX();
        y = randomY();
    }

    private int randomX(){
        return ThreadLocalRandom.current().nextInt(Math.abs(GameConstants.WIDTH+1-20));
    }

    private int randomY(){
        return ThreadLocalRandom.current().nextInt(Math.abs(GameConstants.HEIGHT+1-20));
    }
}