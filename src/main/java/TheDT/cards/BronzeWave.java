package TheDT.cards;

import TheDT.patches.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.IronWaveEffect;

public class BronzeWave extends AbstractDTCard {
	public static final String RAW_ID = "BronzeWave";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DEFAULT;

	private static final int DAMAGE = 8;
	private static final int UPGRADE_DAMAGE = 2;
	private static final int BLOCK = 4;
	private static final int UPGRADE_BLOCK = 2;

	public BronzeWave() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;
		baseBlock = BLOCK;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new GainBlockAction(p, p, block, true));
		if (p != null && m != null) {
			addToBot(new VFXAction(new IronWaveEffect(p.hb.cX, p.hb.cY, m.hb.cX), 0.45F));
		}
		addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
	}

	public AbstractCard makeCopy() {
		return new BronzeWave();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
			upgradeBlock(UPGRADE_BLOCK);
		}
	}
}
