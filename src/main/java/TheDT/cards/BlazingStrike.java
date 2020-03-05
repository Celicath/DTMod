package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlazingStrike extends AbstractDTCard {
	public static final String RAW_ID = "BlazingStrike";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int DAMAGE = 14;
	private static final int UPGRADE_DAMAGE = 4;

	public BlazingStrike() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;
		cardsToPreview = new Burn();
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

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = getDragon();

		if (dragon != null) {
			addToBot(new DamageAction(m, new DamageInfo(dragon, dtDragonDamage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
			addToBot(new MakeTempCardInHandAction(new Burn(), 1));
		}
	}

	public AbstractCard makeCopy() {
		return new BlazingStrike();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
		}
	}
}
