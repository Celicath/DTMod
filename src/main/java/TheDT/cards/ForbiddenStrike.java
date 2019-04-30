package TheDT.cards;

import TheDT.DTMod;
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

import static TheDT.patches.CustomTags.DT_FORBIDDEN;

public class ForbiddenStrike extends AbstractDTCard {
	private static final String RAW_ID = "ForbiddenStrike";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;

	private static final int DAMAGE = 24;
	private static final int UPGRADE_DAMAGE = 1;

	public ForbiddenStrike() {
		this(0);
	}

	public ForbiddenStrike(int upgrades) {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = DAMAGE;
		this.timesUpgraded = upgrades;

		this.tags.add(DT_FORBIDDEN);
		this.tags.add(CardTags.STRIKE);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
	}

	public AbstractCard makeCopy() {
		return new ForbiddenStrike(this.timesUpgraded);
	}

	public void upgrade() {
		this.upgradeDamage(UPGRADE_DAMAGE);
		++this.timesUpgraded;
		this.upgraded = true;
		this.name = NAME + "+" + this.timesUpgraded;
		this.initializeTitle();
	}

	public boolean canUpgrade() {
		return true;
	}
}
