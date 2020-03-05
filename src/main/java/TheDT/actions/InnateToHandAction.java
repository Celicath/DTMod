package TheDT.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javafx.util.Pair;

import java.util.ArrayList;

public class InnateToHandAction extends AbstractGameAction {
	private AbstractPlayer p;

	public InnateToHandAction() {
		p = AbstractDungeon.player;
		actionType = ActionType.CARD_MANIPULATION;
		startDuration = Settings.ACTION_DUR_MED;
		duration = startDuration;
	}

	public void update() {
		if (this.duration == startDuration) {
			ArrayList<Pair<CardGroup, AbstractCard>> list = new ArrayList<>();
			for (AbstractCard c : p.drawPile.group) {
				if (c.isInnate) {
					list.add(new Pair<>(p.drawPile, c));
				}
			}

			for (AbstractCard c : p.discardPile.group) {
				if (c.isInnate) {
					list.add(new Pair<>(p.discardPile, c));
				}
			}

			for (Pair<CardGroup, AbstractCard> pair : list) {
				doAction(pair.getKey(), pair.getValue());
			}
		}
		tickDuration();
	}

	private void doAction(CardGroup pile, AbstractCard card) {
		if (this.p.hand.size() < BaseMod.MAX_HAND_SIZE) {
			card.lighten(true);
			card.unhover();
			card.setAngle(0.0F);
			card.drawScale = 0.12F;
			card.targetDrawScale = 0.75F;
			card.current_x = pile == p.discardPile ? CardGroup.DISCARD_PILE_X : CardGroup.DRAW_PILE_X;
			card.current_y = pile == p.discardPile ? CardGroup.DISCARD_PILE_Y : CardGroup.DRAW_PILE_Y;
			pile.removeCard(card);
			AbstractDungeon.player.hand.addToTop(card);
			AbstractDungeon.player.hand.refreshHandLayout();
			AbstractDungeon.player.hand.applyPowers();
		}
	}
}
