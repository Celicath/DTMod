package TheDT.actions;

import TheDT.DTMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class DiscardAnyNumberAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(DTMod.makeID("DiscardAnyNumberAction"));
	public static final String[] TEXT = uiStrings.TEXT;

	public DiscardAnyNumberAction(AbstractCreature source) {
		this.setValues(AbstractDungeon.player, source, -1);
		this.actionType = ActionType.CARD_MANIPULATION;
	}

	public void update() {
		if (this.duration == 0.5F) {
			if (AbstractDungeon.player.hand.size() == 0) {
				isDone = true;
				return;
			}
			AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);

			AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
			this.tickDuration();
		} else {
			if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
				if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
					Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

					while (var1.hasNext()) {
						AbstractCard c = (AbstractCard) var1.next();
						AbstractDungeon.player.hand.moveToDiscardPile(c);
						GameActionManager.incrementDiscard(false);
						c.triggerOnManualDiscard();
					}
				}

				AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			}

			this.tickDuration();
		}
	}
}
