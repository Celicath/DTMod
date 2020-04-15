package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javafx.util.Pair;

import java.util.ArrayList;

public class ExhaustRandomStatusAction extends AbstractGameAction {
	private AbstractPlayer p;

	public ExhaustRandomStatusAction(int amount) {
		p = AbstractDungeon.player;
		duration = Settings.ACTION_DUR_FAST;
		actionType = ActionType.CARD_MANIPULATION;
		this.amount = amount;
	}

	public void update() {
		ArrayList<Pair<AbstractCard, CardGroup>> list = new ArrayList<>();
		CardGroup[] groups = new CardGroup[]{p.drawPile, p.discardPile, p.hand};
		if (this.duration == Settings.ACTION_DUR_FAST) {
			for (CardGroup cg : groups) {
				for (AbstractCard c : cg.group) {
					if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
						list.add(new Pair<>(c, cg));
					}
				}
			}
			if (amount > list.size()) {
				amount = list.size();
			}
			for (int i = 0; i < amount; i++) {
				Pair<AbstractCard, CardGroup> pair = list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
				AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(pair.getKey(), pair.getValue()) {
					@Override
					public void update() {
						super.update();
						if (pair.getValue() == AbstractDungeon.player.drawPile) {
							pair.getKey().target_x = Settings.WIDTH * 0.4f;
						} else if (pair.getValue() == AbstractDungeon.player.discardPile) {
							pair.getKey().target_x = Settings.WIDTH * 0.6f;
						} else {
							return;
						}
						pair.getKey().target_y = Settings.HEIGHT / 2.0f;
					}
				});
				list.remove(pair);
			}
			isDone = true;
		}
	}
}
