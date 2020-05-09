package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class MaximumCharge extends AbstractDTCard {
	public static final String RAW_ID = "MaximumCharge";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int DAMAGE = 30;
	private static final int UPGRADE_DAMAGE = 5;
	private static final int VULNERABLE = 3;
	private static final int VULNERABLE_UPGRADE = -1;

	public MaximumCharge() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;
		magicNumber = baseMagicNumber = VULNERABLE;
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
			if (m != null) {
				this.addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
			}
			addToBot(new DamageAction(m, new DamageInfo(dragon, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
			addToBot(new ApplyPowerAction(dragon, dragon, new VulnerablePower(dragon, magicNumber, false), magicNumber));
		}
	}

	public AbstractCard makeCopy() {
		return new MaximumCharge();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
			upgradeMagicNumber(VULNERABLE_UPGRADE);
		}
	}
}
