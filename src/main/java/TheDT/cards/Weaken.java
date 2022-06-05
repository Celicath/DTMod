package TheDT.cards;

import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Weaken extends AbstractDTCard {
	public static final String RAW_ID = "Weaken";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.YOU;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 2;

	public Weaken() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = POWER;
		exhaust = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
	}

	public AbstractCard makeCopy() {
		return new Weaken();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
