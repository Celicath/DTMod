package TheDT.cards;

import TheDT.actions.ExhaustRandomStatusAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CleansingStrike extends AbstractDTCard {
	public static final String RAW_ID = "CleansingStrike";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 8;
	private static final int UPGRADE_BONUS = 2;
	private static final int EXHAUST = 1;
	private static final int UPGRADE_EXHAUST = 1;

	public CleansingStrike() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseDamage = POWER;
		baseMagicNumber = magicNumber = EXHAUST;
		tags.add(CardTags.STRIKE);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DamageAction(m,
				new DamageInfo(p, damage, damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		addToBot(new ExhaustRandomStatusAction(magicNumber));
	}

	public AbstractCard makeCopy() {
		return new CleansingStrike();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_BONUS);
			upgradeMagicNumber(UPGRADE_EXHAUST);
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
