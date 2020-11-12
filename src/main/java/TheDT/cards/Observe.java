package TheDT.cards;

import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.defect.AggregateEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Observe extends AbstractDTCard {
	public static final String RAW_ID = "Observe";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int PER = 6;
	private static final int NEW_COST = 0;

	public Observe() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		magicNumber = baseMagicNumber = PER;

		isInnate = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new AggregateEnergyAction(magicNumber));
	}

	public AbstractCard makeCopy() {
		return new Observe();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(NEW_COST);
		}
	}
}
