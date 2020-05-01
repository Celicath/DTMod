package TheDT.cards;

import TheDT.actions.DragonChangeStanceAction;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.powers.DragonLoseStancePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.WrathStance;

public class Outburst extends AbstractDTCard {
	public static final String RAW_ID = "Outburst";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int ENERGY_UPGRADE = 1;
	private static final int EXIT_TURN = 2;

	public Outburst() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		boolean result = super.canUse(p, m);
		if (result && DragonTamer.getLivingDragon() == null) {
			cantUseMessage = dragonNotAvailableMessage();
			return false;
		}
		return result;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon d = DragonTamer.getLivingDragon();
		if (upgraded) {
			addToBot(new GainEnergyAction(ENERGY_UPGRADE));
		}
		if (d != null) {
			addToBot(new DragonChangeStanceAction(d, WrathStance.STANCE_ID));
			addToBot(new ApplyPowerAction(d, d, new DragonLoseStancePower(d, EXIT_TURN), EXIT_TURN));
		}
	}

	public AbstractCard makeCopy() {
		return new Outburst();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
