package TheDT.powers;

import TheDT.DTModMain;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ShadowImagePower extends AbstractPower {
	public static final String RAW_ID = "ShadowImagePower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	public static final TextureAtlas.AtlasRegion IMG128 = new TextureAtlas.AtlasRegion(
		ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 128)), 0, 0, 88, 52);
	public static final TextureAtlas.AtlasRegion IMG48 = new TextureAtlas.AtlasRegion(
		ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 48)), 0, 0, 34, 20);

	private static AbstractGameAction prevAction = null;

	public ShadowImagePower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.region128 = IMG128;
		this.region48 = IMG48;
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		if (prevAction != AbstractDungeon.actionManager.currentAction) {
			if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner == owner) {
				prevAction = AbstractDungeon.actionManager.currentAction;
				flash();
				addToTop(new GainBlockAction(owner, amount, Settings.FAST_MODE));
			}
		}
	}
}
