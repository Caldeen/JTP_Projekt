package com.mygdx.project;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    private World world;

    public WorldContactListener(World world1){
        world=world1;
    }
    @Override
    public void beginContact(Contact contact){
        Fixture firstFi=contact.getFixtureA();
        Fixture secondFi=contact.getFixtureB();
        if(firstFi==null||secondFi==null)
            return;
        if(firstFi.getUserData()==null||secondFi.getUserData()==null)
            return;
        if(isGroundContact(firstFi,secondFi)){
            Bullet bullet=(Bullet) secondFi.getUserData();
            bullet.destroy();
        }
        if(isGroundContact(secondFi,firstFi)){
            Bullet bullet=(Bullet) firstFi.getUserData();
            bullet.destroy();
        }

        if(isPlayerhit(secondFi,firstFi)){
            Bullet bullet=(Bullet) secondFi.getUserData();

            Player player=(Player) firstFi.getUserData();
            player.hit(bullet);
            player.getBody().applyLinearImpulse(bullet.getShootVec().x*30,bullet.getShootVec().y*30,player.getBody().getWorldCenter().x,player.getBody().getWorldCenter().y,true);

            bullet.destroy();
        }

        if(isPlayerhit(firstFi,secondFi)){
            Bullet bullet=(Bullet) firstFi.getUserData();

            Player player=(Player) secondFi.getUserData();
            player.hit(bullet);
            player.getBody().applyLinearImpulse(bullet.getShootVec().x*30,bullet.getShootVec().y*30,player.getBody().getWorldCenter().x,player.getBody().getWorldCenter().y,true);

            bullet.destroy();
        }


    }

    @Override
    public void endContact(Contact contact){
        Fixture firstFi=contact.getFixtureA();
        Fixture secondFi=contact.getFixtureB();
        if(firstFi==null||secondFi==null)
            return;
        if(firstFi.getUserData()==null||secondFi.getUserData()==null)
            return;

    }
    private boolean isPlayerhit(Fixture a,Fixture b){
        return ((a.getUserData() instanceof Bullet && b.getUserData() instanceof Player));
    }
    private boolean isGroundContact(Fixture a, Fixture b) {
        return ((a.getUserData() instanceof LevelBound && b.getUserData() instanceof Bullet));
    }
    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {
    }
    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {
    }
}