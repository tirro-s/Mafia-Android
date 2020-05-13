package com.mafia.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


public class Button {
    private Texture img;
    private Rectangle rec;
    public int x;
    public int y;

    public Button(String txt, int x, int y, boolean center){
        img = new Texture(txt);
        rec = new Rectangle();
        if (center) {
            rec.x = (480 - img.getWidth())/2;
            this.x = (480 - img.getWidth())/2;
        } else {
            rec.x = x;
            this.x = x;
        }
        rec.y = y;
        this.y = y;
        rec.width = img.getWidth();
        rec.height = img.getHeight();
    }

    public Rectangle getRec() {
        return rec;
    }

    public Texture getTexture(){
        return img;
    }

    public void dispose(){
        img.dispose();
    }

}
