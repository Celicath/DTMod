package TheDT.patches;

import TheDT.characters.Dragon;
import TheDT.relics.MagicStone;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

public class MagicStonePatch {

	@SpirePatch(clz = AbstractCreature.class, method = "heal", paramtypez = {int.class, boolean.class})
	public static class OverhealToTempHP {
		@SpireInsertPatch(locator = OverhealLocator.class)
		public static void Insert(AbstractCreature __instance, int healAmount, boolean showEffect) {
			AbstractRelic ms = AbstractDungeon.player.getRelic(MagicStone.ID);
			if (AbstractDungeon.player instanceof Dragon) {
				ms = ((Dragon) AbstractDungeon.player).master.getRelic(MagicStone.ID);
			}
			if (ms != null && __instance.isPlayer && __instance.currentHealth > __instance.maxHealth && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
				ms.flash();
				TempHPField.tempHp.set(__instance, TempHPField.tempHp.get(__instance) + __instance.currentHealth - __instance.maxHealth);
			}
		}
	}

	private static class OverhealLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "maxHealth");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
