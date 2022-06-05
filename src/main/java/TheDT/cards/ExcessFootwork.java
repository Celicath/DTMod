package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class ExcessFootwork extends AbstractDTCard {
	public static final String RAW_ID = "ExcessFootwork";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.REAR;

	private static final int POWER = 4;
	private static final int UPGRADE_BONUS = 2;

	public ExcessFootwork() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseMagicNumber = magicNumber = POWER;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();
		if (DragonTamer.isRearYou()) {
			addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));
		} else {
			addToBot(new ApplyPowerAction(dragon, dragon, new DexterityPower(dragon, magicNumber), magicNumber));
		}
	}

	public AbstractCard makeCopy() {
		return new ExcessFootwork();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
