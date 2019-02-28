package com.mygdx.tetris;

import com.badlogic.gdx.graphics.Texture;
import javafx.geometry.Pos;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Board {

    private Tetrimino activeTetrimino;
    private ArrayList<Tetrimino> passiveTetrimini = new ArrayList<Tetrimino>();
    private ArrayList<Tetrimino> upcomingTetrimini = new ArrayList<Tetrimino>();
    private Texture image;
    private Texture LTetriminoImg;
    private Texture JTetriminoImg;
    private Texture ITetriminoImg;
    private Texture STetriminoImg;
    private Texture ZTetriminoImg;
    private Texture TTetriminoImg;
    private Texture SquareTetriminoImg;
    private int width;
    private int height;

    public Board(int width, int height, Texture image,
                 Texture ITetriminoImg,
                 Texture JTetriminoImg,
                 Texture LTetriminoImg,
                 Texture STetriminoImg,
                 Texture ZTetriminoImg,
                 Texture TTetriminoImg,
                 Texture SquareTetriminoImg
                 ) {

        this.width = width;
        this.height = height;
        this.image = image;
        this.LTetriminoImg = LTetriminoImg;
        this.JTetriminoImg = JTetriminoImg;
        this.ITetriminoImg = ITetriminoImg;
        this.STetriminoImg = STetriminoImg;
        this.ZTetriminoImg = ZTetriminoImg;
        this.TTetriminoImg = TTetriminoImg;
        this.SquareTetriminoImg = SquareTetriminoImg;

        // Generate a list of upcoming tetrimini
        for (int i = 0; i < 3; i++) {
            upcomingTetrimini.add(generateRandomTetrimino());
        }

        // Pick one to be the active tetrimino
        nextTetrimino();

    }

    public void moveLeft() {
        // Check if the space to the left is taken or negative
        for (Position p1 : activeTetrimino.getActualTileLocationsOnBoard()) {
            // The moving left part
            p1.x = p1.x - 1;
            if (p1.x < 0) {
                return;
            }
            for (Position p2 : takenPositions()) {
                if (p1.x == p2.x && p1.y == p2.y) {
                    return;
                }
            }
        }
        // If not, move left
        activeTetrimino.moveLeft();
    }

    public void moveRight() {
        // Check if the space to the right is taken or too far right
        for (Position p1 : activeTetrimino.getActualTileLocationsOnBoard()) {
            // The moving right part
            p1.x = p1.x + 1;
            if (p1.x >= width) {
                return;
            }
            for (Position p2 : takenPositions()) {
                if (p1.x == p2.x && p1.y == p2.y) {
                    return;
                }
            }
        }
        // If not, move right
        activeTetrimino.moveRight();
    }

    private boolean moveDown() {
        // Check if the space below is taken or too far down
        for (Position p1 : activeTetrimino.getActualTileLocationsOnBoard()) {
            // The moving down part
            p1.y = p1.y - 1;
            if (p1.y < 0) {
                return false;
            }
            for (Position p2 : takenPositions()) {
                if (p1.x == p2.x && p1.y == p2.y) {
                    return false;
                }
            }
        }
        // If not, move down
        activeTetrimino.moveDown();
        return true;
    }

    public boolean step() {
        if(!moveDown()) {
            for(Position p : activeTetrimino.getActualTileLocationsOnBoard()) {
                Tetrimino tempTet = new Tetrimino(activeTetrimino.getTileImage());
                tempTet.setPosition(p);
                passiveTetrimini.add(tempTet);
            }
            if (!nextTetrimino()) {
                return false;
            }

        }
        return true;
    }

    public ArrayList<Integer> fullLines() {
        ArrayList<Integer> fullLines = new ArrayList<Integer>();

        for (int y = height-1; y >= 0; y--) {
            int fullLineCounter = 0;
            for (int x = 0; x < width; x++) {
                for (Position p : takenPositions()) {
                    if (x == p.x && y == p.y) {
                        fullLineCounter++;
                    }
                }
            }
            if (fullLineCounter == width) {
                fullLines.add(y);
            }
        }
        return fullLines;
    }

    // TODO: Deal with the fact that I'm removing an extra line sometimes
    public void removeLine(int y) {
        for (Tetrimino t : passiveTetrimini) {
            for (Position p : t.getActualTileLocationsOnBoard()) {
                if (y == p.y) {
                    t.removeTileAtActualLocation(p);
                }
            }
        }
        for (Tetrimino t : passiveTetrimini) {
            if (t.getPosition().y > y) {
                t.setPosition(new Position(t.getPosition().x, t.getPosition().y - 1));
            }
        }
    }



    // TODO: Only rotate if there's room (if there's not, don't just rotate back - fix it so that you're in the right position)
    public void rotate() {

        this.activeTetrimino.rotate();
        for (Position p1 : activeTetrimino.getActualTileLocationsOnBoard()) {
            if (p1.y < 0 || p1.x < 0 || p1.x >= width || p1.y >= height) {
                this.activeTetrimino.rotate();
                this.activeTetrimino.rotate();
                this.activeTetrimino.rotate();
                return;
            }
            for (Position p2 : takenPositions()) {
                if (p1.x == p2.x && p1.y == p2.y) {
                    this.activeTetrimino.rotate();
                    this.activeTetrimino.rotate();
                    this.activeTetrimino.rotate();
                    return;
                }
            }
        }
    }

    private ArrayList<Position> takenPositions() {
        ArrayList<Position> takenPositions = new ArrayList<Position>();

        for (Tetrimino t : passiveTetrimini) {
            for (Position p : t.getActualTileLocationsOnBoard()) {
                takenPositions.add(p);
            }
        }

        return takenPositions;
    }

    public boolean nextTetrimino() {
        activeTetrimino = upcomingTetrimini.remove(0);

        int xPos = width/2 - 2;
        int yPos = height - 2;

        activeTetrimino.setPosition(new Position(xPos, yPos));

        upcomingTetrimini.add(generateRandomTetrimino());

        for (Position p1 : activeTetrimino.getActualTileLocationsOnBoard()) {
            for (Position p2 : takenPositions()) {
                if (p1.x == p2.x && p1.y == p2.y) {
                    return false;
                }
            }
        }
        return true;
    }


    private Tetrimino generateRandomTetrimino() {

        Random r = new Random();
        int  n;
        n = r.nextInt(7);

        switch(n) {
            case 0:
                return new Tetrimino(TetriminoType.I, ITetriminoImg);
            case 1:
                return new Tetrimino(TetriminoType.J, JTetriminoImg);
            case 2:
                return new Tetrimino(TetriminoType.L, LTetriminoImg);
            case 3:
                return new Tetrimino(TetriminoType.S, STetriminoImg);
            case 4:
                return new Tetrimino(TetriminoType.Z, ZTetriminoImg);
            case 5:
                return new Tetrimino(TetriminoType.T, TTetriminoImg);
            case 6:
                return new Tetrimino(TetriminoType.Square, SquareTetriminoImg);
        }


        return null;
    }

    public Tetrimino getActiveTetrimino() {
        return activeTetrimino;
    }

    public ArrayList<Tetrimino> getPassiveTetrimini() {
        return passiveTetrimini;
    }

    public Texture getImage() {
        return image;
    }
}
