package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class StealAllBlockAction extends AbstractGameAction {
	private static final float DUR = 0.25F;

	public StealAllBlockAction(AbstractCreature target, AbstractCreature source) {
		setValues(target, source);
		actionType = ActionType.BLOCK;
		duration = 0.25F;
	}

	public void update() {
		if (!target.isDying && !target.isDead && duration == 0.25F && target.currentBlock > 0) {
			AbstractDungeon.effectList.add(new FlashAtkImgEffect(source.hb.cX, source.hb.cY, AttackEffect.SHIELD));
			source.addBlock(target.currentBlock);
			target.loseBlock();

			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				c.applyPowers();
			}
		}

		tickDuration();
	}
}