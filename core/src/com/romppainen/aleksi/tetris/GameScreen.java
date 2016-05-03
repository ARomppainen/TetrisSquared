package com.romppainen.aleksi.tetris;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectSet;

public class GameScreen implements Screen {

    public TetrisSquared game;

    public Board board;
    public Piece piece;

    public ShaderProgram shader;
    public Vector3 key;

    public ObjectSet<Block> fadeOut;

    public boolean disableInput;
    public float disableInputTimer;

    public boolean tileMatchState;
    public GridPoint2 originTile;
    public GridPoint2 targetTile;

    public GameScreen(TetrisSquared game) {
        this.game = game;
    }

    @Override
    public void show() {
        game.assets.load("tile.png", Texture.class);
        game.assets.finishLoading();

        shader = new ShaderProgram(
                Gdx.files.internal("defaultVS.glsl").readString(),
                Gdx.files.internal("chromaKeyFS.glsl").readString()
        );

        game.batch.setShader(shader);
        key = new Vector3(1.0f, 1.0f, 1.0f);

        fadeOut = new ObjectSet<Block>();

        disableInput = false;
        disableInputTimer = 0.0f;
        tileMatchState = false;
        originTile = null;

        board = new Board();
        piece = PieceFactory.instance().createRandomPiece(game.assets.get("tile.png", Texture.class));
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        game.tween.update(delta);

        if (disableInput) {
            disableInputTimer -= delta;

            if (disableInputTimer <= 0.0f) {
                disableInput = false;
                fadeOut.clear();
            }
        } else if (Gestures.instance().pressed) {
            Vector3 v = game.camera.unproject(new Vector3(
                            Gestures.instance().press.x,
                            Gestures.instance().press.y,
                            0.0f));
            GridPoint2 p = Board.CoordsToPoint(v.x, v.y);

            if (board.checkBounds(p.x, p.y)) {
                if (board.blocks[p.x][p.y] != null) {
                    tileMatchState = true;
                    originTile = p;
                    targetTile = p;
                }
            }
        } else if (tileMatchState) {
            if (Gdx.input.isTouched()) {
                Vector3 v = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
                board.blocks[originTile.x][originTile.y].sprite.setX(v.x - Board.BLOCK_SIZE / 2);
                board.blocks[originTile.x][originTile.y].sprite.setY(v.y - Board.BLOCK_SIZE / 2);
            } else {
                tileMatchState = false;
                Vector2 v = Board.PointToCoords(originTile.x, originTile.y);
                board.blocks[originTile.x][originTile.y].sprite.setX(v.x);
                board.blocks[originTile.x][originTile.y].sprite.setY(v.y);
            }
        } else {
            // keyboard input
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                piece.tryToMove(Input.Keys.LEFT, board);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                piece.tryToMove(Input.Keys.RIGHT, board);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                piece.tryToMove(Input.Keys.UP, board);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                piece.tryToMove(Input.Keys.DOWN, board);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                piece.tryToRotate(board);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                putPieceOnBoard();
            }
        }
    }

    private void putPieceOnBoard() {
        board.put(piece);

        ObjectSet<GridPoint2> matches;
        float totalTime = 0.0f;

        Timeline t = Timeline.createSequence();

        do {
            totalTime += board.dropBlocks(t);
            matches = board.matchTiles(3);

            if (matches.size > 0) {
                removeMatches(matches, t);
                totalTime += Board.FADE_OUT_TIME;
            }

        } while (matches.size > 0);

        t.start(game.tween);

        disableInput = true;
        disableInputTimer = totalTime;

        piece = PieceFactory.instance().createRandomPiece(game.assets.get("tile.png", Texture.class));
    }

    private void removeMatches(ObjectSet<GridPoint2> matches, Timeline t) {
        t.beginParallel();

        for (GridPoint2 p: matches) {
            t.push(Tween.to(board.blocks[p.x][p.y].sprite, SpriteAccessor.COLOR, Board.FADE_OUT_TIME)
                    .ease(TweenEquations.easeNone)
                    .targetRelative(0.0f, 0.0f, 0.0f, -1.0f));

            fadeOut.add(board.blocks[p.x][p.y]);
            board.blocks[p.x][p.y] = null;
        }

        t.end();
    }

    private void draw() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        board.drawBackground(game.shape);

        game.batch.begin();
        shader.setUniformf("u_key", key);

        board.draw(game.batch);

        if (!disableInput && !tileMatchState) {
            piece.draw(game.batch);
        }

        for(Block b: fadeOut) {
            b.sprite.draw(game.batch);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}