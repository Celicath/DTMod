package TheDT.powers;

import TheDT.DTModMain;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LastStandPower extends AbstractPower {
	public AbstractCreature source;

	public static final String RAW_ID = "LastStandPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	public static final TextureAtlas.AtlasRegion IMG128 = new TextureAtlas.AtlasRegion(
		ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 128)), 0, 0, 84, 84);
	public static final TextureAtlas.AtlasRegion IMG48 = new TextureAtlas.AtlasRegion(
		ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 48)), 0, 0, 32, 32);

	public LastStandPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.region128 = IMG128;
		this.region48 = IMG48;
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
		}
	}

	public void atEndOfRound() {
		if (amount == 0) {
			addToBot(new RemoveSpecificPowerAction(owner, owner, LastStandPower.POWER_ID));
		} else {
			addToBot(new ReducePowerAction(owner, owner, LastStandPower.POWER_ID, 1));
		}
	}

	@Override
	public float atDamageReceive(float damage, DamageInfo.DamageType type) {
		if (type == DamageInfo.DamageType.NORMAL) {
			return damage * 0.5f;
		} else {
			return damage;
		}
	}
}
