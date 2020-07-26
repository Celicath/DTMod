package TheDT.cards;

import TheDT.DTModMain;
import TheDT.actions.GainBurnPerEnemyAction;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Inferno extends AbstractDTCard {
	public static final String RAW_ID = "Inferno";
	private static final int COST = 3;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int DAMAGE = 32;
	private static final int UPGRADE_DAMAGE = 8;

	public Inferno() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;
		isMultiDamage = true;
		cardsToPreview = DTModMain.previewBurn;
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
			addToBot(new GainBurnPerEnemyAction());
			addToBot(new DamageAllEnemiesAction(dragon, dragonMultiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
		}
	}

	public AbstractCard makeCopy() {
		return new Inferno();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
		}
	}
}
