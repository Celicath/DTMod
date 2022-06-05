package TheDT.patches;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CtBehavior;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class DragonPotionPatch {
	private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("DragonPotionDescription")).TEXT;
	public static final HashSet<String> potionPatchlist = new HashSet<>(Arrays.asList(
		AncientPotion.POTION_ID,
		BlockPotion.POTION_ID,
		BloodPotion.POTION_ID,
		DexterityPotion.POTION_ID,
		EssenceOfSteel.POTION_ID,
		FruitJuice.POTION_ID,
		GhostInAJar.POTION_ID,
		LiquidBronze.POTION_ID,
		RegenPotion.POTION_ID,
		SpeedPotion.POTION_ID,
		SteroidPotion.POTION_ID,
		StrengthPotion.POTION_ID,
		HeartOfIron.POTION_ID,
		CultistPotion.POTION_ID
	));

	public static boolean targetModeHijack = false;
	public static boolean autoTargetFirst = false;
	public static AbstractPlayer hoveredPlayer = null;

	public static void addTipsToPotions(AbstractPotion potion) {
		if (AbstractDungeon.player instanceof DragonTamer || AbstractDungeon.player instanceof Dragon) {
			if (potionPatchlist.contains(potion.ID)) {
				potion.tips.add(new PowerTip(TEXT[0], TEXT[1]));
			} else if (potion.ID.equals(FairyPotion.POTION_ID)) {
				potion.tips.add(new PowerTip(TEXT[0], TEXT[2] + potion.getPotency() + TEXT[3]));
			}
		}
	}

	@SpirePatch(
		clz = AbstractPotion.class,
		method = SpirePatch.CONSTRUCTOR,
		paramtypez = {
			String.class,
			String.class,
			AbstractPotion.PotionRarity.class,
			AbstractPotion.PotionSize.class,
			AbstractPotion.PotionEffect.class,
			Color.class,
			Color.class,
			Color.class
		}
	)
	public static class PotionConstructorPatch1 {
		@SpirePostfixPatch
		public static void Postfix(AbstractPotion __instance, String name, String id, AbstractPotion.PotionRarity rarity, AbstractPotion.PotionSize size, AbstractPotion.PotionEffect effect, Color liquidColor, Color hybridColor, Color spotsColor) {
			addTipsToPotions(__instance);
		}
	}

	@SpirePatch(
		clz = AbstractPotion.class,
		method = SpirePatch.CONSTRUCTOR,
		paramtypez = {
			String.class,
			String.class,
			AbstractPotion.PotionRarity.class,
			AbstractPotion.PotionSize.class,
			AbstractPotion.PotionColor.class
		}
	)
	public static class PotionConstructorPatch2 {
		@SpirePostfixPatch
		public static void Postfix(AbstractPotion __instance, String name, String id, AbstractPotion.PotionRarity rarity, AbstractPotion.PotionSize size, AbstractPotion.PotionColor color) {
			addTipsToPotions(__instance);
		}
	}

	@SpirePatch(clz = FairyPotion.class, method = "canUse")
	public static class FairyPotionUsablePatch {
		@SpirePostfixPatch
		public static boolean Postfix(boolean __result, FairyPotion __instance) {
			if (AbstractDungeon.player instanceof DragonTamer && ((DragonTamer) AbstractDungeon.player).dragon.isDead) {
				if (AbstractDungeon.getCurrRoom().event != null && AbstractDungeon.getCurrRoom().event instanceof WeMeetAgain) {
					return false;
				} else {
					return AbstractDungeon.getCurrRoom().monsters != null && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && !AbstractDungeon.actionManager.turnHasEnded && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
				}
			} else {
				return __result;
			}
		}
	}

	@SpirePatch(clz = PotionPopUp.class, method = "updateInput")
	public static class PotionDrinkPatch {
		@SpireInsertPatch(locator = HijackLocator.class)
		public static SpireReturn<Void> InsertHijack(PotionPopUp __instance, AbstractPotion ___potion, @ByRef AbstractMonster[] ___hoveredMonster) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				if (___potion instanceof FairyPotion) {
					Dragon d = DragonTamer.getDragon();
					if (d != null && d.isDead) {
						d.isDead = false;
						boolean modified = false;
						Iterator<AbstractPower> it = d.powers.iterator();
						while (it.hasNext()) {
							AbstractPower p = it.next();
							if (p.type == AbstractPower.PowerType.DEBUFF) {
								p.onRemove();
								it.remove();
								modified = true;
							}
						}
						if (modified) {
							AbstractDungeon.onModifyPower();
						}
						d.potions = AbstractDungeon.player.potions;
						AbstractPlayer prevPlayer = AbstractDungeon.player;
						AbstractDungeon.player = d;
						___potion.use(null);
						AbstractDungeon.player = prevPlayer;

						for (AbstractRelic r : AbstractDungeon.player.relics) {
							r.onUsePotion();
						}
						CardCrawlGame.sound.play("POTION_1");
						BaseMod.publishPostPotionUse(___potion);
						CardCrawlGame.metricData.potions_floor_usage.add(AbstractDungeon.floorNum);
						__instance.close();

						return SpireReturn.Return(null);
					}
					return SpireReturn.Continue();
				}

				Dragon d = DragonTamer.getLivingDragon();
				if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && d != null && potionPatchlist.contains(___potion.ID)) {
					__instance.targetMode = true;
					targetModeHijack = true;
					GameCursor.hidden = true;
					autoTargetFirst = true;
					___hoveredMonster[0] = null;
					__instance.close();
					return SpireReturn.Return(null);
				}
			}
			return SpireReturn.Continue();
		}

		@SpireInsertPatch(locator = CancelHijackLocator.class)
		public static void InsertCancelHijack(PotionPopUp __instance) {
			targetModeHijack = false;
		}
	}

	private static class HijackLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(MetricData.class, "potions_floor_usage");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	private static class CancelHijackLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(PotionPopUp.class, "targetMode");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}


	@SpirePatch(clz = PotionPopUp.class, method = "updateControllerTargetInput")
	public static class PotionTargetControllerPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(PotionPopUp __instance) {
			if (!targetModeHijack) {
				return SpireReturn.Continue();
			}
			if (Settings.isControllerMode) {
				int offsetIndex = 0;

				if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
					--offsetIndex;
				}

				if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
					++offsetIndex;
				}

				if (autoTargetFirst) {
					autoTargetFirst = false;
					offsetIndex = 1;
				}

				if (offsetIndex != 0) {
					Dragon d = DragonTamer.getLivingDragon();

					int index = -1;
					if (AbstractDungeon.player.hb.hovered) {
						index = 0;
					}
					if (d != null && d.hb.hovered) {
						index = 1;
					}

					index += offsetIndex;
					if (index > 1) {
						index = 0;
					} else if (index < 0) {
						index = 1;
					}

					if (index == 1 && d == null) {
						index = 0;
					}

					Hitbox target = index == 1 ? d.hb : AbstractDungeon.player.hb;
					Gdx.input.setCursorPosition((int) target.cX, Settings.HEIGHT - (int) target.cY);
				}
			}
			return SpireReturn.Return(null);
		}
	}

	@SpirePatch(clz = PotionPopUp.class, method = "updateTargetMode")
	public static class PotionTargetPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(PotionPopUp __instance, AbstractPotion ___potion, int ___slot) {
			if (!targetModeHijack) {
				return SpireReturn.Continue();
			}

			if (InputHelper.justClickedRight || AbstractDungeon.isScreenUp || InputHelper.mY > Settings.HEIGHT - 80.0F * Settings.scale || AbstractDungeon.player.hoveredCard != null || InputHelper.mY < 140.0F * Settings.scale || CInputActionSet.cancel.isJustPressed()) {
				CInputActionSet.cancel.unpress();
				__instance.targetMode = false;
				targetModeHijack = false;
				GameCursor.hidden = false;
			}

			hoveredPlayer = null;
			if (AbstractDungeon.player.hb.hovered) {
				hoveredPlayer = AbstractDungeon.player;
			}
			Dragon d = DragonTamer.getLivingDragon();
			if (d != null && d.hb.hovered) {
				hoveredPlayer = d;
			}

			if (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) {
				InputHelper.justClickedLeft = false;
				CInputActionSet.select.unpress();
				if (hoveredPlayer != null) {
					CardCrawlGame.metricData.potions_floor_usage.add(AbstractDungeon.floorNum);

					BaseMod.publishPrePotionUse(___potion);
					if (hoveredPlayer == d) {
						d.potions = AbstractDungeon.player.potions;
						AbstractPlayer prev = AbstractDungeon.player;
						AbstractDungeon.player = DragonTamer.getDragon();
						___potion.use(null);
						AbstractDungeon.player = prev;
					} else {
						___potion.use(null);
					}

					for (AbstractRelic r : AbstractDungeon.player.relics) {
						r.onUsePotion();
					}

					CardCrawlGame.sound.play("POTION_1");// 287
					AbstractDungeon.topPanel.destroyPotion(___slot);
					BaseMod.publishPostPotionUse(___potion);

					__instance.targetMode = false;
					targetModeHijack = false;
					GameCursor.hidden = false;
				}
			}

			return SpireReturn.Return(null);
		}
	}

	@SpirePatch(clz = PotionPopUp.class, method = "renderTargetingUi")
	public static class PotionTargetRenderPatch {
		@SpirePrefixPatch
		public static void Prefix(PotionPopUp __instance, SpriteBatch sb) {
			if (targetModeHijack && hoveredPlayer != null) {
				hoveredPlayer.renderReticle(sb);
			}
		}
	}
}
