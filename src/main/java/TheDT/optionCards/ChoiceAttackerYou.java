package TheDT.optionCards;

import TheDT.DTModMain;
import TheDT.actions.ApplyAggroAction;
import TheDT.actions.FastAnimateFastAttackAction;
import TheDT.cards.AbstractDTCard;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChoiceAttackerYou extends AbstractDTCard {
	public static final String RAW_ID = "ChoiceAttackerYou";
	private static final int COST = -2;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.SPECIAL;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	public AbstractMonster targetMonster;

	public ChoiceAttackerYou(String rawID, AbstractMonster m, int baseDamage, int damage) {
		super(rawID, COST, TYPE, COLOR, RARITY, TARGET, DTModMain.CHOICE_ID_YOU, DT_CARD_TARGET);
		this.baseDamage = baseDamage;
		this.damage = damage;
		isDamageModified = baseDamage != damage;
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
		AbstractPlayer p = AbstractDungeon.player;
		addToTop(new ApplyAggroAction());
		addToTop(new DamageAction(targetMonster, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		addToTop(new FastAnimateFastAttackAction(p));
	}

	@Override
	public AbstractCard makeCopy() {
		return new ChoiceAttackerYou(DTModMain.CHOICE_ID_YOU, null, 0, 0);
	}

	@Override
	public void upgrade() {
	}
}
