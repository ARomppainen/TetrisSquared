package com.romppainen.aleksi.tetris;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class Gestures implements GestureDetector.GestureListener {

    public Vector2[] flings;
    public int flingCount;

    public Vector2 pan;
    public boolean panned;

    public Vector2 press;
    public boolean pressed;

    public Vector2[] taps;
    public int tapCount;

    public static final int MAX_FLINGS = 5;
    public static final int MAX_TAPS = 5;
    public static final float LONG_PRESS_TIME = 0.5f;

    private static Gestures instance;

    private Gestures() {
        flings = new Vector2[MAX_FLINGS];

        for (int i = 0; i < MAX_FLINGS; ++i) {
            flings[i] = new Vector2();
        }

        pan = new Vector2();
        panned = false;

        press = new Vector2();
        pressed = false;

        taps = new Vector2[MAX_TAPS];

        for (int i = 0; i < MAX_TAPS; ++i) {
            taps[i] = new Vector2();
        }

        tapCount = 0;
    }

    public static Gestures instance() {
        if (instance == null) {
            instance = new Gestures();
        }

        return instance;
    }

    public void clear() {
        //pan.set(0, 0);

        //panned = false;
        pressed = false;
        flingCount = 0;
        tapCount = 0;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (tapCount < MAX_TAPS) {
            taps[tapCount].set(x, y);
            tapCount++;
        }

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        press.set(x, y);
        pressed = true;

        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (flingCount < MAX_FLINGS) {
            flings[flingCount].set(velocityX, velocityY);
            flingCount++;
        }

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        pan.x += deltaX;
        pan.y += deltaY;
        panned = true;

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        pan.set(0, 0);
        panned = false;

        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
