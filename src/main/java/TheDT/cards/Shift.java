package TheDT.cards;

import TheDT.Interfaces.ChooseAttackerCard;
import TheDT.actions.AddAggroAction;
import TheDT.actions.ApplyAggroAction;
import TheDT.actions.ChooseAttackerAction;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class Shift extends AbstractDTCard implements ChooseAttackerCard {
	public static final String RAW_ID = "Shift";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int AGGRO = 7;

	public Shift() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		magicNumber = baseMagicNumber = AGGRO;
		selfRetain = true;
		exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (upgraded) {
			ArrayList<AbstractCard> choices = new ArrayList<>();
			addToBot(new ChooseAttackerAction(this, m, false));
		} else {
			if (DragonTamer.isFrontDragon()) {
				addToBot(new AddAggroAction(false, magicNumber));
			} else {
				addToBot(new AddAggroAction(true, magicNumber));
			}
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Shift();
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public void onChoseAttacker(AbstractCreature attacker, AbstractMonster m) {
		addToBot(new AddAggroAction(attacker instanceof Dragon, magicNumber));
		addToBot(new ApplyAggroAction());
	}

	@Override
	public String hoverText(AbstractCreature hovered, AbstractMonster m) {
		return ApplyAggroAction.getAggroText(magicNumber);
	}
}
