package TheDT.cards;

import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SneakAttack extends AbstractDTCard {
	public static final String RAW_ID = "SneakAttack";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 4;
	private static final int UPGRADE_BONUS = 1;
	private static final int HIT = 4;

	public SneakAttack() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = POWER;
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		boolean result = super.canUse(p, m);
		if (result && !DragonTamer.isRearYou()) {
			cantUseMessage = EXTENDED_DESCRIPTION[0];
			return false;
		}
		return result;
	}

	public void triggerOnGlowCheck() {
		if (DragonTamer.isRearYou()) {
			glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		} else {
			glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (DragonTamer.isRearYou()) {
			for (int i = 0; i < HIT; i++) {
				addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
			}
		}
	}

	public AbstractCard makeCopy() {
		return new SneakAttack();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_BONUS);
		}
	}
}
