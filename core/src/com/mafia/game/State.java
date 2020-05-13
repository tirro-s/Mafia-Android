package com.mafia.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {
    protected GameStateManager gsm;
    protected OrthographicCamera cam;

    protected State (GameStateManager gsm) {

        this.gsm = gsm;
        cam = new OrthographicCamera();
    }

    protected abstract void HandleInput();
    protected abstract void update(float dt);
    protected abstract void render(SpriteBatch sb);
    protected abstract void dispose();

}

