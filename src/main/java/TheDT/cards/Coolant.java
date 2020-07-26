package TheDT.cards;

import TheDT.actions.ScryExhaustStatusAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Coolant extends AbstractDTCard {
	public static final String RAW_ID = "Coolant";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int SCRY = 5;
	private static final int UPGRADE_BONUS = 2;

	public Coolant() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		magicNumber = baseMagicNumber = SCRY;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new ScryExhaustStatusAction(magicNumber));
	}

	public AbstractCard makeCopy() {
		return new Coolant();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
