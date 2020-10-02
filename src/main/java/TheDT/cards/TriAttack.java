package TheDT.cards;

import TheDT.Interfaces.ChooseAttackerCard;
import TheDT.actions.ApplyAggroAction;
import TheDT.actions.ChooseAttackerAction;
import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TriAttack extends AbstractDTCard implements ChooseAttackerCard {
	public static final String RAW_ID = "TriAttack";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int DAMAGE = 6;
	private static final int UPGRADE_DAMAGE = 3;
	private static final int HITS = 3;

	public TriAttack() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;
		dtBaseDragonDamage = DAMAGE;
		magicNumber = baseMagicNumber = HITS;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		TriAttack thisCard = this;
		for (int i = 0; i < magicNumber; i++) {
			addToBot(new AbstractGameAction() {
				@Override
				public void update() {
					if (!m.isDeadOrEscaped()) {
						addToTop(new ChooseAttackerAction(thisCard, m, true));
					}
					isDone = true;
				}
			});
		}
	}

	public AbstractCard makeCopy() {
		return new TriAttack();
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
		addToTop(new ApplyAggroAction());
		if (attacker instanceof AbstractPlayer) {
			calculateCardDamage(m);
			addToTop(new DamageAction(m, new DamageInfo(attacker, attacker instanceof Dragon ? dtDragonDamage : damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		} else {
			int d = calculateCardDamageAsMonster(attacker, new int[]{baseDamage}, m, null);
			addToTop(new DamageAction(m, new DamageInfo(attacker, d, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		}
		addToTop(new FastAnimateFastAttackAction(attacker));
	}
}
