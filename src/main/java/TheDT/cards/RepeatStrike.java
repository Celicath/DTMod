package TheDT.cards;

import TheDT.DTMod;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RepeatStrike extends AbstractDTCard {
	private static final String RAW_ID = "RepeatStrike";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int DAMAGE = 11;

	public RepeatStrike() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;

		tags.add(CardTags.STRIKE);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DamageAction(m, new DamageInfo(p, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
	}

	public AbstractCard makeCopy() {
		return new RepeatStrike();
	}

	public void upgrade() {
		upgradeName();
		isInnate = true;
		rawDescription = UPGRADE_DESCRIPTION;
		initializeDescription();
	}
}
