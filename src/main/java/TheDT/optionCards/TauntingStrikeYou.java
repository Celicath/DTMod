package TheDT.optionCards;

import TheDT.actions.ApplyAggroAction;
import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.cards.AbstractDTCard;
import TheDT.cards.TauntingStrike;
import TheDT.patches.CardColorEnum;
import TheDT.powers.TauntPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TauntingStrikeYou extends AbstractDTCard {
	public static final String RAW_ID = "TauntingStrikeYou";
	private static final int COST = -2;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.SPECIAL;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int DAMAGE = 10;
	private static final int UPGRADE_DAMAGE = 3;

	private AbstractMonster target;

	public TauntingStrikeYou(AbstractMonster m, int damage) {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, TauntingStrike.RAW_ID, DT_CARD_TARGET);
		baseDamage = DAMAGE;
		this.damage = damage;
		if (baseDamage != damage) {
			isDamageModified = true;
		}
		target = m;
		initializeDescription();

		tags.add(CardTags.STRIKE);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		onChoseThisOption();
	}

	@Override
	public void onChoseThisOption() {
		AbstractPlayer p = AbstractDungeon.player;
		if (target == null) {
			target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
			calculateCardDamage(target);
		}
		addToBot(new FastAnimateFastAttackAction(p));
		addToBot(new DamageAction(target, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		addToBot(new ApplyPowerAction(target, p, new TauntPower(target, false, 1), 1));
		addToBot(new ApplyAggroAction());
	}

	public AbstractCard makeCopy() {
		return new TauntingStrikeYou(null, baseDamage);
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
		}
	}
}
