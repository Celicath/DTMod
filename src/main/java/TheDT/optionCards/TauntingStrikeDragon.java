package TheDT.optionCards;

import TheDT.actions.ApplyAggroAction;
import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.cards.AbstractDTCard;
import TheDT.cards.TauntingStrike;
import TheDT.characters.Dragon;
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

public class TauntingStrikeDragon extends AbstractDTCard {
	public static final String RAW_ID = "TauntingStrikeDragon";
	private static final int COST = -2;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;
	private static final DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int DAMAGE = 10;
	private static final int UPGRADE_DAMAGE = 3;

	private AbstractMonster target;

	public TauntingStrikeDragon(AbstractMonster m, int damage) {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, TauntingStrike.RAW_ID, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;
		this.dtDragonDamage = damage;
		if (dtBaseDragonDamage != dtDragonDamage) {
			isDTDragonDamageModified = true;
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
		Dragon dragon = getDragon();
		if (dragon != null) {
			if (target == null) {
				target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
			}
			addToBot(new FastAnimateFastAttackAction(dragon));
			addToBot(new DamageAction(target, new DamageInfo(dragon, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
			addToBot(new ApplyPowerAction(target, dragon, new TauntPower(target, true, 1), 1));
		}
		addToBot(new ApplyAggroAction());
	}

	public AbstractCard makeCopy() {
		return new TauntingStrikeDragon(null, baseDamage);
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDTDragonDamage(UPGRADE_DAMAGE);
		}
	}
}