package TheDT.patches;

import TheDT.DTModMain;
import TheDT.optionals.FriendlyMinionHelper;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.HashMap;

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

	public boolean affectYou;
	public boolean affectOther;
	public CharacterState playerState;
	public ArrayList<CharacterState> otherState;

	private MythicalGameState() {
		affectYou = false;
		affectOther = false;
	}

	public static HashMap<AbstractCard, MythicalGameState> map = new HashMap<>();

	public static void reset() {
		map.clear();
	}

	public static void save(AbstractCard card) {
		MythicalGameState state;
		if (map.containsKey(card)) {
			state = map.get(card);
		} else {
			state = new MythicalGameState();
			map.put(card, state);
		}
		state.playerState = new CharacterState(AbstractDungeon.player);
		state.otherState = new ArrayList<>();

		for (AbstractMonster creature : AbstractDungeon.getMonsters().monsters) {
			state.otherState.add(new CharacterState(creature));
		}
		if (DTModMain.isFriendlyMinionsLoaded) {
			FriendlyMinionHelper.addFriendlyMinionStates(state.otherState);
		}
	}

	public static boolean checkDiff(AbstractCard card, AbstractGameAction action) {
		MythicalGameState state = map.get(card);
		if (state.affectYou && state.affectOther) return false;

		String msg = "Affect is";
		if (!state.affectYou) {
			state.affectYou = state.playerState.checkStateChanged();
			if (state.affectYou) {
				msg += " You";
			}
		} else {
			msg += " (You)";
		}
		if (!state.affectOther) {
			for (CharacterState cs : state.otherState) {
				if (cs.checkStateChanged()) {
					state.affectOther = true;
					msg += " " + cs.creature.name;
					break;
				}
			}
		} else {
			msg += " (Other)";
		}
		String[] split = action.getClass().getName().split("\\.");
		DTModMain.logger.debug("[" + card.name + "] " + split[split.length - 1] + " -> " + msg);

		return state.affectYou && state.affectOther;
	}
}
