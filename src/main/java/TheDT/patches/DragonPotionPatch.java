package TheDT.patches;

import TheDT.DTModMain;
import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class DragonPotionPatch {
	private static final String[] uiAdd = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("DragonPotionDescription")).TEXT;
	private static final String[] uiSubstitute = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("DragonPotionDescriptionSubstitute")).TEXT;
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
			if (potionPatchlist.contains(__instance.ID) && (AbstractDungeon.player instanceof DragonTamer || AbstractDungeon.player instanceof Dragon)) {
				__instance.tips.add(new PowerTip(uiAdd[2], uiAdd[3]));
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
					AbstractPotion.PotionColor.class
			}
	)
	public static class PotionConstructorPatch2 {
		@SpirePostfixPatch
		public static void Postfix(AbstractPotion __instance, String name, String id, AbstractPotion.PotionRarity rarity, AbstractPotion.PotionSize size, AbstractPotion.PotionColor color) {
			if (potionPatchlist.contains(__instance.ID) && (AbstractDungeon.player instanceof DragonTamer || AbstractDungeon.player instanceof Dragon)) {
				__instance.tips.add(new PowerTip(uiAdd[2], uiAdd[3]));
			}
		}
	}

	@SpirePatch(clz = PotionPopUp.class, method = "updateInput")
	public static class PotionDrinkPatch {
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getClassName().equals("com.megacrit.cardcrawl.potions.AbstractPotion") && m.getMethodName().equals("use")) {
						m.replace("" +
								"TheDT.characters.Dragon d = TheDT.characters.DragonTamer.getLivingDragon();" +
								"if (!potion.isThrown && d != null) {" +
								PotionDrinkPatch.class.getName() + ".Do(potion);" +
								"} else {" +
								"   $_ = $proceed($$);" +
								"}"
						);
					}
				}
			};
		}

		static String convertPotionText(AbstractPotion potion) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (String word : potion.description.split(" ")) {
				if (first) first = false;
				else sb.append(' ');
				if ((word.length() > 0) && (word.charAt(0) == '#')) {
					switch (word.charAt(1)) {
						case 'r':
							word = "[#ff6563]" + word.substring(2) + "[]]";
							break;
						case 'g':
							word = "[#7fff00]" + word.substring(2) + "[]";
							break;
						case 'b':
							word = "[#87ceeb]" + word.substring(2) + "[]";
							break;
						case 'y':
							word = "[#efc851]" + word.substring(2) + "[]";
							break;
					}
				}
				sb.append(word);
			}
			return sb.toString();
		}

		static String dragonify(String text) {
			StringBuilder sb = new StringBuilder(uiAdd[0]);
			boolean first = true;
			for (String word : text.split(" ")) {
				if (first) {
					word = word.toLowerCase();
					first = false;
				} else {
					sb.append(' ');
				}
				for (int i = 0; i < uiSubstitute.length / 2; i++) {
					if (word.equals(uiSubstitute[2 * i])) {
						word = uiSubstitute[2 * i + 1];
					}
				}

				sb.append(word);
			}
			return sb + uiAdd[1];
		}

		@SuppressWarnings("unused")
		public static void Do(AbstractPotion potion) {
			if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT || !potionPatchlist.contains(potion.ID)) {
				potion.use(null);
				return;
			}
			InputHelper.moveCursorToNeutralPosition();
			ArrayList<AbstractCard> choices = new ArrayList<>();
			choices.add(new AbstractDTCard(DTModMain.CHOICE_ID_YOU, -2, AbstractCard.CardType.SKILL, CardColorEnum.DT_ORANGE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.SELF, AbstractDTCard.DTCardTarget.DEFAULT) {
				{
					rawDescription = convertPotionText(potion);
					initializeDescription();
				}

				@Override
				public void upgrade() {
				}

				@Override
				public void use(AbstractPlayer p, AbstractMonster m) {
				}

				@Override
				public void onChoseThisOption() {
					potion.use(null);
				}
			});
			choices.add(new AbstractDTCard(DTModMain.CHOICE_ID_DRAGON, -2, AbstractCard.CardType.SKILL, CardColorEnum.DT_ORANGE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.SELF, AbstractDTCard.DTCardTarget.DEFAULT) {
				{
					rawDescription = dragonify(convertPotionText(potion));
					initializeDescription();
				}

				@Override
				public void upgrade() {
				}

				@Override
				public void use(AbstractPlayer p, AbstractMonster m) {
				}

				@Override
				public void onChoseThisOption() {
					AbstractPlayer prev = AbstractDungeon.player;
					AbstractDungeon.player = DragonTamer.getDragon();
					potion.use(null);
					AbstractDungeon.player = prev;
				}
			});
			AbstractDungeon.actionManager.addToBottom(new ChooseOneAction(choices));
		}
	}
}
