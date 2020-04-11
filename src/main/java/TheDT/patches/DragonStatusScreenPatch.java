package TheDT.patches;

import TheDT.DTModMain;
import TheDT.characters.DragonTamer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DragonStatusScreenPatch {
	@SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
	public static class CloseManual {
		public static void Prefix() {
			if (AbstractDungeon.screen == CurrentScreenEnum.DRAGON_GROWTH) {
				try {
					Method overlayReset = AbstractDungeon.class.getDeclaredMethod("genericScreenOverlayReset");
					overlayReset.setAccessible(true);
					overlayReset.invoke(AbstractDungeon.class);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				DTModMain.dragonStatusScreen.close();
			}
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "update")
	public static class UpdateScreen {
		@SpireInsertPatch(locator = AfterSubscreenUpdateLocator.class)
		public static void Insert(AbstractDungeon __instance) {
			if (AbstractDungeon.screen == CurrentScreenEnum.DRAGON_GROWTH) {
				DTModMain.dragonStatusScreen.update();
			}
		}
	}

	private static class AfterSubscreenUpdateLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "turnPhaseEffectActive");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "render")
	public static class RenderScreen {
		@SpireInsertPatch(locator = BeforeSubscreenRenderLocator.class)
		public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
			if (AbstractDungeon.screen == CurrentScreenEnum.DRAGON_GROWTH) {
				DTModMain.dragonStatusScreen.render(sb);
			}
		}
	}

	private static class BeforeSubscreenRenderLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "screen");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	/*
	@SpirePatch(clz = AbstractDungeon.class, method = "openPreviousScreen")
	public static class ReopenScreen {
		@SpirePostfixPatch
		public static void Insert(AbstractDungeon __instance, AbstractDungeon.CurrentScreen s) {
			if (s == CurrentScreenEnum.DRAGON_GROWTH) {
				DTModMain.dragonStatusScreen.reOpen();
				BossRelic
			}
		}
	}
*/

	@SpirePatch(clz = ProceedButton.class, method = "update")
	public static class ProceedButtonPatch {
		public static boolean disable = false;

		@SpireInsertPatch(locator = GoToNextDungeonLocator.class)
		public static SpireReturn<Void> Insert(ProceedButton __instance, @ByRef boolean[] ___callingBellCheck, String[] ___LABEL, String[] ___MSG) {
			if (disable) {
				disable = false;
				return SpireReturn.Continue();
			}
			if (AbstractDungeon.player instanceof DragonTamer && ((DragonTamer) AbstractDungeon.player).dragon.getTier() < 3) {
				int count = 0;
				for (RewardItem item : AbstractDungeon.combatRewardScreen.rewards) {
					if (item.type == RewardItem.RewardType.RELIC) {
						count++;
					}
				}
				if (count == 3 && ___callingBellCheck[0]) {
					___callingBellCheck[0] = false;
					AbstractDungeon.ftue = new FtueTip(___LABEL[0], ___MSG[0], Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, FtueTip.TipType.CARD_REWARD);
					return SpireReturn.Continue();
				}
				AbstractDungeon.combatRewardScreen.clear();
				AbstractDungeon.previousScreen = null;
				AbstractDungeon.closeCurrentScreen();

				AbstractDungeon.currMapNode.room = new DragonGrowthRoom(AbstractDungeon.currMapNode.room);
				AbstractDungeon.getCurrRoom().onPlayerEntry();
				AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;

				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}

		private static class GoToNextDungeonLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(ProceedButton.class, "goToNextDungeon");
				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			}
		}

		@SpireInsertPatch(locator = RevertRoomLocator.class)
		public static void Insert(ProceedButton __instance, boolean ___isHidden, Hitbox ___hb, @ByRef AbstractRoom[] ___currentRoom) {
			if (AbstractDungeon.currMapNode.room instanceof DragonGrowthRoom) {
				AbstractDungeon.currMapNode.room = ___currentRoom[0] = ((DragonGrowthRoom) AbstractDungeon.currMapNode.room).originalRoom;
				disable = true;
			}
		}

		private static class RevertRoomLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.InstanceOfMatcher(MonsterRoomBoss.class);
				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			}
		}
	}

	public static class DragonGrowthRoom extends AbstractRoom {
		public AbstractRoom originalRoom;

		DragonGrowthRoom(AbstractRoom originalRoom) {
			this.originalRoom = originalRoom;
			phase = RoomPhase.INCOMPLETE;
		}

		@Override
		public void onPlayerEntry() {
			DTModMain.dragonStatusScreen.openSelection();
		}

		@Override
		public void update() {
			super.update();
			if (!AbstractDungeon.isScreenUp) {
				DTModMain.dragonStatusScreen.update();
				if (!DTModMain.dragonStatusScreen.needSelection) {
					AbstractDungeon.overlayMenu.proceedButton.show();
				}
			}
		}

		@Override
		public void render(SpriteBatch sb) {
			super.render(sb);
			if (!AbstractDungeon.isScreenUp) {
				DTModMain.dragonStatusScreen.render(sb);
			}
		}
	}
}
