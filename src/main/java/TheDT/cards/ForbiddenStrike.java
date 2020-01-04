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

public class ForbiddenStrike extends AbstractDTCard {
	private static final String RAW_ID = "ForbiddenStrike";
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
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int DAMAGE = 23;

	public ForbiddenStrike() {
		this(0);
	}

	public ForbiddenStrike(int upgrades) {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		this.baseDamage = DAMAGE;
		this.timesUpgraded = upgrades;

		this.tags.add(CardTags.STRIKE);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
	}

	public AbstractCard makeCopy() {
		return new ForbiddenStrike(this.timesUpgraded);
	}

	public void upgrade() {
		upgradeName();
		rawDescription = UPGRADE_DESCRIPTION;
		initializeDescription();
	}
}
