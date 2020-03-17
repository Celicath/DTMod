package TheDT.cards;

import TheDT.actions.AddAggroAction;
import TheDT.actions.ApplyAggroAction;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class Shift extends AbstractDTCard {
	public static final String RAW_ID = "Shift";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	public static final String RAW_ID_YOU = "ShiftYou";
	public static final String RAW_ID_DRAGON = "ShiftDragon";
	private static final int AGGRO = 7;

	public Shift() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		magicNumber = baseMagicNumber = AGGRO;
		exhaust = true;
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		boolean result = super.canUse(p, m);
		if (result && getDragon() == null) {
			cantUseMessage = dragonNotAvailableMessage();
			return false;
		}
		return result;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = getDragon();

		int outerMagicNumber = magicNumber;
		if (dragon != null) {
			if (upgraded) {
				ArrayList<AbstractCard> choices = new ArrayList<>();
				choices.add(new AbstractDTCard(RAW_ID_YOU, -2, TYPE, COLOR, CardRarity.SPECIAL, TARGET, TauntingStrike.RAW_ID, DTCardTarget.DEFAULT) {
					{
						magicNumber = baseMagicNumber = outerMagicNumber;
					}

					@Override
					public void upgrade() {
					}

					@Override
					public void use(AbstractPlayer p, AbstractMonster m) {
					}

					@Override
					public void onChoseThisOption() {
						addToBot(new AddAggroAction(false, magicNumber));
						addToBot(new ApplyAggroAction());
					}
				});
				choices.add(new AbstractDTCard(RAW_ID_DRAGON, -2, TYPE, COLOR, CardRarity.SPECIAL, TARGET, TauntingStrike.RAW_ID, DTCardTarget.DRAGON_ONLY) {
					{
						magicNumber = baseMagicNumber = outerMagicNumber;
					}

					@Override
					public void upgrade() {
					}

					@Override
					public void use(AbstractPlayer p, AbstractMonster m) {
					}

					@Override
					public void onChoseThisOption() {
						addToBot(new AddAggroAction(true, magicNumber));
						addToBot(new ApplyAggroAction());
					}
				});
				addToBot(new ChooseOneAction(choices));
			} else {
				if (isFrontDragon()) {
					addToBot(new AddAggroAction(false, magicNumber));
				} else {
					addToBot(new AddAggroAction(true, magicNumber));
				}
				addToBot(new ApplyAggroAction());
			}
		}
	}

	public AbstractCard makeCopy() {
		return new Shift();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
