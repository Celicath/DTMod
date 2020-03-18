package TheDT.cards;

import TheDT.actions.TriggerWeakMarksAction;
import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.PressurePointEffect;

public class Overwhelm extends AbstractDTCard {
	public static final String RAW_ID = "Overwhelm";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int WEAK = 2;
	private static final int UPGRADE_BONUS = 1;
	private static final int RATIO = 2;

	public Overwhelm() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);

		magicNumber = baseMagicNumber = WEAK;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (m != null) {
			addToBot(new VFXAction(new PressurePointEffect(m.hb.cX, m.hb.cY)));
		}

		addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
		addToBot(new TriggerWeakMarksAction(RATIO));
	}

	public AbstractCard makeCopy() {
		return new Overwhelm();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
