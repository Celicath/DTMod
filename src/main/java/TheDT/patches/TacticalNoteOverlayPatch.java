package TheDT.patches;

import TheDT.relics.TacticalNote;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;

import java.util.ArrayList;

public class TacticalNoteOverlayPatch {
	static ArrayList<TacticalNote> tacticalNotes = new ArrayList<>();

	public static void renderAndUpdateRelic(SpriteBatch sb, AbstractCard c, int index) {
		TacticalNote tn;
		if (tacticalNotes.size() <= index) {
			tn = (TacticalNote) AbstractDungeon.player.getRelic(TacticalNote.ID).makeCopy();
			tn.scale = 0.7f;
			tacticalNotes.add(tn);
		} else {
			tn = tacticalNotes.get(index);
		}
		tn.currentX = c.current_x;
		tn.currentY = c.current_y + 200.0f * Settings.scale;
		tn.hb.hovered = false;
		tn.render(sb);

		// TODO: move this part to update()?
		tn.hb.move(tn.currentX, tn.currentY);
		tn.hb.update();
		if (tn.hb.hovered) {
			for (int i = 0; i < TacticalNote.tacticCardsId.length; i++) {
				if (c.cardID.equals(TacticalNote.tacticCardsId[i])) {
					float x = InputHelper.mX + (InputHelper.mX < 1400.0F * Settings.scale ? 60.0F : -350.0F) * Settings.scale;
					float y = InputHelper.mY + 100.0F * Settings.scale;
					String body = tn.DESCRIPTIONS[2 + i];
					TipHelper.render(sb);
					TipHelper.renderGenericTip(x, y, tn.name, body);
					break;
				}
			}
		}
		tn.hb.render(sb);
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "render")
	public static class RewardPatch {
		@SpirePostfixPatch
		public static void Postfix(CardRewardScreen __instance, SpriteBatch sb) {
			if (!PeekButton.isPeeking && AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
				int index = 0;
				for (AbstractCard c : __instance.rewardGroup) {
					if (c.hasTag(CustomTags.DT_TACTIC)) {
						renderAndUpdateRelic(sb, c, index);
						index++;
					}
				}
			}
		}
	}

	@SpirePatch(clz = ShopScreen.class, method = "renderCardsAndPrices")
	public static class ShopPatch {
		@SpirePostfixPatch
		public static void Postfix(ShopScreen __instance, SpriteBatch sb) {
			if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
				int index = 0;
				for (AbstractCard c : __instance.coloredCards) {
					if (c.hasTag(CustomTags.DT_TACTIC)) {
						renderAndUpdateRelic(sb, c, index);
						index++;
					}
				}
			}
		}
	}
}