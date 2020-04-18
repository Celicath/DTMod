package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.relics.TacticalNote;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;

import static TheDT.patches.CustomTags.DT_TACTIC;

public class BizarreTactic extends AbstractDTCard {
	public static final String RAW_ID = "BizarreTactic";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int POWER = 10;
	private static final int UPGRADE_BONUS = 4;
	private static final int REFLECT = 4;
	private static final int REFLECT_BONUS = 2;

	public BizarreTactic() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseBlock = POWER;
		magicNumber = baseMagicNumber = REFLECT;
		tags.add(DT_TACTIC);
	}

	@Override
	public void applyPowers() {
		if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
			baseBlock += 2;
		}
		super.applyPowers();
		if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
			baseBlock -= 2;
			magicNumber = baseMagicNumber + 2;
			isBlockModified = true;
			isMagicNumberModified = true;
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster m) {
		if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
			baseBlock += 2;
		}
		super.calculateCardDamage(m);
		if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
			baseBlock -= 2;
			magicNumber = baseMagicNumber + 2;
			isBlockModified = true;
			isMagicNumberModified = true;
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

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
