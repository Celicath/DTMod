package TheDT.cards;

import TheDT.DTMod;
import TheDT.patches.CardColorEnum;
import TheDT.powers.StackedDeckPower;
import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class StackedDeck extends CustomCard {
	private static final String RAW_ID = "StackedDeck";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 2;

	public StackedDeck() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
		this.tags.add(BaseModCardTags.FORM);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (upgraded) {
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
		}
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StackedDeckPower(p, p, this.magicNumber), this.magicNumber));
	}

	public AbstractCard makeCopy() {
		return new StackedDeck();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
