package TheDT.cards;

import TheDT.DTMod;
import TheDT.characters.Dragon;
import TheDT.optionals.FriendlyMinionHelper;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MagicField extends AbstractDTCard {
	private static final String RAW_ID = "MagicField";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ALL;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 10;
	private static final int UPGRADE_BONUS = 3;

	public MagicField() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

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
		Dragon dragon = getDragon();

		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block, true));
		if (dragon != null) {
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(dragon, dragon, dtDragonBlock, true));
		}

		if (DTMod.isFriendlyMinionsLoaded) {
			FriendlyMinionHelper.giveFriendlyMinionsBlock(magicNumber);
		}

		for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
			if (!mo.isDeadOrEscaped()) {
				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(mo, mo, magicNumber));
			}
		}

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
