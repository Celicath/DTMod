package TheDT.optionals;

import TheDT.patches.MythicalGameState;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;

import java.util.ArrayList;

public class FriendlyMinionHelper {
	public static void addFriendlyMinionStates(ArrayList<MythicalGameState.CharacterState> state) {
		if (AbstractDungeon.player instanceof AbstractPlayerWithMinions) {
			for (AbstractMonster m : ((AbstractPlayerWithMinions) AbstractDungeon.player).minions.monsters) {
				state.add(new MythicalGameState.CharacterState(m));
			}
		}
	}
}
