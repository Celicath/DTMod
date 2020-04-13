package TheDT.patches;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DragonTransitionPatch {
	@SpirePatch(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
	public static class DungeonTrainsitionPatch {
		@SpirePostfixPatch
		public static void Postfix() {
			if (AbstractDungeon.player instanceof DragonTamer) {
				Dragon dragon = ((DragonTamer) AbstractDungeon.player).dragon;
				if (AbstractDungeon.ascensionLevel >= 5) {
					dragon.heal(MathUtils.round((dragon.maxHealth - dragon.currentHealth) * 0.75F), false);
				} else {
					dragon.heal(dragon.maxHealth, false);
				}
			}
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "resetPlayer")
	public static class RoomTransitionPatch {
		@SpirePostfixPatch
		public static void Postfix() {
			if (AbstractDungeon.player instanceof DragonTamer) {
				((DragonTamer) AbstractDungeon.player).dragon.loseBlock(true);
			}
		}
	}
}
