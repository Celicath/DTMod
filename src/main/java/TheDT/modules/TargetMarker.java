package TheDT.modules;

import TheDT.DTMod;
import TheDT.characters.TheDT;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import static the_gatherer.GathererMod.potionSackPopupFlipped;

public class TargetMarker {
	private Hitbox hb;
	private static UIStrings uiStrings = null;
	private static Texture targetTexture = null;
	private static Color color = null;
	public static String[] TEXT = null;


	private static final int WIDTH = 128;
	private static final int HEIGHT = 128;

	public TargetMarker() {
		hb = new Hitbox(WIDTH * Settings.scale * 1.2f, HEIGHT * Settings.scale * 1.2f);
		uiStrings = CardCrawlGame.languagePack.getUIString(DTMod.makeID("TargetMarker"));
		targetTexture = ImageMaster.loadImage(DTMod.makePath("ui/Target.png"));
		TEXT = uiStrings.TEXT;
		color = new Color(0x8fcfffee);
	}

	public void update() {
		hb.update();
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
				"+" + Math.abs(TheDT.getAggro()),
				hb.cX,
				hb.cY + 70.0f * Settings.scale,
				color);

		if (hb.hovered) {
			renderTip(TheDT.isSolo(), TheDT.getAggro());
		}

		hb.render(sb);
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
