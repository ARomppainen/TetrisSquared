package com.romppainen.aleksi.tetris;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class Piece {

    public GridPoint2 position;
    public Block[][] blocks;
    public int size;

    public Piece(String shape, int size, Texture texture) {
        this.position = new GridPoint2();
        this.size = size;
        this.blocks = new Block[this.size][this.size];

        for (int x = 0; x < this.size; ++x) {
            for (int y = 0; y < this.size; ++y) {
                char color = shape.charAt(y * this.size + x);

                Block.BlockType t;

                switch (color) {
                    case 'R': t = Block.BlockType.RED;    break;
                    case 'G': t = Block.BlockType.GREEN;  break;
                    case 'B': t = Block.BlockType.BLUE;   break;
                    case 'Y': t = Block.BlockType.YELLOW; break;
                    default:  t = null;                   break;
                }

                if (t != null) {
                    this.blocks[x][y] = new Block(0, 0, t, texture);
                } else {
                    this.blocks[x][y] = null;
                }
            }
        }

        updatePosition();
    }

    public void tryToMove(int direction, Board board) {
        this.move(direction);

        if (board.fit(this)) {
            this.updatePosition();
        } else {
            this.move(reverse(direction));
        }
    }

    private void move(int direction) {
        switch (direction) {
            case Input.Keys.UP:    this.position.y--; break;
            case Input.Keys.DOWN:  this.position.y++; break;
            case Input.Keys.LEFT:  this.position.x--; break;
            case Input.Keys.RIGHT: this.position.x++; break;
        }
    }

    private static int reverse(int direction) {
        int newDir;

        switch(direction) {
            case Input.Keys.UP:    newDir = Input.Keys.DOWN;  break;
            case Input.Keys.DOWN:  newDir = Input.Keys.UP;    break;
            case Input.Keys.LEFT:  newDir = Input.Keys.RIGHT; break;
            case Input.Keys.RIGHT: newDir = Input.Keys.LEFT;  break;
            default:               newDir = -1;               break;
        }

        return newDir;
    }

    public void tryToRotate(Board board) {
        this.rotate();

        if (board.fit(this)) {
            this.updatePosition();
        } else {
            this.rotateReverse();
        }
    }

    /*
        1, 0 -> 0, 6
        0, 6 -> 6, 7
        6, 7 -> 7, 1
        7, 1 -> 1, 0

        (1, 0) <- (7, 1) <- (6, 7) <- (0, 6)
        (x, y) <- (len - y, x) <- (len - x, len - y) <- (y, len - x) <- (x, y)
    */
    private void rotate() {
        int len = blocks.length - 1;

        for (int y = 0; y < blocks.length / 2; ++y) {
            for(int x = 0; x < blocks.length / 2; ++x) {
                Block temp               = blocks[x][y];
                blocks[x][y]             = blocks[len - y][x];
                blocks[len - y][x]       = blocks[len - x][len - y];
                blocks[len - x][len - y] = blocks[y][len - x];
                blocks[y][len - x]       = temp;
            }
        }

        if (blocks.length % 2 == 1) {
            int x = blocks.length / 2;

            for (int y = 0; y < blocks.length / 2; ++y) {
                Block temp               = blocks[x][y];
                blocks[x][y]             = blocks[len - y][x];
                blocks[len - y][x]       = blocks[len - x][len - y];
                blocks[len - x][len - y] = blocks[y][len - x];
                blocks[y][len - x]       = temp;
            }
        }
    }

    /*
        1, 0 <- 0, 6
        0, 6 <- 6, 7
        6, 7 <- 7, 1
        7, 1 <- 1, 0

        (1, 0) -> (7, 1) -> (6, 7) -> (0, 6)
        (x, y) -> (len - y, x) -> (len - x, len - y) -> (y, len - x) -> (x, y)
    */
    private void rotateReverse() {
        int len = blocks.length - 1;

        for (int y = 0; y < blocks.length / 2; ++y) {
            for(int x = 0; x < blocks.length / 2; ++x) {
                Block temp               = blocks[x][y];
                blocks[x][y]             = blocks[y][len - x];
                blocks[y][len - x]       = blocks[len - x][len - y];
                blocks[len - x][len - y] = blocks[len - y][x];
                blocks[len - y][x]       = temp;
            }
        }

        if (blocks.length % 2 == 1) {
            int x = blocks.length / 2;

            for (int y = 0; y < blocks.length / 2; ++y) {
                Block temp               = blocks[x][y];
                blocks[x][y]             = blocks[y][len - x];
                blocks[y][len - x]       = blocks[len - x][len - y];
                blocks[len - x][len - y] = blocks[len - y][x];
                blocks[len - y][x]       = temp;
            }
        }
    }

    private void updatePosition() {
        for (int x = 0; x < this.size; ++x) {
            for (int y = 0; y < this.size; ++y) {
                if (blocks[x][y] != null) {
                    Vector2 coords = Board.PointToCoords(this.position.x + x, this.position.y + y);
                    blocks[x][y].sprite.setPosition(coords.x, coords.y);
                }
            }
        }
    }

    public void draw(Batch batch) {
        for (int x = 0; x < this.size; ++x) {
            for (int y = 0; y < this.size; ++y) {
                if (blocks[x][y] != null) {
                    blocks[x][y].sprite.draw(batch);
                }
            }
        }
    }
}
