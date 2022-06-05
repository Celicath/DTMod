package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RepeatStrike extends AbstractDTCard {
	public static final String RAW_ID = "RepeatStrike";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.DRAGON;

	private static final int DAMAGE = 12;

	public RepeatStrike() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;

		tags.add(CardTags.STRIKE);
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
			addToBot(new DamageAction(m, new DamageInfo(dragon, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		}
	}

	public AbstractCard makeCopy() {
		return new RepeatStrike();
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
