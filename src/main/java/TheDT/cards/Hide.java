package TheDT.cards;

import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

public class Hide extends AbstractDTCard {
	public static final String RAW_ID = "Hide";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 12;
	private static final int UPGRADE_BONUS = 4;
	private static final int DRAW = 2;

	public Hide() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		dtBaseDragonBlock = POWER;
		magicNumber = baseMagicNumber = DRAW;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		if (DragonTamer.isFrontDragon()) {
			rawDescription = EXTENDED_DESCRIPTION[0];
		} else {
			rawDescription = DESCRIPTION;
		}
		initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (DragonTamer.isFrontDragon()) {
			addToBot(new GainBlockAction(((DragonTamer) AbstractDungeon.player).dragon, ((DragonTamer) AbstractDungeon.player).dragon, dtDragonBlock));
		} else {
			addToBot(new GainBlockAction(p, p, block));
		}

		addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, magicNumber), magicNumber));
	}

	public AbstractCard makeCopy() {
		return new Hide();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
