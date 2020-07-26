package TheDT.patches;

import TheDT.DTModMain;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class CardGenPatch {
	@SpirePatch(
			clz = ShowCardAndAddToDiscardEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class,
					float.class,
					float.class
			}
	)
	public static class AddToDiscardConstruct1 {
		@SpirePrefixPatch
		public static void Prefix(ShowCardAndAddToDiscardEffect __instance, AbstractCard srcCard, float x, float y) {
			if (srcCard instanceof Burn) {
				DTModMain.onBurnCreated();
			}
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToDiscardEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class
			}
	)
	public static class AddToDiscardConstruct2 {
		@SpirePrefixPatch
		public static void Prefix(ShowCardAndAddToDiscardEffect __instance, AbstractCard card) {
			if (card instanceof Burn) {
				DTModMain.onBurnCreated();
			}
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToDrawPileEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class,
					float.class,
					float.class,
					boolean.class,
					boolean.class,
					boolean.class
			}
	)
	public static class AddToDrawPileConstruct1 {
		@SpirePrefixPatch
		public static void Prefix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom) {
			if (srcCard instanceof Burn) {
				DTModMain.onBurnCreated();
			}
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToDrawPileEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class,
					boolean.class,
					boolean.class,
			}
	)
	public static class AddToDrawPileConstruct2 {
		@SpirePrefixPatch
		public static void Prefix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, boolean randomSpot, boolean toBottom) {
			if (srcCard instanceof Burn) {
				DTModMain.onBurnCreated();
			}
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToHandEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class,
					float.class,
					float.class
			}
	)
	public static class AddToHandConstructor1 {
		@SpirePrefixPatch()
		public static void Prefix(ShowCardAndAddToHandEffect __instance, AbstractCard card, float offsetX, float offsetY) {
			if (card instanceof Burn) {
				DTModMain.onBurnCreated();
			}
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToHandEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class
			}
	)
	public static class AddToHandConstructor2 {
		@SpirePrefixPatch()
		public static void Prefix(ShowCardAndAddToHandEffect __instance, AbstractCard card) {
			if (card instanceof Burn) {
				DTModMain.onBurnCreated();
			}
		}
	}
}
