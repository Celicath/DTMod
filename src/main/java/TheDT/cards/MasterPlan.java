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
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.BOTH;

	private static final int POWER = 7;
	private static final int UPGRADE_BONUS = 2;
	private static final int NEXT_TURN_BLOCK = 9;
	private static final int UPGRADE_NEXT = 3;

	private boolean flipped = false;

	public MasterPlan() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		dtBaseDragonBlock = NEXT_TURN_BLOCK;
	}

	@Override
	public void applyPowers() {
		if (DragonTamer.isFrontDragon()) {
			if (!flipped) {
				int tmp = dtBaseDragonBlock;
				dtBaseDragonBlock = baseBlock;
				baseBlock = tmp;
				rawDescription = EXTENDED_DESCRIPTION[0];
				initializeDescription();
				flipped = true;
			}
		} else {
			if (flipped) {
				int tmp = dtBaseDragonBlock;
				dtBaseDragonBlock = baseBlock;
				baseBlock = tmp;
				rawDescription = DESCRIPTION;
				initializeDescription();
				flipped = false;
			}
		}

		if (DragonTamer.isSolo()) {
			int currentBlock = baseBlock;
			baseBlock = dtBaseDragonBlock;
			super.applyPowers();

			int nextBlock = block;
			baseBlock = currentBlock;
			super.applyPowers();

			dtDragonBlock = nextBlock;
			isDTDragonBlockModified = dtDragonBlock != dtBaseDragonBlock;
		} else {
			super.applyPowers();
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		if (DragonTamer.isSolo()) {
			int currentBlock = baseBlock;
			baseBlock = dtBaseDragonBlock;
			super.calculateCardDamage(mo);

			int nextBlock = block;
			baseBlock = currentBlock;
			super.calculateCardDamage(mo);

			dtDragonBlock = nextBlock;
			isDTDragonBlockModified = dtDragonBlock != dtBaseDragonBlock;
		} else {
			super.calculateCardDamage(mo);
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();
		if (DragonTamer.isFrontDragon()) {
			addToBot(new GainBlockAction(dragon, dragon, dtDragonBlock));
			addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, block), block));
		} else {
			addToBot(new GainBlockAction(p, p, block));
			if (DragonTamer.isSolo()) {
				addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, dtDragonBlock), dtDragonBlock));
			} else {
				addToBot(new ApplyPowerAction(dragon, dragon, new NextTurnBlockPower(dragon, dtDragonBlock), dtDragonBlock));
			}
		}
	}

	public AbstractCard makeCopy() {
		return new MasterPlan();
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		MasterPlan card = (MasterPlan) super.makeStatEquivalentCopy();
		card.flipped = this.flipped;
		card.rawDescription = this.rawDescription;
		card.initializeDescription();
		return card;
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			if (flipped) {
				upgradeBlock(UPGRADE_NEXT);
				upgradeDTDragonBlock(UPGRADE_BONUS);
			} else {
				upgradeBlock(UPGRADE_BONUS);
				upgradeDTDragonBlock(UPGRADE_NEXT);
			}
		}
	}
}
