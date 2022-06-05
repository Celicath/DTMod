package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class HeatWave extends AbstractDTCard {
	public static final String RAW_ID = "HeatWave";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.DRAGON;

	private static final int DAMAGE = 4;
	private static final int WEAK = 1;
	private static final int UPGRADE_WEAK = 1;

	public HeatWave() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;
		magicNumber = baseMagicNumber = WEAK;
		isMultiDamage = true;
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

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		if (dragon != null) {
			addToBot(new DamageAllEnemiesAction(dragon, dragonMultiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));

			for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
				addToBot(new ApplyPowerAction(mo, dragon, new WeakPower(mo, magicNumber, false), magicNumber, true));
			}
		}
	}

	public AbstractCard makeCopy() {
		return new HeatWave();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_WEAK);
		}
	}
}
