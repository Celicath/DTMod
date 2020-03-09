package TheDT.actions;

import TheDT.DTModMain;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;

public class AddAggroAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("Aggro"));
	public static final String[] TEXT = uiStrings.TEXT;
	boolean isDragon;

	public AddAggroAction(boolean isDragon, int delta) {
		actionType = AbstractGameAction.ActionType.WAIT;
		duration = Settings.ACTION_DUR_FAST;
		this.isDragon = isDragon;
		amount = delta;
	}

	public void update() {
		this.isDone = true;
		if (isDragon) {
			DTModMain.dragonAggro += amount;
		} else {
			DTModMain.yourAggro += amount;
		}
	}
}
