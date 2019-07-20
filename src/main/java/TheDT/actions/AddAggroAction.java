package TheDT.actions;

import TheDT.DTMod;
import TheDT.characters.TheDT;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

public class AddAggroAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(DTMod.makeID("Aggro"));
	public static final String[] TEXT = uiStrings.TEXT;

	public AddAggroAction(AbstractCreature creature, int delta) {
		setValues(creature, creature, delta);
		actionType = AbstractGameAction.ActionType.WAIT;
		duration = Settings.ACTION_DUR_FAST;
		target = creature;
	}

	public void update() {
		this.isDone = true;
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			return;
		}
		if (amount != 0) {
			PowerBuffEffect effect = new PowerBuffEffect(target.hb.cX - target.animX, target.hb.cY + target.hb.height / 2.0F, TEXT[0] + amount + TEXT[1]);
			ReflectionHacks.setPrivate(effect, PowerBuffEffect.class, "targetColor", new Color(0.7f, 0.75f, 0.7f, 1.0f));
			AbstractDungeon.effectList.add(effect);
			((TheDT) AbstractDungeon.player).addAggro(target == AbstractDungeon.player ? -amount : amount);
		}
	}
}
