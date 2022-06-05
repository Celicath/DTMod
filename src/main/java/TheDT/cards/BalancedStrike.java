package TheDT.cards;

import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BalancedStrike extends AbstractDTCard {
	public static final String RAW_ID = "BalancedStrike";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.YOU;

	private static final int DAMAGE = 8;
	private static final int UPGRADE_DAMAGE = 3;
	private static final int BLOCK = 8;
	private static final int UPGRADE_BLOCK = 3;

	public BalancedStrike() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseDamage = DAMAGE;
		baseBlock = BLOCK;
		tags.add(CardTags.STRIKE);
		isInnate = true;
		exhaust = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new GainBlockAction(p, p, block));
		addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
	}

	public AbstractCard makeCopy() {
		return new BalancedStrike();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
			upgradeBlock(UPGRADE_BLOCK);
		}
	}
}
