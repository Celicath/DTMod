package TheDT.cards;

import TheDT.actions.AddAggroAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static TheDT.patches.CustomTags.DT_TACTICS;

public class SneakAttack extends AbstractDTCard {
	public static final String RAW_ID = "SneakAttack";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 14;
	private static final int UPGRADE_BONUS = 4;
	private static final int MAGIC = 1;

	public SneakAttack() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = POWER;
		baseMagicNumber = MAGIC;
		magicNumber = this.baseMagicNumber;
		isInnate = true;
		tags.add(DT_TACTICS);
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		boolean result = super.canUse(p, m);
		if (result && !isRearYou()) {
			cantUseMessage = EXTENDED_DESCRIPTION[0];
			return false;
		}
		return result;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (isRearYou()) {
			addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
			addToBot(new AddAggroAction(false, 3));
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
