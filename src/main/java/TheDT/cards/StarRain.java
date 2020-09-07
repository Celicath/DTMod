package TheDT.cards;

import TheDT.actions.StarRainAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class StarRain extends AbstractDTCard {
	public static final String RAW_ID = "StarRain";
	private static final int COST = -1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int DAMAGE = 5;
	private static final int UPGRADE_BONUS = 2;

	public StarRain() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new StarRainAction(p, m, damage, damageTypeForTurn, freeToPlayOnce, energyOnUse));
	}

	public AbstractCard makeCopy() {
		return new StarRain();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_BONUS);
		}
	}
}
