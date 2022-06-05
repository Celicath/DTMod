package TheDT.cards;

import TheDT.actions.DisableResonanceFormAction;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.patches.CustomTags;
import TheDT.powers.BondingPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class Training extends AbstractDTCard {
	public static final String RAW_ID = "Training";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.BOTH;

	private static final int NEW_COST = 0;
	private static final int POWER = 1;

	public Training() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = POWER;
		tags.add(CustomTags.DT_BONDING);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		addToBot(new DisableResonanceFormAction(true));
		addToBot(new VFXAction(p, new InflameEffect(p), dragon == null ? 0.5F : 0.1F));
		if (dragon != null) {
			addToBot(new VFXAction(dragon, new InflameEffect(dragon), 0.4F));
		}
		addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
		if (dragon != null) {
			addToBot(new ApplyPowerAction(dragon, dragon, new StrengthPower(dragon, this.magicNumber), this.magicNumber));
			addToBot(new ApplyPowerAction(p, p, new BondingPower(p, p, 1), 1));
		}
		addToBot(new DisableResonanceFormAction(false));
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
