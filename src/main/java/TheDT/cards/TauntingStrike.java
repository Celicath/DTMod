package TheDT.cards;

import TheDT.Interfaces.ChooseAttackerCard;
import TheDT.actions.ApplyAggroAction;
import TheDT.actions.ChooseAttackerAction;
import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import TheDT.powers.TauntPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TauntingStrike extends AbstractDTCard implements ChooseAttackerCard {
	public static final String RAW_ID = "TauntingStrike";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int DAMAGE = 10;
	private static final int UPGRADE_DAMAGE = 4;

	public TauntingStrike() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;
		dtBaseDragonDamage = DAMAGE;

		tags.add(CardTags.STRIKE);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new ChooseAttackerAction(this, m, true));
	}

	public AbstractCard makeCopy() {
		return new TauntingStrike();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
		}
	}

	@Override
	public void onChoseAttacker(AbstractCreature attacker, AbstractMonster m) {
		if (attacker != m) {
			TauntPower tp = (TauntPower) (m.getPower(TauntPower.POWER_ID));
			if (tp != null) {
				addToBot(new RemoveSpecificPowerAction(m, m, tp));
			}
			addToBot(new ApplyPowerAction(m, attacker, new TauntPower(m, attacker)));
		}

		addToBot(new FastAnimateFastAttackAction(attacker));
		if (attacker instanceof AbstractPlayer) {
			calculateCardDamage(m);
			addToBot(new DamageAction(m, new DamageInfo(attacker, attacker instanceof Dragon ? dtDragonDamage : damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		} else {
			int d = calculateCardDamageAsMonster(attacker, new int[]{baseDamage}, m, null);
			addToBot(new DamageAction(m, new DamageInfo(attacker, d, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		}
		addToBot(new ApplyAggroAction());
	}

	@Override
	public String hoverText(AbstractCreature hovered, AbstractMonster m) {
		if (hovered == m) {
			return EXTENDED_DESCRIPTION[0];
		} else {
			return null;
		}
	}
}
