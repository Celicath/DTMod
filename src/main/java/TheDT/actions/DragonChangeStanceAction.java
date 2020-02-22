package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.CannotChangeStancePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.AbstractStance;

public class DragonChangeStanceAction extends AbstractGameAction {
	private AbstractPlayer dragon;
	private AbstractStance newStance;

	public DragonChangeStanceAction(AbstractPlayer dragon, String stanceId) {
		this(dragon, AbstractStance.getStanceFromName(stanceId));
	}

	public DragonChangeStanceAction(AbstractPlayer dragon, AbstractStance newStance) {
		this.dragon = dragon;
		this.newStance = newStance;
		duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			if (dragon.hasPower(CannotChangeStancePower.POWER_ID)) {
				isDone = true;
				return;
			}
			if (newStance == null) {
				addToTop(new TalkAction(dragon, "No Stance", 1.5f, 1.5f));
				isDone = true;
				return;
			}

			AbstractStance oldStance = dragon.stance;
			if (!oldStance.ID.equals(newStance.ID)) {
				for (AbstractPower p : dragon.powers) {
					p.onChangeStance(oldStance, newStance);
				}

				for (AbstractRelic r : AbstractDungeon.player.relics) {
					r.onChangeStance(oldStance, newStance);
				}

				oldStance.onExitStance();
				dragon.stance = newStance;
				AbstractPlayer prevPlayer = AbstractDungeon.player;
				AbstractDungeon.player = dragon;
				newStance.onEnterStance();
				AbstractDungeon.player = prevPlayer;
				if (AbstractDungeon.actionManager.uniqueStancesThisCombat.containsKey(this.newStance.ID)) {
					int currentCount = AbstractDungeon.actionManager.uniqueStancesThisCombat.get(this.newStance.ID);
					AbstractDungeon.actionManager.uniqueStancesThisCombat.put(this.newStance.ID, currentCount + 1);
				} else {
					AbstractDungeon.actionManager.uniqueStancesThisCombat.put(this.newStance.ID, 1);
				}

				dragon.switchedStance();

				for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
					c.triggerExhaustedCardsOnStanceChange(this.newStance);
				}

				dragon.onStanceChange(newStance.ID);
			}

			AbstractDungeon.onModifyPower();
			if (Settings.FAST_MODE) {
				this.isDone = true;
				return;
			}
		}

		this.tickDuration();
	}
}
