package com.mafia.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private Texture baza = new Texture("baza_active.png");
    private Texture bazaNone = new Texture("baza_none.png");
    private Texture mafia = new Texture("mafia.png");
    private Texture doctor = new Texture("doctor.png");
    private Texture komisar = new Texture("komisar.png");
    private Texture zhitel = new Texture("zhitel.png");
    private Texture none = new Texture("none.png");
    private Texture rol = none;

    public String name;
    public String role;
    public int active;
    public int score;
    public String vote;

    private Rectangle rec;

    public Player(String name, String role, int active, int score, int y, String vote) {
        this.name = name;
        this.active = active;
        this.score = score;
        this.role = role;
        this.vote = vote;
        rec = new Rectangle();
        rec.x = 80;
        this.rec.y = y;
        rec.width = baza.getWidth();
        rec.height = baza.getHeight();
        if (active == 0) setRole(role);
        //if (PlayState.player.role.equals("mafia")) setRole(role);
    }

    public Texture getBaza() {
        if (active == 1) return baza; else return bazaNone;
    }

    public Texture getRole() {
        return rol;
    }

    public Rectangle getRec() {
        return rec;
    }

    public void setRole(String role) {
        this.role = role;
        if (role.equals("mafia")) {rol = mafia;}
        if (role.equals("doctor")) {rol = doctor;}
        if (role.equals("komisar")) {rol = komisar;}
        if (role.equals("zhitel")) {rol = zhitel;}
    }

    public void dispose(){
        baza.dispose();
        bazaNone.dispose();
        mafia.dispose();
        doctor.dispose();
        komisar.dispose();
        zhitel.dispose();
        none.dispose();
        rol.dispose();
    }
}
