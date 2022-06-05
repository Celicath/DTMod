package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class DragonDance extends AbstractDTCard {
	public static final String RAW_ID = "DragonDance";
	private static final int COST = 2;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.DRAGON;

	private static final int STRENGTH = 4;
	private static final int DEXTERITY = 2;
	private static final int UPGRADE_BONUS = 2;

	public DragonDance() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseMagicNumber = magicNumber = STRENGTH;
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		boolean result = super.canUse(p, m);
		if (result && DragonTamer.getLivingDragon() == null) {
			cantUseMessage = dragonNotAvailableMessage();
			return false;
		}
		return result;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon dragon = DragonTamer.getLivingDragon();

		if (dragon != null) {
			addToBot(new VFXAction(dragon, new InflameEffect(dragon), 1.0F));
			addToBot(new ApplyPowerAction(dragon, dragon, new StrengthPower(dragon, magicNumber), magicNumber));
			addToBot(new ApplyPowerAction(dragon, dragon, new DexterityPower(dragon, DEXTERITY), DEXTERITY));
		}
	}

	public AbstractCard makeCopy() {
		return new DragonDance();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
			if (UPGRADE_DESCRIPTION != null) {
				rawDescription = UPGRADE_DESCRIPTION;
				initializeDescription();
			}
		}
	}
}
