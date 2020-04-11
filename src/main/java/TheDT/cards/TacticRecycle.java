package TheDT.cards;

import TheDT.patches.CardColorEnum;
import TheDT.relics.TacticalNote;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Exhume;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static TheDT.patches.CustomTags.DT_TACTIC;

public class TacticRecycle extends AbstractDTCard {
	public static final String RAW_ID = "TacticRecycle";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 1;
	private static final int UPGRADE_BONUS = 1;

	public TacticRecycle() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		exhaust = true;
		baseMagicNumber = magicNumber = POWER;
		tags.add(DT_TACTIC);
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
			rawDescription = upgraded ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0];
			initializeDescription();
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new FetchAction(
				AbstractDungeon.player.exhaustPile,
				(c -> !c.cardID.equals(Exhume.ID) && !c.cardID.equals(cardID)),
				magicNumber,
				(cards) -> {
					if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
						for (AbstractCard c : cards) {
							c.retain = true;
						}
					}
				}));
	}

	public AbstractCard makeCopy() {
		return new TacticRecycle();
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
