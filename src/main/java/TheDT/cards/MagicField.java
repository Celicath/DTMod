package TheDT.cards;

import TheDT.DTModMain;
import TheDT.actions.DisableResonanceFormAction;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.optionals.FriendlyMinionHelper;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MagicField extends AbstractDTCard {
	public static final String RAW_ID = "MagicField";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ALL;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 9;
	private static final int UPGRADE_BONUS = 3;

	public MagicField() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = POWER;
		baseBlock = POWER;
		dtBaseDragonBlock = POWER;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		rawDescription = DESCRIPTION;
		if (isBlockModified) {
			rawDescription += EXTENDED_DESCRIPTION[0];
		}
		if (isDTDragonBlockModified) {
			rawDescription += EXTENDED_DESCRIPTION[1];
		}
		initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		addToBot(new DisableResonanceFormAction(true));
		addToBot(new GainBlockAction(p, p, block, true));
		if (dragon != null) {
			addToBot(new GainBlockAction(dragon, dragon, dtDragonBlock, true));
		}

		if (DTModMain.isFriendlyMinionsLoaded) {
			FriendlyMinionHelper.giveFriendlyMinionsBlock(magicNumber);
		}

		for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
			if (!mo.isDeadOrEscaped()) {
				addToBot(new GainBlockAction(mo, mo, magicNumber));
			}
		}
		addToBot(new DisableResonanceFormAction(false));

		rawDescription = DESCRIPTION;
		initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new MagicField();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
			upgradeBlock(UPGRADE_BONUS);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
