package com.romppainen.aleksi.tetris;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements TweenAccessor<Sprite> {

    public static final int POSITION_XY = 1;
    public static final int SCALE_XY = 2;
    public static final int ROTATION = 3;
    public static final int COLOR = 4;

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch(tweenType) {
            case POSITION_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            case SCALE_XY:
                returnValues[0] = target.getScaleX();
                return 1;
            case ROTATION:
                returnValues[0] = target.getRotation();
                return 1;
            case COLOR:
                returnValues[0] = target.getColor().r;
                returnValues[1] = target.getColor().g;
                returnValues[2] = target.getColor().b;
                returnValues[3] = target.getColor().a;
                return 4;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch(tweenType) {
            case POSITION_XY:
                target.setPosition(newValues[0], newValues[1]);
                break;
            case SCALE_XY:
                target.setScale(newValues[0]);
                break;
            case ROTATION:
                target.setRotation(newValues[0]);
                break;
            case COLOR:
                target.setColor(newValues[0], newValues[1], newValues[2], newValues[3]);
                break;
            default:
                assert false;
                break;
        }
    }
}
