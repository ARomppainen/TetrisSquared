package com.romppainen.aleksi.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.StringBuilder;

public class PieceFactory {

    private static PieceFactory instance;

    private Array<String> prototypes;

    public static final String JSON_PATH = "pieces.json";

    private PieceFactory() {
        loadPrototypes();
    }

    public static PieceFactory instance() {
        if (instance == null) {
            instance = new PieceFactory();
        }

        return instance;
    }

    public Piece createRandomPiece(Texture texture) {
        String proto = prototypes.get(MathUtils.random(prototypes.size - 1));
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < proto.length(); ++i) {
            char c = proto.charAt(i);

            if (c == ' ') {
                sb.append(c);
            } else {
                int rand = MathUtils.random(3);

                switch(rand) {
                    case 0: sb.append('R'); break;
                    case 1: sb.append('G'); break;
                    case 2: sb.append('B'); break;
                    case 3: sb.append('Y'); break;
                }
            }
        }

        return new Piece(sb.toString(), (int)Math.sqrt(sb.length()), texture);
    }

    private void loadPrototypes() {
        prototypes = new Array<String>();

        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(Gdx.files.internal(JSON_PATH));
        JsonValue pieces = root.child;

        for (JsonValue block = pieces.child; block != null; block = block.next) {
            String[] shape = block.child.asStringArray();
            StringBuilder proto = new StringBuilder();

            for (String s: shape) {
                proto.append(s);
            }

            prototypes.add(proto.toString());
        }
    }
}
