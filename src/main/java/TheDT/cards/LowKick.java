package TheDT.cards;

import TheDT.DTMod;
import TheDT.actions.LowKickAction;
import TheDT.actions.PunisherAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class LowKick extends AbstractDTCard {
	private static final String RAW_ID = "LowKick";
	public static final String ID = DTMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = DTMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int POWER = 7;
	private static final int UPGRADE_POWER = 3;

	public LowKick() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		baseDamage = POWER;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new LowKickAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
	}

	public void triggerOnGlowCheck() {
		this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

		for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!m.isDeadOrEscaped() && m.hasPower(WeakPower.POWER_ID)) {
				this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
				break;
			}
		}
	}

	public AbstractCard makeCopy() {
		return new LowKick();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_POWER);
		}
	}
}
