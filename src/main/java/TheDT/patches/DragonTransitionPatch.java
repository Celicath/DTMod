package TheDT.patches;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.stances.NeutralStance;

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

				if (AbstractDungeon.floorNum <= 1 && CardCrawlGame.dungeon instanceof Exordium) {
					if (AbstractDungeon.ascensionLevel >= 14) {
						dragon.decreaseMaxHealth(dragon.getAscensionMaxHPLoss());
					}

					if (AbstractDungeon.ascensionLevel >= 6) {
						dragon.currentHealth = MathUtils.round(dragon.maxHealth * 0.9F);
					}
				}
			}
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "resetPlayer")
	public static class RoomTransitionPatch {
		@SpirePostfixPatch
		public static void Postfix() {
			if (AbstractDungeon.player instanceof DragonTamer) {
				Dragon dragon = DragonTamer.getDragon();
				dragon.loseBlock(true);
				if (!dragon.stance.ID.equals(NeutralStance.STANCE_ID)) {
					dragon.stance = new NeutralStance();
					dragon.onStanceChange(NeutralStance.STANCE_ID);
				}
			}
		}
	}
}
