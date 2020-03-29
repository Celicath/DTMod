package TheDT.actions;

import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RandomAttackerDamageAction extends AbstractGameAction {
	int damage;
	int dragonDamage;

	public RandomAttackerDamageAction(AbstractMonster m, int damage, int dragonDamage) {
		this.target = m;
		this.damage = damage;
		this.dragonDamage = dragonDamage;
	}

	public void update() {
		isDone = true;
		if (target == null)
			return;

		AbstractPlayer p = AbstractDungeon.player;
		Dragon d = AbstractDTCard.getLivingDragon();
		if (AbstractDungeon.cardRandomRng.randomBoolean() || d == null) {
			addToTop(new DamageAction(target, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AttackEffect.SLASH_DIAGONAL));
			addToTop(new FastAnimateFastAttackAction(p));
		} else {
			addToTop(new DamageAction(target, new DamageInfo(d, dragonDamage, DamageInfo.DamageType.NORMAL), AttackEffect.SLASH_HEAVY));
			addToTop(new FastAnimateFastAttackAction(d));
		}
	}
}
