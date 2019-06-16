package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class LevelBound {
    private World world;
    private BodyDef bodyDef = new BodyDef();
    private Batch batch;
    private Body groundBody;
    private Texture text=new Texture("core/assets/5x5_square.png");
    private Sprite sprite=new Sprite(text);
    private Vector2 rectHalfSize=new Vector2();
    private boolean vertical;

    public LevelBound(World world,Batch batch,float posX,float posY,float halfWidth,float halfHeight,boolean vertical){
        this.world=world;
        levelBoundInit( posX, posY, halfWidth, halfHeight);
        this.batch=batch;
        this.vertical=vertical;
        rectHalfSize.x=halfWidth;
        rectHalfSize.y=halfHeight;
    }
    /**Tworzy ciało w fizycznym świecie box2d*/
    private void levelBoundInit (float posX,float posY,float halfWidth,float    halfHeight){
        bodyDef.position.set(posX,posY);
        groundBody = world.createBody(bodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(halfWidth,halfHeight);
        groundBody.createFixture(groundBox, 1.0f).setUserData(this);
        groundBox.dispose();
    }
    public void draw(){
        float squaresToDraw =vertical?rectHalfSize.y/4*2:rectHalfSize.x/4*2;

        Vector2 drawStart=new Vector2(groundBody.getWorldCenter().x-rectHalfSize.x,groundBody.getWorldCenter().y-rectHalfSize.y);
        if(vertical){
            for(;squaresToDraw>0;drawStart.y+=4){
                sprite.setPosition(drawStart.x*project.meter_to_pixels,drawStart.y*project.meter_to_pixels);
                sprite.draw(batch);
                squaresToDraw--;
            }
        }else{
            for(;squaresToDraw>0;drawStart.x+=4){
                sprite.setPosition(drawStart.x*project.meter_to_pixels,drawStart.y*project.meter_to_pixels);
                sprite.draw(batch);
                squaresToDraw--;
            }
        }
    }

    public void dispose(){
        text.dispose();
    }
}
