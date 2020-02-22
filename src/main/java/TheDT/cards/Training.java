package TheDT.cards;

import TheDT.DTMod;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import TheDT.powers.BondingPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class Training extends AbstractDTCard {
	private static final String RAW_ID = "Training";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	private static final int NEW_COST = 0;
	private static final int POWER = 1;

	public Training() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = POWER;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = getDragon();

		AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new InflameEffect(p), dragon == null ? 0.8F : 0.4F));
		if (dragon != null) {
			AbstractDungeon.actionManager.addToBottom(new VFXAction(dragon, new InflameEffect(dragon), 0.4F));
		}
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
		if (dragon != null) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(dragon, dragon, new StrengthPower(dragon, this.magicNumber), this.magicNumber));
		}
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BondingPower(p, p, 1), 1));
	}

	public AbstractCard makeCopy() {
		return new Training();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(NEW_COST);
		}
	}
}
