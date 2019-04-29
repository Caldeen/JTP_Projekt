package com.mygdx.project;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class project extends ApplicationAdapter {
	public final static float rad_to_deg = 57.2957795f;
	public final static float meter_to_pixels = 9;
	private World world;
	private OrthographicCamera cam, debugCam;
	private Box2DDebugRenderer debugRenderer;
	LevelBound lvlBound1, lvlBound2, lvlBound3, lvlBound4;
	LevelBound obstacle1,obstacle2,obstacle3,obstacle4,obstacle5,obstacle6,obstacle7,obstacle8,obstacle9,obstacle10;
	Vector2 camSize;
	Player player1, player2;
	private static final boolean DEBUG = true;
	private Bullet b1;
	private ArrayList<Bullet> bulletsToDestroy;
	private ArrayList<Bullet> activeBullets;
	private Texture bulletTexture;
	private Sprite bulletSprite;
	private Vector2 score;
	private BitmapFont bitmapFont;
	SpriteBatch batch;
	@Override
	public void create () {
		bulletsToDestroy=new ArrayList<Bullet>();
		activeBullets=new ArrayList<Bullet>();
		camSize=new Vector2(100,100);
		score=new Vector2();

		batch = new SpriteBatch();
		debugRenderer= new Box2DDebugRenderer();
		bitmapFont=new BitmapFont();
		bitmapFont.setColor(242/255f,97/255f,17/255f,1);
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new WorldContactListener(world));
		cam= new OrthographicCamera();
		cam.setToOrtho(false,camSize.x,camSize.y);
		debugCam = new OrthographicCamera();
		debugCam.setToOrtho(false,100*meter_to_pixels, 100*meter_to_pixels);

		Gdx.graphics.setWindowedMode(900,900);

		lvlBound1=new LevelBound(world,batch,4, camSize.y/2f,2, camSize.y/2,true);
		lvlBound2=new LevelBound(world,batch,camSize.x-4, camSize.y/2f,2, camSize.y/2f,true);
		lvlBound3=new LevelBound(world,batch,camSize.x/2,camSize.y-4 ,camSize.x/2, 2,false);
		lvlBound4=new LevelBound(world,batch,camSize.x/2,4 ,camSize.x/2 , 2,false);

		obstacle1=new LevelBound(world,batch,40,25,2,10,true);
		obstacle2=new LevelBound(world,batch,25,40,10,2,false);
		obstacle3=new LevelBound(world,batch,camSize.x-40,camSize.y-25,2,10,true);
		obstacle4=new LevelBound(world,batch,camSize.x-25,camSize.x-40,10,2,false);

		obstacle5=new LevelBound(world,batch,48,48,2,4,true);
		obstacle6=new LevelBound(world,batch,52,48,2,4,true);

		obstacle7=new LevelBound(world,batch,73,23,2,4,true);
		obstacle8=new LevelBound(world,batch,77,23,2,4,true);

		obstacle9=new LevelBound(world,batch,23,73,2,4,true);
		obstacle10=new LevelBound(world,batch,27,73,2,4,true);

		player1=new Player(world,batch,20,20,2,"player1Circle.png",0);
		player2=new Player(world,batch,camSize.x-20,camSize.y-20,2,"player2Circle.png",1);

		bulletTexture=new Texture("bullet.png");
		bulletSprite=new Sprite(bulletTexture);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor((float)172/255, (float)168/255, (float)165/255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(1/60f, 6, 2);
		playerUpdate(player1);
		playerUpdate(player2);
		bulletsUpdate();
		cam.update();
		debugCam.update();

		batch.setProjectionMatrix(debugCam.combined);
		inputUpdate();

		if(DEBUG) {
			debugRenderer.render(world, cam.combined);
			System.out.println("p1 rot: "+player1.getRotation()+"p1 pos: "
					+ player1.getBody().getWorldCenter().x+" "+player1.getBody().getWorldCenter().y+" "+player2.getHp());
		}
		batch.begin();
			player1.draw();
			player2.draw();
			for(Bullet bullet:activeBullets)
				bullet.draw();
			lvlBound3.draw();
			lvlBound4.draw();
			lvlBound1.draw();
			lvlBound2.draw();

			obstacle1.draw();
			obstacle2.draw();
			obstacle3.draw();
			obstacle4.draw();
			obstacle5.draw();
			obstacle6.draw();
			obstacle7.draw();
			obstacle8.draw();
			obstacle9.draw();
			obstacle10.draw();

			bitmapFont.draw(batch,"Score: Red "+(int)score.x  +"  Green "+(int)score.y,10*meter_to_pixels,99.4f*meter_to_pixels);
		batch.end();

	}
	private void playerUpdate(Player player){
		if(player.getHp()==0){
			if(player.getTeam()==0)
				score.x++;
			else
				score.y++;
			player.setHp(12);
			player.getBody().setTransform(player.getStartPos(),0f);
			player.setRotation(0f);
			player.getGunBody().setTransform(player.getBody().getWorldCenter().x+2f,player.getBody().getWorldCenter().y,0f);
		}

	}
	private  void inputUpdate(){
		Vector3 moveDirection=new Vector3(0,0,0);
		Vector3 moveDirection2 =new Vector3(0,0,0);
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			moveDirection.y=1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			moveDirection.y=-1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			moveDirection.x=-1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			moveDirection.x=1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)){
			moveDirection.z=1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q)){
			moveDirection.z=-1;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			moveDirection2.y=1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			moveDirection2.y=-1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			moveDirection2.x=-1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			moveDirection2.x=1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.O)){
			moveDirection2.z=-1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.P)){
			moveDirection2.z=1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			if(player1.canFire())
				b1=new Bullet(world,batch,player1,bulletsToDestroy,activeBullets,bulletSprite);
				activeBullets.add(b1);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)){
			if(player2.canFire())
				b1=new Bullet(world,batch,player2,bulletsToDestroy,activeBullets,bulletSprite);
				activeBullets.add(b1);
		}
		player1.move(moveDirection);
		player2.move(moveDirection2);
	}
	private void bulletsUpdate(){
		for(Bullet b:bulletsToDestroy ){
			world.destroyBody(b.getBody());
			activeBullets.remove(b);
		}
		bulletsToDestroy.clear();
	}
	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
		debugRenderer.dispose();
		lvlBound1.dispose();
		lvlBound2.dispose();
		lvlBound3.dispose();
		lvlBound4.dispose();
		player2.dispose();
		player1.dispose();
		bulletTexture.dispose();


	}
}
