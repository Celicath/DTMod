package TheDT.cards;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BurningForce extends AbstractDTCard {
	public static final String RAW_ID = "BurningForce";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int DAMAGE = 15;
	private static final int UPGRADE_DAMAGE = 5;
	private static final int BURN_BONUS = 5;

	public BurningForce() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;
		magicNumber = baseMagicNumber = BURN_BONUS;
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

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		if (dragon != null) {
			addToBot(new DamageAction(m, new DamageInfo(dragon, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		}
		rawDescription = DESCRIPTION;
		initializeDescription();
	}

	@Override
	public void applyPowers() {
		dtBaseDragonDamage += DTModMain.burnGen * magicNumber;
		super.applyPowers();
		if (DTModMain.burnGen != 0) {
			dtBaseDragonDamage -= DTModMain.burnGen * magicNumber;
			isDTDragonDamageModified = true;
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		dtBaseDragonDamage += DTModMain.burnGen * magicNumber;
		super.calculateCardDamage(mo);
		if (DTModMain.burnGen != 0) {
			dtBaseDragonDamage -= DTModMain.burnGen * magicNumber;
			isDTDragonDamageModified = true;
		}
	}

	public AbstractCard makeCopy() {
		return new BurningForce();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
		}
	}
}
