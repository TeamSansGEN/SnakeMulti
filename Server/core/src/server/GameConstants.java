package server;

public abstract class GameConstants {

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
     * Default size of apples and bonuses
     */
    public static final int NORMAL_CONSUMABLE_SIZE = 16;

    /**
     * Default size of the 'freeze' penalty
     */
    public static final int FREEZE_PENALTY_SIZE = 32;

    /**
     * Texture name of the speed bonus
     */
    public static final String SPEED_BONUS_TEXTURE_NAME = "bonusSpeed.png";

    public static final String FREEZE_SNAKES_BONUS_TEXTURE_NAME = "freezeSnakes.png";

    /**
     * Texture name of the 'freeze' penalty
     */
    public static final String FREEZE_PENALTY_TEXTURE_NAME = "freezePenalty.png";

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
    public static final int MAX_PENALTIES = 5;

    /**
     * First  dead: 0 point
     * Second dead: 1 point
     * Third  dead: 3 points
     * Last  alive: 5 points
     */
    public static int[] SCORES = {0, 1, 3, 5};

    /**
     * Texture name of snakes
     */
    public static String[] SNAKE_TEXTURE = {"snake1.png", "snake2.png", "snake3.png", "snake4.png"};
}
