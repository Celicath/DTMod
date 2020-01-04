package TheDT.cards;

import TheDT.DTMod;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static TheDT.patches.CustomTags.DT_TACTICS;

public class OpeningTactics extends AbstractDTCard {
	private static final String RAW_ID = "OpeningTactics";
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

	private static final int POWER = 12;
	private static final int MAGIC = 1;

	public OpeningTactics() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		this.baseDamage = POWER;
		this.baseMagicNumber = MAGIC;
		this.magicNumber = this.baseMagicNumber;
		this.tags.add(DT_TACTICS);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		if (m.currentHealth == m.maxHealth) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
		}
	}

	public AbstractCard makeCopy() {
		return new OpeningTactics();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			isInnate = true;
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
