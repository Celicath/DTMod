package TheDT.powers;

import TheDT.DTModMain;
import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;

public class ResonanceFormPower extends AbstractPower {
	public static final String RAW_ID = "ResonanceFormPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	public static final TextureAtlas.AtlasRegion IMG128 = new TextureAtlas.AtlasRegion(
			ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 128)), 0, 0, 84, 84);
	public static final TextureAtlas.AtlasRegion IMG48 = new TextureAtlas.AtlasRegion(
			ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 48)), 0, 0, 32, 32);

	public static ArrayList<AbstractPower> blackList = new ArrayList<>();
	public static boolean disabled = false;

	public ResonanceFormPower(AbstractCreature owner) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.region128 = IMG128;
		this.region48 = IMG48;
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

	@Override
	public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
		AbstractPlayer p = AbstractDungeon.player;
		Dragon dragon = AbstractDTCard.getLivingDragon();
		if (dragon == null || target != p && target != dragon || power.type == PowerType.DEBUFF) {
			return;
		}
		AbstractCreature otherTarget = target == p ? dragon : p;

		if (blackList.contains(power)) {
			blackList.remove(power);
		} else {
			AbstractPower newPower = null;
			switch (power.ID) {
				case StrengthPower.POWER_ID:
					newPower = new StrengthPower(otherTarget, power.amount);
					break;
				case DexterityPower.POWER_ID:
					newPower = new DexterityPower(otherTarget, power.amount);
			}
			if (newPower != null) {
				blackList.add(newPower);
				flash();
				addToTop(new ApplyPowerAction(otherTarget, otherTarget, newPower, newPower.amount));
			}
		}
	}

	@Override
	public int onHeal(int healAmount) {
		AbstractPlayer p = AbstractDungeon.player;
		Dragon dragon = AbstractDTCard.getLivingDragon();
		if (disabled || dragon == null || owner != p && owner != dragon) {
			return healAmount;
		}
		flash();
		AbstractCreature otherTarget = owner == p ? dragon : p;
		disabled = true;
		otherTarget.heal(healAmount);
		disabled = false;
		return healAmount;
	}

	@Override
	public void onGainedBlock(float blockAmount) {
		AbstractPlayer p = AbstractDungeon.player;
		Dragon dragon = AbstractDTCard.getLivingDragon();
		if (disabled || dragon == null || owner != p && owner != dragon) {
			return;
		}
		flash();
		AbstractCreature otherTarget = owner == p ? dragon : p;
		disabled = true;
		otherTarget.addBlock((int) blockAmount);
		disabled = false;
	}
}
