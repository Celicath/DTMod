package TheDT.powers;

import TheDT.DTModMain;
import TheDT.Interfaces.OnBondingActivateCard;
import TheDT.optionCards.BondingBonus;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class BondingPower extends AbstractPower {
	public AbstractCreature source;

	public static final String RAW_ID = "BondingPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	public static final TextureAtlas.AtlasRegion IMG128 = new TextureAtlas.AtlasRegion(
			ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 128)), 0, 0, 84, 84);
	public static final TextureAtlas.AtlasRegion IMG48 = new TextureAtlas.AtlasRegion(
			ImageMaster.loadImage(DTModMain.GetPowerPath(RAW_ID, 48)), 0, 0, 32, 32);

	public static final int BONUS_AMOUNT = 5;
	public static final int OPTION_COUNT = 10;

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
		description = DESCRIPTIONS[0] + BONUS_AMOUNT + DESCRIPTIONS[1];
	}

	@Override
	public void stackPower(int stackAmount) {
		super.stackPower(stackAmount);
		while (amount >= BONUS_AMOUNT) {
			ArrayList<AbstractCard> choices = new ArrayList<>();

			int index1 = AbstractDungeon.cardRandomRng.random(OPTION_COUNT - 1);
			int index2 = AbstractDungeon.cardRandomRng.random(OPTION_COUNT - 2);
			int index3 = AbstractDungeon.cardRandomRng.random(OPTION_COUNT - 3);
			if (index2 == index1) {
				index2 = OPTION_COUNT - 1;
			}
			if (index3 == index2) {
				index3 = OPTION_COUNT - 2;
			}
			if (index3 == index1) {
				index3 = OPTION_COUNT - 1;
				if (index3 == index2) {
					index3 = OPTION_COUNT - 2;
				}
			}
			choices.add(new BondingBonus(index1));
			choices.add(new BondingBonus(index2));
			choices.add(new BondingBonus(index3));
			addToBot(new ChooseOneAction(choices) {
				@Override
				public void update() {
					if (this.duration == Settings.ACTION_DUR_FAST) {
						AbstractDungeon.topPanel.unhoverHitboxes();
						AbstractDungeon.actionManager.cleanCardQueue();
						AbstractDungeon.player.releaseCard();
						DTModMain.bondingBonuses++;
						CardGroup[] groups = new CardGroup[]{AbstractDungeon.player.hand, AbstractDungeon.player.discardPile, AbstractDungeon.player.drawPile};
						for (CardGroup g : groups) {
							for (AbstractCard c : g.group) {
								if (c instanceof OnBondingActivateCard) {
									((OnBondingActivateCard) c).onBondingActivate();
								}
							}
						}
					}
					super.update();
				}
			});
			amount -= BONUS_AMOUNT;
			if (amount <= 0) {
				addToTop(new RemoveSpecificPowerAction(owner, owner, ID));
			}
		}
	}
}
