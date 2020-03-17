package TheDT.modules;

import TheDT.DTModMain;
import TheDT.characters.DragonTamer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

public class TargetMarker {
	private Hitbox hb;
	private static UIStrings uiStrings = null;
	private static Texture targetTexture = null;
	private static Color color = null;
	public static String[] TEXT = null;

	private static final int WIDTH = 128;
	private static final int HEIGHT = 128;

	public float flashTimer;
	private Color flashColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);

	public TargetMarker() {
		hb = new Hitbox(WIDTH * Settings.scale * 1.2f, HEIGHT * Settings.scale * 1.2f);
		uiStrings = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("TargetMarker"));
		targetTexture = ImageMaster.loadImage(DTModMain.makePath("ui/Target.png"));
		TEXT = uiStrings.TEXT;
		color = new Color(0x8fcfffee);
		flashTimer = 0;
	}

	public void update() {
		hb.update();

		if (this.flashTimer != 0.0F) {
			this.flashTimer -= Gdx.graphics.getDeltaTime();
			if (this.flashTimer < 0.0F) {
				this.flashTimer = 0.0F;
			}
		}
	}

	public void flash() {
		int roll = MathUtils.random(0, 2);
		if (roll == 0) {
			CardCrawlGame.sound.play("BUFF_1");
		} else if (roll == 1) {
			CardCrawlGame.sound.play("BUFF_2");
		} else {
			CardCrawlGame.sound.play("BUFF_3");
		}
		this.flashTimer = 2.0f;
	}

	public void move(AbstractCreature creature) {
		hb.move(creature.hb.cX, creature.hb.cY + 200.0f * Settings.scale);
	}

	public void render(SpriteBatch sb) {
		sb.draw(
				targetTexture,
				hb.cX - WIDTH / 2.0f,
				hb.cY - HEIGHT / 2.0f,
				WIDTH / 2.0f,
				HEIGHT / 2.0f,
				WIDTH,
				HEIGHT,
				Settings.scale * 0.75f, Settings.scale * 0.75f, 0.0F, 0, 0, WIDTH, HEIGHT, false, false);
		FontHelper.renderFontCentered(
				sb,
				FontHelper.panelNameFont,
				"+" + Math.abs(DragonTamer.getAggro()),
				hb.cX,
				hb.cY + 70.0f * Settings.scale,
				color);

		if (hb.hovered) {
			renderTip(DragonTamer.isSolo(), DragonTamer.getAggro());
		}

		hb.render(sb);
		renderFlash(sb);
	}

	public void renderFlash(SpriteBatch sb) {
		float tmp = Interpolation.exp10In.apply(0.0F, 3.0F, this.flashTimer / 2.0F);
		sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		this.flashColor.a = this.flashTimer * 0.2F;// 1258
		sb.setColor(this.flashColor);

		sb.draw(targetTexture, hb.cX - WIDTH / 2.0f, hb.cY - HEIGHT / 2.0f, WIDTH / 2.0f, HEIGHT / 2.0f, WIDTH, HEIGHT,
				Settings.scale * (0.75f + tmp), Settings.scale * (0.75f + tmp), 0, 0, 0, WIDTH, HEIGHT, false, false);
		sb.draw(targetTexture, hb.cX - WIDTH / 2.0f, hb.cY - HEIGHT / 2.0f, WIDTH / 2.0f, HEIGHT / 2.0f, WIDTH, HEIGHT,
				Settings.scale * (0.75f + tmp * 0.66F), Settings.scale * (0.75f + tmp * 0.66F), 0, 0, 0, WIDTH, HEIGHT, false, false);
		sb.draw(targetTexture, hb.cX - WIDTH / 2.0f, hb.cY - HEIGHT / 2.0f, WIDTH / 2.0f, HEIGHT / 2.0f, WIDTH, HEIGHT,
				Settings.scale * (0.75f + tmp / 3.0F), Settings.scale * (0.75f + tmp / 3.0F), 0, 0, 0, WIDTH, HEIGHT, false, false);
		sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void renderTip(boolean solo, int aggro) {
		if (solo) {
			TipHelper.renderGenericTip((float) InputHelper.mX + 50.0F * Settings.scale, (float) InputHelper.mY, TEXT[0], TEXT[5]);
		} else {
			if (aggro > 0) {
				TipHelper.renderGenericTip((float) InputHelper.mX + 50.0F * Settings.scale, (float) InputHelper.mY, TEXT[0], TEXT[3] + aggro + TEXT[4]);
			} else {
				TipHelper.renderGenericTip((float) InputHelper.mX + 50.0F * Settings.scale, (float) InputHelper.mY, TEXT[0], TEXT[1] + -aggro + TEXT[2]);
			}
		}
	}
}
