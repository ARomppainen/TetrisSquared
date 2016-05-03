package com.romppainen.aleksi.tetris;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Block {

    public enum BlockType {
        BLUE,
        GREEN,
        RED,
        YELLOW
    }

    public BlockType type;
    public Sprite sprite;

    public Block(float x, float y, BlockType type, Texture texture) {
        this.type = type;
        this.sprite = new Sprite(texture);
        this.sprite.setFlip(false, true);
        this.sprite.setPosition(x, y);

        switch(type) {
            case BLUE:   this.sprite.setColor(0, 0, 1, 1); break;
            case GREEN:  this.sprite.setColor(0, 1, 0, 1); break;
            case RED:    this.sprite.setColor(1, 0, 0, 1); break;
            case YELLOW: this.sprite.setColor(1, 1, 0, 1); break;
        }
    }
}