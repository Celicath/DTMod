package TheDT.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class UpgradeAllCardEffect extends AbstractGameEffect {
	public static final float EFFECT_DUR = 0.2f;

	public UpgradeAllCardEffect() {
		duration = startingDuration = EFFECT_DUR;
	}

	@Override
	public void update() {
		duration -= Gdx.graphics.getDeltaTime();
		if (duration < 0.0F) {
			isDone = true;
			for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
				int effectCount = 0;
				if (c.canUpgrade()) {
					effectCount++;
					if (effectCount <= 20) {
						AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
						float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
						float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;

						AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
						AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect(x, y));
					}
					c.upgrade();
				}
			}
		}
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
	}

	@Override
	public void dispose() {

	}
}
