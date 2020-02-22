package TheDT.cards;

import TheDT.DTMod;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class FireUp extends AbstractDTCard {
	private static final String RAW_ID = "FireUp";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int STRENGTH = 2;
	private static final int DEXTERITY = 1;
	private static final int UPGRADE_BONUS = 1;

	public FireUp() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseMagicNumber = magicNumber = STRENGTH;
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

		if (dragon != null) {
			addToBot(new VFXAction(dragon, new InflameEffect(dragon), 1.0F));
			addToBot(new ApplyPowerAction(dragon, dragon, new StrengthPower(dragon, magicNumber), magicNumber));
			addToBot(new ApplyPowerAction(dragon, dragon, new DexterityPower(dragon, DEXTERITY), DEXTERITY));
		}
	}

	public AbstractCard makeCopy() {
		return new FireUp();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
