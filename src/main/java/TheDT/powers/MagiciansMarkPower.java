package TheDT.powers;

import TheDT.DTModMain;
import TheDT.characters.DragonTamer;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MagiciansMarkPower extends AbstractPower {
	public static final String RAW_ID = "MagiciansMarkPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	public static final TextureAtlas.AtlasRegion IMG128 = new TextureAtlas.AtlasRegion(
		ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 128)), 0, 0, 84, 84);
	public static final TextureAtlas.AtlasRegion IMG48 = new TextureAtlas.AtlasRegion(
		ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 48)), 0, 0, 32, 32);

	boolean alreadyRemoving = false;

	public MagiciansMarkPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.DEBUFF;
		this.isTurnBased = false;
		this.region128 = IMG128;
		this.region48 = IMG48;
	}

	public int onAttacked(DamageInfo info, int damageAmount) {
		if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner == DragonTamer.getLivingDragon()) {
			flash();
			AbstractPlayer p = AbstractDungeon.player;
			MagiciansMarkPower thisPower = this;
			addToBot(new AbstractGameAction() {
				@Override
				public void update() {
					if (thisPower.amount > 1) {
						reducePower(1);
						updateDescription();
						AbstractDungeon.onModifyPower();
						addToTop(new ApplyPowerAction(p, p, new BondingPower(p, p, 1), 1));
					} else {
						if (!alreadyRemoving) {
							alreadyRemoving = true;
							addToTop(new RemoveSpecificPowerAction(owner, owner, thisPower));
							addToTop(new ApplyPowerAction(p, p, new BondingPower(p, p, 1), 1));
						}
					}
					isDone = true;
				}
			});
		}

		return damageAmount;
	}

	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
	}
}
