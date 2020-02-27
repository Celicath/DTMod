package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;

public class LowKickAction extends AbstractGameAction {
	private DamageInfo info;

	public LowKickAction(AbstractCreature target, DamageInfo info) {
		this.actionType = ActionType.DAMAGE;
		this.target = target;
		this.info = info;
	}

	public void update() {
		if (target != null && target.hasPower(WeakPower.POWER_ID)) {
			addToTop(new DrawCardAction(AbstractDungeon.player, target.getPower(WeakPower.POWER_ID).amount));
		}

		addToTop(new DamageAction(this.target, this.info, AttackEffect.BLUNT_HEAVY));
		isDone = true;
	}
}
