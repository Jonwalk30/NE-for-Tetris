package com.mygdx.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class Tetris extends ApplicationAdapter {

	private Texture LTetriminoImg;
	private Texture JTetriminoImg;
	private Texture ITetriminoImg;
	private Texture STetriminoImg;
	private Texture ZTetriminoImg;
	private Texture TTetriminoImg;
	private Texture SquareTetriminoImg;
	private Texture BoardImg;

	private ArrayList<Sprite> tetriminoSprites = new ArrayList<Sprite>();
	private Sprite boardSprite;

	private Board board;

	private long startTime = 0;
	private long timeBetweenSteps = 1000000000;

	SpriteBatch batch;
	
	@Override
	public void create () {

		Gdx.graphics.setWindowedMode(300,575);

		batch = new SpriteBatch();

		LTetriminoImg = new Texture("Red Tile.png");
		JTetriminoImg = new Texture("Blue Tile.png");
		ITetriminoImg = new Texture("Aqua Tile.png");
		STetriminoImg = new Texture("Orange Tile.png");
		ZTetriminoImg = new Texture("Purple Tile.png");
		TTetriminoImg = new Texture("Green Tile.png");
		SquareTetriminoImg = new Texture("Yellow Tile.png");
		BoardImg = new Texture("Board.png");

		startTime = TimeUtils.nanoTime();

		board = new Board(10, 20, BoardImg,
				ITetriminoImg,
				JTetriminoImg,
				LTetriminoImg,
				STetriminoImg,
				ZTetriminoImg,
				TTetriminoImg,
				SquareTetriminoImg);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		detectMovements();

		recreateAllSprites();

		batch.begin();

		boardSprite.draw(batch);
		for (Sprite s : tetriminoSprites) {
			s.draw(batch);
		}

		batch.end();

		if (TimeUtils.timeSinceNanos(startTime) > timeBetweenSteps/1.5) {
            if (timeBetweenSteps > 100000000) {
				timeBetweenSteps = timeBetweenSteps - 1000000;
            }
			// TODO: A more advanced menu system needed
			if(!board.step()) {
				dispose();
				System.exit(0);
			}

			startTime = TimeUtils.nanoTime();
		}
		for (int y : board.fullLines()) {
			board.removeLine(y);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		LTetriminoImg.dispose();
		JTetriminoImg.dispose();
		ITetriminoImg.dispose();
		STetriminoImg.dispose();
		ZTetriminoImg.dispose();
		TTetriminoImg.dispose();
		SquareTetriminoImg.dispose();
		BoardImg.dispose();
	}

	private void detectMovements() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			//if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) //for different keyboard movement types???
			board.moveLeft();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			board.moveRight();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			board.rotate();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			if(!board.step()) {
				dispose();
				System.exit(0);
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			create();
		}
	}

	public void recreateAllSprites() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		float squareLength = w / 12;

		tetriminoSprites = new ArrayList<Sprite>();

		for (Tetrimino t : board.getPassiveTetrimini()) {
			for (Position p : t.getActualTileLocationsOnBoard()) {
				Sprite sprite = new Sprite(t.getTileImage());
				sprite.setSize(squareLength, squareLength);
				float xPos = squareLength + (squareLength * p.x);
				float yPos = (2 * squareLength) + (squareLength * p.y);
				sprite.setPosition(xPos, yPos);
				tetriminoSprites.add(sprite);
			}
		}

		for (Position p : board.getActiveTetrimino().getActualTileLocationsOnBoard()) {
			Sprite sprite = new Sprite(board.getActiveTetrimino().getTileImage());
			sprite.setSize(squareLength, squareLength);
			float xPos = squareLength + (squareLength * p.x);
			float yPos = (2 * squareLength) + (squareLength * p.y);
			sprite.setPosition(xPos, yPos);
			tetriminoSprites.add(sprite);
		}

		Sprite sprite = new Sprite(board.getImage());
		sprite.setSize(w, h);
		float xPos = 0; // w/2
		float yPos = 0; // h/2
		sprite.setPosition(xPos, yPos);
		boardSprite = sprite;

	}

}
