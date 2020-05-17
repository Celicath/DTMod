package TheDT.patches;

import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import javassist.ClassClassPath;
import javassist.ClassPool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class DTMetricsPatch {
	@SpirePatch(clz = Metrics.class, method = "sendPost", paramtypez = {String.class, String.class})
	public static class SendPostPatch {
		@SpirePrefixPatch
		public static void Prefix(Metrics __instance, @ByRef String[] url, String fileName) {
			ClassPool.getDefault().insertClassPath(new ClassClassPath(SendPostPatch.class));
			if (AbstractDungeon.player.chosenClass == TheDTEnum.THE_DT) {
				url[0] = "http://52.187.168.135:12008/upload";
			}
		}
	}

	@SpirePatch(clz = DeathScreen.class, method = "shouldUploadMetricData")
	public static class shouldUploadMetricData {
		@SpirePostfixPatch
		public static boolean Postfix(boolean __retVal) {
			if (AbstractDungeon.player.chosenClass == TheDTEnum.THE_DT) {
				__retVal = Settings.UPLOAD_DATA;
			}
			return __retVal;
		}
	}

	@SpirePatch(clz = Metrics.class, method = "run")
	public static class RunPatch {
		@SpirePostfixPatch
		public static void Postfix(Metrics __instance) {
			if (__instance.type == Metrics.MetricRequestType.UPLOAD_METRICS && AbstractDungeon.player.chosenClass == TheDTEnum.THE_DT) {
				try {
					Method m = Metrics.class.getDeclaredMethod("gatherAllDataAndSend", boolean.class, boolean.class, MonsterGroup.class);
					m.setAccessible(true);
					m.invoke(__instance, __instance.death, __instance.trueVictory, __instance.monsters);
				} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SpirePatch(clz = Metrics.class, method = "gatherAllData")
	public static class BuildJsonPatch {
		@SpirePostfixPatch
		public static void Postfix(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters, HashMap<Object, Object> ___params) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				___params.put("dtmod_dragon_data", ((DragonTamer) AbstractDungeon.player).dragon.onSave());
				___params.put("metric_token", Loader.MODINFOS.length * 64 + Settings.language.ordinal() + 19);
			}
		}
	}
}
