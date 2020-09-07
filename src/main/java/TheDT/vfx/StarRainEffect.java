package TheDT.vfx;

import TheDT.DTModMain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class StarRainEffect extends AbstractGameEffect {
	public static Texture starRainTexture = ImageMaster.loadImage(DTModMain.GetVfxPath("StarRain"));
	private float x;
	private float y;
	private static final float DUR = 0.5F;
	private boolean playedSound = false;

	public StarRainEffect(float x, float y) {
		this.x = x + MathUtils.random(-50.0F, 50.0F) * Settings.scale;
		this.y = y + MathUtils.random(-25.0F, 25.0F) * Settings.scale;
		this.startingDuration = DUR;
		this.duration = DUR;
		this.scale = Settings.scale * 0.4f;
		this.rotation = MathUtils.random(-5.0F, 5.0F);
		this.color = Color.WHITE.cpy();
	}

	private void playSfX() {
		CardCrawlGame.sound.playV("RELIC_DROP_MAGICAL", 1.5F);
	}

	public void update() {
		if (!this.playedSound) {
			this.playSfX();
			this.playedSound = true;
		}

		this.duration -= Gdx.graphics.getDeltaTime();
		if (this.duration < 0.0F) {
			this.isDone = true;
		}

		if (this.duration > DUR / 2.0f) {
			this.color.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - DUR / 2.0f) / (DUR / 2.0F));
		} else {
			this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / (DUR / 2.0F));
		}

		this.scale = Interpolation.bounceIn.apply(Settings.scale * 0.4F, Settings.scale * 1.2F, this.duration / DUR);
	}

	public void render(SpriteBatch sb) {
		sb.setColor(this.color);
		sb.draw(starRainTexture,
				x - starRainTexture.getWidth() / 2.0F,
				y - starRainTexture.getHeight() / 8.0F,
				starRainTexture.getWidth() / 2.0f,
				starRainTexture.getHeight() / 8.0f,
				starRainTexture.getWidth(),
				starRainTexture.getHeight(),
				scale * 1.5f,
				scale,
				rotation,
				0,
				0,
				starRainTexture.getWidth(),
				starRainTexture.getHeight(),
				false, false);
		sb.setBlendFunction(770, 1);
		sb.draw(starRainTexture,
				x - starRainTexture.getWidth() / 2.0F,
				y - starRainTexture.getHeight() / 8.0F,
				starRainTexture.getWidth() / 2.0f,
				starRainTexture.getHeight() / 8.0f,
				starRainTexture.getWidth(),
				starRainTexture.getHeight(),
				scale * 0.75f,
				scale * 0.75f,
				rotation,
				0,
				0,
				starRainTexture.getWidth(),
				starRainTexture.getHeight(),
				false, false);
		sb.setBlendFunction(770, 771);
	}

	public void dispose() {
	}
}
