package TheDT.cards;

import TheDT.DTMod;
import TheDT.actions.DragonChangeStanceAction;
import TheDT.characters.Dragon;
import TheDT.patches.CardColorEnum;
import TheDT.powers.DragonLoseStancePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.WrathStance;

public class Outburst extends AbstractDTCard {
	private static final String RAW_ID = "Outburst";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int NEW_COST = 0;
	private static final int EXIT_TURN = 2;

	public Outburst() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
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

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon d = getDragon();
		if (d != null) {
			addToBot(new DragonChangeStanceAction(d, WrathStance.STANCE_ID));
			addToBot(new ApplyPowerAction(d, d, new DragonLoseStancePower(d, EXIT_TURN), EXIT_TURN));
		}
	}

	public AbstractCard makeCopy() {
		return new Outburst();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(NEW_COST);
		}
	}
}
