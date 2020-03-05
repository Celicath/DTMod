package TheDT.cards;

import TheDT.patches.CardColorEnum;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Exhume;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static TheDT.patches.CustomTags.DT_TACTICS;

public class RefreshTactics extends AbstractDTCard {
	public static final String RAW_ID = "RefreshTactics";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 1;
	private static final int UPGRADE_BONUS = 1;

	public RefreshTactics() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		exhaust = true;
		baseMagicNumber = magicNumber = POWER;
		tags.add(DT_TACTICS);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new FetchAction(
				AbstractDungeon.player.exhaustPile,
				(c -> !c.cardID.equals(Exhume.ID) && !c.cardID.equals(cardID)),
				magicNumber));
	}

	public AbstractCard makeCopy() {
		return new RefreshTactics();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
