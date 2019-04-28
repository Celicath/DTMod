package TheDT.patches;

import TheDT.actions.ActivateExhaustEffectsAction;
import TheDT.relics.DeckCase;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import javassist.CtBehavior;

import java.util.ArrayList;

public class DeckCasePatch {
	public static class Util {
		static ArrayList<AbstractGameEffect> gainCardEffectToExhaust = new ArrayList<>();

		public static boolean needToExhaust(AbstractCard card) {
			if (card.type == AbstractCard.CardType.STATUS) {
				for (AbstractRelic r : AbstractDungeon.player.relics) {
					if (r.relicId.equals(DeckCase.ID) && r.counter > 0) {
						r.flash();
						r.counter--;
						return true;
					}
				}
			}
			return false;
		}

		public static void setExhaustStatus(AbstractGameEffect age) {
			gainCardEffectToExhaust.add(age);
		}

		public static void exhaustIncomingCard(AbstractGameEffect age, AbstractCard c) {
			AbstractDungeon.actionManager.addToTop(new ActivateExhaustEffectsAction(c));
			c.unhover();
			c.untip();
			c.stopGlowing();

			AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(c));
			AbstractDungeon.player.exhaustPile.addToTop(c);
			gainCardEffectToExhaust.remove(age);
		}
	}

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
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndAddToDiscardEffect __instance, AbstractCard srcCard, float x, float y) {
			if (Util.needToExhaust(srcCard)) {
				AbstractDungeon.player.discardPile.removeCard(srcCard);
				__instance.duration *= 0.5f;
				Util.setExhaustStatus(__instance);
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
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndAddToDiscardEffect __instance, AbstractCard card) {
			if (Util.needToExhaust(card)) {
				AbstractDungeon.player.discardPile.removeCard(card);
				__instance.duration *= 0.5f;
				Util.setExhaustStatus(__instance);
			}
		}
	}

	@SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
	public static class AddToDiscardUpdate {
		@SpireInsertPatch(locator = AddToDiscardLocator.class, localvars = {"card"})
		public static SpireReturn Insert(ShowCardAndAddToDiscardEffect __instance, AbstractCard card) {
			if (Util.gainCardEffectToExhaust.contains(__instance)) {
				Util.exhaustIncomingCard(__instance, card);
				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}
	}

	private static class AddToDiscardLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "shrink");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
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
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom) {
			if (Util.needToExhaust(srcCard)) {
				AbstractDungeon.player.drawPile.removeCard(srcCard);
				__instance.duration *= 0.5f;
				Util.setExhaustStatus(__instance);
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
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, boolean randomSpot, boolean toBottom) {
			if (Util.needToExhaust(srcCard)) {
				AbstractDungeon.player.drawPile.removeCard(srcCard);
				__instance.duration *= 0.5f;
				Util.setExhaustStatus(__instance);
			}
		}
	}

	@SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "update")
	public static class AddToDrawPileUpdate {
		@SpireInsertPatch(locator = AddToDrawPileLocator.class, localvars = {"card"})
		public static SpireReturn Insert(ShowCardAndAddToDrawPileEffect __instance, AbstractCard card) {
			if (Util.gainCardEffectToExhaust.contains(__instance)) {
				Util.exhaustIncomingCard(__instance, card);
				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}
	}

	private static class AddToDrawPileLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "shrink");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
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
		@SpirePostfixPatch()
		public static void Postfix(ShowCardAndAddToHandEffect __instance, AbstractCard card, float offsetX, float offsetY) {
			if (Util.needToExhaust(card)) {
				Util.setExhaustStatus(__instance);
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
		@SpirePostfixPatch()
		public static void Postfix(ShowCardAndAddToHandEffect __instance, AbstractCard card) {
			if (Util.needToExhaust(card)) {
				Util.setExhaustStatus(__instance);
			}
		}
	}

	@SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = "update")
	public static class AddToHandUpdate {
		@SpireInsertPatch(locator = AddToHandLocator.class, localvars = {"card"})
		public static void Insert(ShowCardAndAddToHandEffect __instance, AbstractCard card) {
			if (Util.gainCardEffectToExhaust.contains(__instance)) {
				AbstractDungeon.player.hand.removeCard(card);
				Util.exhaustIncomingCard(__instance, card);
			}
		}
	}

	private static class AddToHandLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(ShowCardAndAddToHandEffect.class, "isDone");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
