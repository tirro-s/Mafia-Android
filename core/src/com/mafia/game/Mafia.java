package com.mafia.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Mafia extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "Mafia";

	//public static boolean isCreator  = false;
	public static String name = "name";
	public static String game = "game";

	public static Preferences prefs;

	private GameStateManager gsm;
	SpriteBatch batch;

	@Override
	public void create () {

		prefs = Gdx.app.getPreferences("MyPreferences");
		name = prefs.getString("name");
		game = prefs.getString("game");

		batch = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

    @Override
    public void dispose(){
        PlayState.zapros.send("del_player.php?playername=" + Mafia.name+ "&game=" + Mafia.game, "");
        batch.dispose();
    }

}
