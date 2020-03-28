package TheDT.cards;

import TheDT.DTModMain;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BurningEnergy extends AbstractDTCard {
	public static final String RAW_ID = "BurningEnergy";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 2;
	private static final int UPGRADE_BONUS = 1;

	public BurningEnergy() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = POWER;
		cardsToPreview = DTModMain.previewBurn;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new GainEnergyAction(magicNumber));
		addToBot(new MakeTempCardInDiscardAction(new Burn(), 1));
	}

	public AbstractCard makeCopy() {
		return new BurningEnergy();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
