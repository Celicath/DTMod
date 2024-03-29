package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;

public class Hide extends AbstractDTCard {
	public static final String RAW_ID = "Hide";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.YOU;

	private static final int POWER = 12;
	private static final int UPGRADE_BONUS = 4;
	private static final int ENERGY = 1;

	public Hide() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		dtBaseDragonBlock = POWER;
		magicNumber = baseMagicNumber = ENERGY;
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
			Dragon dragon = DragonTamer.getLivingDragon();
			addToBot(new GainBlockAction(dragon, dragon, dtDragonBlock));
		} else {
			addToBot(new GainBlockAction(p, p, block));
		}

		addToBot(new ApplyPowerAction(p, p, new EnergizedBluePower(p, magicNumber), magicNumber));
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
