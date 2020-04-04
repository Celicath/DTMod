package TheDT.powers;

import TheDT.DTModMain;
import TheDT.Interfaces.SwitchPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class ArbitrationPower extends AbstractPower implements SwitchPower {
	public static final String RAW_ID = "ArbitrationPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public ArbitrationPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.loadRegion("wave_of_the_hand");
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	@Override
	public void onSwitch() {
		flash();
		for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
			addToBot(new ApplyPowerAction(m, owner, new WeakPower(m, amount, false), amount, true, AbstractGameAction.AttackEffect.NONE));
		}
	}
}
