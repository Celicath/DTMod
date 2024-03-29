package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import TheDT.relics.TacticalNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static TheDT.patches.CustomTags.DT_TACTIC;

public class SwitchingTactic extends AbstractDTCard {
	public static final String RAW_ID = "SwitchingTactic";
	public static final int COST = 0;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.BOTH;

	private static final int NOTE_BONUS = 2;

	public SwitchingTactic() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseMagicNumber = 0;
		tags.add(DT_TACTIC);
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
			magicNumber = NOTE_BONUS;
			if (AbstractDungeon.player instanceof DragonTamer) {
				magicNumber += Math.abs(((DragonTamer) AbstractDungeon.player).aggro);
			}
			isMagicNumberModified = true;
			rawDescription = (upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION) + EXTENDED_DESCRIPTION[0];
			initializeDescription();
		}
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

	public void use(AbstractPlayer p, AbstractMonster m) {
		Dragon d = DragonTamer.getLivingDragon();
		if (d != null) {
			DragonTamer dtp = (DragonTamer) p;
			if (dtp.aggro == 0) {
				dtp.setFront(dtp.front == d ? dtp : d);
			} else {
				dtp.setAggro(-dtp.aggro);
			}
			magicNumber = Math.abs(dtp.aggro);
			if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
				magicNumber += NOTE_BONUS;
			}
			if (magicNumber > 0) {
				addToBot(new AbstractGameAction() {
					@Override
					public void update() {
						for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
							addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -magicNumber), -magicNumber, true, AbstractGameAction.AttackEffect.NONE));
							if (!mo.hasPower(ArtifactPower.POWER_ID)) {
								addToBot(new ApplyPowerAction(mo, p, new GainStrengthPower(mo, magicNumber), magicNumber, true, AbstractGameAction.AttackEffect.NONE));
							}
						}
						isDone = true;
					}
				});
			}
		}
		rawDescription = upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION;
		initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new SwitchingTactic();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			selfRetain = true;
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
