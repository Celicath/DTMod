package TheDT.cards;

import TheDT.DTMod;
import TheDT.characters.TheDT;
import TheDT.patches.CardColorEnum;
import TheDT.relics.BasicTextbook;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TargetDefense extends AbstractDTCard {
	private static final String RAW_ID = "TargetDefense";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 5;
	private static final int UPGRADE_BONUS = 3;

	public TargetDefense() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseBlock = dtBaseDragonBlock = POWER;
		tags.add(BaseModCardTags.BASIC_DEFEND);
	}

	@Override
	public void applyPowers() {
		int blockModifier = 0;
		for (AbstractRelic r : AbstractDungeon.player.relics) {
			if (r instanceof BasicTextbook) {
				blockModifier += BasicTextbook.BONUS;
			}
		}
		baseBlock += blockModifier;
		dtBaseDragonBlock += blockModifier;
		super.applyPowers();
		if (blockModifier > 0) {
			baseBlock -= blockModifier;
			dtBaseDragonBlock -= blockModifier;
			isBlockModified = isDTDragonBlockModified = true;
		}

		if (isFrontDragon()) {
			rawDescription = EXTENDED_DESCRIPTION[0];
		} else {
			rawDescription = DESCRIPTION;
		}
		initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (AbstractDungeon.player instanceof TheDT && ((TheDT) AbstractDungeon.player).front == ((TheDT) AbstractDungeon.player).dragon) {
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(((TheDT) AbstractDungeon.player).dragon, ((TheDT) AbstractDungeon.player).dragon, dtDragonBlock));
		} else {
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
		}
	}

	public AbstractCard makeCopy() {
		return new TargetDefense();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
