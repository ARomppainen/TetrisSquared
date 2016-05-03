package com.romppainen.aleksi.tetris;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TetrisSquared extends Game {

    public AssetManager assets;
	public SpriteBatch batch;
	public ShapeRenderer shape;
    public FitViewport viewport;
    public OrthographicCamera camera;
    public TweenManager tween;

    public static final float SCREEN_WIDTH = 480;
    public static final float SCREEN_HEIGHT = 800;
	
	@Override
	public void create () {
        assets = new AssetManager();
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(true, SCREEN_WIDTH, SCREEN_HEIGHT);
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        Tween.setCombinedAttributesLimit(4);
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        tween = new TweenManager();

        GestureDetector gd = new GestureDetector(Gestures.instance());
        gd.setLongPressSeconds(Gestures.LONG_PRESS_TIME);
        Gdx.input.setInputProcessor(gd);

        this.setScreen(new GameScreen(this));
	}

	@Override
	public void resize(int width, int height) {
        viewport.update(width, height);
	}

	@Override
	public void render () {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        super.render();
        Gestures.instance().clear();
        //System.out.println("Render calls: " + batch.renderCalls);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batch.dispose();
        assets.dispose();
	}
}
