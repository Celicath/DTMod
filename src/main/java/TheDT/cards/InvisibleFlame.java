package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.powers.InvisibleFlamePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class InvisibleFlame extends AbstractDTCard {
	public static final String RAW_ID = "InvisibleFlame";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 2;

	public InvisibleFlame() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseMagicNumber = magicNumber = POWER;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new ApplyPowerAction(p, p, new InvisibleFlamePower(p, magicNumber), magicNumber));
		Dragon d = DragonTamer.getLivingDragon();
		if (d != null) {
			addToBot(new ApplyPowerAction(d, d, new InvisibleFlamePower(d, magicNumber), magicNumber));
		}
	}

	public AbstractCard makeCopy() {
		return new InvisibleFlame();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
