package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

public class MasterPlan extends AbstractDTCard {
	public static final String RAW_ID = "MasterPlan";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 6;
	private static final int UPGRADE_BONUS = 2;
	private static final int NEXT_TURN_BLOCK = 9;
	private static final int UPGRADE_NEXT = 3;

	public MasterPlan() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		dtBaseDragonBlock = NEXT_TURN_BLOCK;
	}

	@Override
	public void applyPowers() {
		if (DragonTamer.isFrontDragon()) {
			baseBlock = NEXT_TURN_BLOCK;
			dtBaseDragonBlock = POWER;
			rawDescription = EXTENDED_DESCRIPTION[0];
		} else {
			baseBlock = POWER;
			dtBaseDragonBlock = NEXT_TURN_BLOCK;
			rawDescription = DESCRIPTION;
		}
		super.applyPowers();
		initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();
		if (DragonTamer.isFrontDragon()) {
			addToBot(new GainBlockAction(dragon, dragon, dtDragonBlock));
			addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, block), block));
		} else {
			addToBot(new GainBlockAction(p, p, block));
			addToBot(new ApplyPowerAction(dragon, dragon, new NextTurnBlockPower(dragon, dtDragonBlock), dtDragonBlock));
		}
	}

	public AbstractCard makeCopy() {
		return new MasterPlan();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_NEXT);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
