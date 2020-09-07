package TheDT.patches;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import com.megacrit.cardcrawl.powers.SharpHidePower;
import com.megacrit.cardcrawl.powers.SporeCloudPower;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;

public class MonsterPowerPatch {
	static AbstractPlayer prevPlayer = null;

	static void changePlayer(AbstractPlayer player) {
		if (player != null) {
			prevPlayer = AbstractDungeon.player;
			AbstractDungeon.player = player;
		}
	}

	static void RestorePlayer() {
		if (prevPlayer != null) {
			AbstractDungeon.player = prevPlayer;
		}
	}

	@SpirePatch(clz = BlockReturnPower.class, method = "onAttacked")
	public static class TalkToTheHandPatch {
		@SpirePrefixPatch
		public static void Prefix(BlockReturnPower __instance, DamageInfo info, int damageAmount) {
			if (info.owner instanceof Dragon) {
				changePlayer((AbstractPlayer) info.owner);
			}
		}

		@SpirePostfixPatch
		public static void Postfix(BlockReturnPower __instance, DamageInfo info, int damageAmount) {
			RestorePlayer();
		}
	}

	@SpirePatch(clz = SporeCloudPower.class, method = "onDeath")
	public static class SporeCloudPatch {
		@SpirePrefixPatch
		public static void Prefix(SporeCloudPower __instance) {
			if (DragonTamer.isCurrentTargetDragon(__instance.owner)) {
				changePlayer(DragonTamer.getDragon());
			}
		}

		@SpirePostfixPatch
		public static void Postfix(SporeCloudPower __instance) {
			RestorePlayer();
		}
	}

	@SpirePatch(clz = SharpHidePower.class, method = "onUseCard")
	public static class SharpHidePatch {
		@SpirePrefixPatch
		public static void Prefix(SharpHidePower __instance, AbstractCard card, UseCardAction action) {
			if (DragonTamer.isCurrentTargetDragon(__instance.owner)) {
				changePlayer(DragonTamer.getDragon());
			}
		}

		@SpirePostfixPatch
		public static void Postfix(SharpHidePower __instance, AbstractCard card, UseCardAction action) {
			RestorePlayer();
		}
	}

	@SpirePatch(clz = BeatOfDeathPower.class, method = "onAfterUseCard")
	public static class BeatOfDeathPatch {
		@SpirePrefixPatch
		public static void Prefix(BeatOfDeathPower __instance, AbstractCard card, UseCardAction action) {
			if (DragonTamer.isCurrentTargetDragon(__instance.owner)) {
				changePlayer(DragonTamer.getDragon());
			}
		}

		@SpirePostfixPatch
		public static void Postfix(BeatOfDeathPower __instance, AbstractCard card, UseCardAction action) {
			RestorePlayer();
		}
	}

	@SpirePatch(clz = ExplosivePower.class, method = "duringTurn")
	public static class ExplosivePatch {
		@SpirePrefixPatch
		public static void Prefix(ExplosivePower __instance) {
			if (DragonTamer.isCurrentTargetDragon(__instance.owner)) {
				changePlayer(DragonTamer.getDragon());
			}
		}

		@SpirePostfixPatch
		public static void Postfix(ExplosivePower __instance) {
			RestorePlayer();
		}
	}
}
