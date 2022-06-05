package TheDT.cards;

import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Astonish extends AbstractDTCard {
	public static final String RAW_ID = "Astonish";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.REAR;

	private static final int DAMAGE_MULTIPLIER = 2;
	private static final int UPGRADE_BONUS = 1;

	public Astonish() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		magicNumber = baseMagicNumber = DAMAGE_MULTIPLIER;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		if (dragon != null && !DragonTamer.isRearYou()) {
			addToBot(new FastAnimateFastAttackAction(dragon));
			addToBot(new DamageAction(m, new DamageInfo(dragon, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		} else {
			addToBot(new FastAnimateFastAttackAction(p));
			addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		}
		rawDescription = DESCRIPTION;
		initializeDescription();
	}

	public void applyPowers() {
		boolean attackerIsYou = true;
		if (DragonTamer.getLivingDragon() != null) {
			baseDamage = Math.abs(((DragonTamer) AbstractDungeon.player).aggro) * magicNumber;
			dtBaseDragonDamage = Math.abs(((DragonTamer) AbstractDungeon.player).aggro) * magicNumber;
			if (!DragonTamer.isRearYou()) {
				attackerIsYou = false;
			}
		} else {
			baseDamage = 0;
		}
		super.applyPowers();
		rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[attackerIsYou ? 0 : 1];
		initializeDescription();
	}

	public void onMoveToDiscard() {
		this.rawDescription = cardStrings.DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new Astonish();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
