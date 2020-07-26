package TheDT.cards;

import TheDT.patches.CardColorEnum;
import TheDT.relics.TacticalNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static TheDT.patches.CustomTags.DT_TACTIC;

public class OpeningTactic extends AbstractDTCard {
	public static final String RAW_ID = "OpeningTactic";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 11;
	private static final int UPGRADE_BONUS = 4;
	private static final int MAGIC = 1;

	public OpeningTactic() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = POWER;
		baseMagicNumber = MAGIC;
		magicNumber = this.baseMagicNumber;
		isInnate = true;
		tags.add(DT_TACTIC);
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
			magicNumber = baseMagicNumber + 1;
			isMagicNumberModified = true;
		}
	}

	public void triggerOnGlowCheck() {
		boolean glow = false;

		for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!m.isDeadOrEscaped() && m.currentHealth == m.maxHealth) {
				glow = true;
				break;
			}
		}

		if (glow) {
			glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		} else {
			glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		if (m.currentHealth == m.maxHealth) {
			addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));
		}
	}

	public AbstractCard makeCopy() {
		return new OpeningTactic();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_BONUS);
		}
	}
}
