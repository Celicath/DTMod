package TheDT.powers;

import TheDT.DTModMain;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class DeepFocusPower extends AbstractChannelingPower {
	public static final String RAW_ID = "DeepFocusPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public DeepFocusPower(AbstractCreature owner, int turn, int strength) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = turn;
		this.amount2 = strength;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.loadRegion("rupture");
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0] + DESCRIPTIONS[3] + amount2 + DESCRIPTIONS[4];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3] + amount2 + DESCRIPTIONS[4];
		}
	}

	@Override
	public void onActivate() {
		addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount2), amount2));
	}
}
