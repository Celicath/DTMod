package TheDT.actions;

import TheDT.DTModMain;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class PredictionAction extends AbstractGameAction {
	private AbstractPlayer p;
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("PredictionAction"));
	public static final String[] TEXT = uiStrings.TEXT;

	public PredictionAction() {
		p = AbstractDungeon.player;
		duration = Settings.ACTION_DUR_MED;
		actionType = ActionType.CARD_MANIPULATION;
		amount = 1;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_MED) {
			CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

			for (AbstractCard card : p.drawPile.group) {
				tmp.addToBottom(card);
			}
			tmp.sortAlphabetically(true);
			tmp.sortByRarityPlusStatusCardType(true);

			if (tmp.size() == 0) {
				this.isDone = true;
			} else {
				AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);

				this.tickDuration();
			}
		} else {
			if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
				for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
					if (c.cost > 0) {
						c.freeToPlayOnce = true;
					}
					if (AbstractDungeon.player.drawPile.group.remove(c)) {
						AbstractDungeon.player.drawPile.addToTop(c);
					}
				}
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
			}

			this.tickDuration();
		}
	}
}
