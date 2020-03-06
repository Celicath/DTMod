package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;

public class BizarreTactic extends AbstractDTCard {
	public static final String RAW_ID = "BizarreTactic";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 9;
	private static final int UPGRADE_BONUS = 3;
	private static final int REFLECT = 4;
	private static final int REFLECT_BONUS = 2;

	public BizarreTactic() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		magicNumber = baseMagicNumber = REFLECT;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = getDragon();

		addToBot(new GainBlockAction(p, p, block));
		if (dragon != null) {
			addToBot(new ApplyPowerAction(dragon, dragon, new FlameBarrierPower(dragon, magicNumber), magicNumber));
		}
	}

	public AbstractCard makeCopy() {
		return new BizarreTactic();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeMagicNumber(REFLECT_BONUS);
		}
	}
}
