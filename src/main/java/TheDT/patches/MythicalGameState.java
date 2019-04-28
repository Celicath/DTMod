package TheDT.patches;

import TheDT.DTMod;
import TheDT.optionals.FriendlyMinionHelper;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class MythicalGameState {
	public static class CharacterState {
		AbstractCreature creature;
		int hp;
		int block;
		int tempHp;
		ArrayList<Class<? extends AbstractPower>> powerType;
		ArrayList<Integer> powerAmount;

		public CharacterState(AbstractCreature creature) {
			this.creature = creature;

			hp = creature.currentHealth;
			block = creature.currentBlock;
			tempHp = TempHPField.tempHp.get(creature);
			powerType = new ArrayList<>();
			powerAmount = new ArrayList<>();
			for (AbstractPower p : creature.powers) {
				powerType.add(p.getClass());
				powerAmount.add(p.amount);
			}
		}

		boolean checkStateChanged() {
			if (creature.powers.size() != powerType.size())
				return true;
			for (int i = 0; i < creature.powers.size(); i++) {
				AbstractPower p = creature.powers.get(i);
				if (p.getClass() != powerType.get(i) || p.amount != powerAmount.get(i))
					return true;
			}
			return hp != creature.currentHealth ||
					block != creature.currentBlock ||
					tempHp != TempHPField.tempHp.get(creature);
		}
	}

	public static boolean affectYou;
	public static boolean affectOther;
	public static CharacterState playerState;
	public static ArrayList<CharacterState> otherState;

	public static void reset() {
		affectYou = false;
		affectOther = false;
	}

	public static void save() {
		playerState = new CharacterState(AbstractDungeon.player);
		otherState = new ArrayList<>();
		for (AbstractMonster creature : AbstractDungeon.getMonsters().monsters) {
			otherState.add(new CharacterState(creature));
		}
		if (DTMod.isFriendlyMinionsLoaded) {
			FriendlyMinionHelper.addFriendlyMinionStates(otherState);
		}
	}

	public static boolean checkDiff() {
		if (affectYou && affectOther) return false;

		String msg = "Affect is ";
		if (!affectYou) {
			affectYou = playerState.checkStateChanged();
			if (affectYou) {
				msg += "You";
			}
		}
		if (!affectOther) {
			for (CharacterState cs : otherState) {
				if (cs.checkStateChanged()) {
					affectOther = true;
					msg += " " + cs.creature.name;
					break;
				}
			}
		}
		return affectYou && affectOther;
	}
}
