package TheDT.patches;

import TheDT.potions.BlazePotion;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PotionHelper;

import java.util.ArrayList;

public class BlazePotionPatch {
	@SpirePatch(clz = PotionHelper.class, method = "getPotions")
	public static class RemoveBlazePotionFormPoolPatch {
		@SpirePostfixPatch
		public static ArrayList<String> Postfix(ArrayList<String> __result, AbstractPlayer.PlayerClass c, boolean getAll) {
			if (!getAll) {
				__result.removeIf(p -> p.equals(BlazePotion.POTION_ID));
			}
			return __result;
		}
	}
}
