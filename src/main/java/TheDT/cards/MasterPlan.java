package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

public class MasterPlan extends AbstractDTCard {
	public static final String RAW_ID = "MasterPlan";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 13;
	private static final int UPGRADE_BONUS = 3;

	public MasterPlan() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		dtBaseDragonBlock = POWER;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = getDragon();
		if (dragon != null) {
			addToBot(new GainBlockAction(dragon, dragon, dtDragonBlock));
		}
		addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, this.block), this.block));

		rawDescription = DESCRIPTION;
		initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new MasterPlan();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
