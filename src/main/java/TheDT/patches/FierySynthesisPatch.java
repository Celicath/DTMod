package TheDT.patches;

import TheDT.cards.FierySynthesis;
import TheDT.potions.BlazePotion;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;

import java.util.ArrayList;

public class FierySynthesisPatch {
	@SpirePatch(clz = SoulGroup.class, method = "obtain")
	public static class SoulGroupObtainPatch {
		@SpirePostfixPatch
		public static void Postfix(SoulGroup __instance, AbstractCard card, boolean obtainCard) {
			if (card instanceof FierySynthesis) {
				for (int i = 0; i < AbstractDungeon.player.potionSlots; i++) {
					if (!(AbstractDungeon.player.potions.get(i) instanceof PotionSlot)) {
						AbstractDungeon.player.obtainPotion(i, new BlazePotion());
					}
				}

				for (RewardItem item : AbstractDungeon.combatRewardScreen.rewards) {
					if (item.potion != null) {
						item.potion = new BlazePotion();
						item.text = item.potion.name;
					}
				}

				if (AbstractDungeon.shopScreen != null) {
					ArrayList<StorePotion> potions = (ArrayList<StorePotion>) ReflectionHacks.getPrivate(AbstractDungeon.shopScreen, ShopScreen.class, "potions");
					for (int i = 0; i < potions.size(); i++) {
						StorePotion potion = new StorePotion(new BlazePotion(), i, AbstractDungeon.shopScreen);
						if (!Settings.isDailyRun) {
							potion.price = MathUtils.round((float) potion.price * AbstractDungeon.merchantRng.random(0.95F, 1.05F));
						}
						potions.set(i, potion);
					}
				}

				for (RewardItem item : AbstractDungeon.combatRewardScreen.rewards) {
					if (item.potion != null) {
						item.potion = new BlazePotion();
					}
				}
			}
		}
	}

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

	public static boolean hasFierySynthesis() {
		if (AbstractDungeon.player == null) {
			return false;
		}
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c instanceof FierySynthesis) {
				return true;
			}
		}
		return false;
	}

	@SpirePatch(clz = PotionHelper.class, method = "getRandomPotion", paramtypez = {})
	public static class ChangeRandomPotionToBlazePatch1 {
		@SpirePostfixPatch
		public static AbstractPotion Postfix(AbstractPotion __result) {
			if (hasFierySynthesis()) {
				return new BlazePotion();
			}
			return __result;
		}
	}

	@SpirePatch(clz = PotionHelper.class, method = "getRandomPotion", paramtypez = {Random.class})
	public static class ChangeRandomPotionToBlazePatch2 {
		@SpirePostfixPatch
		public static AbstractPotion Postfix(AbstractPotion __result, Random rng) {
			if (hasFierySynthesis()) {
				return new BlazePotion();
			}
			return __result;
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "returnRandomPotion", paramtypez = {AbstractPotion.PotionRarity.class, boolean.class})
	public static class ChangeRandomPotionToBlazePatch3 {
		@SpirePrefixPatch
		public static SpireReturn<AbstractPotion> Prefix(AbstractPotion.PotionRarity rarity, boolean limited) {
			if (hasFierySynthesis()) {
				return SpireReturn.Return(new BlazePotion());
			}
			return SpireReturn.Continue();
		}
	}
}
