package TheDT.cards;

import TheDT.patches.CardColorEnum;
import TheDT.powers.LastStandPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LastStand extends AbstractDTCard {
	public static final String RAW_ID = "LastStand";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int BLOCK = 5;
	private static final int UPGRADE_BONUS = 5;

	public LastStand() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseBlock = BLOCK;
		exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new GainBlockAction(p, block));
		addToBot(new ApplyPowerAction(p, p, new LastStandPower(p, 1), 1));
	}

	public AbstractCard makeCopy() {
		return new LastStand();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
		}
	}
}
