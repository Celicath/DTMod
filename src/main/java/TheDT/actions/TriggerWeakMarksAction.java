package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class TriggerWeakMarksAction extends AbstractGameAction {
	private int ratio;

	public TriggerWeakMarksAction(int ratio) {
		this.ratio = ratio;
	}

	public void update() {
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (mo.hasPower(WeakPower.POWER_ID)) {
				addToBot(new LoseHPAction(mo, null, mo.getPower(WeakPower.POWER_ID).amount * ratio, AttackEffect.POISON));
			}
		}

		isDone = true;
	}
}
