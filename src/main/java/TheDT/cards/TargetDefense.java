package TheDT.cards;

import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.relics.BasicTextbook;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TargetDefense extends AbstractDTCard {
	public static final String RAW_ID = "TargetDefense";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 5;
	private static final int UPGRADE_BONUS = 3;

	public TargetDefense() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseBlock = dtBaseDragonBlock = POWER;
		tags.add(CardTags.STARTER_DEFEND);
	}

	@Override
	public void applyPowers() {
		int blockModifier = 0;
		for (AbstractRelic r : AbstractDungeon.player.relics) {
			if (r instanceof BasicTextbook) {
				blockModifier += BasicTextbook.BONUS;
			}
		}
		baseBlock += blockModifier;
		dtBaseDragonBlock += blockModifier;
		super.applyPowers();
		if (blockModifier > 0) {
			baseBlock -= blockModifier;
			dtBaseDragonBlock -= blockModifier;
			isBlockModified = isDTDragonBlockModified = true;
		}

		if (DragonTamer.isFrontDragon()) {
			rawDescription = EXTENDED_DESCRIPTION[0];
		} else {
			rawDescription = DESCRIPTION;
		}
		initializeDescription();
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		int blockModifier = 0;
		for (AbstractRelic r : AbstractDungeon.player.relics) {
			if (r instanceof BasicTextbook) {
				blockModifier += BasicTextbook.BONUS;
			}
		}
		baseBlock += blockModifier;
		dtBaseDragonBlock += blockModifier;
		super.calculateCardDamage(mo);
		if (blockModifier > 0) {
			baseBlock -= blockModifier;
			dtBaseDragonBlock -= blockModifier;
			isBlockModified = isDTDragonBlockModified = true;
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (DragonTamer.isFrontDragon()) {
			addToBot(new GainBlockAction(((DragonTamer) AbstractDungeon.player).dragon, ((DragonTamer) AbstractDungeon.player).dragon, dtDragonBlock));
		} else {
			addToBot(new GainBlockAction(p, p, block));
		}
	}

	public AbstractCard makeCopy() {
		return new TargetDefense();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
