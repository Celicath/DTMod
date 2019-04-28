package TheDT.powers;

import TheDT.DTMod;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

public class BondingPower extends AbstractPower {
	public AbstractCreature source;

	private static final String RAW_ID = "BondingPower";
	public static final String POWER_ID = DTMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	public static final TextureAtlas.AtlasRegion IMG128 = new TextureAtlas.AtlasRegion(
			ImageMaster.loadImage(DTMod.GetPowerPath(RAW_ID, 128)), 0, 0, 84, 84);
	public static final TextureAtlas.AtlasRegion IMG48 = new TextureAtlas.AtlasRegion(
			ImageMaster.loadImage(DTMod.GetPowerPath(RAW_ID, 48)), 0, 0, 32, 32);

	public static final int THRESHOLD = 4;

	public BondingPower(AbstractCreature owner, AbstractCreature source, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.region128 = IMG128;
		this.region48 = IMG48;
		this.source = source;
	}

	@Override
	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + THRESHOLD + DESCRIPTIONS[1];
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer) {
			if (amount >= THRESHOLD) {
				this.flash();
				AbstractDungeon.effectList.add(new SpeechBubble(
						AbstractDungeon.player.dialogX,
						AbstractDungeon.player.dialogY,
						2.0f,
						"A Mythical help appears! (LEVEL " + amount + ")",
						true));
				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, amount));
				AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
			}
		}
	}
}
