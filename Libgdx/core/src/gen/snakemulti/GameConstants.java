package gen.snakemulti;

public class GameConstants {

    /**
     * Window width (pixels)
     */
    public static final int WIDTH = 960;

    /**
     * Window Height (pixels)
     */
    public static final int HEIGHT = 640;

    /**
     * Buffer size for UDP client-server communication
     */
    public static final int BUFF_SIZE = 32768;

    /**
     * Size of an brick unit (pixels)
     */
    public static final int BRICK_SIZE = 16;

    /**
     * Texture name of the apple
     */
    public static final String APPLE_TEXTURE_NAME = "apple16.png";

    /**
     * Texture name of the speed bonus
     */
    public static final String SPEED_BONUS_TEXTURE_NAME = "bonusSpeed.png";


    public static final String PENALTY_TEXTURE_NAME = "";

    /**
     * Texture name of the bricks
     */
    public static final String BRICKS_TEXTURE_NAME = "bricks16.png";

    /**
     * Growth of the snake whenever it eats an apple
     */
    public static final int GROWTH = 4;

    /**
     * Default speed of snakes
     */
    public static final int DEFAULT_SPEED = 1;

    /**
     * Number of apples
     */
    public static final int MAX_APPLES = 1;

    /**
     * Number of bonuses
     */
    public static final int MAX_BONUSES = 1;

    /**
     * Number of penalties
     */
    public static final int MAX_PENALTIES = 1;
}
