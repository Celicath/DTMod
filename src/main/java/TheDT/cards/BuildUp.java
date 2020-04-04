package TheDT.cards;

import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class BuildUp extends AbstractDTCard {
	public static final String RAW_ID = "BuildUp";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int DAMAGE = 7;
	private static final int UPGRADE_DAMAGE = 2;
	private static final int MAGIC = 4;
	private static final int UPGRADE_MAGIC = 2;

	public BuildUp() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;
		baseMagicNumber = magicNumber = MAGIC;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DamageAction(m,
				new DamageInfo(p, damage, damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber), magicNumber));
	}

	public AbstractCard makeCopy() {
		return new BuildUp();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
			upgradeMagicNumber(UPGRADE_MAGIC);
		}
	}
}
