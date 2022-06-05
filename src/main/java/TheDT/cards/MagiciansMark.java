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
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.YOU;

	private static final int POWER = 10;
	private static final int UPGRADE_BONUS = 3;
	private static final int MAGIC = 4;
	private static final int UPGRADE_MAGIC = 3;

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
			upgradeMagicNumber(UPGRADE_MAGIC);
		}
	}
}
