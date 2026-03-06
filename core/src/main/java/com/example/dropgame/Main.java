package com.example.dropgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {

    Texture dropTexture;
    Texture backgroundTexture;
    Texture bucketTexture;
    Sound dropSound;
    Music backgrounMusic;
    FitViewport viewport;
    SpriteBatch spriteBatch;
    Texture whitePixel;

    Sprite bucketSprite;
    Vector2 touchPosition;
    Array<Sprite> dropSprites;
    float dropTimer;
    int score;
    BitmapFont font;
    int lives;
    float dropSpeed;
    boolean gameOver;
    @Override
    public void create() {
        // Prepare your application here.
        backgroundTexture = new Texture("background.png");
        dropTexture = new Texture("drop.png");
        bucketTexture = new Texture("bucket.png");
        dropSound  = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        backgrounMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8,5);
        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1,1);

        touchPosition = new Vector2();
        dropSprites = new Array<>();

        score = 0;
        font = new BitmapFont();
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.015f); // Un poco más grande
        font.setColor(Color.YELLOW);

        lives = 3;
        dropSpeed = 2f;
        gameOver = false;

        // Crear una textura blanca de 1x1 para fondos
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
        viewport.update(width,height,true);
    }

    @Override
    public void render() {
        // Draw your application here.
        input();
        if (!gameOver) {
            logic();
        }
        draw();
    }

    public void logic() {
        float worldWidth = viewport.getWorldWidth();

        float bucketWidth = bucketSprite.getWidth();

        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(),0,worldWidth-bucketWidth));

        float delta = Gdx.graphics.getDeltaTime();

        //Bucle for echa
        //for(Sprite dropSprite: dropSprites)
        //{
        //    dropSprite.translateY(-2f*delta);
        //}
        for (int i =dropSprites.size-1;i>=0;i--)
        {
            Sprite dropSprite = dropSprites.get(i);
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-dropSpeed * delta);

            if (dropSprite.getY() < -dropHeight) {
                dropSprites.removeIndex(i);
                lives--;
                if (lives <= 0) gameOver = true;
            } else if (dropSprite.getBoundingRectangle().overlaps(bucketSprite.getBoundingRectangle())) {
                score++;
                dropSpeed += 0.1f; // Aumentar velocidad
                dropSound.play();
                dropSprites.removeIndex(i);
            }
        }

        dropTimer += delta;
        if (dropTimer>0.75f)
        {
            dropTimer = 0;
            createDroplet();
        }


    }

    public void input() {

        float speed = 4;
        float delta = Gdx.graphics.getDeltaTime();

        if (gameOver) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
                resetGame();
            }
            return;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            bucketSprite.translateX(speed*delta);

        }else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bucketSprite.translateX(-speed*delta);

        if(Gdx.input.isTouched())
        {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPosition);
            bucketSprite.setCenterX(touchPosition.x);
        }
    }
    public void draw() {

        ScreenUtils.clear(Color.BLUE);

        viewport.apply();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTexture,0,0,worldWidth,worldHeight);

        // Fondos para el marcador
        spriteBatch.setColor(0, 0, 0, 0.5f); // Negro semi-transparente
        spriteBatch.draw(whitePixel, 0.3f, 4.4f, 2.2f, 0.5f); // Fondo Drops
        spriteBatch.draw(whitePixel, 6.3f, 4.4f, 1.4f, 0.5f); // Fondo Vidas
        spriteBatch.setColor(Color.WHITE); // Resetear color

        // DIBUJAR EL BUCKET CORRECTAMENTE
        bucketSprite.draw(spriteBatch);

        // dibujar gotas
         for(Sprite dropSprite: dropSprites)
            dropSprite.draw(spriteBatch);

        // Dibujar sombra (negra)
        font.setColor(Color.BLACK);
        font.draw(spriteBatch, "Gotas: " + score, 0.51f, 4.79f);
        // Dibujar texto principal (amarillo)
        font.setColor(Color.YELLOW);
        font.draw(spriteBatch, "Gotas: " + score, 0.5f, 4.8f);

        // Vidas con color condicional
        if (lives > 1) font.setColor(Color.GREEN);
        else font.setColor(Color.RED);
        font.draw(spriteBatch, "Vidas: " + lives, 6.5f, 4.8f);

        if (gameOver) {
            font.getData().setScale(0.03f); // Muy grande para Game Over
            font.setColor(Color.BLACK);
            font.draw(spriteBatch, "GAME OVER", 2.2f, 2.78f);
            font.setColor(Color.RED);
            font.draw(spriteBatch, "GAME OVER", 2.0f, 2.8f);

            font.getData().setScale(0.015f);
            font.setColor(Color.WHITE);
            font.draw(spriteBatch, "Pulsa ESPACIO para reintentar", 1.5f, 2.0f);
        }

        spriteBatch.end();
    }

    private void resetGame() {
        score = 0;
        lives = 3;
        dropSpeed = 2f;
        gameOver = false;
        dropSprites.clear();
        bucketSprite.setX(4 - bucketSprite.getWidth() / 2f);
    }

    public void createDroplet()
    {
        float dropWidth = 1;
        float dropHeight =1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth,dropHeight);
        dropSprite.setX(MathUtils.random(0f,worldWidth-dropWidth));
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
        backgroundTexture.dispose();
        dropTexture.dispose();
        bucketTexture.dispose();
        dropSound.dispose();
        backgrounMusic.dispose();
        spriteBatch.dispose();
        font.dispose();
        whitePixel.dispose();
    }
}
