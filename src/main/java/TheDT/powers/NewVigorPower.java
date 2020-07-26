package TheDT.powers;

import TheDT.DTModMain;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NewVigorPower extends AbstractPower {
	public static final String RAW_ID = "Vigor";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Vigor");

	public NewVigorPower(AbstractCreature owner, int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.loadRegion("vigor");
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
	}

	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
	}

	public float atDamageGive(float damage, DamageInfo.DamageType type) {
		return type == DamageInfo.DamageType.NORMAL ? damage + amount : damage;
	}

	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card.type == AbstractCard.CardType.ATTACK) {
			this.flash();
			this.addToBot(new ReducePowerAction(owner, owner, this, amount));
		}
	}
}
