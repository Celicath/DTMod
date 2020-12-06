package TheDT.cards;

import TheDT.patches.CardColorEnum;
import TheDT.powers.StunTrapPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class StunTrap extends AbstractDTCard {
	public static final String RAW_ID = "StunTrap";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int DRAW = 2;
	private static final int UPGRADE_BONUS = 2;

	public StunTrap() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = DRAW;
		exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new ApplyPowerAction(p, p, new StunTrapPower(m, p, 2, magicNumber)));
	}

	public AbstractCard makeCopy() {
		return new StunTrap();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
