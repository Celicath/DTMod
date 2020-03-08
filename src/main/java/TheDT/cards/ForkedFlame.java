package TheDT.cards;

import TheDT.actions.ForkedFlameAction;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ForkedFlame extends AbstractDTCard {
	public static final String RAW_ID = "ForkedFlame";
	private static final int COST = -1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int DAMAGE = 7;
	private static final int UPGRADE_DAMAGE = 2;

	public ForkedFlame() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		boolean result = super.canUse(p, m);
		if (result && getDragon() == null) {
			cantUseMessage = dragonNotAvailableMessage();
			return false;
		}
		return result;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = getDragon();

		if (dragon != null) {
			addToBot(new ForkedFlameAction(dragon, this, freeToPlayOnce, energyOnUse));
		}
	}

	public AbstractCard makeCopy() {
		return new ForkedFlame();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
		}
	}
}
