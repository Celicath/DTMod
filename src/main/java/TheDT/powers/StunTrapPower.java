package TheDT.powers;

import TheDT.DTModMain;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class StunTrapPower extends AbstractChannelingPower {
	public static final String RAW_ID = "StunTrapPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	AbstractMonster target;

	public StunTrapPower(AbstractMonster target, AbstractCreature owner, int turn) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.target = target;
		this.amount = turn;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.loadRegion("channel");
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0] + target.name + DESCRIPTIONS[3];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + target.name + DESCRIPTIONS[3];
		}
	}

	@Override
	public void onActivate() {
		addToBot(new VFXAction(new WeightyImpactEffect(target.hb.cX, target.hb.cY)));
		addToBot(new WaitAction(0.4F));
		addToBot(new ApplyPowerAction(target, owner, new StunMonsterPower(target, 1), 1));
	}
}