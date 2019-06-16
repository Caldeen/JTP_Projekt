package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;


public class HpBar {
    private Player player;
    private Batch batch;
    private Texture texture=new Texture("core/assets/noHp.png");
    private Texture hptext=new Texture("core/assets/fulhp.png");
    private Sprite sprite=new Sprite(hptext);

    public HpBar(Player player,  Batch batch) {
        this.player = player;
        this.batch = batch;
    }

    public void draw(){
        Vector2 drawStart=new Vector2(player.getBody().getWorldCenter().x-2f,player.getBody().getWorldCenter().y);
        Vector2 startPos=new Vector2();
        int validator=0;
        for(int i=-1;i<1;i++){
            for(int j=0;j<6;j++){
                startPos.x=(drawStart.x+j*5/9f)*project.meter_to_pixels+3f;
                startPos.y=(drawStart.y+i*5/9f)*project.meter_to_pixels;
                if(validator<player.getHp())
                    sprite.setTexture(hptext);
                else
                    sprite.setTexture(texture);
                sprite.setPosition(startPos.x,startPos.y);
                sprite.draw(batch);
                validator++;
            }
        }
    }
}
