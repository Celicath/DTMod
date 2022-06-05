package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.powers.SafeguardPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Safeguard extends AbstractDTCard {
	public static final String RAW_ID = "Safeguard";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.BOTH;

	public Safeguard() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (!p.hasPower(SafeguardPower.POWER_ID)) {
			addToBot(new ApplyPowerAction(p, p, new SafeguardPower(p)));
		}
		Dragon d = DragonTamer.getLivingDragon();
		if (d != null) {
			if (!d.hasPower(SafeguardPower.POWER_ID)) {
				addToBot(new ApplyPowerAction(d, d, new SafeguardPower(d)));
			}
		}
	}

	public AbstractCard makeCopy() {
		return new Safeguard();
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
