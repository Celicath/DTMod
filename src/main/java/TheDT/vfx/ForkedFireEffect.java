package TheDT.vfx;

import TheDT.DTModMain;
import TheDT.actions.ForkedFireDamageAction;
import TheDT.characters.Dragon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class ForkedFireEffect extends AbstractGameEffect {
	public static final float EFFECT_DUR = 0.6f;
	private boolean init = false;
	public static Texture fireballTexture = ImageMaster.loadImage(DTModMain.GetVfxPath("fireball"));

	private Dragon d;
	private float originX, originY;

	private ArrayList<Vector2> dest;
	private ArrayList<Vector2> curPos;
	private float curA;
	ArrayList<AbstractMonster> targets;
	ArrayList<Integer> targets_damage;

	public ForkedFireEffect(Dragon d, float starting_delay, ArrayList<AbstractMonster> targets, ArrayList<Integer> targets_damage) {
		this.duration = starting_delay + EFFECT_DUR;

		this.d = d;

		originX = d.hb.cX;
		originY = d.hb.cY + 100.0f * Settings.scale;

		dest = new ArrayList<>();
		curPos = new ArrayList<>();

		this.targets = targets;
		this.targets_damage = targets_damage;
	}

	void addDest(Hitbox hb) {
		dest.add(new Vector2(hb.cX, hb.cY));
		curPos.add(new Vector2(originX, originY));
	}

	@Override
	public void update() {
		this.duration -= Gdx.graphics.getDeltaTime();
		if (duration > EFFECT_DUR) {
			return;
		}

		if (!init) {
			CardCrawlGame.sound.playV("ATTACK_FIRE", 0.8f);
			for (AbstractMonster m : targets) {
				addDest(m.hb);
			}
			curA = 0;
			init = true;
		}

		float t = (EFFECT_DUR - this.duration) / EFFECT_DUR;
		this.scale = 1.0f + 0.5f * t * Settings.scale;

		if (t > 1) {
			this.isDone = true;
		} else {
			float a = t * t;

			for (int i = 0; i < dest.size(); i++) {
				float x, y;
				float p = (curA > 0.999f ? 1 : (a - curA) / (1 - curA));

				Vector2 vc = curPos.get(i);
				Vector2 v2 = dest.get(i);
				x = Interpolation.linear.apply(vc.x, v2.x, p);
				y = Interpolation.linear.apply(vc.y, v2.y, p);

				curPos.set(i, new Vector2(x, y));
			}
			curA = a;
		}

		if (isDone) {
			ForkedFireDamageAction.target_queue.add(targets);
			ForkedFireDamageAction.target_damage_queue.add(targets_damage);
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE.cpy());
		for (int i = 0; i < curPos.size(); i++) {
			this.renderImg(sb, fireballTexture, rotation, i);
		}
	}

	private void renderImg(SpriteBatch sb, Texture img, float rotation, int loc) {
		Vector2 v = curPos.get(loc);
		sb.draw(img,
				v.x - img.getWidth() * scale / 4.0f,
				v.y - img.getHeight() * scale / 4.0f,
				img.getWidth() * scale / 2.0f,
				img.getHeight() * scale / 2.0f,
				0,
				0,
				img.getWidth(),
				img.getHeight(),
				false, false);
	}

	@Override
	public void dispose() {
	}
}
