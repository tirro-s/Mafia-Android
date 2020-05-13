package com.mafia.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import static jdk.nashorn.internal.objects.NativeString.trim;

public class MenuState<font> extends State {

    private Texture img = new Texture("mafia_logo.png");

    private Button startBtn;
    //private Button createBtn;
    private Button nameBtn;
    private Button gameBtn;

    private BitmapFont font;

    private Zapros zapros;

    private boolean showAlert = false;
    private long alertTimer;
    private String alertText;

    public MenuState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, Mafia.WIDTH, Mafia.HEIGHT);

        zapros = (new Zapros());

        //font = new BitmapFont(Gdx.files.internal("myfont.fnt"));

        /*font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("font.ttf"), FONT_CHARACTERS, 12.5f, 7.5f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.setColor(1f, 0f, 0f, 1f);*/

        font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font_0.png"), false);
        font.setColor(Color.TEAL);

        //startBtn = new Button("button_start.png", 0, 10, true);
        startBtn = new Button("button_start.png", 0, 10, true);
       // createBtn = new Button("create_btn.png", 0, 100, true);
        nameBtn = new Button("blue_btn.png", 0, 175, true);
        gameBtn = new Button("blue_btn.png", 0, 265, true);

    }

    @Override
    protected void HandleInput() {
        if (Gdx.input.justTouched()){

            Vector3 touchPosition;
            touchPosition = new Vector3(0,0,0);
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPosition);
            if (nameBtn.getRec().contains(touchPosition.x, touchPosition.y)) {
                String name_str = Mafia.name;
                //if (Mafia.name != "name") name_str = Mafia.name;
                Gdx.input.getTextInput(new TextInputListener() {
                    @Override
                    public void input (String text) {
                        Mafia.name = text;
                        //message = "message: " + text + ", touch screen for new dialog";
                    }

                    @Override
                    public void canceled () {
                        //message = "cancled by user";
                    }
                },"Enter nickname", name_str, "");
            }
            if (gameBtn.getRec().contains(touchPosition.x, touchPosition.y)) {
                String game_str = Mafia.game;
                //if (Mafia.game != "game") game_str = Mafia.game;
                Gdx.input.getTextInput(new TextInputListener() {
                    @Override
                    public void input (String text) {
                        Mafia.game = text;
                        //message = "message: " + text + ", touch screen for new dialog";
                    }

                    @Override
                    public void canceled () {
                        //message = "cancled by user";
                    }
                },"Enter game name", game_str, "");
            }
            if (startBtn.getRec().contains(touchPosition.x, touchPosition.y)) {
                if (Mafia.game.equals("") || Mafia.game.equals("game") || !Mafia.game.matches("^[a-zA-Z0-9]+$")) {
                    alertTimer = System.nanoTime();
                    showAlert = true;
                    alertText = "НЕПРАВИЛЬНОЕ НАЗВАНИЕ ИГРЫ";
                } else if (Mafia.name.equals("") || Mafia.name.equals("name") || !Mafia.name.matches("^[a-zA-Z0-9]+$")) {
                    alertTimer = System.nanoTime();
                    showAlert = true;
                    alertText = "НЕПРАВИЛЬНОЕ ИМЯ";
                } else {
                    Mafia.prefs.putString("game", Mafia.game);
                    Mafia.prefs.putString("name", Mafia.name);
                    Mafia.prefs.flush();

                    zapros.send("add_player.php?playername=" + Mafia.name + "&game=" + Mafia.game, "");
                    gsm.set(new PlayState(gsm));
                }
            }
        }
    }

    @Override
    protected void update(float dt) {
        //System.out.println(atest);
        HandleInput();
    }

    @Override
    protected void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(img, (480 - img.getWidth())/2, 800 - img.getHeight());
        sb.draw(startBtn.getTexture(), startBtn.x, startBtn.y);
        //sb.draw(createBtn.getTexture(), createBtn.x, createBtn.y);
        sb.draw(nameBtn.getTexture(), nameBtn.x, nameBtn.y);
        sb.draw(gameBtn.getTexture(), gameBtn.x, gameBtn.y);
        //sb.draw(checkBox, 400, 760);
        font.draw(sb, "Название игры:", 50, 320);
        font.draw(sb, "Ваше имя:", 50, 230);
        font.draw(sb, Mafia.game, 270, 320);
        font.draw(sb, Mafia.name, 270, 230);
        font.draw(sb, "v 0.2", 20, 770);
        //sb.font.draw(sb, "dfjghdfjg", 100, 100);

        if (showAlert) {
            long elapsed = (System.nanoTime() - alertTimer) / 1000000;
            if (elapsed < 800) {
                font.draw(sb, alertText, 20, 570);
            } else {
                showAlert = false;
            }
        }

        sb.end();
    }

    @Override
    protected void dispose() {
        img.dispose();
        font.dispose();
        startBtn.dispose();
        nameBtn.dispose();
    }

}
