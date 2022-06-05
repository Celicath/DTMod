package TheDT.cards;

import TheDT.Interfaces.ChooseAttackerCard;
import TheDT.actions.ApplyAggroAction;
import TheDT.actions.ChooseAttackerAction;
import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.actions.FastLoseBlockAction;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

public class RecklessFlurry extends AbstractDTCard implements ChooseAttackerCard {
	public static final String RAW_ID = "RecklessFlurry";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.BOTH;

	private static final int RATIO = 2;
	private static final int NEW_COST = 0;

	public RecklessFlurry() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		isMultiDamage = true;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		baseDamage = AbstractDungeon.player.currentBlock * RATIO;
		Dragon d = DragonTamer.getLivingDragon();
		dtBaseDragonDamage = d == null ? 0 : d.currentBlock * RATIO;
		super.calculateCardDamage(mo);
	}

	@Override
	public int calculateCardDamageAsMonster(AbstractCreature attacker, int[] baseDamage, AbstractMonster mo, int[] enemyMultiDamage) {
		baseDamage[0] = attacker.currentBlock * RATIO;
		return super.calculateCardDamageAsMonster(attacker, baseDamage, mo, enemyMultiDamage);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new ChooseAttackerAction(this, m, true));
	}

	public AbstractCard makeCopy() {
		return new RecklessFlurry();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(NEW_COST);
		}
	}

	@Override
	public void onChoseAttacker(AbstractCreature attacker, AbstractMonster m) {
		addToBot(new FastLoseBlockAction(attacker, attacker.currentBlock));
		addToBot(new FastAnimateFastAttackAction(attacker));
		addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));
		addToBot(new SFXAction("ATTACK_HEAVY"));
		addToBot(new VFXAction(attacker, new CleaveEffect(), 0.0F));
		if (attacker instanceof AbstractPlayer) {
			if (attacker instanceof Dragon) {
				calculateCardDamage(m);
				addToBot(new DamageAllEnemiesAction(attacker, dragonMultiDamage, damageType, AbstractGameAction.AttackEffect.NONE, true));
			} else {
				calculateCardDamage(m);
				addToBot(new DamageAllEnemiesAction(attacker, multiDamage, damageType, AbstractGameAction.AttackEffect.NONE, true));
			}
		} else {
			int[] enemyMultiDamage = new int[AbstractDungeon.getCurrRoom().monsters.monsters.size()];
			calculateCardDamageAsMonster(attacker, new int[]{attacker.currentBlock * RATIO}, m, enemyMultiDamage);
			addToBot(new DamageAllEnemiesAction(attacker, enemyMultiDamage, damageType, AbstractGameAction.AttackEffect.NONE, true));
		}
		addToBot(new ApplyAggroAction());
	}
}
