package TheDT.cards;

import TheDT.actions.HasteAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Haste extends AbstractDTCard {
	public static final String RAW_ID = "Haste";
	private static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	public Haste() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		exhaust = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new HasteAction(upgraded));
	}

	public void triggerOnGlowCheck() {
		boolean glow = false;

		for (AbstractCard c : AbstractDungeon.player.hand.group) {
			if (c.type == CardType.POWER && c != this) {
				glow = true;
				break;
			}
		}

		if (glow) {
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		} else {
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	public AbstractCard makeCopy() {
		return new Haste();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
