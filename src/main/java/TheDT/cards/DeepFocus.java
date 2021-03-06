package TheDT.cards;

import TheDT.patches.CardColorEnum;
import TheDT.powers.DeepFocusPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DeepFocus extends AbstractDTCard {
	public static final String RAW_ID = "DeepFocus";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int STRENGTH = 6;
	private static final int UPGRADE_BONUS = 2;

	public DeepFocus() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = STRENGTH;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new ApplyPowerAction(p, p, new DeepFocusPower(p, 2, magicNumber), magicNumber));
	}

	public AbstractCard makeCopy() {
		return new DeepFocus();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
