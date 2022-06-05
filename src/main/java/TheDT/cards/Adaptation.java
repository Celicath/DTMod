package TheDT.cards;

import TheDT.patches.CardColorEnum;
import TheDT.powers.AdaptationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Adaptation extends AbstractDTCard {
	public static final String RAW_ID = "Adaptation";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.YOU;

	public Adaptation() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (!p.hasPower(AdaptationPower.POWER_ID)) {
			addToBot(new ApplyPowerAction(p, p, new AdaptationPower(p, p)));
		}
	}

	public AbstractCard makeCopy() {
		return new Adaptation();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			isInnate = true;
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
