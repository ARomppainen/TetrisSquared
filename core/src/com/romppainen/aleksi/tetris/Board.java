package com.romppainen.aleksi.tetris;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;

public class Board {

    public static final int WIDTH = 7;
    public static final int HEIGHT = 12;
    public static final int PADDING = 16;
    public static final int BLOCK_SIZE = 64;
    public static final float FALL_TIME_STEP = 0.06f;
    public static final float FADE_OUT_TIME = 0.35f;

    public Block[][] blocks;

    public Board() {
        blocks = new Block[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                blocks[x][y] = null;
            }
        }
    }

    public boolean checkBounds(int x, int y) {
        return (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT);
    }

    public boolean fit(Piece p) {

        for (int i = 0, x = p.position.x; i < p.size; ++i, ++x) {
            for (int j = 0, y = p.position.y; j < p.size; ++j, ++y) {
                if (p.blocks[i][j] != null) {
                    //check bounds
                    if (x < 0 || x >= WIDTH ||
                            y < 0 || y >= HEIGHT) {
                        return false;
                    }

                    if (this.blocks[x][y] != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void put(Piece p) {
        for (int i = 0, x = p.position.x; i < p.size; ++i, ++x) {
            for (int j = 0, y = p.position.y; j < p.size; ++j, ++y) {
                if (p.blocks[i][j] != null) {
                    this.blocks[x][y] = p.blocks[i][j];
                }
            }
        }
    }

    public ObjectSet<GridPoint2> matchTiles(int minLength) {
        ObjectSet<GridPoint2> tiles = new ObjectSet<GridPoint2>();

        // search horizontal lines
        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x <= WIDTH - minLength; ++x) {
                int length = match(x, y, true);

                if (length >= minLength) {
                    for (int dx = x; dx < x + length; ++dx) {
                        tiles.add(new GridPoint2(dx, y));
                    }
                }
            }
        }

        // search vertical lines
        for (int y = 0; y <= HEIGHT - minLength; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                int length = match(x, y, false);

                if (length >= minLength) {
                    for (int dy = y; dy < y + length; ++dy) {
                        tiles.add(new GridPoint2(x, dy));
                    }
                }
            }
        }

        return tiles;
    }

    private int match(int x, int y, boolean horizontal) {
        int length = 0;

        if (blocks[x][y] != null) {
            Block.BlockType type = blocks[x][y].type;

            while(x < WIDTH && y < HEIGHT) {
                if (blocks[x][y] != null && blocks[x][y].type == type) {
                    length++;
                } else {
                    break;
                }

                if (horizontal) {
                    x++;
                } else {
                    y++;
                }
            }
        }

        return length;
    }

    public float dropBlocks(Timeline t) {
        float maxTime = 0.0f;

        t.beginParallel();

        for (int x = 0; x < WIDTH; ++x) {
            for (int y = HEIGHT - 2; y >= 0; --y) {
                if (blocks[x][y] != null) {
                    float time = dropBlock(x, y, t);

                    if (time > maxTime) {
                        maxTime = time;
                    }
                }
            }
        }

        t.end();

        return maxTime;
    }

    private float dropBlock(int x, int y, Timeline t) {
        int steps = 0;

        int dy = y;

        while(dy + 1 < HEIGHT && blocks[x][dy + 1] == null) {
            steps++;
            dy++;
        }

        if (steps > 0) {
            blocks[x][dy] = blocks[x][y];
            blocks[x][y] = null;

            t.push(Tween.to(blocks[x][dy].sprite, SpriteAccessor.POSITION_XY, steps * FALL_TIME_STEP)
                    .ease(TweenEquations.easeNone)
                    .targetRelative(0, steps * BLOCK_SIZE));
        }

        return steps * FALL_TIME_STEP;
    }

    public void drawBackground(ShapeRenderer shape) {
        shape.setAutoShapeType(true);
        shape.begin();
        shape.set(ShapeRenderer.ShapeType.Filled);
        shape.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        shape.rect(PADDING, PADDING, WIDTH * BLOCK_SIZE, HEIGHT * BLOCK_SIZE);
        shape.end();
    }

    public void draw(Batch batch) {
        for (int x = 0; x < WIDTH; ++x) {
            for (int y = 0; y < HEIGHT; ++y) {
                if (blocks[x][y] != null) {
                    blocks[x][y].sprite.draw(batch);
                }
            }
        }
    }

    public void swapBlocks(GridPoint2 p1, GridPoint2 p2) {
        Block temp = blocks[p1.x][p1.y];
        blocks[p1.x][p1.y] = blocks[p2.x][p2.y];
        blocks[p2.x][p2.y] = temp;
    }

    public static boolean isNeighbor(int x1, int y1, int x2, int y2) {
        boolean neighbor = false;

        if (x1 == x2) {
            if (y1 == y2 + 1 || y1 == y2 - 1) {
                neighbor = true;
            }
        } else if (y1 == y2) {
            if (x1 == x2 + 1 || x1 == x2 - 1) {
                neighbor = true;
            }
        }

        return neighbor;
    }

    public static GridPoint2 CoordsToPoint(float x, float y) {
        x -= PADDING;
        y -= PADDING;

        return new GridPoint2((int)x / BLOCK_SIZE, (int)y / BLOCK_SIZE);
    }

    public static Vector2 PointToCoords(int x, int y) {
        return new Vector2(PADDING + x * BLOCK_SIZE , PADDING + y * BLOCK_SIZE);
    }
}