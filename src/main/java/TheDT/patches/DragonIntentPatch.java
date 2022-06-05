package TheDT.patches;

import TheDT.DTModMain;
import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;
import javassist.CtBehavior;

public class DragonIntentPatch {
	@SpirePatch(clz = AbstractMonster.class, method = "updateIntentTip")
	public static class UpdateIntentTipPatch {
		private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("MonsterIntent"));
		public static final String[] TEXT = uiStrings == null ?
			AbstractMonster.TEXT :
			uiStrings.TEXT;

		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractMonster __instance, PowerTip ___intentTip, boolean ___isMultiDmg, int ___intentDmg, int ___intentMultiAmt) {
			if (DragonTamer.getCurrentTarget(__instance) instanceof Dragon) {
				switch (__instance.intent) {
					case ATTACK:
						___intentTip.header = TEXT[0];
						if (___isMultiDmg) {
							___intentTip.body = TEXT[1] + ___intentDmg + TEXT[2] + ___intentMultiAmt + TEXT[3];
						} else {
							___intentTip.body = TEXT[4] + ___intentDmg + TEXT[5];
						}
						int tmp;
						if (___isMultiDmg) {
							tmp = ___intentDmg * ___intentMultiAmt;
						} else {
							tmp = ___intentDmg;
						}

						if (tmp < 5) {
							___intentTip.img = ImageMaster.INTENT_ATK_TIP_1;
						} else if (tmp < 10) {
							___intentTip.img = ImageMaster.INTENT_ATK_TIP_2;
						} else if (tmp < 15) {
							___intentTip.img = ImageMaster.INTENT_ATK_TIP_3;
						} else if (tmp < 20) {
							___intentTip.img = ImageMaster.INTENT_ATK_TIP_4;
						} else if (tmp < 25) {
							___intentTip.img = ImageMaster.INTENT_ATK_TIP_5;
						} else {
							___intentTip.img = tmp < 30 ? ImageMaster.INTENT_ATK_TIP_6 : ImageMaster.INTENT_ATK_TIP_7;
						}
						break;
					case ATTACK_BUFF:
						___intentTip.header = TEXT[6];
						if (___isMultiDmg) {
							___intentTip.body = TEXT[7] + ___intentDmg + TEXT[2] + ___intentMultiAmt + TEXT[8];
						} else {
							___intentTip.body = TEXT[9] + ___intentDmg + TEXT[5];
						}

						___intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
						break;
					case ATTACK_DEBUFF:
						___intentTip.header = TEXT[10];
						___intentTip.body = TEXT[11] + ___intentDmg + TEXT[5];
						___intentTip.img = ImageMaster.INTENT_ATTACK_DEBUFF;
						break;
					case ATTACK_DEFEND:
						___intentTip.header = TEXT[0];
						if (___isMultiDmg) {
							___intentTip.body = TEXT[12] + ___intentDmg + TEXT[2] + ___intentMultiAmt + TEXT[3];
						} else {
							___intentTip.body = TEXT[12] + ___intentDmg + TEXT[5];
						}

						___intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
						break;
					case BUFF:
						___intentTip.header = TEXT[10];
						___intentTip.body = TEXT[19];
						___intentTip.img = ImageMaster.INTENT_BUFF;
						break;
					case DEBUFF:
						___intentTip.header = TEXT[10];
						___intentTip.body = TEXT[20];
						___intentTip.img = ImageMaster.INTENT_DEBUFF;
						break;
					case STRONG_DEBUFF:
						___intentTip.header = TEXT[10];
						___intentTip.body = TEXT[21];
						___intentTip.img = ImageMaster.INTENT_DEBUFF2;
						break;
					case DEFEND:
						___intentTip.header = TEXT[13];
						___intentTip.body = TEXT[22];
						___intentTip.img = ImageMaster.INTENT_DEFEND;
						break;
					case DEFEND_DEBUFF:
						___intentTip.header = TEXT[13];
						___intentTip.body = TEXT[23];
						___intentTip.img = ImageMaster.INTENT_DEFEND;
						break;
					case DEFEND_BUFF:
						___intentTip.header = TEXT[13];
						___intentTip.body = TEXT[24];
						___intentTip.img = ImageMaster.INTENT_DEFEND_BUFF;
						break;
					case ESCAPE:
						___intentTip.header = TEXT[14];
						___intentTip.body = TEXT[25];
						___intentTip.img = ImageMaster.INTENT_ESCAPE;
						break;
					case MAGIC:
						___intentTip.header = TEXT[15];
						___intentTip.body = TEXT[26];
						___intentTip.img = ImageMaster.INTENT_MAGIC;
						break;
					case SLEEP:
						___intentTip.header = TEXT[16];
						___intentTip.body = TEXT[27];
						___intentTip.img = ImageMaster.INTENT_SLEEP;
						break;
					case STUN:
						___intentTip.header = TEXT[17];
						___intentTip.body = TEXT[28];
						___intentTip.img = ImageMaster.INTENT_STUN;
						break;
					case UNKNOWN:
						___intentTip.header = TEXT[18];
						___intentTip.body = TEXT[29];
						___intentTip.img = ImageMaster.INTENT_UNKNOWN;
						break;
					case NONE:
						___intentTip.header = "";
						___intentTip.body = "";
						___intentTip.img = ImageMaster.INTENT_UNKNOWN;
						break;
					default:
						return SpireReturn.Continue();
				}
				return SpireReturn.Return(null);
			} else {
				return SpireReturn.Continue();
			}
		}
	}

	@SpirePatch(clz = AbstractMonster.class, method = "renderIntent")
	public static class RenderIntentPatch {
		@SpireInsertPatch(locator = DrawIntentLocator.class)
		public static void Insert(AbstractMonster __instance, SpriteBatch sb) {
			if (DragonTamer.getCurrentTarget(__instance) instanceof Dragon) {
				//sb.setColor(new Color(1.0F, 0.6F, 0.2F, __instance.intentAlpha));
			}
		}

		public static class DrawIntentLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
				int[] all = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
				return new int[]{all[1]};
			}
		}

		@SpirePostfixPatch
		public static void Postfix(AbstractMonster __instance, SpriteBatch sb, BobEffect ___bobEffect) {
			if (DragonTamer.getCurrentTarget(__instance) instanceof Dragon) {
				Color c = Color.WHITE.cpy();
				c.a = __instance.intentAlpha * 0.95f;
				sb.setColor(c);

				sb.draw(AbstractDTCard.getDragonIconTexture(),
					__instance.intentHb.cX - AbstractDTCard.DRAGON_ICON_WIDTH / 2.0f + 18.0f * Settings.scale,
					__instance.intentHb.cY - AbstractDTCard.DRAGON_ICON_HEIGHT / 2.0f + ___bobEffect.y + 18.0f * Settings.scale,
					AbstractDTCard.DRAGON_ICON_WIDTH / 2.0f,
					AbstractDTCard.DRAGON_ICON_HEIGHT / 2.0f,
					AbstractDTCard.DRAGON_ICON_WIDTH,
					AbstractDTCard.DRAGON_ICON_HEIGHT,
					Settings.scale * 0.5f, Settings.scale * 0.5f,
					0, 0, 0, AbstractDTCard.DRAGON_ICON_WIDTH, AbstractDTCard.DRAGON_ICON_HEIGHT, false, false);
			}
		}
	}
}
