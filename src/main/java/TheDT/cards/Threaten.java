package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Threaten extends AbstractDTCard {
	public static final String RAW_ID = "Threaten";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int POWER = 6;
	private static final int UPGRADE_BONUS = 2;
	private static final int WEAK = 1;
	private static final int WEAK_BONUS = 1;

	public Threaten() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		dtBaseDragonBlock = POWER;
		magicNumber = baseMagicNumber = WEAK;
		isInnate = true;
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
		Dragon dragon = DragonTamer.getLivingDragon();

		if (dragon != null) {
			addToBot(new GainBlockAction(dragon, dragon, dtDragonBlock));
			addToBot(new ApplyPowerAction(m, dragon, new WeakPower(dragon, magicNumber, false), magicNumber));
		}
	}

	public AbstractCard makeCopy() {
		return new Threaten();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDTDragonBlock(UPGRADE_BONUS);
			upgradeMagicNumber(WEAK_BONUS);
		}
	}
}
