package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlueScale extends AbstractDTCard {
	public static final String RAW_ID = "BlueScale";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int BLOCK = 13;

	public static HardSkin preview = new HardSkin();
	public static HardSkin previewPlus = new HardSkin() {
		{
			this.upgrade();
		}
	};

	public BlueScale() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonBlock = BLOCK;

		cardsToPreview = preview;
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		boolean result = super.canUse(p, m);
		if (result && DragonTamer.getLivingDragon() == null) {
			cantUseMessage = dragonNotAvailableMessage();
			return false;
		}
		return result;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		if (dragon != null) {
			addToBot(new GainBlockAction(dragon, dragon, dtDragonBlock));
			addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy(), 1));
		}
	}

	public AbstractCard makeCopy() {
		return new BlueScale();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			cardsToPreview = previewPlus;
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
