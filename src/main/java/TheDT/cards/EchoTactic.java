package TheDT.cards;

import TheDT.actions.EchoTacticAction;
import TheDT.patches.CardColorEnum;
import TheDT.relics.TacticalNote;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static TheDT.patches.CustomTags.DT_TACTIC;

public class EchoTactic extends AbstractDTCard {
	public static final String RAW_ID = "EchoTactic";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.YOU;

	private static final int MAGIC = 1;
	private static final int UPGRADE_BONUS = 1;

	public EchoTactic() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		exhaust = true;
		magicNumber = baseMagicNumber = MAGIC;
		tags.add(DT_TACTIC);
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
			setCostForTurn(-9);
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new EchoTacticAction(magicNumber));
	}

	public AbstractCard makeCopy() {
		return new EchoTactic();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
