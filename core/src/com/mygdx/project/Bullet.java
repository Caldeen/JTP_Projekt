package com.mygdx.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.Random;

public class Bullet {
    private World world;
    private BodyDef bodyDef = new BodyDef();
    private Batch batch;
    private Body body;
    private final float projSpeed=3f;
    private Player player;
    private Vector2 shootVec;
    private ArrayList<Bullet> bullets;
    private ArrayList<Bullet> activeBullets;
    private Random rand=new Random();
    private Sprite sprite;
    boolean drawable=true;

    private final float dispersionMax=0.45f;
    public Vector2 getShootVec() {
        return shootVec;
    }
    public Bullet(World world, Batch batch, Player player, ArrayList<Bullet> bullets,ArrayList<Bullet> activeBullets,Sprite sprite) {
        this.world = world;
        this.batch = batch;
        this.player = player;
        this.bullets=bullets;
        this.activeBullets=activeBullets;
        this.sprite=sprite;
        createBullet();
        shootBullet();
    }
    private void createBullet(){
        shootVec=new Vector2();
        shootVec.y= 2*MathUtils.sin(player.getBody().getTransform().getRotation());
        shootVec.x=2*MathUtils.cos(player.getBody().getTransform().getRotation());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(player.getBody().getWorldCenter().x+2*shootVec.x,player.getBody().getWorldCenter().y+2f*shootVec.y);
        bodyDef.fixedRotation=true;
        body = world.createBody(bodyDef);
        body.setBullet(true);
        Shape circle =new CircleShape();
        circle.setRadius(5/18f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.7f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.05f;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        circle.dispose();

    }

    private void shootBullet(){
        int variance=rand.nextInt(2);
        float dispersion=rand.nextFloat()%dispersionMax;
        System.out.println("  va"+variance);
        body.applyLinearImpulse(shootVec.x*projSpeed,shootVec.y*projSpeed
                ,body.getWorldCenter().x,body.getWorldCenter().y,true);

        if(variance==0){
            body.applyLinearImpulse(dispersion*shootVec.y,dispersion*-1*shootVec.x,
                    body.getWorldCenter().x,body.getWorldCenter().y,true);
        }else{
            body.applyLinearImpulse(dispersion*-1*shootVec.y,dispersion*shootVec.x,
                    body.getWorldCenter().x,body.getWorldCenter().y,true);
        }
    }
    public void draw(){
        sprite.setPosition((body.getWorldCenter().x-5/18f)*project.meter_to_pixels,(body.getWorldCenter().y-5/18f)*project.meter_to_pixels);
        if(drawable)
        sprite.draw(batch);

    }

    public Body getBody() {
        return body;
    }
    public void destroy(){
        drawable=false;
        if(!bullets.contains(this)){

            bullets.add(this);
        }
    }
}
