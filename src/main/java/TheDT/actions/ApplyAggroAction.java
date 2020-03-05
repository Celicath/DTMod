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

public class ApplyAggroAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(DTMod.makeID("Aggro"));
	public static final String[] TEXT = uiStrings.TEXT;

	public ApplyAggroAction() {
		actionType = AbstractGameAction.ActionType.WAIT;
		duration = Settings.ACTION_DUR_FASTER;
	}

	public void update() {
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead() || !(AbstractDungeon.player instanceof TheDT) || (DTMod.yourAggro == 0 && DTMod.dragonAggro == 0)) {
			this.isDone = true;
			return;
		}
		if (DTMod.yourAggro != 0) {
			addAggroChangeEffect(AbstractDungeon.player, DTMod.yourAggro);
		}
		if (DTMod.dragonAggro != 0) {
			addAggroChangeEffect(((TheDT) AbstractDungeon.player).dragon, DTMod.dragonAggro);
		}
		if (DTMod.dragonAggro - DTMod.yourAggro != 0) {
			((TheDT) AbstractDungeon.player).addAggro(DTMod.dragonAggro - DTMod.yourAggro);
		}
		DTMod.dragonAggro = 0;
		DTMod.yourAggro = 0;
		tickDuration();
	}

	void addAggroChangeEffect(AbstractCreature target, int amount) {
		PowerBuffEffect effect = new PowerBuffEffect(target.hb.cX - target.animX, target.hb.cY + target.hb.height / 2.0F, TEXT[0] + amount + TEXT[1]);
		ReflectionHacks.setPrivate(effect, PowerBuffEffect.class, "targetColor", new Color(0.7f, 0.75f, 0.7f, 1.0f));
		AbstractDungeon.effectList.add(effect);
	}
}