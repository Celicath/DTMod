package TheDT.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class AbstractChannelingPower extends TwoAmountPower implements NonStackablePower {
	public AbstractChannelingPower() {

	}

	@Override
	public void atStartOfTurn() {
		this.addToBot(new ReducePowerAction(owner, owner, this, 1));
		if (amount == 1) {
			onActivate();
		}
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != owner) {
			flash();
			addToTop(new RemoveSpecificPowerAction(owner, owner, this));
		}
		return damageAmount;
	}

	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		if (target != owner && info.type == DamageInfo.DamageType.NORMAL) {
			flash();
			addToTop(new RemoveSpecificPowerAction(owner, owner, this));
		}
	}

	public abstract void onActivate();
}
