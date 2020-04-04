package TheDT.powers;

import TheDT.DTModMain;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TauntPower extends AbstractPower {
	public static final String RAW_ID = "TauntPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public boolean targetIsDragon;

	public TauntPower(AbstractCreature owner, boolean targetIsDragon, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.targetIsDragon = targetIsDragon;
		this.updateDescription();
		this.type = PowerType.DEBUFF;
		this.isTurnBased = true;
		this.loadRegion("lockon");
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
		}
		if (targetIsDragon) {
			description += DESCRIPTIONS[5] + DESCRIPTIONS[6];
		} else {
			description += DESCRIPTIONS[4] + DESCRIPTIONS[6];
		}
	}

	@Override
	public void atEndOfRound() {
		if (amount == 0) {
			addToBot(new RemoveSpecificPowerAction(owner, owner, TauntPower.POWER_ID));
		} else {
			addToBot(new ReducePowerAction(owner, owner, TauntPower.POWER_ID, 1));
		}
	}
}
