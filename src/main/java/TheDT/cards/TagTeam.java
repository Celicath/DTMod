package TheDT.cards;

import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.patches.CustomTags;
import TheDT.powers.BondingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TagTeam extends AbstractDTCard {
	public static final String RAW_ID = "TagTeam";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int BONDING = 1;
	private static final int BLOCK = 6;
	private static final int UPGRADE_BONUS = 3;

	public TagTeam() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseBlock = dtBaseDragonBlock = BLOCK;
		magicNumber = baseMagicNumber = BONDING;
		tags.add(CustomTags.DT_BONDING);
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		if (DragonTamer.isFrontDragon()) {
			rawDescription = EXTENDED_DESCRIPTION[0];
		} else {
			rawDescription = DESCRIPTION;
		}
		initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int multiplier = DragonTamer.frontChangedThisTurn ? 2 : 1;
		addToBot(new ApplyPowerAction(p, p, new BondingPower(p, p, magicNumber * multiplier), magicNumber * multiplier));

		if (DragonTamer.isFrontDragon()) {
			addToBot(new GainBlockAction(((DragonTamer) AbstractDungeon.player).dragon, ((DragonTamer) AbstractDungeon.player).dragon, dtDragonBlock * multiplier));
		} else {
			addToBot(new GainBlockAction(p, p, block * multiplier));
		}
	}

	public void triggerOnGlowCheck() {
		if (DragonTamer.frontChangedThisTurn) {
			glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		} else {
			glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	public AbstractCard makeCopy() {
		return new TagTeam();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeDTDragonBlock(UPGRADE_BONUS);
		}
	}
}
