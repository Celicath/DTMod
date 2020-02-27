package TheDT.cards;

import TheDT.DTMod;
import TheDT.actions.PunisherAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Punisher extends AbstractDTCard {
	private static final String RAW_ID = "Punisher";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 7;
	private static final int UPGRADE_POWER = 3;

	public Punisher() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseDamage = POWER;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
		addToBot(new PunisherAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
	}

	public AbstractCard makeCopy() {
		return new Punisher();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_POWER);
		}
	}
}
