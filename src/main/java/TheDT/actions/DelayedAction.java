package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DelayedAction extends AbstractGameAction {
	private AbstractGameAction action;

	public DelayedAction(AbstractGameAction action) {
		this.setValues(AbstractDungeon.player, AbstractDungeon.player);
		this.actionType = AbstractGameAction.ActionType.WAIT;
		this.duration = Settings.ACTION_DUR_FAST;
		this.action = action;
	}

	public void update() {
		this.isDone = true;
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			return;
		}
		addToBot(action);
	}
}
