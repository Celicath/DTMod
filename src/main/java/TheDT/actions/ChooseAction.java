package TheDT.actions;

import TheDT.cards.ChooseOption;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ChooseAction extends AbstractGameAction {
	private String message;
	private CardGroup options;

	public ChooseAction(String message, ChooseOption... optionCards) {
		this.setValues(AbstractDungeon.player, AbstractDungeon.player, 1);
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
		this.message = message;
		this.duration = Settings.ACTION_DUR_FASTER;

		options = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
		for (ChooseOption c : optionCards) {
			options.addToTop(c);
		}
	}

	@Override
	public void update() {
		if (options == null || options.size() == 0) {
			this.isDone = true;
			return;
		}
		if (this.duration == Settings.ACTION_DUR_FASTER) {
			if (options.size() > 1) {
				AbstractDungeon.gridSelectScreen.open(options, 1, this.message, false);
				this.tickDuration();
				return;
			} else {
				options.getTopCard().use(AbstractDungeon.player, null);
				this.isDone = true;
				return;
			}
		} else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			AbstractCard pick = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
			AbstractDungeon.gridSelectScreen.selectedCards.clear();
			pick.use(AbstractDungeon.player, null);
			this.isDone = true;
		}
		this.tickDuration();
	}
}
