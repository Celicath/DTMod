package TheDT.cards;

import TheDT.actions.AddAggroAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static TheDT.patches.CustomTags.DT_TACTICS;

public class RunningTactic extends AbstractDTCard {
	public static final String RAW_ID = "RunningTactic";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 1;

	public RunningTactic() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		magicNumber = baseMagicNumber = POWER;
		tags.add(DT_TACTICS);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DrawCardAction(p, magicNumber));
		addToBot(new AddAggroAction(false, 2));
	}

	public AbstractCard makeCopy() {
		return new RunningTactic();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
