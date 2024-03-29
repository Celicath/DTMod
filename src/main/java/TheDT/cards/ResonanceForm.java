package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.powers.ResonanceFormPower;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ResonanceForm extends AbstractDTCard {
	public static final String RAW_ID = "ResonanceForm";
	private static final int COST = 3;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.BOTH;

	public ResonanceForm() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		isEthereal = true;

		tags.add(BaseModCardTags.FORM);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (!p.hasPower(ResonanceFormPower.POWER_ID)) {
			addToBot(new ApplyPowerAction(p, p, new ResonanceFormPower(p)));
		}
		Dragon d = DragonTamer.getLivingDragon();
		if (d != null) {
			if (!d.hasPower(ResonanceFormPower.POWER_ID)) {
				addToBot(new ApplyPowerAction(d, d, new ResonanceFormPower(d)));
			}
		}
	}

	public AbstractCard makeCopy() {
		return new ResonanceForm();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			isEthereal = false;
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
