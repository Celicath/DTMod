package TheDT.cards;

import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Cover extends AbstractDTCard {
	public static final String RAW_ID = "Cover";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 8;
	private static final int UPGRADE_BONUS = 4;
	private static final int DRAW = 2;

	public Cover() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		dtBaseDragonBlock = POWER;
		magicNumber = baseMagicNumber = DRAW;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		if (DragonTamer.isRearYou()) {
			rawDescription = DESCRIPTION;
		} else {
			rawDescription = EXTENDED_DESCRIPTION[0];
		}
		initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (DragonTamer.isRearYou()) {
			addToBot(new GainBlockAction(p, p, block));
		} else {
			addToBot(new GainBlockAction(((DragonTamer) AbstractDungeon.player).dragon, ((DragonTamer) AbstractDungeon.player).dragon, dtDragonBlock));
		}
		addToBot(new DrawCardAction(magicNumber));
	}

	public AbstractCard makeCopy() {
		return new Cover();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
