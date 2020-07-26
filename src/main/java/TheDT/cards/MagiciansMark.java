package TheDT.cards;

import TheDT.patches.CardColorEnum;
import TheDT.patches.CustomTags;
import TheDT.powers.MagiciansMarkPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MagiciansMark extends AbstractDTCard {
	public static final String RAW_ID = "MagiciansMark";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 20;
	private static final int UPGRADE_BONUS = 10;
	private static final int MAGIC = 1;

	public MagiciansMark() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = POWER;
		baseMagicNumber = magicNumber = MAGIC;

		exhaust = true;
		tags.add(CustomTags.DT_BONDING);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		addToBot(new ApplyPowerAction(m, p, new MagiciansMarkPower(m, magicNumber), magicNumber));
	}

	public AbstractCard makeCopy() {
		return new MagiciansMark();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_BONUS);
		}
	}
}
