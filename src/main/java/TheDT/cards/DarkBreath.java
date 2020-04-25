package TheDT.cards;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.patches.CardColorEnum;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class DarkBreath extends AbstractDTCard {
	public static final String RAW_ID = "DarkBreath";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final AbstractDTCard.DTCardTarget DT_CARD_TARGET = DTCardTarget.DRAGON_ONLY;

	private static final int DAMAGE = 4;
	private static final int UPGRADE_BONUS = 1;
	private static final int WEAK = 1;
	private static final int WEAK_BONUS = 1;
	private static final int HIT = 2;

	public DarkBreath() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		dtBaseDragonDamage = DAMAGE;
		magicNumber = baseMagicNumber = WEAK;
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
		Dragon dragon = DragonTamer.getLivingDragon();

		if (dragon != null) {
			for (int i = 0; i < HIT; i++) {
				if (m != null) {
					addToBot(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.FIRE) {
						boolean init = false;

						@Override
						public void update() {
							if (!init) {
								init = true;
								color = new Color(0.3f, 0.7f, 0.7f, 1.0f);
							}
							super.update();
						}

					}, 0.15f));
				}
				addToBot(new DamageAction(m, new DamageInfo(dragon, dtDragonDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
			}
			addToBot(new ApplyPowerAction(m, dragon, new WeakPower(m, magicNumber, false), magicNumber));
		}
	}

	public AbstractCard makeCopy() {
		return new DarkBreath();
	}

	public void upgrade() {
		upgradeName();
		upgradeDTDragonDamage(UPGRADE_BONUS);
		upgradeMagicNumber(WEAK_BONUS);
	}
}
