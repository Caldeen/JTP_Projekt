package com.mygdx.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.*;

public class Player {
    private World world;
    private BodyDef bodyDef = new BodyDef();
    private Batch batch;
    private Body body;
    private HpBar hpBar;
    private  float radius;
    private float rotation=0;
    private final float runSpeed =13;
    private int hp;
    private Texture text;
    private Texture gunT=new Texture("gun.png");
    private Sprite sprite;
    private Sprite gun;
    private Body gunBody;
    private float timeStart=0;
    private float shieldTimeStart=0;
    private float shieldCooldownTimer=5;
    private String path;
    private Vector2 startPos=new Vector2();
    private int team;
    private boolean shieldActive=false;


    public void setHp(int hp) {
        this.hp = hp;
    }


    public Vector2 getStartPos() {
        return startPos;
    }

    public int getTeam() {
        return team;
    }

    public Player(World world, Batch batch, float posX, float posY, float radius, String pathToModel, int team){
        this.world=world;
        startPos.set(posX,posY);
        path=pathToModel;
        this.team=team;
        this.radius=radius;
        createPlayerBody( posX, posY, radius);
        hp=12;
        this.batch=batch;
        text=new Texture(pathToModel);
        sprite=new Sprite(text);
        gun=new Sprite(gunT);
        timeStart= Gdx.graphics.getRawDeltaTime();
        createGunBody();
        hpBar=new HpBar(this,batch);
    }

    /**Tworzy ciało w fizycznym świecie box2d, stałe własności gracza*/
    private void createPlayerBody(float posX,float posY,float radius){
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX,posY);
        bodyDef.fixedRotation=true;
        body = world.createBody(bodyDef);
        Shape circle =new CircleShape();
        circle.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.8f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        circle.dispose();
        body.setAwake(true);
    }
    /**Tworzy ciało broni w świecie fizycznym box2d */
    private void createGunBody(){
        Vector2 gunVector=new Vector2();
        gunVector.y= 2* MathUtils.sin(body.getTransform().getRotation());
        gunVector.x=2*MathUtils.cos(body.getTransform().getRotation());
        BodyDef gunDef=new BodyDef();
        gunDef.type=BodyDef.BodyType.DynamicBody;
        gunDef.position.set(body.getWorldCenter().x+2,body.getWorldCenter().y);
        gunBody=world.createBody(gunDef);
        PolygonShape gunShape=new PolygonShape();
        gunShape.setAsBox(10/18f,3/18f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = gunShape;
        fixtureDef.density = 0.000000001f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;
        Fixture fix=gunBody.createFixture(fixtureDef);
        fix.setUserData(this);
        gunShape.dispose();
        createJoint();
    }
    /**Tworzy połączenie między ciałem gracza,a broni  */
    private void createJoint(){
        RevoluteJointDef revoluteJointDef=new RevoluteJointDef();
        revoluteJointDef.initialize(body,gunBody,new Vector2(body.getWorldCenter().x+2,body.getWorldCenter().y));
        revoluteJointDef.collideConnected=false;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.enableMotor = true;
        world.createJoint(revoluteJointDef);
    }
    /**Przesuwa ciało gracza w stronę wektora oraz rotuje je
     * @param moveDir pierwsze 2 wartości- kierunek; 3cia- rotacja */
    public void move(Vector3 moveDir){
        body.setLinearVelocity(moveDir.x*runSpeed,moveDir.y*runSpeed);
        if(moveDir.z!=0){
            rotation-=moveDir.z*0.06;
            body.setTransform(body.getWorldCenter(),rotation);
            body.setAwake(true);
        }

    }


    /**Rysowanie gracza, broni i obsługa tarczy*/
    public void draw(){
        Vector2 drawStart=new Vector2(body.getWorldCenter().x-radius,body.getWorldCenter().y-radius);
        sprite.setPosition(drawStart.x*project.meter_to_pixels,drawStart.y*project.meter_to_pixels);
        sprite.setSize(radius*2*project.meter_to_pixels,radius*2*project.meter_to_pixels);

        sprite.draw(batch);
        gun.setRotation(gunBody.getTransform().getRotation()*project.rad_to_deg);

        gun.setPosition(project.meter_to_pixels*(gunBody.getWorldCenter().x-10/18f),
                project.meter_to_pixels*(gunBody.getWorldCenter().y-3/18f));
        gun.draw(batch);

        timeStart+= Gdx.graphics.getRawDeltaTime();
        if(shieldActive){

            shieldTimeStart+=Gdx.graphics.getRawDeltaTime();
            if(shieldTimeStart>=3) {
                shieldActive = false;
                shieldTimeStart = 0;
            }

        }else
            shieldCooldownTimer+=Gdx.graphics.getRawDeltaTime();

        hpBar.draw();
    }
    public boolean canFire(){
        if(timeStart>0.13){
            timeStart=0;
            return  true;
        }
        return false;
    }
    public Body getBody() {
        return body;
    }

    public void hit(){
        hp--;
    }
    /**Aktywacja tarczy */
    public void activateShield(){
        if(!shieldActive&&shieldCooldownTimer>5){
            shieldActive=true;
            shieldCooldownTimer=0;
        }

    }

    public int getHp() {
        return hp;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public double getRotation() {
        return rotation;
    }
    public void dispose(){
        text.dispose();
        gunT.dispose();
    }
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
    public Body getGunBody() {
        return gunBody;
    }
}
