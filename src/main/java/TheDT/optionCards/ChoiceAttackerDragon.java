package TheDT.optionCards;

import TheDT.DTModMain;
import TheDT.actions.ApplyAggroAction;
import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.cards.AbstractDTCard;
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

public class ChoiceAttackerDragon extends AbstractDTCard {
	public static final String RAW_ID = "ChoiceAttackerDragon";
	private static final int COST = -2;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.ENEMY;
	private static final DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	public AbstractMonster targetMonster;

	public ChoiceAttackerDragon(String rawID, AbstractMonster m, int baseDamage, int damage) {
		super(rawID, COST, TYPE, COLOR, RARITY, TARGET, DTModMain.CHOICE_ID_DRAGON, DT_CARD_TARGET);
		this.dtBaseDragonDamage = baseDamage;
		this.dtDragonDamage = damage;
		isDTDragonDamageModified = dtBaseDragonDamage != dtDragonDamage;
		targetMonster = m;
		if (targetMonster == null) {
			targetMonster = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
			calculateCardDamage(targetMonster);
		}
		initializeDescription();

		tags.add(CardTags.STRIKE);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		onChoseThisOption();
	}

	@Override
	public void onChoseThisOption() {
		Dragon dragon = DragonTamer.getLivingDragon();
		if (dragon != null) {
			addToTop(new ApplyAggroAction());
			addToTop(new DamageAction(targetMonster, new DamageInfo(dragon, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
			addToTop(new FastAnimateFastAttackAction(dragon));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ChoiceAttackerDragon(DTModMain.CHOICE_ID_DRAGON, null, 0, 0);
	}

	@Override
	public void upgrade() {
	}
}
