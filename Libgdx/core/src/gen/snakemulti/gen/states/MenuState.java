
package gen.snakemulti.gen.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import gen.snakemulti.SnakeMulti;

public class MenuState extends State {

    private Texture background;
    private ImageButton playButton;
    private ImageButton leaderButton;
    private ImageButton exitButton;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        background  = new Texture("backgroundLobby.png");

        Texture playTexture = new Texture("button_play.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(playTexture));
        playButton = new ImageButton(drawable);
        playButton.setPosition(400,300);

        Texture leaderTexture = new Texture("button_leader-board.png");
        drawable = new TextureRegionDrawable(new TextureRegion(leaderTexture));
        leaderButton = new ImageButton(drawable);
        leaderButton.setPosition(350,200);

        Texture quitTexture = new Texture("button_exit.png");
        drawable = new TextureRegionDrawable(new TextureRegion(quitTexture));
        exitButton = new ImageButton(drawable);
        exitButton.setPosition(400,100);

        Stage stage= new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(playButton);
        stage.addActor(leaderButton);
        stage.addActor(exitButton);

        playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                goToLobbyState();
            }
        });

        leaderButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                goToLeaderBoardState();
            }
        });

        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                System.exit(0);
            }
        });

    }



    public void goToLobbyState(){
        gsm.set(new LobbyState(gsm));
    }

    public void goToLeaderBoardState(){
        gsm.set(new LeaderBoardState(gsm));
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput(); //will always be checking for inputs
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin(); //open the box

        //put everything to be rendered inside the box
        sb.draw(background,  0, 0, SnakeMulti.WIDTH, SnakeMulti.HEIGHT);
        playButton.draw(sb,5);
        leaderButton.draw(sb,5);
        exitButton.draw(sb,5);


        sb.end(); // close the box

        //everything inside the box will be rendered
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
