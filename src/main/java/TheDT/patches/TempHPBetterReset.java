package TheDT.patches;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class TempHPBetterReset {
	@SpirePatch(clz = AbstractRoom.class, method = "endBattle")
	public static class BetterBattleEnd {
		@SpirePostfixPatch
		public static void Postfix(AbstractRoom __instance) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				TempHPField.tempHp.set(AbstractDungeon.player, 0);
				Dragon dragon = ((DragonTamer) AbstractDungeon.player).dragon;
				TempHPField.tempHp.set(dragon, 0);
			}
		}
	}
}
