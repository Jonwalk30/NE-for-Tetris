package com.mygdx.tetris;

import com.badlogic.gdx.graphics.Texture;
import javafx.geometry.Pos;

import java.util.ArrayList;

public class Tetrimino {

    private TetriminoType type;
    private Texture tileImage;
    private ArrayList<Position> tileLocations;
    private Position pos = new Position(0,0);
    private Direction direction;


    public Tetrimino(TetriminoType type, Texture tileImage) {

        this.type = type;
        this.tileImage = tileImage;
        this.direction = Direction.UP;
        this.tileLocations = new ArrayList<Position>();

        switch(type) {
            case I:
                tileLocations = defineI();
                break;
            case J:
                tileLocations = defineJ();
                break;
            case L:
                tileLocations = defineL();
                break;
            case S:
                tileLocations = defineS();
                break;
            case Z:
                tileLocations = defineZ();
                break;
            case T:
                tileLocations = defineT();
                break;
            case Square:
                tileLocations = defineSquare();
                break;
            default:
                tileLocations.add(new Position(1,0));
                break;
        }

    }

    public Tetrimino(Texture tileImage) {

        this.type = null;
        this.tileImage = tileImage;
        this.direction = Direction.UP;
        this.tileLocations = new ArrayList<Position>();

        tileLocations.add(new Position(0,0));

    }

    public Direction getDirection() {
        return this.direction;
    }
    public void rotate() {
        switch(this.direction) {
            case UP:
                this.direction = Direction.RIGHT;
                break;
            case RIGHT:
                this.direction = Direction.DOWN;
                break;
            case DOWN:
                this.direction = Direction.LEFT;
                break;
            case LEFT:
                this.direction = Direction.UP;
                break;
            default:
                this.direction = Direction.UP;
                break;
        }
    }

    public Position getPosition() {
        return this.pos;
    }

    public void setPosition(Position newPos) {
        this.pos = newPos;
    }

    public Texture getTileImage() {
        return this.tileImage;
    }

    public TetriminoType getType() {
        return this.type;
    }

    public ArrayList<Position> getTileLocations() {
        return tileLocations;
    }

    // TODO: Comment! any issues with rotations will likely start here
    public ArrayList<Position> getActualTileLocations() {
        ArrayList<Position> tempTileLocations = new ArrayList<Position>();

        switch(this.direction) {
            case UP:
                tempTileLocations = this.tileLocations;
                break;
            case RIGHT:
                for(Position p : this.tileLocations) {
                    Position newP = new Position(1+p.y, 1-p.x);
                    tempTileLocations.add(newP);
                }
                break;
            case DOWN:
                for(Position p : this.tileLocations) {
                    Position newP = new Position(2-p.x, -p.y);
                    tempTileLocations.add(newP);
                }
                break;
            case LEFT:
                for(Position p : this.tileLocations) {
                    Position newP = new Position(1-p.y, p.x-1);
                    tempTileLocations.add(newP);
                }
                break;
        }
        return tempTileLocations;
    }

    public ArrayList<Position> getActualTileLocationsOnBoard() {
        ArrayList<Position> tempTileLocations = new ArrayList<Position>();
        for (Position p : getActualTileLocations()) {
            tempTileLocations.add(new Position(pos.x + p.x, pos.y + p.y));
        }
        return tempTileLocations;
    }

    private ArrayList<Position> defineI() {
        ArrayList<Position> tempTileLocations = new ArrayList<Position>();
        tempTileLocations.add(new Position(0,0));
        tempTileLocations.add(new Position(1,0));
        tempTileLocations.add(new Position(2,0));
        tempTileLocations.add(new Position(3,0));
        return tempTileLocations;
    }
    private ArrayList<Position> defineJ() {
        ArrayList<Position> tempTileLocations = new ArrayList<Position>();
        tempTileLocations.add(new Position(0,0));
        tempTileLocations.add(new Position(1,0));
        tempTileLocations.add(new Position(2,0));
        tempTileLocations.add(new Position(0,1));
        return tempTileLocations;
    }
    private ArrayList<Position> defineL() {
        ArrayList<Position> tempTileLocations = new ArrayList<Position>();
        tempTileLocations.add(new Position(0,0));
        tempTileLocations.add(new Position(1,0));
        tempTileLocations.add(new Position(2,0));
        tempTileLocations.add(new Position(2,1));
        return tempTileLocations;
    }
    private ArrayList<Position> defineS() {
        ArrayList<Position> tempTileLocations = new ArrayList<Position>();
        tempTileLocations.add(new Position(0,0));
        tempTileLocations.add(new Position(1,0));
        tempTileLocations.add(new Position(1,1));
        tempTileLocations.add(new Position(2,1));
        return tempTileLocations;
    }
    private ArrayList<Position> defineZ() {
        ArrayList<Position> tempTileLocations = new ArrayList<Position>();
        tempTileLocations.add(new Position(1,0));
        tempTileLocations.add(new Position(2,0));
        tempTileLocations.add(new Position(0,1));
        tempTileLocations.add(new Position(1,1));
        return tempTileLocations;
    }
    private ArrayList<Position> defineT() {
        ArrayList<Position> tempTileLocations = new ArrayList<Position>();
        tempTileLocations.add(new Position(0,0));
        tempTileLocations.add(new Position(1,0));
        tempTileLocations.add(new Position(2,0));
        tempTileLocations.add(new Position(1,1));
        return tempTileLocations;
    }
    private ArrayList<Position> defineSquare() {
        ArrayList<Position> tempTileLocations = new ArrayList<Position>();
        tempTileLocations.add(new Position(1,0));
        tempTileLocations.add(new Position(2,0));
        tempTileLocations.add(new Position(1,1));
        tempTileLocations.add(new Position(2,1));
        return tempTileLocations;
    }

    public void moveLeft() {
        this.pos.x = this.pos.x - 1;
    }

    public void moveRight() {
        this.pos.x = this.pos.x + 1;
    }

    public void moveDown() {
        this.pos.y = this.pos.y - 1;
    }

    public Position actualToRecorded(Position p) {

        Position returnPos = new Position(0, 0);

        p.x = p.x - pos.x;
        p.y = p.y - pos.y;

        switch(this.direction) {
            case UP:
                returnPos = p;
                break;
            case RIGHT:
                returnPos = new Position(1-p.y, p.x-1);
                break;
            case DOWN:
                returnPos = new Position(2-p.x, -p.y);
                break;
            case LEFT:
                returnPos = new Position(1+p.y, 1-p.x);
                break;
        }

        return returnPos;

    }

    public Position recordedToActual(Position p) {

        Position returnPos = new Position(0, 0);

        switch(this.direction) {
            case UP:
                returnPos = p;
                break;
            case RIGHT:
                returnPos = new Position(1+p.y, 1-p.x);
                break;
            case DOWN:
                returnPos = new Position(2-p.x, -p.y);
                break;
            case LEFT:
                returnPos = new Position(1-p.y, p.x-1);
                break;
        }
        returnPos.x = returnPos.x + pos.x;
        returnPos.y = returnPos.y + pos.y;

        return returnPos;
    }


    public void removeTileAtActualLocation(Position p) {
        Position posToRemove = actualToRecorded(p);

        for (Position l : this.tileLocations) {
            if (l.x == posToRemove.x && l.y == posToRemove.y) {
                tileLocations.remove(l);
                break;
            }
        }
    }

}
