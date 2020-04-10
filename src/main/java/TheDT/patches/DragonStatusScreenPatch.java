package TheDT.patches;

import TheDT.DTModMain;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
}
