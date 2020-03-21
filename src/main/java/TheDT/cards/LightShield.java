package TheDT.cards;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LightShield extends AbstractDTCard {
	public static final String RAW_ID = "LightShield";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 10;
	private static final int RATIO = 2;
	private static final int UPGRADE_RATIO = 2;

	public LightShield() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = RATIO;
		baseBlock = POWER;
		dtBaseDragonBlock = POWER;
	}

	@Override
	public void applyPowers() {
		int bonus = magicNumber * DTModMain.bondingGained;
		baseBlock += bonus;
		dtBaseDragonBlock += bonus;
		super.applyPowers();
		if (bonus != 0) {
			baseBlock -= bonus;
			dtBaseDragonBlock -= bonus;
			isBlockModified = true;
			isDTDragonBlockModified = true;
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		int bonus = magicNumber * DTModMain.bondingGained;
		baseBlock += bonus;
		dtBaseDragonBlock += bonus;
		super.calculateCardDamage(mo);
		if (bonus != 0) {
			baseBlock -= bonus;
			dtBaseDragonBlock -= bonus;
			isBlockModified = true;
			isDTDragonBlockModified = true;
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = getDragon();

		addToBot(new GainBlockAction(p, p, block));
		if (dragon != null) {
			addToBot(new GainBlockAction(dragon, dragon, dtDragonBlock));
		}
	}

	public AbstractCard makeCopy() {
		return new LightShield();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_RATIO);
		}
	}
}
