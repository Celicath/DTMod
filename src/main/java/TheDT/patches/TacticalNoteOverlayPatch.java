package TheDT.patches;

import TheDT.cards.RecklessFlurry;
import TheDT.cards.TauntingStrike;
import TheDT.cards.TriAttack;
import TheDT.relics.MindBender;
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
import java.util.HashSet;

public class TacticalNoteOverlayPatch {
	static ArrayList<TacticalNote> tacticalNotes = new ArrayList<>();
	static ArrayList<MindBender> mindBenders = new ArrayList<>();

	static HashSet<Class<? extends AbstractCard>> mindBenderClasses = new HashSet<Class<? extends AbstractCard>>() {
		{
			add(TauntingStrike.class);
			add(RecklessFlurry.class);
			add(TriAttack.class);
		}
	};

	public static void renderAndUpdateTacticalNote(SpriteBatch sb, AbstractCard c, int index) {
		TacticalNote tn;
		if (tacticalNotes.size() <= index) {
			tn = (TacticalNote) AbstractDungeon.player.getRelic(TacticalNote.ID).makeCopy();
			tn.scale = 0.6f;
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

	public static void renderAndUpdateMindBender(SpriteBatch sb, AbstractCard c, int index) {
		MindBender mb;
		if (mindBenders.size() <= index) {
			mb = (MindBender) AbstractDungeon.player.getRelic(MindBender.ID).makeCopy();
			mb.scale = 0.6f;
			mindBenders.add(mb);
		} else {
			mb = mindBenders.get(index);
		}
		mb.currentX = c.current_x;
		mb.currentY = c.current_y + 200.0f * Settings.scale;
		mb.hb.hovered = false;
		mb.render(sb);

		// maybe move this part to update()?
		mb.hb.move(mb.currentX, mb.currentY);
		mb.hb.update();
		if (mb.hb.hovered) {
			for (int i = 0; i < TacticalNote.tacticCardsId.length; i++) {
				float x = InputHelper.mX + (InputHelper.mX < 1400.0F * Settings.scale ? 60.0F : -350.0F) * Settings.scale;
				float y = InputHelper.mY + 100.0F * Settings.scale;
				TipHelper.render(sb);
				TipHelper.renderGenericTip(x, y, mb.name, mb.description);
			}
		}
		mb.hb.render(sb);
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "render")
	public static class RewardPatch {
		@SpirePostfixPatch
		public static void Postfix(CardRewardScreen __instance, SpriteBatch sb) {
			if (!PeekButton.isPeeking && __instance.rewardGroup != null) {
				if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
					int index = 0;
					for (AbstractCard c : __instance.rewardGroup) {
						if (c.hasTag(CustomTags.DT_TACTIC)) {
							renderAndUpdateTacticalNote(sb, c, index);
							index++;
						}
					}
				}
				if (AbstractDungeon.player.hasRelic(MindBender.ID)) {
					int index = 0;
					for (AbstractCard c : __instance.rewardGroup) {
						for (Class<? extends AbstractCard> cls : mindBenderClasses) {
							if (cls.isInstance(c)) {
								renderAndUpdateMindBender(sb, c, index);
								index++;
							}
						}
					}
				}
			}
		}
	}

	@SpirePatch(clz = ShopScreen.class, method = "renderCardsAndPrices")
	public static class ShopPatch {
		@SpirePostfixPatch
		public static void Postfix(ShopScreen __instance, SpriteBatch sb) {
			if (__instance.coloredCards == null) {
				return;
			}
			if (AbstractDungeon.player.hasRelic(TacticalNote.ID)) {
				int index = 0;
				for (AbstractCard c : __instance.coloredCards) {
					if (c.hasTag(CustomTags.DT_TACTIC)) {
						renderAndUpdateTacticalNote(sb, c, index);
						index++;
					}
				}
			}
			if (AbstractDungeon.player.hasRelic(MindBender.ID)) {
				int index = 0;
				for (AbstractCard c : __instance.coloredCards) {
					for (Class<? extends AbstractCard> cls : mindBenderClasses) {
						if (cls.isInstance(c)) {
							renderAndUpdateMindBender(sb, c, index);
							index++;
						}
					}
				}
			}
		}
	}
}
