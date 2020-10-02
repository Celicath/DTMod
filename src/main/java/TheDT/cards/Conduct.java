package TheDT.cards;

import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Conduct extends AbstractDTCard {
	public static final String RAW_ID = "Conduct";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int DRAW = 2;
	private static final int UPGRADE_BONUS = 1;
	private static final int COND = 5;

	public Conduct() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = DRAW;
	}

	public static boolean checkCondition() {
		return DragonTamer.getAggro() >= COND || DragonTamer.getAggro() <= -COND;
	}

	public void triggerOnGlowCheck() {
		glowColor = checkCondition() ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy() : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new AbstractGameAction() {
			{
				this.amount = magicNumber;
			}

			@Override
			public void update() {
				if (checkCondition()) {
					addToTop(new DrawCardAction(amount));
				}
				isDone = true;
			}
		});
	}

	public AbstractCard makeCopy() {
		return new Conduct();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
