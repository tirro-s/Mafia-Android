package com.mafia.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

public class PlayState extends State {

    private Texture day = new Texture("day.png");
    private Texture night = new Texture("night.png");

    private Texture bazaSelect = new Texture("baza_select.png");
    private Texture podlozhka = new Texture("podlozhka.png");

    private Button startBtn;
    private Button exitBtn;
    private Button selectBtn;
    private Button kaznitBtn;
    private Button noneBtn;

    private Button yesBtn;
    private Button noBtn;

    public static Zapros zapros;
    public static String resp;
    public static String respPeriod;

    private BitmapFont font;

    private long updateTimer;
    private long updateDelay = 5000;

    public static Player player;
    private ArrayList<Player> players;

    private String myVote;
    private boolean canVote = false;
    public int period = 0;
    public int oldPeriod = 0;
    public String gameRes = "";
    public String gameResV = "";
    public String kto = "";

    public static boolean needUpdate = false;
    public static boolean needUpdatePeriod = false;

   private boolean showDialog = false;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, Mafia.WIDTH, Mafia.HEIGHT);

        zapros = (new Zapros());

        font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font_0.png"), false);
        font.setColor(Color.LIME);

        //font = new BitmapFont();

        startBtn = new Button("deal_btn.png", 210, 730, false);
        exitBtn = new Button("exit_btn.png", 10, 730, false);
        selectBtn = new Button("approve_btn.png", 210, 730, false);
        kaznitBtn = new Button("kaznit_btn.png", 170, 730, false);
        noneBtn = new Button("none_btn.png", 345, 730, false);

        yesBtn = new Button("yesbtn.png", 116, 470, false);
        noBtn = new Button("nobtn.png", 116 + 124, 470, false);

        player = (new Player(Mafia.name, "none", 1 , 0, 600, ""));
        players = new ArrayList<Player>();

        updateTimer = System.nanoTime();

    }

    private void checkTouch(Vector3 touchPosition) {
        if (startBtn.getRec().contains(touchPosition.x, touchPosition.y) && period == 0) {
            zapros.send("gamestart.php?game=" + Mafia.game, "");
        }
        if (exitBtn.getRec().contains(touchPosition.x, touchPosition.y)) {
            showDialog = true;
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getRec().contains(touchPosition.x, touchPosition.y)) {
                if (player.role.equals("mafia")) {
                    if (!players.get(i).role.equals("mafia") && players.get(i).active == 1) myVote = players.get(i).name;
                } else {
                    if (players.get(i).active == 1) myVote = players.get(i).name;
                }
            }
        }
        if (selectBtn.getRec().contains(touchPosition.x, touchPosition.y) && myVote != null && canVote && player.active == 1 && period == 1) {
            zapros.send("vote.php?nickname=" + Mafia.name + "&kogo=" + myVote + "&role=" + player.role + "&game=" + Mafia.game, "");
            myVote = null;
            canVote = false;
        }
        if (kaznitBtn.getRec().contains(touchPosition.x, touchPosition.y) && myVote != null && canVote && player.active == 1 && period == 2) {
            zapros.send("voteday.php?nickname=" + Mafia.name + "&kogo=" + myVote + "&game=" + Mafia.game, "");
            myVote = null;
            canVote = false;
        }
        if (noneBtn.getRec().contains(touchPosition.x, touchPosition.y) && period == 2 && canVote && player.active == 1) {
            zapros.send("voteday.php?nickname=" + Mafia.name + "&kogo=none" + "&game=" + Mafia.game, "");
            myVote = null;
            canVote = false;
        }
    }

    private void checkTouchDialog(Vector3 touchPosition) {
        if (yesBtn.getRec().contains(touchPosition.x, touchPosition.y)) {
            zapros.send("del_player.php?playername=" + Mafia.name+ "&game=" + Mafia.game, "");
            gsm.set(new MenuState(gsm));
        }
        if (noBtn.getRec().contains(touchPosition.x, touchPosition.y)) {
            showDialog = false;
        }
    }

    @Override
    protected void HandleInput() {
        if (Gdx.input.justTouched()){

            Vector3 touchPosition;
            touchPosition = new Vector3(0,0,0);
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPosition);

            if (!showDialog) checkTouch(touchPosition);
            if (showDialog) checkTouchDialog(touchPosition);

        }

    }

    private void updatePlayers(){
        JsonValue root = new JsonReader().parse(resp);
        players.clear();
        int i = 0;
        int j = 0;
        while (i < root.size) {
            if (!root.get(i).getString("nickname").equals(Mafia.name)) {
                players.add(new Player(
                        root.get(i).getString("nickname"),
                        root.get(i).getString("role"),
                        root.get(i).getInt("active"),
                        root.get(i).getInt("score"),
                        500 - (j * 100),
                        root.get(i).getString("vote")
                ));
                j++;
            } else {
                player.setRole(root.get(i).getString("role"));
                player.active = root.get(i).getInt("active");
                player.score = root.get(i).getInt("score");
            }
            i++;
        }
    }

    private void updatePeriod(){
        JsonValue root = new JsonReader().parse(respPeriod);
        period = root.getInt("period");
        gameRes = root.getString("results");
        kto = root.getString("kto");
        if (period != oldPeriod) {
            oldPeriod = period;
            canVote = true;
        }
        gameResV = "";
        if (gameRes.equals("mafia_win")) gameResV = "Мафия выиграла";
        if (gameRes.equals("mafia_loss")) gameResV = "Мафия проиграла";
        if (gameRes.equals("ugadal")) gameResV = "Комиссар угадал ";
        if (gameRes.equals("neugadal")) gameResV = "Комиссар не угадал ";
        if (!kto.equals("") && !kto.equals("vilichili")) gameResV = gameResV + "Убили " + kto;
        if (kto.equals("vilichili")) gameResV = gameResV + "Вылечили";
        if (gameRes.equals("ubili")) gameResV = "Убили " + kto;
        if (gameRes.equals("neubili")) gameResV = "Никого не убили";

        //if (gameRes.equals("mafia_win") || gameRes.equals("mafia_loss")) canVote = true; // Нужно будет убрать
        //System.out.println(period);
    }


    @Override
    protected void update(float dt) {
        HandleInput();

        long elapsedTimer = (System.nanoTime() - updateTimer) / 1000000;
        if(elapsedTimer > updateDelay) {
            zapros.send("get_period.php?game=" + Mafia.game, "getPeriod");
            zapros.send("get_players.php?game=" + Mafia.game, "getPlayers");
            updateTimer = System.nanoTime();
        }
        if (needUpdate) {
            updatePlayers();
            needUpdate = false;
        }
        if (needUpdatePeriod) {
            updatePeriod();
            needUpdatePeriod = false;
        }

    }

    @Override
    protected void render(SpriteBatch sb) {
        sb.begin();
        if (period == 2) sb.draw(day, 0,0);
        if (period == 1) sb.draw(night, 0,0);

        sb.draw(podlozhka, (480 - podlozhka.getWidth())/2,695);

        sb.draw(exitBtn.getTexture(), exitBtn.x,exitBtn.y);
        if (period == 0) sb.draw(startBtn.getTexture(), startBtn.x, startBtn.y);
        if (canVote && player.active == 1 && period == 1) sb.draw(selectBtn.getTexture(), selectBtn.x, selectBtn.y);
        if (canVote && player.active == 1 && period == 2) {
            sb.draw(kaznitBtn.getTexture(), kaznitBtn.x, kaznitBtn.y);
            sb.draw(noneBtn.getTexture(), noneBtn.x,noneBtn.y);
        }

        sb.draw(player.getBaza(), (480 - player.getBaza().getWidth())/2,600);
        sb.draw(player.getRole(), 360,605);
        font.draw(sb, Mafia.name, 100, 660);
        font.draw(sb, "" + player.score, 430, 660);

        font.draw(sb, gameResV, 40, 723);

        for (int i = 0; i < players.size(); i++) {
            sb.draw(players.get(i).getBaza(), (480 - players.get(i).getBaza().getWidth())/2, 500 - (i * 100));
            if (player.role.equals("mafia") && players.get(i).role.equals("mafia")) players.get(i).setRole("mafia");
            sb.draw(players.get(i).getRole(), 360,505 - (i * 100));
            if (myVote != null) if (myVote.equals(players.get(i).name)) sb.draw(bazaSelect, (480 - players.get(i).getBaza().getWidth())/2, 500 - (i * 100));
            font.draw(sb, players.get(i).name, 100, 560 - (i * 100));
            font.draw(sb, "" + players.get(i).score, 430, 560 - (i * 100));
            if (period == 2 && players.get(i).active == 1) font.draw(sb, players.get(i).vote, 290, 560 - (i * 100));
            if (period == 1 && player.role.equals("mafia") && players.get(i).role.equals("mafia")) font.draw(sb, players.get(i).vote, 290, 560 - (i * 100));
        }

        if (showDialog) {
            sb.draw(yesBtn.getTexture(), yesBtn.x, yesBtn.y);
            sb.draw(noBtn.getTexture(), noBtn.x, noBtn.y);
        }
        sb.end();
    }

    @Override
    protected void dispose() {
        day.dispose();
        night.dispose();
        podlozhka.dispose();
        startBtn.dispose();
        selectBtn.dispose();
        kaznitBtn.dispose();
        yesBtn.dispose();
        noBtn.dispose();
        exitBtn.dispose();
        bazaSelect.dispose();
        player.dispose();
    }
}
