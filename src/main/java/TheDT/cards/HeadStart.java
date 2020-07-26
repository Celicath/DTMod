package TheDT.cards;

import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class HeadStart extends AbstractDTCard {
	public static final String RAW_ID = "HeadStart";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int MAGIC = 2;
	private static final int UPGRADE_BONUS = 1;

	public static final String RAW_ID_ENERGY = "HeadStartEnergy";
	public static final String RAW_ID_DRAW = "HeadStartDraw";

	public HeadStart() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		magicNumber = baseMagicNumber = MAGIC;
		isInnate = true;
		exhaust = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		int magic = magicNumber;

		ArrayList<AbstractCard> choices = new ArrayList<>();
		choices.add(new AbstractDTCard(RAW_ID_ENERGY, -2, TYPE, COLOR, CardRarity.SPECIAL, TARGET, RAW_ID, DTCardTarget.DEFAULT) {
			{
				baseMagicNumber = magicNumber = magic;
			}

			@Override
			public void upgrade() {
				rawDescription = UPGRADE_DESCRIPTION;
				initializeDescription();
			}

			@Override
			public void use(AbstractPlayer p, AbstractMonster m) {
			}

			@Override
			public void onChoseThisOption() {
				this.addToBot(new GainEnergyAction(magicNumber));
			}
		});
		choices.add(new AbstractDTCard(RAW_ID_DRAW, -2, TYPE, COLOR, CardRarity.SPECIAL, TARGET, RAW_ID, DTCardTarget.DEFAULT) {
			{
				baseMagicNumber = magicNumber = magic;
			}

			@Override
			public void upgrade() {
			}

			@Override
			public void use(AbstractPlayer p, AbstractMonster m) {
			}

			@Override
			public void onChoseThisOption() {
				this.addToBot(new DrawCardAction(magicNumber));
			}
		});
		if (upgraded) {
			for (AbstractCard choice : choices) {
				choice.upgrade();
			}
		}
		addToBot(new ChooseOneAction(choices));
	}

	public AbstractCard makeCopy() {
		return new HeadStart();
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
