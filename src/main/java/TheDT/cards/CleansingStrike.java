package TheDT.cards;

import TheDT.DTMod;
import TheDT.actions.ExhaustRandomStatusAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CleansingStrike extends AbstractDTCard {
	private static final String RAW_ID = "CleansingStrike";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 9;
	private static final int UPGRADE_BONUS = 3;
	private static final int EXHAUST = 1;
	private static final int UPGRADE_EXHAUST = 1;

	public CleansingStrike() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseDamage = POWER;
		baseMagicNumber = magicNumber = EXHAUST;
		tags.add(CardTags.STRIKE);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
				new DamageInfo(p, damage, damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		AbstractDungeon.actionManager.addToBottom(new ExhaustRandomStatusAction(magicNumber));
	}

	public AbstractCard makeCopy() {
		return new CleansingStrike();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_BONUS);
			upgradeMagicNumber(UPGRADE_EXHAUST);
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
