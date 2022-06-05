package TheDT.cards;

import TheDT.patches.CardColorEnum;
import TheDT.vfx.UpgradeAllCardEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SealedStrike extends AbstractDTCard {
	public static final String RAW_ID = "SealedStrike";
	private static final int COST = 1;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.DT_ORANGE;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
	private static final DTCardUser DT_CARD_TARGET = DTCardUser.YOU;

	private static final int DAMAGE = 10;
	private static final int UPGRADE_BONUS = 2;

	public SealedStrike() {
		super(RAW_ID, COST, TYPE, COLOR, RARITY, TARGET, DT_CARD_TARGET);
		baseDamage = DAMAGE;

		tags.add(CardTags.STRIKE);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
	}

	@Override
	public AbstractCard makeCopy() {
		return new SealedStrike();
	}

	@Override
	public boolean canUpgrade() {
		return timesUpgraded < 5;
	}

	@Override
	public void upgrade() {
		if (timesUpgraded < 5) {
			timesUpgraded++;
			upgraded = true;
			upgradeDamage(UPGRADE_BONUS);
			name = cardStrings.NAME + "+" + timesUpgraded;
			initializeTitle();

			if (timesUpgraded == 5 && (AbstractDungeon.player != null && AbstractDungeon.player.masterDeck.contains(this))) {
				AbstractDungeon.topLevelEffectsQueue.add(new UpgradeAllCardEffect());
			}
		}
	}
}
