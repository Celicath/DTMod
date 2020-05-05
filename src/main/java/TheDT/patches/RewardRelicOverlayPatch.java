package TheDT.patches;

import TheDT.relics.TacticalNote;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;

public class RewardRelicOverlayPatch {
	static TacticalNote tacticalNote = null;

	@SpirePatch(clz = CardRewardScreen.class, method = "render")
	public static class ShowRelic {
		@SpirePostfixPatch
		public static void Postfix(CardRewardScreen __instance, SpriteBatch sb) {
			if (!PeekButton.isPeeking && AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
				for (AbstractCard c : __instance.rewardGroup) {
					if (c.hasTag(CustomTags.DT_TACTIC)) {
						if (tacticalNote == null) {
							tacticalNote = (TacticalNote) AbstractDungeon.player.getRelic(TacticalNote.ID).makeCopy();
							tacticalNote.scale = 0.7f;
						}
						tacticalNote.currentX = c.target_x;
						tacticalNote.currentY = c.target_y + 200.0f * Settings.scale;
						tacticalNote.render(sb);
					}
				}
			}
		}
	}
}
