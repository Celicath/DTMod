package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.patches.CustomTags;
import TheDT.powers.HarmonizePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Harmonize extends AbstractDTCard {
	public static final String RAW_ID = "Harmonize";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.BOTH;

	private static final int MAGIC = 1;

	public Harmonize() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseMagicNumber = magicNumber = MAGIC;
		tags.add(CustomTags.DT_BONDING);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new ApplyPowerAction(p, p, new HarmonizePower(p, magicNumber)));
		Dragon d = DragonTamer.getLivingDragon();
		if (d != null) {
			addToBot(new ApplyPowerAction(d, d, new HarmonizePower(d, magicNumber)));
		}
	}

	public AbstractCard makeCopy() {
		return new Harmonize();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			isInnate = true;
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
