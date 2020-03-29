package TheDT.cards;

import TheDT.actions.ApplyAggroAction;
import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import TheDT.powers.BondingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ComboAttack extends AbstractDTCard {
	public static final String RAW_ID = "ComboAttack";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 5;
	private static final int UPGRADE_POWER = 2;
	private static final int DRAGON_POWER = 5;
	private static final int UPGRADE_DRAGON = 2;

	public ComboAttack() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseDamage = POWER;
		dtBaseDragonDamage = DRAGON_POWER;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon d = getLivingDragon();

		addToBot(new FastAnimateFastAttackAction(p));
		addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		addToBot(new ApplyAggroAction());
		if (d != null) {
			addToBot(new WaitAction(0.1f));
			addToBot(new FastAnimateFastAttackAction(d));
			addToBot(new DamageAction(m, new DamageInfo(d, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		}
		addToBot(new ApplyAggroAction());
		addToBot(new ApplyPowerAction(p, p, new BondingPower(p, p, 1), 1));
	}

	public AbstractCard makeCopy() {
		return new ComboAttack();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_POWER);
			upgradeDTDragonDamage(UPGRADE_DRAGON);
		}
	}
}
