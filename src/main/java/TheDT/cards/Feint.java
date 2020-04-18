package TheDT.cards;

import TheDT.actions.RandomAttackerDamageAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Feint extends AbstractDTCard {
	public static final String RAW_ID = "Feint";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int DAMAGE = 7;
	private static final int UPGRADE_DAMAGE = 3;

	public Feint() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;
		dtBaseDragonDamage = DAMAGE;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new RandomAttackerDamageAction(m, damage, dtDragonDamage));
	}

	public AbstractCard makeCopy() {
		return new Feint();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
		}
	}
}
