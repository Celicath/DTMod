package TheDT.cards;

import TheDT.actions.EchoTacticAction;
import TheDT.patches.CardColorEnum;
import TheDT.relics.TacticalNote;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static TheDT.patches.CustomTags.DT_TACTIC;

public class EchoTactic extends AbstractDTCard {
	public static final String RAW_ID = "EchoTactic";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	public EchoTactic() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		exhaust = true;
		tags.add(DT_TACTIC);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (upgraded) {
			addToBot(new DrawCardAction(1));
		}
		addToBot(new EchoTacticAction(p.hasRelic(TacticalNote.ID)));
	}

	public AbstractCard makeCopy() {
		return new EchoTactic();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
