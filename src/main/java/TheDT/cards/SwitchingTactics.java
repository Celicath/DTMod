package TheDT.cards;

import TheDT.DTMod;
import TheDT.characters.Dragon;
import TheDT.characters.TheDT;
import TheDT.patches.CardColorEnum;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static TheDT.patches.CustomTags.DT_TACTICS;

public class SwitchingTactics extends AbstractDTCard {
	private static final String RAW_ID = "SwitchingTactics";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.BOTH;

	public SwitchingTactics() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		this.tags.add(DT_TACTICS);
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
			TheDT dtp = (TheDT) p;
			if (dtp.aggro == 0) {
				dtp.setTarget(dtp.target == d ? dtp : d);
			} else {
				magicNumber = Math.abs(dtp.aggro);
				dtp.setAggro(-dtp.aggro);
				for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new StrengthPower(mo, -magicNumber), -magicNumber, true, AbstractGameAction.AttackEffect.NONE));
					if (!mo.hasPower(ArtifactPower.POWER_ID)) {
						AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new GainStrengthPower(mo, magicNumber), magicNumber, true, AbstractGameAction.AttackEffect.NONE));
					}
				}
			}
		}
	}

	public AbstractCard makeCopy() {
		return new SwitchingTactics();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			AlwaysRetainField.alwaysRetain.set(this, true);
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
